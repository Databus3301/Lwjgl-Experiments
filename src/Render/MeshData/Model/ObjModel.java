package Render.MeshData.Model;

import Render.MeshData.IndexBuffer;
import Render.MeshData.Vertex;
import Render.MeshData.VertexBuffer;
import org.joml.Vector4f;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ObjModel {
    public static final ObjModel SQUARE = ObjModelParser.parseOBJ("square.obj");


    // TODO: OPTIMISATION:: add duplicate vertex recognition through faces (cheaper comparision) and handle indices accordingly
    public ArrayList<float[]> _positions = new ArrayList<>();
    public ArrayList<float[]> _normals = new ArrayList<>();
    public ArrayList<float[]> _textures = new ArrayList<>();
    public ArrayList<short[][]> _faces = new ArrayList<>(); // indices

    public ArrayList<ObjMaterial> _materials = new ArrayList<>();
    public ArrayList<short[]> _materialIDs = new ArrayList<>();

    private float[][] positions;
    private float[][] normals;
    private float[][] textures;
    private short[][][] faces;
    private ObjMaterial[] materials;
    private short[] materialIDs;

    ///
    private int[] indices;
    private IndexBuffer ib;

    private float[] vertexBufferData;
    private VertexBuffer vertexBuffer;

    private int vertexCount;
    private int indexCount;

    /**
     * The bounding box of the model
     * x, y, width, height
     */
    private Vector4f boundingBox;

    public ObjModel() {
        // set the default material
        _materials.add(new ObjMaterial());
    }
    public ObjModel(String path) {
       ObjModelParser.parseOBJ(path, this);
    }

    public ObjModel(float[][] vertexPositions, short[][] indices) { // TODO: test this
        this.positions = vertexPositions;
        this.faces = new short[indices.length][3][1];
        for(int i = 0; i < indices.length; i++) {
            this.faces[i] = new short[][] {indices[i]};
        }
    }

    public void castToArrays() {
        positions = toArray(_positions, float[].class);
        normals = toArray(_normals, float[].class);
        textures = toArray(_textures, float[].class);
        faces = toArray(_faces, short[][].class);
        materials = toArray(_materials, ObjMaterial.class);
        short[][] IDs = toArray(_materialIDs, short[].class);
        materialIDs = new short[IDs.length];
        for(int i = 0; i < IDs.length; i++) {
            materialIDs[i] = IDs[i][0];
        }

        calcIndexCount();
        calcVertexCount();

        _positions.clear();
        _normals.clear();
        _textures.clear();
        _faces.clear();
        _materials.clear();
        _materialIDs.clear();
    }

    // TODO: handle this through the <Entity2D> class, to allow for batch rendering, with attributes like position and scale by adding to this calculation
    public float[] getVertexBufferData() {
        if(vertexBufferData!=null) return vertexBufferData;


        int length = Vertex.SIZE * faces.length * 3; // 9 floats per vertex, 3 vertices per face
        if(vertexBufferData==null || vertexBufferData.length != length)
            vertexBufferData = new float[length];

        indices = new int[faces.length * 3]; // 3 vertices per face
        short dataIndex = 0;
        short faceIndex = 0;

        for (short[][] face : faces) {
            for (short i = 0; i < face.length; i++) {
                float[] position = positions[face[i][0] - 1];
                vertexBufferData[dataIndex++] = position[0];
                vertexBufferData[dataIndex++] = position[1];
                vertexBufferData[dataIndex++] = position[2];

                if (Vertex.SIZE > 3) {
                    if (face[i].length > 1) {
                        float[] texture = textures[face[i][1] - 1];
                        vertexBufferData[dataIndex++] = texture[0];
                        vertexBufferData[dataIndex++] = texture[1];
                    } else {
                        dataIndex += 2;
                    }
                }

                if(Vertex.SIZE > 5) {
                    if (face[i].length > 2) {
                        float[] normal = normals[face[i][2] - 1];
                        vertexBufferData[dataIndex++] = normal[0];
                        vertexBufferData[dataIndex++] = normal[1];
                        vertexBufferData[dataIndex++] = normal[2];
                    } else {
                        dataIndex += 3;
                    }
                }

                if(Vertex.SIZE > 8) {
                    if (face[i].length > 3)
                        vertexBufferData[dataIndex++] = materialIDs[faceIndex];
                    else
                        dataIndex++;
                }

                indices[dataIndex / Vertex.SIZE -1] = (short) (dataIndex / Vertex.SIZE  - 1);
            }
            faceIndex++;
        }

        return vertexBufferData;
    }
    public VertexBuffer getVertexBuffer() {
        if(vertexBuffer==null) {
            vertexBuffer = new VertexBuffer(getVertexBufferData());
        }
        return vertexBuffer;
    }

    public void calcIndexBuffer() {
        if (indices == null) {
            getVertexBufferData();
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
            getVertexBufferData();
        }
        return indices;
    }

    public ObjModel clone()  {
        ObjModel model = new ObjModel();
        model.positions = positions.clone();
        model.normals = normals.clone();
        model.textures = textures.clone();
        model.faces = faces.clone();
        model.materials = materials.clone();
        model.materialIDs = materialIDs.clone();
        model.boundingBox = new Vector4f(boundingBox);
        model.calcIndexCount();
        model.calcVertexCount();
        return model;
    }

    public int calcVertexCount() {
        vertexCount = faces.length*3;
        return vertexCount;
    }
    public int calcIndexCount() {
        indexCount = faces.length*3;
        return indexCount;
    }


    private static <T> T[] toArray(ArrayList<T> list, Class<T> c) {
        @SuppressWarnings("unchecked")
        T[] array = (T[]) Array.newInstance(c, list.size());
        list.toArray(array);
        return array;
    }

    public void replaceTextureCoords(float[][] newTextures) {
        assert newTextures.length == textures.length: "The new texture coordinates must have the same length as the old ones";

        textures = newTextures;

        if(Vertex.SIZE < 4) return;

        int length = Vertex.SIZE * faces.length * 3; // 9 floats per vertex, 3 vertices per face

        if(vertexBufferData==null)
            vertexBufferData = getVertexBufferData();
        if(vertexBufferData.length != length)
            vertexBufferData = new float[length];

        short dataIndex = 0;

        for (short[][] face : faces) {
            for (short i = 0; i < face.length; i++) {
                dataIndex += 3;

                if (face[i].length > 1) {
                    float[] texture = textures[face[i][1] - 1];
                    vertexBufferData[dataIndex++] = texture[0];
                    vertexBufferData[dataIndex++] = texture[1];
                }

                if(Vertex.SIZE > 5)
                    dataIndex += 3;
                if(Vertex.SIZE > 8)
                    dataIndex++;
            }
        }
        if(vertexBuffer!=null)
            vertexBuffer.update(vertexBufferData, 0);
        else
            vertexBuffer = new VertexBuffer(vertexBufferData);
    }


    public float[][] getPositions() {
        return positions;
    }
    public float[][] getNormals() {
        return normals;
    }
    public float[][] getTextures() {
        return textures;
    }
    public short[][][] getFaces() {
        return faces;
    }
    public ObjMaterial[] getMaterials() {
        return materials;
    }
    public short[] getMaterialIDs() {
        return materialIDs;
    }
    public Vector4f getBoundingBox() {
        return new Vector4f(boundingBox);
    }

    public int getVertexCount() {
        return vertexCount;
    }
    public int getIndexCount() {
        return indexCount;
    }

    public void setBoundingBox(Vector4f boundingBox) {
        this.boundingBox = boundingBox;
    }
    public void setTextures(float[][] textures) {
        this.textures = textures;
    }
    public void setPositions(float[][] positions) {
        this.positions = positions;
    }

    public void setNormals(float[][] normals) {
        this.normals = normals;
    }

    public void setFaces(short[][][] faces) {
        this.faces = faces;
        calcIndexCount();
        calcVertexCount();
    }

    public void setMaterials(ObjMaterial[] materials) {
        this.materials = materials;
    }

    public void setMaterialIDs(short[] materialIDs) {
        this.materialIDs = materialIDs;
    }

    public void setIndices(int[] indices) {
        this.indices = indices;
    }


}
