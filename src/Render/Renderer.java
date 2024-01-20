package Render;

import Render.Entity.Entity2D;
import Render.Shader.Shader;
import Render.Vertices.IndexBuffer;
import Render.Vertices.Model.ObjModel;
import Render.Vertices.Vertex;
import Render.Vertices.VertexArray;
import Render.Vertices.VertexBuffer;

import static org.lwjgl.opengl.GL43.*;

public class Renderer {

    Shader defaultShader;
    public Renderer() {
        defaultShader = new Shader("res/shaders/default.shader");
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

//    public void tmpDraw(int vB_ID, int[] iB) {
//        defaultShader.Bind();
//
////        int vertexBuffer_ID = glGenBuffers();
////        glBindBuffer(GL_ARRAY_BUFFER, vertexBuffer_ID);
////        glBufferData(GL_ARRAY_BUFFER, vB, GL_STATIC_DRAW);
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

        VertexArray va = new VertexArray();
        va.AddBuffer(model.getVertexBuffer(), Vertex.GetLayout());
        IndexBuffer ib = model.getIndexBuffer();

        va.Bind();
        ib.Bind();



        glDrawElements(GL_TRIANGLES, ib.GetCount(), GL_UNSIGNED_INT, 0);
    }

    public void SetUniforms(Shader shader, Entity2D entity) {
        shader.SetUniformMat4f("uModel", entity.calcModelMatrix());
        shader.SetUniformMat4f("uView", Entity2D.getCamera().getViewMatrix());
        shader.SetUniformMat4f("uProj", Entity2D.getCamera().getProjectionMatrix());
    }


    public void Clear() {
         glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
    }
}
