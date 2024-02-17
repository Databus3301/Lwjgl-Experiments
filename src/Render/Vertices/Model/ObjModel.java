package Render.Vertices.Model;

import Render.Vertices.IndexBuffer;
import Render.Vertices.Vertex;
import Render.Vertices.VertexBuffer;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ObjModel {
    // TODO: OPTIMISATION:: add duplicate vertex recognition through faces (cheaper comparision) and handle indices accordingly
    public ArrayList<float[]> _positions = new ArrayList<>();
    public ArrayList<float[]> _normals = new ArrayList<>();
    public ArrayList<float[]> _textures = new ArrayList<>();
    public ArrayList<short[][]> _faces = new ArrayList<>(); // indices

    public ArrayList<ObjMaterial> _materials = new ArrayList<>();
    public ArrayList<Short> _materialIDs = new ArrayList<>();

    public float[] center = new float[3];

    private float[][] positions;
    private float[][] normals;
    private float[][] textures;
    private short[][][] faces;
    private ObjMaterial[] materials;
    private Short[] materialIDs;

    private int[] indices;
    private IndexBuffer ib;

    public ObjModel() {
        // set the default material
        _materials.add(new ObjMaterial());
    }

    public void castToArrays() {
        positions = toArray(_positions, float[].class);
        normals = toArray(_normals, float[].class);
        textures = toArray(_textures, float[].class);
        faces = toArray(_faces, short[][].class);
        materials = toArray(_materials, ObjMaterial.class);
        materialIDs = toArray(_materialIDs, Short.class);
    }

    // TODO: handle this through the <Entity2D> class, to allow for batch rendering, with attributes like position and scale by adding to this calculation
    public float[] getVertexBufferData() {
        float[] data = new float[Vertex.SIZE * faces.length * 3]; // 9 floats per vertex, 3 vertices per face
        indices = new int[faces.length * 3]; // 3 vertices per face
        short dataIndex = 0;
        short faceIndex = 0;

        for (short[][] face : faces) {
            for (short i = 0; i < face.length; i++) {
                float[] position = positions[face[i][0] - 1];
                data[dataIndex++] = position[0];
                data[dataIndex++] = position[1];
                data[dataIndex++] = position[2];

                if (Vertex.SIZE > 3) {
                    if (face[i].length > 1) {
                        float[] texture = textures[face[i][1] - 1];
                        data[dataIndex++] = texture[0];
                        data[dataIndex++] = texture[1];
                    } else {
                        dataIndex += 2;
                    }
                }

                if(Vertex.SIZE > 5) {
                    if (face[i].length > 2) {
                        float[] normal = normals[face[i][2] - 1];
                        data[dataIndex++] = normal[0];
                        data[dataIndex++] = normal[1];
                        data[dataIndex++] = normal[2];
                    } else {
                        dataIndex += 3;
                    }
                }

                if(Vertex.SIZE > 8) {
                    if (face[i].length > 3)
                        data[dataIndex++] = materialIDs[faceIndex];
                    else
                        dataIndex++;
                }

                indices[dataIndex / Vertex.SIZE -1] = (short) (dataIndex / Vertex.SIZE  - 1);
            }
            faceIndex++;
        }

        return data;
    }

    public VertexBuffer getVertexBuffer() {
        return new VertexBuffer(getVertexBufferData());
    }

    public void calcIndexBuffer() {
        if (indices == null) {
            getVertexBuffer();
        }
        ib = new IndexBuffer(indices);
    }

    public IndexBuffer getIndexBuffer() {
        if(ib==null) {
            calcIndexBuffer();
        }
        return ib;
    }

    public int[] getIndexBufferData() {
        if (indices == null) {
            getVertexBuffer();
        }
        return indices;
    }
    public int[] getIndexBufferData(int offset) {
        if (indices == null) {
            getVertexBuffer();
        }
        for (int i = 0; i < indices.length; i++) {
            indices[i] += offset;
        }
        return indices;
    }

    private static <T> T[] toArray(ArrayList<T> list, Class<T> c) {
        @SuppressWarnings("unchecked")
        T[] array = (T[]) Array.newInstance(c, list.size());
        list.toArray(array);
        list = null;
        return array;
    }

}
