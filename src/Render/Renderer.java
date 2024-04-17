package Render;

import Game.Action.Ability;
import Game.Entities.Able;
import Game.Entities.Projectiles.Projectile;
import Render.Entity.Camera.Camera;
import Render.Entity.Entity2D;
import Render.MeshData.Texturing.ColorReplacement;
import Render.MeshData.Texturing.Font;
import Render.MeshData.Texturing.TextPosParams;
import Render.Entity.Interactable.Button;
import Render.Entity.Interactable.Interactable;
import Render.Entity.Interactable.Label;
import Render.MeshData.Shader.Shader;
import Render.MeshData.*;
import Render.MeshData.Model.ObjModel;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL43;

import java.util.ArrayList;
import java.util.function.Function;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL43.*;

public class Renderer { // TODO: drawUI method to draw absolute positioned UI elements

    private Shader currentShader;
    private int mode = GL_FILL;
    private Camera camera;

    public Renderer() {
        currentShader = new Shader("res/shaders/default.shader");
        Shader.DEFAULT.forceBind();
        camera = new Camera();
        glPolygonMode(GL_FRONT_AND_BACK, mode);
    }

    ////////// DRAW //////////
    public void draw(VertexArray va, IndexBuffer ib) {
        va.bind();
        ib.bind();

        glDrawElements(GL_TRIANGLES, ib.getCount(), GL_UNSIGNED_INT, 0);
    }
    public void draw(VertexArray va, IndexBuffer ib, Vector4f color) {
        SetUniforms(currentShader, null, color);
        draw(va, ib);
    }
    public void draw(VertexArray va, IndexBuffer ib, Matrix4f colorSwaps) {
        SetUniforms(currentShader, null, colorSwaps);
        draw(va, ib);
    }
    public void draw(VertexArray va, IndexBuffer ib, Shader shader) {
        shader.bind();
        SetUniforms(shader, null);
        draw(va, ib);
    }


