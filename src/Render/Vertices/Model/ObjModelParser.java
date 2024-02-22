package Render.Vertices.Model;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.awt.geom.Rectangle2D;
import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class ObjModelParser {
    public static ObjModel parseOBJ(String path) {
        if(!path.startsWith("res/models/")) path = "res/models/" + path;

        ObjModel model = new ObjModel();
        short currentMaterialID = 0;

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
                        short count = 0;
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

        // calculate the bounding box
        Vector3f min = new Vector3f(Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE);
        Vector3f max = new Vector3f(Float.MIN_VALUE, Float.MIN_VALUE, Float.MIN_VALUE);
        for(float[] vertex : model.getPositions()) {
            Vector3f v = new Vector3f(vertex[0], vertex[1], vertex[2]);
            min.min(v);
            max.max(v);
        }

        model.setBoundingBox(new Vector4f(min.x, min.y, max.x - min.x, max.y - min.y));

        // calculate the scale factor and the offset
        Vector3f scale = new Vector3f(max).sub(min);
        float scaleFactor = Math.max(scale.x, Math.max(scale.y, scale.z));
        if (scaleFactor == 0) scaleFactor = 1;
        Vector3f offset = new Vector3f(min).add(scale.mul(0.5f));

        // normalize the vertices
        for(float[] vertex : model.getPositions()) {
            for(int i = 0; i < 3; i++) {
                vertex[i] = (vertex[i] - offset.get(i)) / (scaleFactor)*2;
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

    private static short[][] parseFace(String[] parts) {
        short[][] arr = new short[parts.length - START_INDEX][];

        for (int i = 0; i < parts.length-1; i++) {
            String[] subParts = parts[i + START_INDEX].split("/");
            arr[i] = new short[subParts.length];
            for (int j =0 ; j < subParts.length; j++)
                arr[i][j] = Short.parseShort(subParts[j]);
            }

        return arr;
    }

}

