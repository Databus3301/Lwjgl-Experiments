package Render.Vertices;

import Render.Window.Window;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;

public class VertexBuffer {
    private int m_rendererID;

    private int size = 0;

    public VertexBuffer(float[] data){
        m_rendererID = glGenBuffers();
        size = data.length;

        glBindBuffer(GL_ARRAY_BUFFER, m_rendererID);
        glBufferData(GL_ARRAY_BUFFER, data, GL_DYNAMIC_DRAW);
    }

    public VertexBuffer(int size){
        m_rendererID = glGenBuffers();
        this.size = size;

        glBindBuffer(GL_ARRAY_BUFFER, m_rendererID);
        glBufferData(GL_ARRAY_BUFFER, (long) size * Float.BYTES, GL_DYNAMIC_DRAW);
    }

    public void Update(float[] data, long offset) {
        glBindBuffer(GL_ARRAY_BUFFER, m_rendererID);
        glBufferSubData(GL_ARRAY_BUFFER, offset, data);

        glGetBufferSubData(GL_ARRAY_BUFFER, data.length*4L, data);
    }

    public void Bind(){
        glBindBuffer(GL_ARRAY_BUFFER, m_rendererID);
    }
    public void Unbind() {
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    public int GetSize() { return size; }

}
