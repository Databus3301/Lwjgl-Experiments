package Render.Vertices.Model;

import Render.Entity.Texturing.Texture;

public class ObjMaterial {
    private String name;
    private float[] ambient;
    private float[] diffuse;
    private float[] specular;
    private float[] transmissionFilter;
    private float[] emission;
    private float shininess;
    private float opticalDensity;
    private int illuminationModel;
    private String ambientTextureMap;
    private String diffuseTextureMap;
    private String specularTextureMap;
    private String specularHighlightTextureMap;
    private String bumpMap;
    private String displacementMap;
    private String stencilDecalMap;
    private String alphaTextureMap;
    private String reflectionMap;

    private Texture ambientTexture;
    private Texture diffuseTexture;
    private Texture specularTexture;
    private Texture specularHighlightTexture;
    private Texture bumpTexture;
    private Texture displacementTexture;
    private Texture stencilDecalTexture;
    private Texture alphaTexture;
    private Texture reflectionTexture;


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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float[] getAmbient() {
        return ambient;
    }

    public void setAmbient(float[] ambient) {
        this.ambient = ambient;
    }

    public float[] getDiffuse() {
        return diffuse;
    }

    public void setDiffuse(float[] diffuse) {
        this.diffuse = diffuse;
    }

    public float[] getSpecular() {
        return specular;
    }

    public void setSpecular(float[] specular) {
        this.specular = specular;
    }

    public float[] getTransmissionFilter() {
        return transmissionFilter;
    }

    public void setTransmissionFilter(float[] transmissionFilter) {
        this.transmissionFilter = transmissionFilter;
    }

    public float[] getEmission() {
        return emission;
    }

    public void setEmission(float[] emission) {
        this.emission = emission;
    }

    public float getShininess() {
        return shininess;
    }

    public void setShininess(float shininess) {
        this.shininess = shininess;
    }

    public float getOpticalDensity() {
        return opticalDensity;
    }

    public void setOpticalDensity(float opticalDensity) {
        this.opticalDensity = opticalDensity;
    }

    public int getIlluminationModel() {
        return illuminationModel;
    }

    public void setIlluminationModel(int illuminationModel) {
        this.illuminationModel = illuminationModel;
    }

    public String getAmbientTextureMap() {
        return ambientTextureMap;
    }

    public void setAmbientTextureMap(String ambientTextureMap) {
        this.ambientTextureMap = ambientTextureMap;
        ambientTexture = new Texture(ambientTextureMap);
    }

    public String getDiffuseTextureMap() {
        return diffuseTextureMap;
    }

    public void setDiffuseTextureMap(String diffuseTextureMap) {
        this.diffuseTextureMap = diffuseTextureMap;
        diffuseTexture = new Texture(diffuseTextureMap);
    }

    public String getSpecularTextureMap() {
        return specularTextureMap;
    }

    public void setSpecularTextureMap(String specularTextureMap) {
        this.specularTextureMap = specularTextureMap;
        specularTexture = new Texture(specularTextureMap);
    }

    public String getSpecularHighlightTextureMap() {
        return specularHighlightTextureMap;
    }

    public void setSpecularHighlightTextureMap(String specularHighlightTextureMap) {
        this.specularHighlightTextureMap = specularHighlightTextureMap;
        specularHighlightTexture = new Texture(specularHighlightTextureMap);
    }

    public String getBumpMap() {
        return bumpMap;
    }

    public void setBumpMap(String bumpMap) {
        this.bumpMap = bumpMap;
        bumpTexture = new Texture(bumpMap);
    }

    public String getDisplacementMap() {
        return displacementMap;
    }

    public void setDisplacementMap(String displacementMap) {
        this.displacementMap = displacementMap;
        displacementTexture = new Texture(displacementMap);
    }

    public String getStencilDecalMap() {
        return stencilDecalMap;
    }

    public void setStencilDecalMap(String stencilDecalMap) {
        this.stencilDecalMap = stencilDecalMap;
        stencilDecalTexture = new Texture(stencilDecalMap);
    }

    public String getAlphaTextureMap() {
        return alphaTextureMap;
    }

    public void setAlphaTextureMap(String alphaTextureMap) {
        this.alphaTextureMap = alphaTextureMap;
        alphaTexture = new Texture(alphaTextureMap);
    }

    public String getReflectionMap() {
        return reflectionMap;
    }

    public void setReflectionMap(String reflectionMap) {
        this.reflectionMap = reflectionMap;
        reflectionTexture = new Texture(reflectionMap);
    }

}

