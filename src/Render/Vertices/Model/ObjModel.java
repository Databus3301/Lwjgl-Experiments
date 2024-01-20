package Render.Vertices.Model;

import Render.Vertices.IndexBuffer;
import Render.Vertices.Vertex;
import Render.Vertices.VertexBuffer;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ObjModel {
    public ArrayList<float[]> _positions = new ArrayList<>();
    public ArrayList<float[]> _normals = new ArrayList<>();
    public ArrayList<float[]> _textures = new ArrayList<>();
    public ArrayList<int[][]> _faces = new ArrayList<>(); // indices

    public ArrayList<ObjMaterial> _materials = new ArrayList<>();
    public ArrayList<Integer> _materialIDs = new ArrayList<>();


    private float[][] positions;
    private float[][] normals;
    private float[][] textures;
    private int[][][] faces;
    private ObjMaterial[] materials;
    private Integer[] materialIDs;

    private int[] indices;

    public ObjModel() {
        // set the default material
        _materials.add(new ObjMaterial());
    }

    public void castToArrays() {
        positions = toArray(_positions, float[].class);
        normals = toArray(_normals, float[].class);
        textures = toArray(_textures, float[].class);
        faces = toArray(_faces, int[][].class);
        materials = toArray(_materials, ObjMaterial.class);
        materialIDs = toArray(_materialIDs, Integer.class);
    }


    public VertexBuffer getVertexBuffer() {
        float[] data = new float[9 * faces.length * 3]; // 9 floats per vertex, 3 vertices per face
        indices = new int[faces.length * 3]; // 3 vertices per face
        int dataIndex = 0;
        int faceIndex = 0;

        for (int[][] face : faces) {
            for (int i = 0; i < face.length; i++) {
                float[] position = positions[face[i][0]-1];
                data[dataIndex++] = position[0];
                data[dataIndex++] = position[1];
                data[dataIndex++] = position[2];

                float[] texture = textures[face[i][1]-1];
                data[dataIndex++] = texture[0];
                data[dataIndex++] = texture[1];

                float[] normal = normals[face[i][2]-1];
                data[dataIndex++] = normal[0];
                data[dataIndex++] = normal[1];
                data[dataIndex++] = normal[2];

                data[dataIndex++] = materialIDs[faceIndex];

                indices[dataIndex / 9 -1] = dataIndex / 9  -1;
            }
            faceIndex++;
        }

        return new VertexBuffer(data);
    }


    public IndexBuffer getIndexBuffer() {
        // if we haven't generated the index buffer yet, do so
        if (indices == null) {
            getVertexBuffer();
        }
        return new IndexBuffer(indices);
    }

    public int[] getIndices() {
        if (indices == null) {
            getVertexBuffer();
        }
        return indices;
    }


    private static <T> T[] toArray(ArrayList<T> list, Class<T> c) {
        @SuppressWarnings("unchecked")
        T[] array = (T[]) Array.newInstance(c, list.size());
        return list.toArray(array);
    }

}
