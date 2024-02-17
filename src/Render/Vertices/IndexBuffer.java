package Render.Vertices;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;

public class IndexBuffer {
    private int m_rendererID;
    private int m_Count;

    public IndexBuffer(int[] data){
        m_Count = data.length;

        m_rendererID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, m_rendererID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, data, GL_DYNAMIC_DRAW);
    }
    public IndexBuffer(short[] data){
        m_Count = data.length;

        m_rendererID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, m_rendererID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, data, GL_DYNAMIC_DRAW);
    }
    public IndexBuffer(int size){
        m_Count = size;

        m_rendererID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, m_rendererID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, (long) size * Float.BYTES, GL_DYNAMIC_DRAW);
    }

    public void Update(int[] data, long offset) {
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, m_rendererID);
        glBufferSubData(GL_ELEMENT_ARRAY_BUFFER, offset, data);
    }
    public void Update(short[] data, long offset) {
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, m_rendererID);
        glBufferSubData(GL_ELEMENT_ARRAY_BUFFER, offset, data);
    }

    public void Bind(){
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, m_rendererID);
    }
    public void Unbind() {
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);

    }

    public int GetCount() { return m_Count; }
}
