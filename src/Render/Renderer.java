package Render;

import Render.Shader.Shader;
import Render.Vertices.IndexBuffer;
import Render.Vertices.VertexArray;

import static org.lwjgl.opengl.GL43.*;

public class Renderer {


    public Renderer() {

    }
    public void Draw(VertexArray va, IndexBuffer ib, Shader shader) {
        shader.Bind();
        va.Bind();
        ib.Bind();

        glDrawElements(GL_TRIANGLES, ib.GetCount(), GL_UNSIGNED_INT, 0);
    }

    public void Clear(){
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

    }
}
