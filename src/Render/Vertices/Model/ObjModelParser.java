package Render.Vertices.Model;

import java.io.*;
import java.util.ArrayList;

public class ObjModelParser {
    public static ObjModel parseOBJ(String path) {
        if(!path.startsWith("res/models/")) path = "res/models/" + path;

        ObjModel model = new ObjModel();
        int currentMaterialID = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // splits line on whitespace
                String[] parts = line.split("\\s+");
                switch (parts[0]) {
                    case "v":
                        model._positions.add(parseFloatArray(parts));
                        break;
                    case "vn":
                        model._normals.add(parseFloatArray(parts));
                        break;
                    case "vt":
                        model._textures.add(parseFloatArray(parts));
                        break;
                    case "f":
                        model._faces.add(parseFace(parts));
                        model._materialIDs.add(currentMaterialID);
                        break;
                    case "mtllib": model._materials.addAll(parseMTL(parts[1]));
                        break;
                    case "usemtl":
                        int count = 0;
                        for(ObjMaterial material : model._materials) {
                            if (material == null) continue;
                            if (material.name.equals(parts[1])) {
                                currentMaterialID = count;
                                break; // found index
                            }
                            count++;
                        }
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        model.castToArrays();

        // normalize model to 0, 0 position and 1 to -1 space
        float[] min = new float[]{Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE};
        float[] max = new float[]{Float.MIN_VALUE, Float.MIN_VALUE, Float.MIN_VALUE};
        for(float[] vertex : model._positions) {
            for(int i = 0; i < 3; i++) {
                if(vertex[i] < min[i]) min[i] = vertex[i];
                if(vertex[i] > max[i]) max[i] = vertex[i];
            }
        }
        for(float[] vertex : model._positions) {
            for(int i = 0; i < 3; i++) {
                vertex[i] = vertex[i] / (Math.abs(max[i]) + Math.abs(min[i]));
                vertex[i] = vertex[i] * 2 - 1;
            }
        }



        return model;
    }

    public static ArrayList<ObjMaterial> parseMTL(String path) {
        path = "res/models/" + path;

        ArrayList<ObjMaterial> materials = new ArrayList<>();
        int currentReadingIndex = -1;
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\s+");
                switch (parts[0]) {
                    case "newmtl": materials.add(new ObjMaterial());
                        currentReadingIndex++;
                        materials.get(currentReadingIndex).name = parts[1];
                        break;
                    case "Ka": materials.get(currentReadingIndex).ambient = parseFloatArray(parts);
                        break;
                    case "Kd": materials.get(currentReadingIndex).diffuse = parseFloatArray(parts);
                        break;
                    case "Ks": materials.get(currentReadingIndex).specular = parseFloatArray(parts);
                        break;
                    case "Tf": materials.get(currentReadingIndex).transmissionFilter = parseFloatArray(parts);
                        break;
                    case "Ke": materials.get(currentReadingIndex).emission = parseFloatArray(parts);
                        break;
                    case "Ns": materials.get(currentReadingIndex).shininess = Float.parseFloat(parts[1]);
                        break;
                    case "Ni": materials.get(currentReadingIndex).opticalDensity = Float.parseFloat(parts[1]);
                        break;
                    case "illum": materials.get(currentReadingIndex).illuminationModel = Integer.parseInt(parts[1]);
                        break;
                    case "map_Ka": materials.get(currentReadingIndex).ambientTextureMap = parts[1];
                        break;
                    case "map_Kd": materials.get(currentReadingIndex).diffuseTextureMap = parts[1];
                        break;
                    case "map_Ks": materials.get(currentReadingIndex).specularTextureMap = parts[1];
                        break;
                    case "map_Ns": materials.get(currentReadingIndex).specularHighlightTextureMap = parts[1];
                        break;
                    case "map_bump": materials.get(currentReadingIndex).bumpMap = parts[1];
                        break;
                    case "disp": materials.get(currentReadingIndex).displacementMap = parts[1];
                        break;
                    case "decal": materials.get(currentReadingIndex).stencilDecalMap = parts[1];
                        break;
                    case "map_d": materials.get(currentReadingIndex).alphaTextureMap = parts[1];
                        break;
                    case "refl":
                        parts[0] = ""; // TODO: parse possible reflection map arguments correctly
                        materials.get(currentReadingIndex).reflectionMap = String.join( " ", parts);
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return materials;

    }

    private static final int START_INDEX = 1;
    private static float[] parseFloatArray(String[] parts) {
        float[] array = new float[parts.length - START_INDEX];
        for (int i = START_INDEX; i < parts.length; i++) {
            array[i - START_INDEX] = Float.parseFloat(parts[i]);
        }
        return array;
    }

    private static int[][] parseFace(String[] parts) {
        int[][] arr = new int[parts.length - START_INDEX][];

        for (int i = 0; i < parts.length-1; i++) {
            String[] subParts = parts[i + START_INDEX].split("/");
            arr[i] = new int[subParts.length];
            for (int j =0 ; j < subParts.length; j++)
                arr[i][j] = Integer.parseInt(subParts[j]);
            }

        return arr;
    }

}