    public Batch drawText(String text, Vector2f pos, float size, Font font, Shader shader, Function<TextPosParams, Vector2f> layoutingFunction, ColorReplacement cR, Integer maxWidth) {
        if(text == null || text.isEmpty()) return null;
        if(font == null) font = Font.RETRO;
        if(shader == null) shader = Shader.TEXTURING;
        if(size <= 0) size = 1;
        if(pos == null) pos = new Vector2f(0, 0);
        if(layoutingFunction == null) layoutingFunction = TextPosParams::getPos;

        setCurrentShader(shader);
        font.getTexture().bind();

        pos = layoutingFunction.apply(new TextPosParams(pos, new Vector2f(size), font, text, null, maxWidth != null ? maxWidth : Integer.MAX_VALUE));

        Vector2f scale = new Vector2f(size);
        float characterAspect = font.getCharacterAspect();
        scale.x *= characterAspect;


        float[][][] texCoordArr = new float[text.length()][][];
        ObjModel model = ObjModel.SQUARE.clone();

        ArrayList<Integer> newLineIndices = new ArrayList<>();
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == '\n')
                newLineIndices.add(i);
            texCoordArr[i] = font.getTexCoords(text.charAt(i));
        }

        VertexArray va = new VertexArray();
        int totalVertices = model.getVertexCount() * texCoordArr.length * Vertex.SIZE;
        int totalIndices  = model.getIndexCount()  * texCoordArr.length;

        // Create combined vertex and index buffers
        VertexBuffer vb = new VertexBuffer(totalVertices);
        IndexBuffer ib = new IndexBuffer(totalIndices);

        long vertexOffset = 0;
        long indexOffset = 0;

        // Append data from each entity's buffers to the combined buffers
        short[][][] faces = model.getFaces();
        float[][] positions = model.getPositions();
        int xOffset = 0;

        for (int i = 0; i < texCoordArr.length; i++) {
            float[] data = new float[model.getVertexCount()*Vertex.SIZE];
            int[] indices = new int[model.getIndexCount()];

            if (newLineIndices.contains(i)) {
                pos.x -= (((xOffset + 1f) * scale.x * 2));
                pos.y -= scale.y * 2;
                xOffset = 0;
                continue;
            }

            short dataIndex = 0;
            for (short[][] face : faces) {
                for (short k = 0; k < face.length; k++) {
                    float[] position = positions[face[k][0] - 1];
                    data[dataIndex++] = position[0] * scale.x  + pos.x + scale.x * 2 * i;
                    data[dataIndex++] = position[1] * scale.y  + pos.y +  - scale.y / characterAspect * 2; // offset.y * i
                    data[dataIndex++] = position[2];

                    if (Vertex.SIZE > 3) {
                        if (face[k].length > 1) {
                            float[] texture = texCoordArr[i][face[k][1]-1];
                            data[dataIndex++] = texture[0];
                            data[dataIndex++] = texture[1];
                        } else {
                            dataIndex += 2;
                        }
                    }
                    indices[dataIndex / Vertex.SIZE -1] = (short) (dataIndex / Vertex.SIZE  - 1) + (int)indexOffset/4;
                }
            }
            xOffset++;

            vb.update(data, vertexOffset);
            ib.update(indices, indexOffset);

            vertexOffset += data.length    * 4L;
            indexOffset += indices.length  * 4L;
        }

        va.addBuffer(vb, Vertex.getLayout());
        if(cR != null)
            draw(va, ib, cR.getSwappingMatrix());
        else
            draw(va, ib, currentShader);

        return new Batch(va, ib);
    }
    public Batch drawText(TextPosParams tp, Shader shader, Function<TextPosParams, Vector2f> layoutingFunction, ColorReplacement cR) {
        if(shader == null) shader = Shader.TEXTURING;
        if(layoutingFunction == null) layoutingFunction = TextPosParams::getPos;

        tp.pos = layoutingFunction.apply(tp);
        return drawText(tp.text, tp.pos, tp.size.x, tp.font, shader, null, cR, tp.maxWidth);
    }
    public Batch drawText(String text, Vector2f pos, float scale, Font font) {
        return drawText(text, pos, scale, font, null, null, null, null);
    }
    public Batch drawText(String text, Vector2f pos, float scale) {
        return drawText(text, pos, scale, null, null, null, null, null);
    }


    // TODO: make this work fully
    public void drawInstanced(Entity2D entity, Matrix4f[] modelMatrices) {
        chooseShader(entity);
        SetUniforms(currentShader, entity);

        // choose Texture
        if(entity.getTexture() != null)
            entity.getTexture().bind();
        // choose Model
        ObjModel model = entity.getModel();
        assert model != null : "[ERROR] (Render.Renderer.DrawEntity2D) Entity2D has no model";
        // choose VertexArray and IndexBuffer
        VertexArray va = new VertexArray();
        va.addBuffer(model.getVertexBuffer(), Vertex.getLayout());
        IndexBuffer ib = model.getIndexBuffer();

        //// setup instance VBO
        // collect matrices into array
        float[] modelMatricesArr = new float[modelMatrices.length * 4 * 4];
        for (int i = 0; i < modelMatrices.length; i++) {
            float[] m = new float[16];
            modelMatrices[i].get(m);
            System.arraycopy(m, 0, modelMatricesArr, i * 16, 16);
        }
        //
        VertexBuffer vb = new VertexBuffer(modelMatricesArr);

        VertexBufferLayout layout = new VertexBufferLayout();
        layout.pushF(4);
        layout.pushF(4);
        layout.pushF(4);
        layout.pushF(4);

        va.addBufferI(vb, layout);

        va.bind();
        ib.bind();

        glDrawElementsInstanced(GL_TRIANGLES, ib.getCount(), GL_UNSIGNED_INT, 0, modelMatrices.length);
    }

    public void drawBatch(Batch b) {
        SetUniforms(currentShader, null);
        draw(b.va, b.ib);
    }
    public <T extends Entity2D> void draw(T entity) {
        assert entity != null : "[ERROR] (Render.Renderer.DrawEntity2D) Entity2D is null";
        if(entity.isHidden()) return;

        chooseShader(entity);
        SetUniforms(currentShader, entity);

        // choose Texture
        if(entity.getTexture() != null)
            entity.getTexture().bind();
        if(entity.getAnimation() != null) {
            entity.getAnimation().getAtlas().getTexture().bind();
            entity.getModel().replaceTextureCoords(entity.getAnimation().getTexCoords());
        }
        // choose Model
        ObjModel model = entity.getModel();
        assert model != null : "[ERROR] (Render.Renderer.DrawEntity2D) Entity2D has no model";
        // choose VertexArray and IndexBuffer
        VertexArray va = new VertexArray();
        va.addBuffer(model.getVertexBuffer(), Vertex.getLayout());
        IndexBuffer ib = model.getIndexBuffer();

        va.bind();
        ib.bind();

        glDrawElements(GL_TRIANGLES, ib.getCount(), GL_UNSIGNED_INT, 0);

        //draw abilities
        if (entity instanceof Able able) {
            if(able.getAbilities() == null ) return;
            for(Ability ability : able.getAbilities()) {
                for(Projectile projectile : ability.getProjectiles()) {
                    draw(projectile);
                }
            }
        }
    }
    public <T extends Entity2D> void draw(T[] entities) {
        for (T entity : entities) {
            if(entity != null)
                draw(entity);
        }
    }
    public <T extends Entity2D> void draw(ArrayList<T> entities) {
        for (T entity : entities) {
            if(entity != null)
                draw(entity);
        }
    }

    public <T extends Entity2D> void drawUI(Entity2D entity) {
        entity.setOffset(camera.getPosition().mul(-1, new Vector2f()));
        draw(entity);
    }
    public <T extends Entity2D> void drawUI(T[] entities) {
        for (T entity : entities) {
            if(entity != null)
                drawUI(entity);
        }
    }
    public <T extends Button> void draw(T button) {
        this.draw((Entity2D) button);

        Label label = button.getLabel();
        drawText(
            new TextPosParams(
                    button.getPosition(),
                    new Vector2f(label.getScale()),
                    label.getFont(),
                    label.getText(),
                    null,
                    (int)(button.getScale().x-label.getFont().getCharWidth()*3)
            ), Shader.TEXTURING, Font::centerFirstLine_UI_MaxLength, button.getColorReplacement());

        if(button.getState() != Interactable.States.HOVER) return;
        if(!button.shouldDisplayTooltip()) return;
        if(button.getTooltip() == null) return;
        if(button.getTooltip().getText().isEmpty()) return;

        Label tooltip = button.getTooltip();
        drawText(
            new TextPosParams(
                screenToWorldCoords(tooltip.getScreenPosition()),
                new Vector2f(tooltip.getScale()),
                tooltip.getFont(),
                tooltip.getText(),
                null
            ), Shader.TEXTURING, null, null
        );
    }

    public Batch setupBatch(Entity2D[] entities) {
        VertexArray va = new VertexArray();
        int totalVertices = 0;
        int totalIndices = 0;

        // Calculate total size of all vertex and index buffers
        for (Entity2D entity : entities) {
            if (entity == null) continue;
            totalVertices += entity.getModel().getVertexCount();
            totalIndices += entity.getModel().getIndexCount();
        }

        // Create combined vertex and index buffers
        VertexBuffer vb = new VertexBuffer(totalVertices*Vertex.SIZE);
        IndexBuffer ib = new IndexBuffer(totalIndices);

        long vertexOffset = 0;
        long indexOffset = 0;

        // Append data from each entity's buffers to the combined buffers
        for (Entity2D entity : entities) {
            if (entity == null) continue;
            ObjModel model = entity.getModel();

            float[] data = model.getVertexBufferData().clone(); // need to be cloned to protect ObjModel data from being set off arbitrarily
            int[] indices = model.getIndexBufferData().clone(); // needs to be offset by the number of vertices in the previous entities

            // calculate actual positions of vertices through model matrices
            int dataIndex = 0;
            for (int j = 0; j < indices.length; j++) { // TODO: apply rotation
                data[dataIndex++] = data[dataIndex - 1] * entity.getScale().x + entity.getPosition().x + entity.getOffset().x ;
                data[dataIndex++] = data[dataIndex - 1] * entity.getScale().y + entity.getPosition().y + entity.getOffset().y;
                dataIndex += Vertex.SIZE - 2;

                indices[j] += (int)indexOffset/4;
            }

            vb.update(data, vertexOffset);
            ib.update(indices, indexOffset);

            vertexOffset += data.length    * 4L;
            indexOffset += indices.length  * 4L;
        }

        va.addBuffer(vb, Vertex.getLayout());
        return new Batch(va, ib);
    }
    private Batch _setupBatch(float[][][] texCoordArr, Entity2D base, Vector2f offset) {
        assert base != null : "[ERROR] (Render.Renderer.setupBatch) base entity is null";
        assert base.getModel() != null : "[ERROR] (Render.Renderer.setupBatch) base entity has no model";
        assert texCoordArr != null : "[ERROR] (Render.Renderer.setupBatch) textureCoords array is null";
        assert texCoordArr.length > 0 : "[ERROR] (Render.Renderer.setupBatch) textureCoords array is empty";
        assert texCoordArr[0][0].length == 2 : "[ERROR] (Render.Renderer.setupBatch) textureCoords array has wrong dimensions";
        assert offset != null : "[ERROR] (Render.Renderer.setupBatch) offset is null";

        ObjModel model = base.getModel();
        setCurrentShader(base.getShader());

        VertexArray va = new VertexArray();
        int totalVertices = model.getVertexCount() * texCoordArr.length * Vertex.SIZE;
        int totalIndices  = model.getIndexCount()  * texCoordArr.length;

        // Create combined vertex and index buffers
        VertexBuffer vb = new VertexBuffer(totalVertices);
        IndexBuffer ib = new IndexBuffer(totalIndices);

        long vertexOffset = 0;
        long indexOffset = 0;

        // Append data from each entity's buffers to the combined buffers
        short[][][] faces = model.getFaces();
        float[][] positions = model.getPositions();
        int xOffset = 0;

        for (int i = 0; i < texCoordArr.length; i++) {
            float[] data = new float[model.getVertexCount()*Vertex.SIZE];
            int[] indices = new int[model.getIndexCount()];

            short dataIndex = 0;
            for (short[][] face : faces) {
                for (short k = 0; k < face.length; k++) {
                    float[] position = positions[face[k][0] - 1];
                    data[dataIndex++] = position[0] * base.getScale().x  + base.getPosition().x + offset.x * i - (((xOffset + 1f) * base.getScale().x * 2));
                    data[dataIndex++] = position[1] * base.getScale().y  + base.getPosition().y + offset.y * i - base.getScale().y /* / characterAspect */ * 2; //TODO: sum up drawText and this into one???
                    data[dataIndex++] = position[2];

                    if (Vertex.SIZE > 3) {
                        if (face[k].length > 1) {
                            float[] texture = texCoordArr[i][face[k][1]-1]; // potential optimisation:
                            data[dataIndex++] = texture[0];
                            data[dataIndex++] = texture[1];
                        } else {
                            dataIndex += 2;
                        }
                    }
                    indices[dataIndex / Vertex.SIZE -1] = (short) (dataIndex / Vertex.SIZE  - 1) + (int)indexOffset/4;
                }
            }
            xOffset++;

            vb.update(data, vertexOffset);
            ib.update(indices, indexOffset);

            vertexOffset += data.length    * 4L;
            indexOffset += indices.length  * 4L;
        }

        va.addBuffer(vb, Vertex.getLayout());
        return new Batch(va, ib);
    } // delete eventually? texture-attlassing

    // TODO: investigate performance gain of CPU side MVP calculation
    public void SetUniforms(Shader shader, Entity2D entity) {
        Matrix4f modelMatrix;
        if(entity == null || entity.equals(camera))
            modelMatrix = camera.calcModelMatrix();
        else
            modelMatrix = entity.calcModelMatrix().mul(camera.calcModelMatrix());

        shader.setUniformMat4f("uModel", modelMatrix);
        shader.setUniformMat4f("uView", camera.calcViewMatrix());
        shader.setUniformMat4f("uProj", camera.getProjectionMatrix());

        if(shader.hasUniform("uColor")) {
            if(entity != null)
                shader.setUniform4f("uColor", entity.getColor());
            else
                shader.setUniform4f("uColor",0.976f, 0.164f, 0.976f, 1.0f); // default no-texture-or-color-pink
        }
        if(shader.hasUniform("uColors")) {
            if(entity != null && entity.getColorReplacement() != null)
                shader.setUniformMat4f("uColors", entity.getColorReplacement().getSwappingMatrix());
            else
                shader.setUniformMat4f("uColors", ColorReplacement.NO_SWAP_MATRIX);
        }
        if(shader.hasUniform("uResolution"))
            shader.setUniform2f("uResolution", Window.dim.x, Window.dim.y);
        if(shader.hasUniform("uTime"))
            shader.setUniform1f("uTime",  ((System.currentTimeMillis() % 10000) / 1000.0f));
    }
    public void SetUniforms(Shader shader, Entity2D entity, Vector4f color) {
        SetUniforms(shader, entity);
        if(shader.hasUniform("uColor")) {
            shader.setUniform4f("uColor", color.x, color.y, color.z, color.w);
        }
    }
    public void SetUniforms(Shader shader, Entity2D entity, Matrix4f colors) {
        SetUniforms(shader, entity);
        if(shader.hasUniform("uColors"))
            shader.setUniformMat4f("uColors", colors);
    }

    ///// PRIMITIVES /////
    public void drawPoint(Vector2f pos, float size, Vector4f color) {
        Shader.DEFAULT.bind();
        SetUniforms(Shader.DEFAULT, null, color);

        GL43.glPointSize(size);
        GL43.glBegin(GL_POINTS);
        GL43.glVertex2f(pos.x, pos.y);
        GL43.glEnd();
    }
    public void drawPoint(Vector2f pos, float size) {
        drawPoint(pos, size, new Vector4f(1, 1, 1, 1));
    }
    public void drawPoint(Vector2f pos) {
        drawPoint(pos, 2);
    }

    public void drawPoints(Vector2f[] positions, float size, Vector4f color) {
        Shader.DEFAULT.bind();
        SetUniforms(Shader.DEFAULT, null, color);

        GL43.glPointSize(size);
        GL43.glBegin(GL_POINTS);
        for (Vector2f pos : positions) {
            GL43.glVertex2f(pos.x, pos.y);
        }
        GL43.glEnd();
    }
    public void drawPoints(Vector2f[] positions, float size) {
        drawPoints(positions, size, new Vector4f(1, 1, 1, 1));
    }

    public void drawLine(Vector2f from, Vector2f to, float size, Vector4f color) {
        Shader.DEFAULT.bind();
        SetUniforms(Shader.DEFAULT, null, color);

        GL43.glLineWidth(size);
        GL43.glBegin(GL_LINES);
        GL43.glVertex2f(from.x , from.y);
        GL43.glVertex2f(to.x , to.y);
        GL43.glEnd();
    }
    public void drawLine(Vector2f from, Vector2f to, float size) {
        drawLine(from, to, size, new Vector4f(1, 1, 1, 1));
    }
    public void drawLine(Vector2f from, Vector2f to) {
        drawLine(from, to, 2);
    }

    public void drawLinesConnected(Vector2f[] positions, float size, boolean loop, Vector4f color) {
        Shader.DEFAULT.bind();
        SetUniforms(Shader.DEFAULT, null, color);

        GL43.glLineWidth(size);
        GL43.glBegin(loop ? GL_LINE_LOOP : GL_LINE_STRIP);
        for (Vector2f pos : positions) {
            GL43.glVertex2f(pos.x, pos.y);
        }
        GL43.glEnd();
    }
    public void drawLinesConnected(Vector2f[] positions, float size, boolean loop) {
        drawLinesConnected(positions, size, loop, new Vector4f(1, 1, 1, 1));
    }
    public void drawLinesConnected(Vector2f[] positions, float size) {
        drawLinesConnected(positions, size, false);
    }
    public void drawLinesConnected(Vector2f[] positions) {
        drawLinesConnected(positions, 2);
    }

    public void drawRect(Vector2f pos, Vector2f dim, Vector4f color) {
        Shader.DEFAULT.bind();
        SetUniforms(Shader.DEFAULT, null, color);

        drawLinesConnected(new Vector2f[] {
                new Vector2f(pos.x, pos.y),
                new Vector2f(pos.x + dim.x, pos.y),
                new Vector2f(pos.x + dim.x, pos.y + dim.y),
                new Vector2f(pos.x, pos.y + dim.y)
        }, 2, true, color);
    }
    public void drawRect(Vector2f pos, Vector2f dim) {
        drawRect(pos, dim, new Vector4f(1, 1, 1, 1));
    }
    public void drawRect(float x, float y, float w, float h) {
        drawRect(new Vector2f(x, y), new Vector2f(w, h));
    }
    public void drawRect(Vector4f rect) {
        drawRect(new Vector2f(rect.x, rect.y), new Vector2f(rect.z, rect.w));
    }
    public void drawRect(Vector4f rect, Vector4f color) {
        drawRect(new Vector2f(rect.x, rect.y), new Vector2f(rect.z, rect.w), color);
    }

    public void fillRect(Vector2f pos, Vector2f dim, Vector4f color) {
        Shader.DEFAULT.bind();
        SetUniforms(Shader.DEFAULT, null, color);

        GL43.glBegin(GL_QUADS);
        GL43.glVertex2f(pos.x, pos.y);
        GL43.glVertex2f(pos.x + dim.x, pos.y);
        GL43.glVertex2f(pos.x + dim.x, pos.y + dim.y);
        GL43.glVertex2f(pos.x, pos.y + dim.y);
        GL43.glEnd();
    }
    public void fillRect(Vector2f pos, Vector2f dim) {
        fillRect(pos, dim, new Vector4f(1, 1, 1, 1));
    }
    public void drawCircle(Vector2f pos, float radius, Vector4f color) {
        Shader.DEFAULT.bind();
        SetUniforms(Shader.DEFAULT, null, color);

        GL43.glBegin(GL_LINE_LOOP);
        for (int i = 0; i < 360; i++) {
            float angle = (float) Math.toRadians(i);
            GL43.glVertex2f(pos.x + (float) Math.cos(angle) * radius, pos.y + (float) Math.sin(angle) * radius);
        }
        GL43.glEnd();
    }
    public void drawCircle(Vector2f pos, float radius) {
        drawCircle(pos, radius, new Vector4f(1, 1, 1, 1));
    }

    ////////// DEBUGS //////////
    public <T extends Entity2D>void drawCollisionAABB(T entity) {
        ObjModel model = entity.getModel();
        Vector2f scale = entity.getScale();
        if(model == null) return;
        Vector4f rect = model.getBoundingBox();
        Vector2f pos = entity.getPosition();
        float x = pos.x + rect.x * scale.x;
        float y = pos.y + rect.y * scale.y;
        float w = rect.z * scale.x;
        float h = rect.w * scale.y;
        drawRect(new Vector4f(x, y, w, h), new Vector4f(1, 0, 0, 1));
    }
    public <T extends Entity2D>void drawCollisionCircle(T entity) {
        ObjModel model = entity.getModel();
        Vector2f scale = entity.getScale();
        if(model == null) return;
        float r = scale.x;
        drawCircle(entity.getPosition(), r, new Vector4f(1, 0, 0, 1));
    }
    private final Vector4f trans = new Vector4f();
    public <T extends Entity2D> void drawCollisionRect(T entity) { // TODO: rotate the rect correctly
        assert entity.getModel() != null: "[ERROR] (Render.Renderer.drawCollisionRect) Entity2D has no model";

        ObjModel model = entity.getModel();
        Vector2f scale = entity.getScale();

        Vector4f rect1 = model.getBoundingBox();

        trans.x = rect1.x;
        trans.y = rect1.y;

        entity.calcModelMatrix().transform(trans);
        rect1.x = trans.x;
        rect1.y = trans.y;

        rect1.z *= scale.x;
        rect1.w *= scale.y;

        drawRect(new Vector4f(rect1.x, rect1.y, rect1.z, rect1.w), new Vector4f(1, 0, 0, 1));
    }

    public <T extends Entity2D>  void drawCollisionRectRotated(T entity) {
        System.err.println("drawCollisionRectRotated not implemented yet");
        // TODO: transform each corner seperately and use drawLinesConnected
    }

    ///////// HELPERS //////////
    public void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
    }
    /**
     * Choose the shader to use for rendering
     * @param entity whose shader is chosen,<br> if the <b>entity has no shader</b> the camera's shader is chosen,<br> if the <b>camera has no shader</b> the default shader is chosen
     */
    public void chooseShader(Entity2D entity){
        assert entity != null && camera != null : "[ERROR] (Render.Renderer.chooseShader) No entity to choose shader from (null)";

        if /**/ (entity.getShader() != null)
            entity.getShader().bind();
        else if (camera.getShader() != null)
            camera.getShader().bind();
        else
            Shader.DEFAULT.bind();
    }
    public Vector2f screenToWorldCoords(Vector2f screenCoords) { // TODO: adapt to moving camera
        // copy vec
        Vector4f projectedCoords = new Vector4f(screenCoords, 0, 1);
        // normalize
        projectedCoords.x = (projectedCoords.x / Window.dim.x) * 2 - 1;
        projectedCoords.y = (projectedCoords.y / Window.dim.y) * -2 + 1;
        // "3D to 2D" (inverse of projection matrix "2D to 3D")
        projectedCoords.mul(camera.getProjectionMatrix().invert(new Matrix4f()));
        return new Vector2f(projectedCoords.x, projectedCoords.y).sub(camera.getPosition());
    }

    public void toggleCursor() {
        
    }
    public void cursorHide() {
        glfwSetInputMode(Window.getWindowPtr(), GLFW_CURSOR, GLFW_CURSOR_HIDDEN);
    }
    public void cursorShow() {
        glfwSetInputMode(Window.getWindowPtr(), GLFW_CURSOR, GLFW_CURSOR_NORMAL);
    }

    ///////// GETTERS & SETTERS //////////
    public void setCamera(Camera camera) {
        this.camera = camera;
    }
    public void setCurrentShader(Shader currentShader) {
        if(!this.currentShader.equals(currentShader)) {
            this.currentShader = currentShader;
            currentShader.forceBind();
        }
    }
    public void setMode(int mode) {
        this.mode = mode;
        glPolygonMode(GL_FRONT_AND_BACK, mode);
    }
    public void setCursorMode(int mode) {
        glfwSetInputMode(Window.getWindowPtr(), GLFW_CURSOR, mode);
    }
    public Camera getCamera() {
        return camera;
    }
    public Shader getCurrentShader() {
        return currentShader;
    }
    public int getMode() {
        return mode;
    }
}
