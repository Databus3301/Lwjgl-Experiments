
import glm_.vec2.Vec2;
import glm_.vec2.Vec2i;
import glm_.vec4.Vec4;
import gln.cap.Caps;
import imgui.Cond;
import imgui.Flag;
import imgui.ImGui;
import imgui.MutableReference;
import imgui.classes.Context;
import imgui.classes.IO;
import imgui.impl.gl.ImplGL3;
import imgui.impl.glfw.ImplGlfw;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;
import uno.gl.GlWindow;
import uno.glfw.GlfwWindow;
import uno.glfw.Hints;
import uno.glfw.VSync;

import static gln.GlnKt.glClearColor;
import static gln.GlnKt.glViewport;
import static imgui.ImguiKt.DEBUG;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;

public class OpenGL3 {

    // The window handle
    private GlWindow window;
    private Context ctx;

    private uno.glfw.glfw glfw = uno.glfw.glfw.INSTANCE;
    private ImGui imgui = ImGui.INSTANCE;
    private IO io;

    private ImplGlfw implGlfw;
    private ImplGL3 implGl3;

    public static void main(String[] args) {
        new OpenGL3();
    }

    private OpenGL3() {

        // Setup window
        GLFW.glfwSetErrorCallback((error, description) -> System.out.println("Glfw Error " + error + ": " + description));
        glfw.init();
        Hints.Context hintCtx = glfw.getHints().getContext();
        hintCtx.setDebug(DEBUG);

        ImplGL3.Companion.getData().setGlslVersion(130);
        hintCtx.setVersion("3.0");

        // Create window with graphics context
        GlfwWindow glfwWindow = GlfwWindow.create(1280, 720, "Dear ImGui GLFW+OpenGL3 OpenGL example", MemoryUtil.NULL, null, new Vec2i(30));
        window = new GlWindow(glfwWindow, Caps.Profile.COMPATIBILITY, true);
        window.makeCurrent(true);
        glfw.setSwapInterval(VSync.ON);   // Enable vsync

        // Initialize OpenGL loader
        GL.createCapabilities();

        // Setup Dear ImGui context
        ctx = new Context(null);

        // Setup Dear ImGui style
        imgui.styleColorsDark(null);

        // Setup Platform/Render.Renderer backends
        implGlfw = new ImplGlfw(window, true, null);
        implGl3 = new ImplGL3();

        io = imgui.getIo();

        // On Application close
        implGlfw.shutdown();
        implGl3.shutdown();
        ctx.destroy();

        window.destroy();
        glfw.terminate();
    }

    private void mainLoop() {

        // Start the Dear ImGui frame
        implGl3.newFrame();
        implGlfw.newFrame();

        imgui.newFrame();

        imgui.text("Application average %.3f ms/frame (%.1f FPS)", 1_000f / io.getFramerate(), io.getFramerate());

        // 2. Show another simple window. In most cases you will use an explicit begin/end pair to name the window.
        if (showAnotherWindow.get()) {
            imgui.begin("Another Render.Window.Render.Window", showAnotherWindow, Flag.none());
            imgui.text("Hello from another window!");
            if (imgui.button("Close Me", new Vec2()))
                showAnotherWindow.set(false);
            imgui.end();
        }

    /*  3. Show the ImGui demo window. Most of the sample code is in imgui.showDemoWindow().
            Read its code to learn more about Dear ImGui!  */
        if (showDemo[0]) {
        /*  Normally user code doesn't need/want to call this because positions are saved in .ini file anyway.
                Here we just want to make the demo initial state a bit more friendly!                 */
            imgui.setNextWindowPos(new Vec2(650, 20), Cond.FirstUseEver.INSTANCE, new Vec2());
            imgui.showDemoWindow(showDemo);
        }

        // Rendering
        imgui.render();
        glViewport(window.getFramebufferSize());
        glClearColor(clearColor);
        glClear(GL_COLOR_BUFFER_BIT);
        implGl3.renderDrawData(imgui.getDrawData());
    }
}

