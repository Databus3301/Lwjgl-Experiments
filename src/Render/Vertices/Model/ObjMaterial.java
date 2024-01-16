package Render.Vertices.Model;

public class ObjMaterial {
    public String name;
    public float[] ambient;
    public float[] diffuse;
    public float[] specular;
    public float[] transmissionFilter;
    public float[] emission;
    public float shininess;
    public float opticalDensity;
    public int illuminationModel;
    public String ambientTextureMap;
    public String diffuseTextureMap;
    public String specularTextureMap;
    public String specularHighlightTextureMap;
    public String bumpMap;
    public String displacementMap;
    public String stencilDecalMap;
    public String alphaTextureMap;
    public String reflectionMap;

    public ObjMaterial() {
        name = "default"; // TODO: define a default material
        ambient = new float[] {0, 0, 0};
        diffuse = new float[] {0, 0, 0};
        specular = new float[] {0, 0, 0};
        transmissionFilter = new float[] {0, 0, 0};
        emission = new float[] {0, 0, 0};
        shininess = 0;
        opticalDensity = 0;
        illuminationModel = 0;
        ambientTextureMap = "";
        diffuseTextureMap = "";
        specularTextureMap = "";
        specularHighlightTextureMap = "";
        bumpMap = "";
        displacementMap = "";
        stencilDecalMap = "";
        alphaTextureMap = "";
        reflectionMap = "";
    }
}

