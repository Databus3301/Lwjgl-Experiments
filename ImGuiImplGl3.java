
import glm_.vec2.Vec2;
import glm_.vec4.Vec4;
import imgui.ImGui;
import imgui.classes.IO;
import imgui.font.FontAtlas;
import imgui.internal.DrawData;
import imgui.internal.DrawVert;
import imgui.internal.sections.DrawDataBuilder;

import java.nio.ByteBuffer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.lwjgl.opengl.GL32.*;

@SuppressWarnings("MagicNumber")
public final class ImGuiImplGl3 {
    // OpenGL Data
    private int glVersion = 0;
    private String glslVersion = "";
    private int gFontTexture = 0;
    private int gShaderHandle = 0;
    private int gVertHandle = 0;
    private int gFragHandle = 0;
    private int gAttribLocationTex = 0;
    private int gAttribLocationProjMtx = 0;
    private int gAttribLocationVtxPos = 0;
    private int gAttribLocationVtxUV = 0;
    private int gAttribLocationVtxColor = 0;
    private int gVboHandle = 0;
    private int gElementsHandle = 0;
    private int gVertexArrayObjectHandle = 0;

    // Used to store tmp renderer data
    private Vec2 displaySize = new Vec2();
    private Vec2 framebufferScale = new Vec2();
    private Vec2 displayPos = new Vec2();
    private final Vec4 clipRect = new Vec4();
    private final float[] orthoProjMatrix = new float[4 * 4];

    // Variables used to backup GL state before and after the rendering of Dear ImGui
    private final int[] lastActiveTexture = new int[1];
    private final int[] lastProgram = new int[1];
    private final int[] lastTexture = new int[1];
    private final int[] lastArrayBuffer = new int[1];
    private final int[] lastVertexArrayObject = new int[1];
    private final int[] lastViewport = new int[4];
    private final int[] lastScissorBox = new int[4];
    private final int[] lastBlendSrcRgb = new int[1];
    private final int[] lastBlendDstRgb = new int[1];
    private final int[] lastBlendSrcAlpha = new int[1];
    private final int[] lastBlendDstAlpha = new int[1];
    private final int[] lastBlendEquationRgb = new int[1];
    private final int[] lastBlendEquationAlpha = new int[1];
    private boolean lastEnableBlend = false;
    private boolean lastEnableCullFace = false;
    private boolean lastEnableDepthTest = false;
    private boolean lastEnableStencilTest = false;
    private boolean lastEnableScissorTest = false;

    public void init() {
        init(null);
    }

    /**
     * Method to do an initialization of the {@link ImGuiImplGl3} state.
     * <p>
     * Method takes an argument, which should be a valid GLSL string with the version to use.
     * <pre>
     * ----------------------------------------
     * OpenGL    GLSL      GLSL
     * version   version   string
     * ---------------------------------------
     *  2.0       110       "#version 110"
     *  2.1       120       "#version 120"
     *  3.0       130       "#version 130"
     *  3.1       140       "#version 140"
     *  3.2       150       "#version 150"
     *  3.3       330       "#version 330 core"
     *  4.0       400       "#version 400 core"
     *  4.1       410       "#version 410 core"
     *  4.2       420       "#version 410 core"
     *  4.3       430       "#version 430 core"
     *  ES 3.0    300       "#version 300 es"   = WebGL 2.0
     * ---------------------------------------
     * </pre>
     * <p>
     * If the argument is null, then a "#version 130" string will be used by default.
     *
     * @param glslVersion string with the version of the GLSL
     */
    public void init(final String glslVersion) {
        readGlVersion();
        setupBackendCapabilitiesFlags();

        if (glslVersion == null) {
            this.glslVersion = "#version 130";
        } else {
            this.glslVersion = glslVersion;
        }

        createDeviceObjects();

      //  if (ImGui.INSTANCE.getIo().hasConfigFlags(ImGuiConfigFlags.ViewportsEnable)) {
      //      initPlatformInterface();
      //  }
    }
    
