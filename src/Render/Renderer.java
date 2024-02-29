package Render;

import Render.Entity.Camera.Camera;
import Render.Entity.Entity2D;
import Render.Entity.Projectile;
import Render.Entity.Texturing.Font;
import Render.Shader.Shader;
import Render.Vertices.*;
import Render.Vertices.Model.ObjModel;
import Render.Vertices.Model.ObjModelParser;
import Render.Window.Window;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL43;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL43.*;

public class Renderer {

    public Shader currentShader;
    public int mode = GL_FILL;
    Camera camera;

    public Renderer() {
        currentShader = new Shader("res/shaders/default.shader");
        Shader.DEFAULT.forceBind();
        camera = new Camera();
        glPolygonMode(GL_FRONT_AND_BACK, mode);
    }
    public void draw(VertexArray va, IndexBuffer ib, Shader shader) {
        shader.bind();

        va.bind();
        ib.bind();

        glDrawElements(GL_TRIANGLES, ib.getCount(), GL_UNSIGNED_INT, 0);
    }

    public void draw(VertexArray va, IndexBuffer ib) {
        SetUniforms(currentShader, null);

        va.bind();
        ib.bind();

        glDrawElements(GL_TRIANGLES, ib.getCount(), GL_UNSIGNED_INT, 0);
    }

    public void drawText(String text, Font font, Shader shader, Vector2f pos, Vector2f scale) {
        shader.bind();

        float characterAspect = font.getCharacterAspect();
        scale.x *= characterAspect;

        //Entity2D[] eChars = new Entity2D[text.length()];
        float[][][] texCoordArr = new float[text.length()][][];
        Entity2D base = new Entity2D(ObjModel.SQUARE.clone());
        base.scale(scale);
        base.setPosition(pos);
        base.setShader(shader);

        int xOffset = 0;

//        for (int i = 0; i < text.length(); i++) {
//            if (text.charAt(i) == '\n') {
//                pos.y -= scale.y/characterAspect*2;
//                pos.x -= (((xOffset+1f) * scale.x*2));
//                xOffset = 0;
//                continue;
//            }
//
//            ObjModel model = base.clone();
//            eChars[i] = new Entity2D(new Vector2f(pos.x + i * scale.x*2, pos.y), model, font.getTexture(), shader);
//            eChars[i].scale(scale);
//            eChars[i].getModel().setTextures(font.getCharTexCoords(text.charAt(i)));
//            xOffset++;
//        }
        for (int i = 0; i < text.length(); i++) {
//            if (text.charAt(i) == '\n') {
//                pos.y -= scale.y / characterAspect * 2;
//                pos.x -= (((xOffset + 1f) * scale.x * 2));
//                xOffset = 0;
//                continue;
//            }

            texCoordArr[i] = font.getCharTexCoords(text.charAt(i));
            //xOffset++;
        }

        Batch batch = setupBatch(texCoordArr, base, new Vector2f(scale.x*2, 0));
        drawBatch(batch);
    }
    
