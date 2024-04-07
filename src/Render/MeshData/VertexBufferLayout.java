package Render.MeshData;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;

public class VertexBufferLayout {
    private ArrayList<VertexBufferElement> m_Elements = new ArrayList<>();
    private int m_Stride;

    public VertexBufferLayout() {m_Stride = 0;}

    public void pushI(int count) {
        m_Elements.add(new VertexBufferElement(GL_UNSIGNED_INT, count, false));
        m_Stride += Integer.BYTES * count;
    }
    public void pushF(int count) {
        m_Elements.add(new VertexBufferElement(GL_FLOAT, count, false));
        m_Stride += Float.BYTES * count;
    }
    public void pushB(int count) {
        m_Elements.add(new VertexBufferElement(GL_UNSIGNED_BYTE, count, true));
        m_Stride += Character.BYTES * count;
    }

    public ArrayList<VertexBufferElement> getElements() { return m_Elements; }
    public int getStride() {
        return m_Stride;
    }
}
