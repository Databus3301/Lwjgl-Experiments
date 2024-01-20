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


    public float[][] positions;
    public float[][] normals;
    public float[][] textures;
    public int[][][] faces;
    public ObjMaterial[] materials;
    public Integer[] materialIDs;

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


    private Integer index;
    public Vertex[] getVertices() {
        ArrayList<Vertex> vB = new ArrayList<>(_positions.size());
        ArrayList<Integer> iB = new ArrayList<>();

        index = 0;
        for (int i = 0; i < _faces.size(); i++) {
            for (int j = 0; j < _faces.get(i).length; j++) {

                // generate a vertex as described by the specific indexes provided by a face part
                //System.out.print(faces.get(i)[j][0] + " " + faces.get(i)[j][1] + " " + faces.get(i)[j][2]);
                //System.out.print("\t\t");
                Vertex v = new Vertex(_positions.get(_faces.get(i)[j][0]-1));
                if(_faces.get(i)[j].length >= 2)
                    v.texture = _textures.get(_faces.get(i)[j][1]-1);
                if(_faces.get(i)[j].length >= 3) {
                    v.normal = _normals.get(_faces.get(i)[j][2]-1);
                }

                // if we generated a unique vertex, add it to the vertex buffer
                if (!vB.contains(v)) {
                    vB.add(v);
                    iB.add(index);
                }
                // if not then add a reference to the copy in the vertex buffer
                else {
                    iB.add(vB.indexOf(v));
                }
                index++;
            }
        }

        indices = new int[iB.size()];
        for (int i = 0; i < iB.size(); i++) {
            indices[i] = iB.get(i);
        }

        Vertex[] vBArray = new Vertex[vB.size()];
        return vB.toArray(vBArray);
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
