package Render.Vertices;

public class Vertex {

    public static final short SIZE = 5;

    public float[] position;
    public float[] texture; // uvs
    public float[] normal;
    public int materialID;
    private static VertexBufferLayout layout;


    public Vertex(float[] position, float[] texture, float[] normal, int materialID) {
        this(position, texture, normal);
        this.materialID = materialID;
    }

    public Vertex(float[] position, float[] texture, float[] normal) {
        this(position, texture);
        this.normal = normal;
    }
    public Vertex(float[] position, float[] texture) {
        this(position);
        this.texture = texture;
    }

    public Vertex(float[] position) {
        this.position = position;
        this.texture = new float[] {0, 0};
        this.normal = new float[] {0, 0, 0};
        this.materialID = 0;
    }

    public short getSize() {
        return SIZE;
    }
    public short getByteSize() {
        return SIZE * 4; // float and int both are 4 bytes
    }

    public static VertexBufferLayout getLayout() {
        if(layout == null) {
            layout = new VertexBufferLayout();
            layout.pushF(3); // position
            layout.pushF(2); // texture
            //layout.PushF(3); // normal
            //layout.PushI(1); // materialID
        }
        return layout;
    }
}