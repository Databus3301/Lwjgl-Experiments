package Render;

import Render.Shader.Shader;
import Render.Vertices.IndexBuffer;
import Render.Vertices.VertexArray;
public class Batch {
    VertexArray va;
    IndexBuffer ib;

    public Batch(VertexArray va, IndexBuffer ib) {
        this.va = va;
        this.ib = ib;
    }

}
