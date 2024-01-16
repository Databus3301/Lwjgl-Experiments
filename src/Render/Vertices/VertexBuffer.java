package Render.Vertices;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;

public class VertexBuffer {
    private int m_rendererID;

    public VertexBuffer(float[] data){
        m_rendererID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, m_rendererID);
        glBufferData(GL_ARRAY_BUFFER, data, GL_STATIC_DRAW);
    }

    public void Bind(){
        glBindBuffer(GL_ARRAY_BUFFER, m_rendererID);
    }
    public void Unbind() {
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    public static float[] parseVertexArray(Vertex[] vertices) {
        float[] data = new float[vertices.length * vertices[0].getSize()];
        for (int i = 0; i < vertices.length; i++) {
            int offset = i * vertices[0].getSize();
            for (int j = 0; j < vertices[i].position.length; j++) {
                data[offset + j] = vertices[i].position[j];
            }
            offset += vertices[i].position.length;
            for (int j = 0; j < vertices[i].color.length; j++) {
                data[offset + j] = vertices[i].color[j];
            }
            offset += vertices[i].color.length;
            for (int j = 0; j < vertices[i].texture.length; j++) {
                data[offset + j] = vertices[i].texture[j];
            }
            offset += vertices[i].texture.length;
            for (int j = 0; j < vertices[i].normal.length; j++) {
                data[offset + j] = vertices[i].normal[j];
            }
            offset += vertices[i].normal.length;
            data[offset] = vertices[i].materialID;
        }
        return data;
    }
}