    public void renderDrawData(final DrawData drawData) {
        if (drawData.getCmdLists().size() <= 0) {
            return;
        }

        // Will project scissor/clipping rectangles into framebuffer space
        displaySize = drawData.getDisplaySize();           // (0,0) unless using multi-viewports
        displayPos = drawData.getDisplayPos();
        framebufferScale = drawData.getFramebufferScale(); // (1,1) unless using retina display which are often (2,2)

        final float clipOffX = displayPos.getX();
        final float clipOffY = displayPos.getY();
        final float clipScaleX = framebufferScale.getX();
        final float clipScaleY = framebufferScale.getY();

        // Avoid rendering when minimized, scale coordinates for retina displays (screen coordinates != framebuffer coordinates)
        final int fbWidth = (int) (displaySize.getX() * framebufferScale.getX());
        final int fbHeight = (int) (displaySize.getY() * framebufferScale.getY());

        if (fbWidth <= 0 || fbHeight <= 0) {
            return;
        }

        backupGlState();
        bind(fbWidth, fbHeight);

        // Render command lists
        for (int cmdListIdx = 0; cmdListIdx < drawData.getCmdLists().size(); cmdListIdx++) {
            // Upload vertex/index buffers

            DrawDataBuilder
            glBufferData(GL_ARRAY_BUFFER, drawData.getCmdListVtxBufferData(cmdListIdx), GL_STREAM_DRAW);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, drawData.getCmdListIdxBufferData(cmdListIdx), GL_STREAM_DRAW);

            for (int cmdBufferIdx = 0; cmdBufferIdx < drawData.getCmdListCmdBufferSize(cmdListIdx); cmdBufferIdx++) {
                drawData.getCmdListCmdBufferClipRect(cmdListIdx, cmdBufferIdx, clipRect);

                final float clipMinX = (clipRect.getX() - clipOffX) * clipScaleX;
                final float clipMinY = (clipRect.getY() - clipOffY) * clipScaleY;
                final float clipMaxX = (clipRect.getZ() - clipOffX) * clipScaleX;
                final float clipMaxY = (clipRect.getW() - clipOffY) * clipScaleY;

                if (clipMaxX <= clipMinX || clipMaxY <= clipMinY) {
                    continue;
                }

                // Apply scissor/clipping rectangle (Y is inverted in OpenGL)
                glScissor((int) clipMinX, (int) (fbHeight - clipMaxY), (int) (clipMaxX - clipMinX), (int) (clipMaxY - clipMinY));

                // Bind texture, Draw
                final int textureId = drawData.getCmdListCmdBufferTextureId(cmdListIdx, cmdBufferIdx);
                final int elemCount = drawData.getCmdListCmdBufferElemCount(cmdListIdx, cmdBufferIdx);
                final int idxBufferOffset = drawData.getCmdListCmdBufferIdxOffset(cmdListIdx, cmdBufferIdx);
                final int vtxBufferOffset = drawData.getCmdListCmdBufferVtxOffset(cmdListIdx, cmdBufferIdx);
                final int indices = idxBufferOffset * drawData.getTotalIdxCount();

                glBindTexture(GL_TEXTURE_2D, textureId);

                if (glVersion >= 320) {
                    glDrawElementsBaseVertex(GL_TRIANGLES, elemCount, GL_UNSIGNED_SHORT, indices, vtxBufferOffset);
                } else {
                    glDrawElements(GL_TRIANGLES, elemCount, GL_UNSIGNED_SHORT, indices);
                }
            }
        }

