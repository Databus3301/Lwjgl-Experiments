package Render.Vertices;

import static org.lwjgl.opengl.GL15.*;

public class VertexBuffer {
    private final int m_rendererID;

    private final int size;

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

    public void update(float[] data, long offset) {
        glBindBuffer(GL_ARRAY_BUFFER, m_rendererID);
        glBufferSubData(GL_ARRAY_BUFFER, offset, data);

        //glGetBufferSubData(GL_ARRAY_BUFFER, data.length*4L, data);
    }

    public void bind(){ // TODO: implement force bind on performance relevant call-counts
        glBindBuffer(GL_ARRAY_BUFFER, m_rendererID);
    }

    public void unbind() {
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    public int getSize() { return size; }

}
