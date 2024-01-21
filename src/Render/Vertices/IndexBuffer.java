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

    public void Bind(){
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, m_rendererID);
    }
    public void Unbind() {
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);

    }

    public int GetCount() { return m_Count; }
}