        unbind();
        restoreModifiedGlState();
    }

    /**
     * Call this method in the end of your application cycle to dispose resources used by {@link ImGuiImplGl3}.
     */
    public void dispose() {
        glDeleteBuffers(gVboHandle);
        glDeleteBuffers(gElementsHandle);
        glDetachShader(gShaderHandle, gVertHandle);
        glDetachShader(gShaderHandle, gFragHandle);
        glDeleteProgram(gShaderHandle);
        glDeleteTextures(gFontTexture);
        shutdownPlatformInterface();
    }

    /**
     * Method rebuilds the font atlas for Dear ImGui. Could be used to update application fonts in runtime.
     */
    public void updateFontsTexture() {
        glDeleteTextures(gFontTexture);

        final FontAtlas fontAtlas = ImGui.INSTANCE.getIo().getFonts();
        final int width = 0;
        final int height = 0;
        final ByteBuffer buffer = fontAtlas.getTexDataAsRGBA32().component1();

        gFontTexture = glGenTextures();

        glBindTexture(GL_TEXTURE_2D, gFontTexture);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);

        fontAtlas.setTexID(gFontTexture);
    }

    private void readGlVersion() {
        final int[] major = new int[1];
        final int[] minor = new int[1];
        glGetIntegerv(GL_MAJOR_VERSION, major);
        glGetIntegerv(GL_MINOR_VERSION, minor);
        glVersion = major[0] * 100 + minor[0] * 10;
    }

    private void setupBackendCapabilitiesFlags() {
        final IO io = ImGui.INSTANCE.getIo();
        io.setBackendRendererName("imgui_java_impl_opengl3");
    }

    private void createDeviceObjects() {
        // Backup GL state
        final int[] lastTexture = new int[1];
        final int[] lastArrayBuffer = new int[1];
        final int[] lastVertexArray = new int[1];
        glGetIntegerv(GL_TEXTURE_BINDING_2D, lastTexture);
        glGetIntegerv(GL_ARRAY_BUFFER_BINDING, lastArrayBuffer);
        glGetIntegerv(GL_VERTEX_ARRAY_BINDING, lastVertexArray);

        createShaders();

        gAttribLocationTex = glGetUniformLocation(gShaderHandle, "Render.Texturing.Texture");
        gAttribLocationProjMtx = glGetUniformLocation(gShaderHandle, "ProjMtx");
        gAttribLocationVtxPos = glGetAttribLocation(gShaderHandle, "Position");
        gAttribLocationVtxUV = glGetAttribLocation(gShaderHandle, "UV");
        gAttribLocationVtxColor = glGetAttribLocation(gShaderHandle, "Color");

        // Create buffers
        gVboHandle = glGenBuffers();
        gElementsHandle = glGenBuffers();

        updateFontsTexture();

        // Restore modified GL state
        glBindTexture(GL_TEXTURE_2D, lastTexture[0]);
        glBindBuffer(GL_ARRAY_BUFFER, lastArrayBuffer[0]);
        glBindVertexArray(lastVertexArray[0]);
    }

    private void createShaders() {
        final int glslVersionValue = parseGlslVersionString();

        // Select shaders matching our GLSL versions
        final CharSequence vertShaderSource;
        final CharSequence fragShaderSource;

        if (glslVersionValue < 130) {
            vertShaderSource = getVertexShaderGlsl120();
            fragShaderSource = getFragmentShaderGlsl120();
        } else if (glslVersionValue == 300) {
            vertShaderSource = getVertexShaderGlsl300es();
            fragShaderSource = getFragmentShaderGlsl300es();
        } else if (glslVersionValue >= 410) {
            vertShaderSource = getVertexShaderGlsl410Core();
            fragShaderSource = getFragmentShaderGlsl410Core();
        } else {
            vertShaderSource = getVertexShaderGlsl130();
            fragShaderSource = getFragmentShaderGlsl130();
        }

        gVertHandle = createAndCompileShader(GL_VERTEX_SHADER, vertShaderSource);
        gFragHandle = createAndCompileShader(GL_FRAGMENT_SHADER, fragShaderSource);

        gShaderHandle = glCreateProgram();
        glAttachShader(gShaderHandle, gVertHandle);
        glAttachShader(gShaderHandle, gFragHandle);
        glLinkProgram(gShaderHandle);

        if (glGetProgrami(gShaderHandle, GL_LINK_STATUS) == GL_FALSE) {
            throw new IllegalStateException("Failed to link shader program:\n" + glGetProgramInfoLog(gShaderHandle));
        }
    }

    private int parseGlslVersionString() {
        final Pattern p = Pattern.compile("\\d+");
        final Matcher m = p.matcher(glslVersion);

        if (m.find()) {
            return Integer.parseInt(m.group());
        } else {
            throw new IllegalArgumentException("Invalid GLSL version string: " + glslVersion);
        }
    }

    private void backupGlState() {
        glGetIntegerv(GL_ACTIVE_TEXTURE, lastActiveTexture);
        glActiveTexture(GL_TEXTURE0);
        glGetIntegerv(GL_CURRENT_PROGRAM, lastProgram);
        glGetIntegerv(GL_TEXTURE_BINDING_2D, lastTexture);
        glGetIntegerv(GL_ARRAY_BUFFER_BINDING, lastArrayBuffer);
        glGetIntegerv(GL_VERTEX_ARRAY_BINDING, lastVertexArrayObject);
        glGetIntegerv(GL_VIEWPORT, lastViewport);
        glGetIntegerv(GL_SCISSOR_BOX, lastScissorBox);
        glGetIntegerv(GL_BLEND_SRC_RGB, lastBlendSrcRgb);
        glGetIntegerv(GL_BLEND_DST_RGB, lastBlendDstRgb);
        glGetIntegerv(GL_BLEND_SRC_ALPHA, lastBlendSrcAlpha);
        glGetIntegerv(GL_BLEND_DST_ALPHA, lastBlendDstAlpha);
        glGetIntegerv(GL_BLEND_EQUATION_RGB, lastBlendEquationRgb);
        glGetIntegerv(GL_BLEND_EQUATION_ALPHA, lastBlendEquationAlpha);
        lastEnableBlend = glIsEnabled(GL_BLEND);
        lastEnableCullFace = glIsEnabled(GL_CULL_FACE);
        lastEnableDepthTest = glIsEnabled(GL_DEPTH_TEST);
        lastEnableStencilTest = glIsEnabled(GL_STENCIL_TEST);
        lastEnableScissorTest = glIsEnabled(GL_SCISSOR_TEST);
    }

    private void restoreModifiedGlState() {
        glUseProgram(lastProgram[0]);
        glBindTexture(GL_TEXTURE_2D, lastTexture[0]);
        glActiveTexture(lastActiveTexture[0]);
        glBindVertexArray(lastVertexArrayObject[0]);
        glBindBuffer(GL_ARRAY_BUFFER, lastArrayBuffer[0]);
        glBlendEquationSeparate(lastBlendEquationRgb[0], lastBlendEquationAlpha[0]);
        glBlendFuncSeparate(lastBlendSrcRgb[0], lastBlendDstRgb[0], lastBlendSrcAlpha[0], lastBlendDstAlpha[0]);
        // @formatter:off CHECKSTYLE:OFF
        if (lastEnableBlend) glEnable(GL_BLEND); else glDisable(GL_BLEND);
        if (lastEnableCullFace) glEnable(GL_CULL_FACE); else glDisable(GL_CULL_FACE);
        if (lastEnableDepthTest) glEnable(GL_DEPTH_TEST); else glDisable(GL_DEPTH_TEST);
        if (lastEnableStencilTest) glEnable(GL_STENCIL_TEST); else glDisable(GL_STENCIL_TEST);
        if (lastEnableScissorTest) glEnable(GL_SCISSOR_TEST); else glDisable(GL_SCISSOR_TEST);
        // @formatter:on CHECKSTYLE:ON
        glViewport(lastViewport[0], lastViewport[1], lastViewport[2], lastViewport[3]);
        glScissor(lastScissorBox[0], lastScissorBox[1], lastScissorBox[2], lastScissorBox[3]);
    }

    // Setup desired GL state
    private void bind(final int fbWidth, final int fbHeight) {
        // Recreate the VAO every time (this is to easily allow multiple GL contexts to be rendered to. VAO are not shared among GL contexts)
        // The renderer would actually work without any VAO bound, but then our VertexAttrib calls would overwrite the default one currently bound.
        gVertexArrayObjectHandle = glGenVertexArrays();

        // Setup render state: alpha-blending enabled, no face culling, no depth testing, scissor enabled, polygon fill
        glEnable(GL_BLEND);
        glBlendEquation(GL_FUNC_ADD);
        glBlendFuncSeparate(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, GL_ONE, GL_ONE_MINUS_SRC_ALPHA);
        glDisable(GL_CULL_FACE);
        glDisable(GL_DEPTH_TEST);
        glDisable(GL_STENCIL_TEST);
        glEnable(GL_SCISSOR_TEST);

        // Setup viewport, orthographic projection matrix
        // Our visible imgui space lies from draw_data->DisplayPos (top left) to draw_data->DisplayPos+data_data->DisplaySize (bottom right).
        // DisplayPos is (0,0) for single viewport apps.
        glViewport(0, 0, fbWidth, fbHeight);
        final float left = displayPos.getX();
        final float right = displayPos.getX() + displaySize.getX();
        final float top = displayPos.getY();
        final float bottom = displayPos.getY() + displaySize.getY();

        // Orthographic matrix projection
        orthoProjMatrix[0] = 2.0f / (right - left);
        orthoProjMatrix[5] = 2.0f / (top - bottom);
        orthoProjMatrix[10] = -1.0f;
        orthoProjMatrix[12] = (right + left) / (left - right);
        orthoProjMatrix[13] = (top + bottom) / (bottom - top);
        orthoProjMatrix[15] = 1.0f;

        // Bind shader
        glUseProgram(gShaderHandle);
        glUniform1i(gAttribLocationTex, 0);
        glUniformMatrix4fv(gAttribLocationProjMtx, false, orthoProjMatrix);

        glBindVertexArray(gVertexArrayObjectHandle);

        // Bind vertex/index buffers and setup attributes for ImDrawVert
        glBindBuffer(GL_ARRAY_BUFFER, gVboHandle);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, gElementsHandle);
        glEnableVertexAttribArray(gAttribLocationVtxPos);
        glEnableVertexAttribArray(gAttribLocationVtxUV);
        glEnableVertexAttribArray(gAttribLocationVtxColor);
        glVertexAttribPointer(gAttribLocationVtxPos, 2, GL_FLOAT, false, DrawVert.Companion.getSIZE(), 0);
        glVertexAttribPointer(gAttribLocationVtxUV, 2, GL_FLOAT, false, DrawVert.Companion.getSIZE(), 8);
        glVertexAttribPointer(gAttribLocationVtxColor, 4, GL_UNSIGNED_BYTE, true, DrawVert.Companion.getSIZE(), 16);
    }

    private void unbind() {
        // Destroy the temporary VAO
        glDeleteVertexArrays(gVertexArrayObjectHandle);
    }

    //--------------------------------------------------------------------------------------------------------
    // MULTI-VIEWPORT / PLATFORM INTERFACE SUPPORT
    // This is an _advanced_ and _optional_ feature, allowing the back-end to create and handle multiple viewports simultaneously.
    // If you are new to dear imgui or creating a new binding for dear imgui, it is recommended that you completely ignore this section first..
    //--------------------------------------------------------------------------------------------------------

    private void shutdownPlatformInterface() {
        //ImGui.INSTANCE.end();
    }

    private int createAndCompileShader(final int type, final CharSequence source) {
        final int id = glCreateShader(type);

        glShaderSource(id, source);
        glCompileShader(id);

        if (glGetShaderi(id, GL_COMPILE_STATUS) == GL_FALSE) {
            throw new IllegalStateException("Failed to compile shader:\n" + glGetShaderInfoLog(id));
        }

        return id;
    }

    private String getVertexShaderGlsl120() {
        return glslVersion + "\n"
                + "uniform mat4 ProjMtx;\n"
                + "attribute vec2 Position;\n"
                + "attribute vec2 UV;\n"
                + "attribute vec4 Color;\n"
                + "varying vec2 Frag_UV;\n"
                + "varying vec4 Frag_Color;\n"
                + "void main()\n"
                + "{\n"
                + "    Frag_UV = UV;\n"
                + "    Frag_Color = Color;\n"
                + "    gl_Position = ProjMtx * vec4(Position.getX()y,0,1);\n"
                + "}\n";
    }

    private String getVertexShaderGlsl130() {
        return glslVersion + "\n"
                + "uniform mat4 ProjMtx;\n"
                + "in vec2 Position;\n"
                + "in vec2 UV;\n"
                + "in vec4 Color;\n"
                + "out vec2 Frag_UV;\n"
                + "out vec4 Frag_Color;\n"
                + "void main()\n"
                + "{\n"
                + "    Frag_UV = UV;\n"
                + "    Frag_Color = Color;\n"
                + "    gl_Position = ProjMtx * vec4(Position.getX()y,0,1);\n"
                + "}\n";
    }

    private String getVertexShaderGlsl300es() {
        return glslVersion + "\n"
                + "precision highp float;\n"
                + "layout (location = 0) in vec2 Position;\n"
                + "layout (location = 1) in vec2 UV;\n"
                + "layout (location = 2) in vec4 Color;\n"
                + "uniform mat4 ProjMtx;\n"
                + "out vec2 Frag_UV;\n"
                + "out vec4 Frag_Color;\n"
                + "void main()\n"
                + "{\n"
                + "    Frag_UV = UV;\n"
                + "    Frag_Color = Color;\n"
                + "    gl_Position = ProjMtx * vec4(Position.getX()y,0,1);\n"
                + "}\n";
    }

    private String getVertexShaderGlsl410Core() {
        return glslVersion + "\n"
                + "layout (location = 0) in vec2 Position;\n"
                + "layout (location = 1) in vec2 UV;\n"
                + "layout (location = 2) in vec4 Color;\n"
                + "uniform mat4 ProjMtx;\n"
                + "out vec2 Frag_UV;\n"
                + "out vec4 Frag_Color;\n"
                + "void main()\n"
                + "{\n"
                + "    Frag_UV = UV;\n"
                + "    Frag_Color = Color;\n"
                + "    gl_Position = ProjMtx * vec4(Position.getX()y,0,1);\n"
                + "}\n";
    }

    private String getFragmentShaderGlsl120() {
        return glslVersion + "\n"
                + "#ifdef GL_ES\n"
                + "    precision mediump float;\n"
                + "#endif\n"
                + "uniform sampler2D Render.Texturing.Texture;\n"
                + "varying vec2 Frag_UV;\n"
                + "varying vec4 Frag_Color;\n"
                + "void main()\n"
                + "{\n"
                + "    gl_FragColor = Frag_Color * texture2D(Render.Texturing.Texture, Frag_UV.st);\n"
                + "}\n";
    }

    private String getFragmentShaderGlsl130() {
        return glslVersion + "\n"
                + "uniform sampler2D Render.Texturing.Texture;\n"
                + "in vec2 Frag_UV;\n"
                + "in vec4 Frag_Color;\n"
                + "out vec4 Out_Color;\n"
                + "void main()\n"
                + "{\n"
                + "    Out_Color = Frag_Color * texture(Render.Texturing.Texture, Frag_UV.st);\n"
                + "}\n";
    }

    private String getFragmentShaderGlsl300es() {
        return glslVersion + "\n"
                + "precision mediump float;\n"
                + "uniform sampler2D Render.Texturing.Texture;\n"
                + "in vec2 Frag_UV;\n"
                + "in vec4 Frag_Color;\n"
                + "layout (location = 0) out vec4 Out_Color;\n"
                + "void main()\n"
                + "{\n"
                + "    Out_Color = Frag_Color * texture(Render.Texturing.Texture, Frag_UV.st);\n"
                + "}\n";
    }

    private String getFragmentShaderGlsl410Core() {
        return glslVersion + "\n"
                + "in vec2 Frag_UV;\n"
                + "in vec4 Frag_Color;\n"
                + "uniform sampler2D Render.Texturing.Texture;\n"
                + "layout (location = 0) out vec4 Out_Color;\n"
                + "void main()\n"
                + "{\n"
                + "    Out_Color = Frag_Color * texture(Render.Texturing.Texture, Frag_UV.st);\n"
                + "}\n";
    }
}
