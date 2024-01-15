package Render.Vertices.Model;

import java.io.*;

public class OBJParser {
    public static OBJModel parseOBJ(String path) {
        OBJModel model = new OBJModel();

        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // splits line on whitespace
                String[] parts = line.split("\\s+");
                switch (parts[0]) {
                    case "v":
                        model.vertices.add(parseFloatArray(parts));
                        break;
                    case "vn":
                        model.normals.add(parseFloatArray(parts));
                        break;
                    case "vt":
                        model.textures.add(parseFloatArray(parts));
                        break;
                    case "f":
                        model.faces.add(parseFace(parts));
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return model;
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

