package Tests;

import Render.Vertices.Model.ObjModel;
import Render.Vertices.Model.ObjMaterial;
import Render.Vertices.Model.ObjModelParser;

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
        for(float[] vertex : model._positions) {
            System.out.print("  ");
            for(int i = 0; i < vertex.length; i++) {
                System.out.print(vertex[i] + "\t\t");
            }
            System.out.println();
        }


        System.out.println("normals:");
        for(float[] normal : model._normals) {
            System.out.print("  ");
            for(float coord : normal) {
                System.out.print(coord + " ");
            }
            System.out.println();
        }
        System.out.println("textures:");
        for(float[] texture : model._textures) {
            System.out.print("  ");
            for(float coord : texture) {
                System.out.print(coord + " ");
            }
            System.out.println();
        }
        System.out.println("faces:");
        for(short[][] face : model._faces) {
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
        for(int id : model._materialIDs) {
            System.out.print("  " + id);
        }
        System.out.println("");

        System.out.println("materials:");
        for(ObjMaterial material : model._materials) {
            System.out.println("_".repeat(42));
            System.out.println("  name: " + material.name);
            System.out.print("  ambient: ");
            for(float coord : material.ambient) {
                System.out.print(coord + " ");
            }
            System.out.println();
            System.out.print("  diffuse: ");
            for(float coord : material.diffuse) {
                System.out.print(coord + " ");
            }
            System.out.println();
            System.out.print("  specular: ");
            for(float coord : material.specular) {
                System.out.print(coord + " ");
            }
            System.out.println();
            System.out.print("  transmissionFilter: ");
            for(float coord : material.transmissionFilter) {
                System.out.print(coord + " ");
            }
            System.out.println();
            System.out.print("  emission: ");
            for(float coord : material.emission) {
                System.out.print(coord + " ");
            }
            System.out.println();
            System.out.println("  shininess: " + material.shininess);
            System.out.println("  opticalDensity: " + material.opticalDensity);
            System.out.println("  illuminationModel: " + material.illuminationModel);
            System.out.println("  ambientTextureMap: " + material.ambientTextureMap);
            System.out.println("  diffuseTextureMap: " + material.diffuseTextureMap);
            System.out.println("  specularTextureMap: " + material.specularTextureMap);
            System.out.println("  specularHighlightTextureMap: " + material.specularHighlightTextureMap);
            System.out.println("  bumpMap: " + material.bumpMap);
            System.out.println("  displacementMap: " + material.displacementMap);
            System.out.println("  stencilDecalMap: " + material.stencilDecalMap);
            System.out.println("  alphaTextureMap: " + material.alphaTextureMap);
            System.out.println("  reflectionMap: " + material.reflectionMap);

            int[] iB = model.getIndexBufferData();
            System.out.println("indexBuffer:");
            for (int i = 0; i < iB.length; i++) {
                if(i % 3 == 0)
                    System.out.print("\n");
                System.out.print(iB[i] + "\t\t");
            }
            System.out.println("");
        }



    }




}
