package Render.Vertices;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL43.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL43.glVertexAttribPointer;
import static org.lwjgl.opengl.GL43.glBindVertexArray;
import static org.lwjgl.opengl.GL43.glGenVertexArrays;

public class VertexArray {
    int m_RendererID;
    public VertexArray()  {

    }

    public void Bind() {
        glBindVertexArray(m_RendererID);
    }
    public void Unbind() {
        glBindVertexArray(0);
    }

    public void AddBuffer(VertexBuffer vb, VertexBufferLayout layout) {
        // can be called multiple times (once for each index with <size> and <type> their <stride> (byteSize) and <pointer> location in vertex)
        Bind();
        vb.Bind();
        ArrayList<VertexBufferElement> elements =  layout.GetElements();
        int offset = 0;;
        for(int i = 0; i < elements.size(); i++) {
            VertexBufferElement element = elements.get(i);

            glEnableVertexAttribArray(i);
            glVertexAttribPointer(i, element.count, element.type, element.normalized, layout.GetStride(), offset);
            offset += element.count * element.GetTypeSize(element.type);
        }


    }
}
