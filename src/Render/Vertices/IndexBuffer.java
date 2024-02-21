package Render.Vertices;

import static org.lwjgl.opengl.GL15.*;

public class IndexBuffer {
    private final int m_rendererID;
    private final int count;

    public IndexBuffer(int[] data){
        count = data.length;

        m_rendererID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, m_rendererID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, data, GL_DYNAMIC_DRAW);
    }
    public IndexBuffer(short[] data){
        count = data.length;

        m_rendererID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, m_rendererID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, data, GL_DYNAMIC_DRAW);
    }
    public IndexBuffer(int size){
        count = size;

        m_rendererID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, m_rendererID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, (long) size * Float.BYTES, GL_DYNAMIC_DRAW);
    }

    public void update(int[] data, long offset) {
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, m_rendererID);
        glBufferSubData(GL_ELEMENT_ARRAY_BUFFER, offset, data);
    }
    public void update(short[] data, long offset) {
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, m_rendererID);
        glBufferSubData(GL_ELEMENT_ARRAY_BUFFER, offset, data);
    }

    public void bind(){
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, m_rendererID);
    }
    public void unbind() {
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);

    }

    public int getCount() { return count; }
}
