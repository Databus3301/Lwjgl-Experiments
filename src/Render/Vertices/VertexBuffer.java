package Render.Vertices;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;

public class VertexBuffer {
    private int m_rendererID;

    public VertexBuffer(float[] data){
        m_rendererID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, m_rendererID);
        glBufferData(GL_ARRAY_BUFFER, data, GL_DYNAMIC_DRAW);
    }

    public void Bind(){
        glBindBuffer(GL_ARRAY_BUFFER, m_rendererID);
    }
    public void Unbind() {
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

}
