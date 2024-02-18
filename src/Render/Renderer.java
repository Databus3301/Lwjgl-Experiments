package Render;

import Render.Entity.Camera.Camera;
import Render.Entity.Entity2D;
import Render.Shader.Shader;
import Render.Vertices.*;
import Render.Vertices.Model.ObjModel;
import org.joml.Matrix4f;

import static org.lwjgl.opengl.GL43.*;

public class Renderer {

    public Shader defaultShader;
    Camera camera;

    public Renderer() {
        defaultShader = new Shader("res/shaders/batching.shader");
        camera = new Camera();
    }
    public void Draw(VertexArray va, IndexBuffer ib, Shader shader) {
        shader.Bind();
        SetUniforms(shader);

        va.Bind();
        ib.Bind();

        glDrawElements(GL_TRIANGLES, ib.GetCount(), GL_UNSIGNED_INT, 0);
    }

    public void Draw(VertexArray va, IndexBuffer ib) {
        defaultShader.Bind();
        SetUniforms(defaultShader);

        va.Bind();
        ib.Bind();

        glDrawElements(GL_TRIANGLES, ib.GetCount(), GL_UNSIGNED_INT, 0);
    }

    // TODO: make this work fully

    public void DrawInstanced(Entity2D entity, Matrix4f[] modelMatrices) {
        chooseShader(entity);

        // choose Texture
        if(entity.getTexture() != null)
            entity.getTexture().Bind();
        // choose Model
        ObjModel model = entity.getModel();
        assert model != null : "[ERROR] (Render.Renderer.DrawEntity2D) Entity2D has no model";
        // choose VertexArray and IndexBuffer
        VertexArray va = new VertexArray();
        va.AddBuffer(model.getVertexBuffer(), Vertex.GetLayout());
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
        layout.PushF(4);
        layout.PushF(4);
        layout.PushF(4);
        layout.PushF(4);

        va.AddBufferI(vb, layout);

        va.Bind();
        ib.Bind();

        glDrawElementsInstanced(GL_TRIANGLES, ib.GetCount(), GL_UNSIGNED_INT, 0, modelMatrices.length);
    }

    public void DrawBatch(Batch b) {
        defaultShader.Bind();
        SetUniforms(defaultShader);

        b.ib.Bind();
        b.va.Bind();

        glDrawElements(GL_TRIANGLES, b.ib.GetCount(), GL_UNSIGNED_INT, 0);
    }
    public void DrawEntity2D(Entity2D entity) {
        chooseShader(entity);
        // choose Texture
        if(entity.getTexture() != null)
            entity.getTexture().Bind();
        // choose Model
        ObjModel model = entity.getModel();
        assert model != null : "[ERROR] (Render.Renderer.DrawEntity2D) Entity2D has no model";
        // choose VertexArray and IndexBuffer
        VertexArray va = new VertexArray();
        va.AddBuffer(model.getVertexBuffer(), Vertex.GetLayout());
        IndexBuffer ib = model.getIndexBuffer();

        va.Bind();
        ib.Bind();

        glDrawElements(GL_TRIANGLES, ib.GetCount(), GL_UNSIGNED_INT, 0);
    }
    public void DrawEntities2D(Entity2D[] entities) {
        for (Entity2D entity : entities) {
            if(entity != null)
                DrawEntity2D(entity);
        }
    }

    public Batch SetupBatch(Entity2D[] entities) {
        VertexArray va = new VertexArray();
        int totalVertices = 0;
        int totalIndices = 0;

        // Calculate total size of all vertex and index buffers
        for (Entity2D entity : entities) {

            totalVertices += entity.getModel().getVertexBuffer().GetSize();
            totalIndices += entity.getModel().getIndexBuffer().GetCount();
        }

        // Create combined vertex and index buffers
        VertexBuffer vb = new VertexBuffer(totalVertices);
        IndexBuffer ib = new IndexBuffer(totalIndices);

        long vertexOffset = 0;
        long indexOffset = 0;

        // Append data from each entity's buffers to the combined buffers
        for (Entity2D entity : entities) {
            ObjModel model = entity.getModel();

            float[] data = model.getVertexBufferData();
            int[] indices = model.getIndexBufferData((int)indexOffset/4); // needs to be offset by the number of vertices in the previous entities

            // calculate actual positions of vertices through model matrices
            int dataIndex = 0;
            for (int j = 0; j < indices.length; j++) {
                data[dataIndex++] = data[dataIndex - 1] * entity.getScale().x + entity.getPosition().x;
                data[dataIndex++] = data[dataIndex - 1] * entity.getScale().y  + entity.getPosition().y;
                dataIndex += Vertex.SIZE - 2;
            }

            vb.Update(data, vertexOffset);
            ib.Update(indices, indexOffset);

            vertexOffset += data.length    * 4L;
            indexOffset += indices.length  * 4L;
        }

        va.AddBuffer(vb, Vertex.GetLayout());
        return new Batch(va, ib);
    }
    public void SetUniforms(Shader shader, Entity2D entity) {
        Matrix4f modelmatrix = entity.calcModelMatrix().mul(camera.calcModelMatrix());
        shader.SetUniformMat4f("uModel", modelmatrix);
        shader.SetUniformMat4f("uView", camera.calcViewMatrix());
        shader.SetUniformMat4f("uProj", camera.getProjectionMatrix());
    }
    public void SetUniforms(Shader shader) {
        shader.SetUniformMat4f("uModel", camera.calcModelMatrix());
        shader.SetUniformMat4f("uView", camera.calcViewMatrix());
        shader.SetUniformMat4f("uProj", camera.getProjectionMatrix());
    }
    public void chooseShader(Entity2D entity){
        if(entity.getShader() != null) {
            entity.getShader().Bind();
            SetUniforms(entity.getShader(), entity);
        }
        else if(camera.getShader() != null) {
            camera.getShader().Bind();
            SetUniforms(camera.getShader(), entity);
        }
        else {
            defaultShader.Bind();
            SetUniforms(defaultShader, entity);
        }
    }

    public void Clear() {
         glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
    }
    public void setCamera(Camera camera) {
        this.camera = camera;
    }
}
