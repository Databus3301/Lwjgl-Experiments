package Render.Vertices;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;

public class VertexBufferLayout {
    private ArrayList<VertexBufferElement> m_Elements = new ArrayList<>();
    private int m_Stride;

    public VertexBufferLayout() {m_Stride = 0;}

    public void PushI(int count) {
        m_Elements.add(new VertexBufferElement(GL_UNSIGNED_INT, count, false));
        m_Stride += Integer.BYTES * count;
    }
    public void PushF(int count) {
        m_Elements.add(new VertexBufferElement(GL_FLOAT, count, false));
        m_Stride += Float.BYTES * count;
    }
    public void PushB(int count) {
        m_Elements.add(new VertexBufferElement(GL_UNSIGNED_BYTE, count, true));
        m_Stride += Character.BYTES * count;
    }

    public ArrayList<VertexBufferElement> GetElements() { return m_Elements; }
    public int GetStride() {
        return m_Stride;
    }
}
