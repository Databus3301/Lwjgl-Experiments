package Render;

import Render.Entity.Entity2D;
import Render.Shader.Shader;
import Render.Vertices.IndexBuffer;
import Render.Vertices.Model.ObjModel;
import Render.Vertices.Vertex;
import Render.Vertices.VertexArray;
import Render.Vertices.VertexBuffer;
import Render.Window.Window;
import org.joml.Matrix4f;

import static org.lwjgl.opengl.GL43.*;

public class Renderer {

    Shader defaultShader;
    public Renderer() {
        defaultShader = new Shader("res/shaders/batching.shader");
    }
    public void Draw(VertexArray va, IndexBuffer ib, Shader shader) {
        shader.Bind();
        va.Bind();
        ib.Bind();

        glDrawElements(GL_TRIANGLES, ib.GetCount(), GL_UNSIGNED_INT, 0);
    }

    public void Draw(VertexArray va, IndexBuffer ib) {
        defaultShader.Bind();
        va.Bind();
        ib.Bind();

        glDrawElements(GL_TRIANGLES, ib.GetCount(), GL_UNSIGNED_INT, 0);
    }

    public void DrawBatch(Batch b) {
        defaultShader.Bind();
        SetUniforms(defaultShader);

        b.ib.Bind();
        b.va.Bind();

        glDrawElements(GL_TRIANGLES, b.ib.GetCount(), GL_UNSIGNED_INT, 0);
    }

//    public void tmpDraw(int vB_ID, int[] iB) {
//        defaultShader.Bind();
//
//        int vertexBuffer_ID = glGenBuffers();
//        glBindBuffer(GL_ARRAY_BUFFER, vertexBuffer_ID);
//        glBufferData(GL_ARRAY_BUFFER, vB, GL_STATIC_DRAW);
//
//        glBindBuffer(GL_ARRAY_BUFFER, vB_ID); // bind the vertex buffer
//
//        int indexBuffer_ID = glGenBuffers();
//        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBuffer_ID);
//        glBufferData(GL_ELEMENT_ARRAY_BUFFER, iB, GL_STATIC_DRAW);
//
//        int vertexArray_ID = glGenVertexArrays();
//        glBindVertexArray(vertexArray_ID);
//
//        glEnableVertexAttribArray(0);
//        glVertexAttribPointer(0, 2, GL_FLOAT, false, 12 * 4, 0);
//
//        glEnableVertexAttribArray(1);
//        glVertexAttribPointer(1, 2, GL_FLOAT, false, 12 * 4, GL_FLOAT*2);
//
//        glEnableVertexAttribArray(2);
//        glVertexAttribPointer(2, 2, GL_FLOAT, false, 12 * 4, GL_FLOAT*3);
//
//        glEnableVertexAttribArray(3);
//        glVertexAttribPointer(3, 3, GL_FLOAT, false, 12 * 4, GL_FLOAT*2);
//
//        glEnableVertexAttribArray(4);
//        glVertexAttribPointer(4, 1, GL_INT, false, 12 * 4, GL_FLOAT*3);
//
//
//        glDrawElements(GL_TRIANGLES, iB.length, GL_UNSIGNED_INT, 0);
//    }

    public void DrawEntity2D(Entity2D entity) {
        if(entity.getShader() != null) {
            entity.getShader().Bind();
            SetUniforms(entity.getShader(), entity);
        } else {
            defaultShader.Bind();
            SetUniforms(defaultShader, entity);
        }

        if(entity.getTexture() != null) {
            entity.getTexture().Bind();
        }

        ObjModel model = entity.getModel();
        if(model == null) {
            assert false : "[ERROR] (Render.Renderer.DrawEntity2D) Entity2D has no model";
        }

        //        VertexArray va = new VertexArray();
        //        va.AddBuffer(model.getVertexBuffer(), Vertex.GetLayout());
        IndexBuffer ib = model.getIndexBuffer();

        entity.getVa().Bind();
        ib.Bind();

        glDrawElements(GL_TRIANGLES, ib.GetCount(), GL_UNSIGNED_INT, 0);
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
        VertexBuffer vb = new VertexBuffer(new float[totalVertices]);
        IndexBuffer ib = new IndexBuffer(new int[totalIndices]);

        long vertexOffset = 0; // TODO: something about offset calculation, as resetting these to 0 every loop only renders the last element ergo data writing is fine up until we write past 1 enttiy
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

                dataIndex += 7;
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
        Matrix4f modelmatrix = entity.calcModelMatrix().mul(Entity2D.getCamera().calcModelMatrix());
        shader.SetUniformMat4f("uModel", modelmatrix);
        shader.SetUniformMat4f("uView", Entity2D.getCamera().getViewMatrix());
        shader.SetUniformMat4f("uProj", Entity2D.getCamera().getProjectionMatrix());
    }
    public void SetUniforms(Shader shader) {
        shader.SetUniformMat4f("uModel", Entity2D.getCamera().calcModelMatrix());
        shader.SetUniformMat4f("uView", Entity2D.getCamera().getViewMatrix());
        shader.SetUniformMat4f("uProj", Entity2D.getCamera().getProjectionMatrix());
    }


    public void Clear() {
         glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
    }
}
