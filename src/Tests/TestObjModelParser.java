package Tests;

import Render.MeshData.Model.ObjModel;
import Render.MeshData.Model.ObjMaterial;
import Render.MeshData.Model.ObjModelParser;

public class TestObjModelParser extends Test {

    ObjModel model;
    public TestObjModelParser() {
        super();
        parseOBJ("res/models/testModel3.obj");
        displayModel();
    }
    public TestObjModelParser(String path) {
        super();
        parseOBJ(path);
        displayModel();
    }

    public TestObjModelParser(ObjModel model) {
        super();
        this.model = model;
        displayModel();
    }

    public void parseOBJ(String path) {
        model = ObjModelParser.parseOBJ(path);
    }
    public void displayModel() {
        // print out all the fields of model
        System.out.println("vertices:");
        for(float[] vertex : model.getPositions()) {
            System.out.print("  ");
            for(int i = 0; i < vertex.length; i++) {
                System.out.print(vertex[i] + "\t\t");
            }
            System.out.println();
        }


        System.out.println("normals:");
        for(float[] normal : model.getNormals()) {
            System.out.print("  ");
            for(float coord : normal) {
                System.out.print(coord + " ");
            }
            System.out.println();
        }
        System.out.println("textures:");
        for(float[] texture : model.getTextures()) {
            System.out.print("  ");
            for(float coord : texture) {
                System.out.print(coord + " ");
            }
            System.out.println();
        }
        System.out.println("faces:");
        for(short[][] face : model.getFaces()) {
            System.out.print("  ");
            for(short[] group : face) {
                System.out.print("(");
                for(int info : group) {
                    System.out.print(info + " ");
                }
                System.out.print(") ");
            }
            System.out.println();
        }

        System.out.println("materialIDs:");
        for(short id : model.getMaterialIDs()) {
            System.out.print("  " + id);
        }
        System.out.println("");

        System.out.println("materials:");
        for(ObjMaterial material : model.getMaterials()) {
            System.out.println("_".repeat(42));
            System.out.println("  name: " + material.getName());
            System.out.print("  ambient: ");
            for(float coord : material.getAmbient()) {
                System.out.print(coord + " ");
            }
            System.out.println();
            System.out.print("  diffuse: ");
            for(float coord : material.getDiffuse()) {
                System.out.print(coord + " ");
            }
            System.out.println();
            System.out.print("  specular: ");
            for(float coord : material.getSpecular()) {
                System.out.print(coord + " ");
            }
            System.out.println();
            System.out.print("  transmissionFilter: ");
            for(float coord : material.getTransmissionFilter()) {
                System.out.print(coord + " ");
            }
            System.out.println();
            System.out.print("  emission: ");
            for(float coord : material.getEmission()) {
                System.out.print(coord + " ");
            }
            System.out.println();
            System.out.println("  shininess: " + material.getShininess());
            System.out.println("  opticalDensity: " + material.getOpticalDensity());
            System.out.println("  illuminationModel: " + material.getIlluminationModel());
            System.out.println("  ambientTextureMap: " + material.getAmbientTextureMap());
            System.out.println("  diffuseTextureMap: " + material.getDiffuseTextureMap());
            System.out.println("  specularTextureMap: " + material.getSpecularTextureMap());
            System.out.println("  specularHighlightTextureMap: " + material.getSpecularHighlightTextureMap());
            System.out.println("  bumpMap: " + material.getBumpMap());
            System.out.println("  displacementMap: " + material.getDisplacementMap());
            System.out.println("  stencilDecalMap: " + material.getStencilDecalMap());
            System.out.println("  alphaTextureMap: " + material.getAlphaTextureMap());
            System.out.println("  reflectionMap: " + material.getReflectionMap());


        }

//        int[] iB = model.getIndexBufferData();
//        System.out.println("indexBuffer:");
//        for (int i = 0; i < iB.length; i++) {
//            if(i % 3 == 0)
//                System.out.print("\n");
//            System.out.print(iB[i] + "\t\t");
//        }
//        System.out.println("");



    }




}
