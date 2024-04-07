package Render;

import Render.MeshData.IndexBuffer;
import Render.MeshData.VertexArray;
public class Batch {
    VertexArray va;
    IndexBuffer ib;

    public Batch(VertexArray va, IndexBuffer ib) {
        this.va = va;
        this.ib = ib;
    }

}
