package Render.Vertices;

public class Vertex {
    public float[] position;
    public float[] color;
    public float[] texture; // uvs
    public float[] normal;
    public int materialID;

    public Vertex(float[] position, float[] texture, float[] normal, int materialID) {
        this(position, texture, normal);
        this.materialID = materialID;
    }

    public Vertex(float[] position, float[] texture, float[] normal) {
        this(position, texture);
        this.normal = normal;
    }

    public Vertex(float[] position, float[] color) {
        this(position);
        this.color = color;
    }

    public Vertex(float[] position) {
        this.position = position;
        this.color = new float[] {0, 0, 0};
        this.texture = new float[] {0, 0};
        this.normal = new float[] {0, 0, 0};
        this.materialID = 0;
    }

    public int getSize() {
        return position.length + color.length + texture.length + normal.length + 1;
    }
    public int getByteSize() {
        return getSize() * 4; // float and int both are 4 bytes
    }

    public static VertexBufferLayout GetLayout() {
        VertexBufferLayout layout = new VertexBufferLayout();
        layout.PushF(3); // position
        layout.PushF(3); // color
        layout.PushF(2); // texture
        layout.PushF(3); // normal
        layout.PushI(1); // materialID
        return layout;
    }
}