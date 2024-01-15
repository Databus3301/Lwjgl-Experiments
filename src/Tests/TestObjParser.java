package Tests;

import Render.Vertices.Model.OBJModel;
import Render.Vertices.Model.OBJParser;

public class TestObjParser extends Test {

    OBJModel model;
    public TestObjParser() {
        super();
        parseOBJ("res/models/square.obj");
        displayModel();
    }
    public TestObjParser(String path) {
        super();
        parseOBJ(path);
        displayModel();
    }

    public void parseOBJ(String path) {
        model = OBJParser.parseOBJ(path);
    }
    public void displayModel() {
        // print out all the fields of model
        System.out.println("vertices:");
        for(float[] vertex : model.vertices) {
            System.out.print("  ");
            for(float coord : vertex) {
                System.out.print(coord + " ");
            }
            System.out.println();
        }
        System.out.println("normals:");
        for(float[] normal : model.normals) {
            System.out.print("  ");
            for(float coord : normal) {
                System.out.print(coord + " ");
            }
            System.out.println();
        }
        System.out.println("textures:");
        for(float[] texture : model.textures) {
            System.out.print("  ");
            for(float coord : texture) {
                System.out.print(coord + " ");
            }
            System.out.println();
        }
        System.out.println("faces:");
        for(int[][] face : model.faces) {
            System.out.print("  ");
            for(int[] group : face) {
                System.out.print("(");
                for(int info : group) {
                    System.out.print(info + " ");
                }
                System.out.print(") ");
            }
            System.out.println();
        }

    }


}
