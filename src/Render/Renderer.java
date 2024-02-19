package Render;

import Render.Entity.Camera.Camera;
import Render.Entity.Entity2D;
import Render.Shader.Shader;
import Render.Vertices.*;
import Render.Vertices.Model.ObjModel;
import org.joml.Matrix4f;

import java.lang.reflect.Array;

import static org.lwjgl.opengl.GL43.*;

public class Renderer {

    public Shader defaultShader, currentShader;
    Camera camera;

    public Renderer() {
        defaultShader = currentShader = new Shader("res/shaders/default.shader");
        camera = new Camera();
    }
    public void draw(VertexArray va, IndexBuffer ib, Shader shader) {
        shader.bind();
        SetUniforms(shader);

        va.bind();
        ib.bind();

        glDrawElements(GL_TRIANGLES, ib.getCount(), GL_UNSIGNED_INT, 0);
    }

    public void draw(VertexArray va, IndexBuffer ib) {
        defaultShader.bind();
        SetUniforms(defaultShader);

        va.bind();
        ib.bind();

        glDrawElements(GL_TRIANGLES, ib.getCount(), GL_UNSIGNED_INT, 0);
    }

    // TODO: make this work fully

    public void drawInstanced(Entity2D entity, Matrix4f[] modelMatrices) {
        chooseShader(entity);

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
        defaultShader.bind();
        SetUniforms(defaultShader);

        b.ib.bind();
        b.va.bind();

        glDrawElements(GL_TRIANGLES, b.ib.getCount(), GL_UNSIGNED_INT, 0);
    }
    public void drawEntity2D(Entity2D entity) {
        chooseShader(entity);
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

    public Batch setupBatch(Entity2D[] entities) {
        VertexArray va = new VertexArray();
        int totalVertices = 0;
        int totalIndices = 0;

        // Calculate total size of all vertex and index buffers
        for (Entity2D entity : entities) {
            totalVertices += entity.getModel().getVertexBuffer().getSize();
            totalIndices += entity.getModel().getIndexBuffer().getCount();
        }

        // Create combined vertex and index buffers
        VertexBuffer vb = new VertexBuffer(totalVertices);
        IndexBuffer ib = new IndexBuffer(totalIndices);

        long vertexOffset = 0;
        long indexOffset = 0;

        // Append data from each entity's buffers to the combined buffers
        for (Entity2D entity : entities) {
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

    // TODO: investigate performance gain of CPU side MVP calculation
    public void SetUniforms(Shader shader, Entity2D entity) {
        Matrix4f modelmatrix = entity.calcModelMatrix().mul(camera.calcModelMatrix());
        shader.setUniformMat4f("uModel", modelmatrix);
        shader.setUniformMat4f("uView", camera.calcViewMatrix());
        shader.setUniformMat4f("uProj", camera.getProjectionMatrix());
    }
    public void SetUniforms(Shader shader) {
        shader.setUniformMat4f("uModel", camera.calcModelMatrix());
        shader.setUniformMat4f("uView", camera.calcViewMatrix());
        shader.setUniformMat4f("uProj", camera.getProjectionMatrix());
    }
    public void chooseShader(Entity2D entity){
        if /**/ (entity.getShader() != null)
            entity.getShader().bind();
        else if (camera.getShader() != null)
            camera.getShader().bind();
        else
            defaultShader.bind();
        SetUniforms(currentShader, entity);

    }

    public void Clear() {
         glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
    }
    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public void setCurrentShader(Shader currentShader) {
        this.currentShader = currentShader;
    }
    public Shader getCurrentShader() {
        return currentShader;
    }
}
