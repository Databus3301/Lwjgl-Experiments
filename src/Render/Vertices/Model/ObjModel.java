package Render.Vertices.Model;

import Render.Vertices.IndexBuffer;
import Render.Vertices.Vertex;
import Render.Vertices.VertexBuffer;

import java.util.ArrayList;

public class ObjModel {
    public ArrayList<float[]> positions = new ArrayList<>();
    public ArrayList<float[]> normals = new ArrayList<>();
    public ArrayList<float[]> textures = new ArrayList<>();
    public ArrayList<int[][]> faces = new ArrayList<>(); // indices

    public ArrayList<ObjMaterial> materials = new ArrayList<>();

    public ArrayList<Integer> materialIDs = new ArrayList<>();

    public ObjModel() {
        // set the default material
        materials.add(new ObjMaterial());
    }


    private int[] indices;
    public Vertex[] getVertices() {
        ArrayList<Vertex> vB = new ArrayList<>(positions.size());
        ArrayList<Integer> iB = new ArrayList<>(faces.size() * 3);

        int index = 0;
        for (int i = 0; i < faces.size(); i++) {
            for (int j = 0; j < faces.get(i).length; j++) {

                // generate a vertex as described by the specific indexes provided by a face part
                //System.out.print(faces.get(i)[j][0] + " " + faces.get(i)[j][1] + " " + faces.get(i)[j][2]);
                //System.out.print("\t\t");
                Vertex v = new Vertex(positions.get(faces.get(i)[j][0]-1));
                if(faces.get(i)[j].length >= 2)
                    v.texture = textures.get(faces.get(i)[j][1]-1);
                if(faces.get(i)[j].length >= 3) {
                    v.normal = normals.get(faces.get(i)[j][2]-1);
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

    public VertexBuffer getVertexBuffer() {
        return VertexBuffer.parseVertexBuffer(getVertices());
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


}
