package Render.Vertices;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL33.glVertexAttribDivisor;
import static org.lwjgl.opengl.GL43.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL43.glVertexAttribPointer;
import static org.lwjgl.opengl.GL43.glBindVertexArray;
import static org.lwjgl.opengl.GL43.glGenVertexArrays;

public class VertexArray {
    int m_RendererID;
    int m_VertexBufferIndex = 0;

    public VertexArray()  {

    }

    public void bind() {
        glBindVertexArray(m_RendererID);
    }
    public void unbind() {
        glBindVertexArray(0);
    }

    public void addBuffer(VertexBuffer vb, VertexBufferLayout layout) {
        // can be called multiple times (once for each index with <size> and <type> their <stride> (byteSize) and <pointer> location in vertex)
        bind();
        vb.bind();
        ArrayList<VertexBufferElement> elements =  layout.getElements();
        int offset = 0;;
        for(int i = 0; i < elements.size(); i++) {
            VertexBufferElement element = elements.get(i);

            glEnableVertexAttribArray(i);
            glVertexAttribPointer(m_VertexBufferIndex+i, element.count, element.type, element.normalized, layout.getStride(), offset);
            offset += element.count * element.getTypeSize(element.type);
        }

        m_VertexBufferIndex += elements.size();
    }

    public void addBufferI(VertexBuffer vb, VertexBufferLayout layout) {
        addBuffer(vb, layout);

        assert layout.getElements().size() != 4 : "Method might need to be updated to support different layouts"; // TODO: finish

        for(int i = 0; i < layout.getElements().size(); i++) {
            glVertexAttribDivisor(m_VertexBufferIndex - layout.getElements().size()+i, 1);
        }
    }
}