    public void drawText(String text, Vector2f pos, Vector2f scale) {
        setCurrentShader(Shader.TEXTURING);
        drawText(text, Font.RETRO, currentShader, pos, scale);
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

        b.ib.bind();
        b.va.bind();

        glDrawElements(GL_TRIANGLES, b.ib.getCount(), GL_UNSIGNED_INT, 0);
    }
    public void drawEntity2D(Entity2D entity) {
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

        va.bind();
        ib.bind();

        glDrawElements(GL_TRIANGLES, ib.getCount(), GL_UNSIGNED_INT, 0);
    }
    public void drawEntities2D(Entity2D[] entities) {
        for (Entity2D entity : entities) {
            if(entity != null)
                drawEntity2D(entity);
        }
    }
    public void drawEntities2D(ArrayList<Entity2D> entities) {
        for (Entity2D entity : entities) {
            if(entity != null) {
                drawEntity2D(entity);
            }

        }
    }
    public void drawProjectiles(ArrayList<Projectile> projectiles) {
        for (Projectile projectile : projectiles) {
            if(projectile != null) {
                drawEntity2D(projectile);
            }
        }
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
        VertexBuffer vb = new VertexBuffer(totalVertices);
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
                data[dataIndex++] = data[dataIndex - 1] * entity.getScale().x + entity.getPosition().x;
                data[dataIndex++] = data[dataIndex - 1] * entity.getScale().y  + entity.getPosition().y;
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
    public Batch setupBatch(float[][][] texCoordArr, Entity2D base, Vector2f offset) {
        assert base != null : "[ERROR] (Render.Renderer.setupBatch) base entity is null";
        assert base.getModel() != null : "[ERROR] (Render.Renderer.setupBatch) base entity has no model";
        assert texCoordArr != null : "[ERROR] (Render.Renderer.setupBatch) textureCoords array is null";
        assert texCoordArr.length > 0 : "[ERROR] (Render.Renderer.setupBatch) textureCoords array is empty";
        assert texCoordArr[0][0].length == 2 : "[ERROR] (Render.Renderer.setupBatch) textureCoords array has wrong dimensions";
        assert offset != null : "[ERROR] (Render.Renderer.setupBatch) offset is null";

        ObjModel model = base.getModel();
        setCurrentShader(base.getShader());

        VertexArray va = new VertexArray();
        int totalVertices = model.getVertexCount() * texCoordArr.length;
        int totalIndices  = model.getIndexCount()  * texCoordArr.length;

        // Create combined vertex and index buffers
        VertexBuffer vb = new VertexBuffer(totalVertices);
        IndexBuffer ib = new IndexBuffer(totalIndices);

        long vertexOffset = 0;
        long indexOffset = 0;

        // Append data from each entity's buffers to the combined buffers
        for (int i = 0; i < texCoordArr.length; i++) {
            model.setTextures(texCoordArr[i]);

            // log all texture coordinates
            System.out.println("tex1");
            for (float[] texCoord : texCoordArr[i]) {
                System.out.println(texCoord[0] + " " + texCoord[1]);
            }
            System.out.println("...............");

            // because we cache the vertexbuffer darta in the getVertexBufferData method the texture coordinates aren't updated automatically
            // change them at tex2


            float[] data = model.getVertexBufferData().clone(); // need to be cloned to protect ObjModel data from being set off arbitrarily
            int[] indices = model.getIndexBufferData().clone(); // needs to be offset by the number of vertices in the previous entities

            // TODO: calculate actual positions of vertices through model matrices
            int dataIndex = 0;
            System.out.println("tex2");
            for (int j = 0; j < indices.length; j++) { // TODO: apply rotation
                data[dataIndex++] = data[dataIndex - 1] * base.getScale().x  + base.getPosition().x + offset.x * i; // baseScale /1.2f
                data[dataIndex++] = data[dataIndex - 1] * base.getScale().y  + base.getPosition().y + offset.y * i; // baseScale /1.2f

                System.out.println(data[dataIndex] + " " + data[dataIndex+1]);

                dataIndex += Vertex.SIZE - 2;

                indices[j] += (int)indexOffset/4;
            }
            System.out.println("...............");


            vb.update(data, vertexOffset);
            ib.update(indices, indexOffset);

            vertexOffset += data.length    * 4L;
            indexOffset += indices.length  * 4L;
        }

        va.addBuffer(vb, Vertex.getLayout());
        return new Batch(va, ib);
    }

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
        if(shader.hasUniform("uColor"))
            shader.setUniform4f("uColor",1 ,1 , 1, 1);
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

    ///////////////////////
    public void SetUniforms(Shader shader, Entity2D entity, Vector4f color) {
        SetUniforms(shader, entity);
        if(shader.hasUniform("uColor")) {
            shader.setUniform4f("uColor", color.x, color.y, color.z, color.w);
        }
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

    public void clear() {
         glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
    }

    public Vector2f screenToWorldCoords(Vector2f screenCoords) {
        // copy vec
        Vector4f projectedCoords = new Vector4f(screenCoords, 0, 1);
        // normalize
        projectedCoords.x = (projectedCoords.x / Window.dim.x) * 2 - 1;
        projectedCoords.y = (projectedCoords.y / Window.dim.y) * -2 + 1;
        // "3D to 2D" (inverse of projection matrix "2D to 3D")
        projectedCoords.mul(camera.getProjectionMatrix().invert(new Matrix4f()));
        return new Vector2f(projectedCoords.x, projectedCoords.y);
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }
    public void setCurrentShader(Shader currentShader) {
        if(!this.currentShader.equals(currentShader)) {
            this.currentShader = currentShader;
            currentShader.forceBind();
        }
    }
    public Shader getCurrentShader() {
        return currentShader;
    }

    public void setMode(int mode) {
        this.mode = mode;
        glPolygonMode(GL_FRONT_AND_BACK, mode);
    }
}
