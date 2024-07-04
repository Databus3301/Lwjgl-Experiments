package Tests;

import Render.MeshData.Shader.Shader;
import Render.Window;
import org.joml.*;

import java.io.File;
import java.io.FileReader;
import java.lang.Math;

import static org.lwjgl.glfw.GLFW.*;

public class TestPhyphoxVis extends Test {

    String path = "res/phyphox/holzachterbahn.csv";
    DataPoint[] data;
    Quaternionf rotation = new Quaternionf();
    float time;

    boolean shouldSpin, acceleratedLeft, acceleratedRight;


    public TestPhyphoxVis() {
        super();

        System.out.println("Trying to load data from: " + path);
        try {
            File file = new File(path);
            FileReader fr = new FileReader(file);
            char[] chars = new char[(int) file.length()];
            int r = fr.read(chars);
            System.out.println("read: " + r + " chars");

            String[] lines = new String(chars).split("\n");
            data = new DataPoint[lines.length-1];

            for (int i = 1; i < lines.length; i++) {
                String[] values = lines[i].split(",");
                float[] parsed = new float[values.length];
                for (int j = 0; j < values.length; j++) {
                    if(values[j] == null || values[j].isEmpty() || values[j].equals("NaN")) {
                        values[j] = "0";
                    }
                    parsed[j] = Float.parseFloat(values[j]);
                }
                data[i-1] = new DataPoint(parsed[0], parsed[1], parsed[2], parsed[3], parsed[4], parsed[5], parsed[6], parsed[7], parsed[8], parsed[9], parsed[10]);
            }

            System.out.println("parsed: " + data.length + " data points");
            fr.close();
        } catch (Exception e) {
            System.out.println("[ERROR] Error parsing file at: " + path);
            e.printStackTrace();
        }

        Vector2f smallest_LatLng = new Vector2f(Float.MAX_VALUE, Float.MAX_VALUE);
        Vector2f largest_LatLng = new Vector2f(Float.MIN_VALUE, Float.MIN_VALUE);
        Vector2f LatLng_diff;

        float smallest_altitude = Float.MAX_VALUE;
        float largest_altitude = Float.MIN_VALUE;
        float altitude_diff;

        float top_speed = 0;

        for (DataPoint dp : data) {
            if(dp.latitude < smallest_LatLng.x) smallest_LatLng.x = dp.latitude;
            if(dp.longitude < smallest_LatLng.y) smallest_LatLng.y = dp.longitude;
            if(dp.latitude > largest_LatLng.x) largest_LatLng.x = dp.latitude;
            if(dp.longitude > largest_LatLng.y) largest_LatLng.y = dp.longitude;

            if (dp.altitude < smallest_altitude) smallest_altitude = dp.altitude;
            if (dp.altitude > largest_altitude) largest_altitude = dp.altitude;

            if(dp.speed > top_speed) top_speed = dp.speed;
        }
        LatLng_diff = largest_LatLng.sub(smallest_LatLng, new Vector2f());
        altitude_diff = largest_altitude - smallest_altitude;

        System.out.println("\nsmallest lat lng: " + smallest_LatLng.x + ", " + smallest_LatLng.y);
        System.out.println("largest lat lng: " + largest_LatLng.x + ", " + largest_LatLng.y);
        System.out.println("lat lng diff: " + LatLng_diff.x + ", " + LatLng_diff.y);

        System.out.println("\nNormalising data...");
        for (DataPoint dp : data) {
            // normalise
            dp.pos = new Vector3f((dp.latitude - smallest_LatLng.x) / LatLng_diff.x, (dp.altitude - smallest_altitude) / altitude_diff, (dp.longitude - smallest_LatLng.y) / LatLng_diff.y);
            dp.normalisedSpeed = dp.speed / top_speed;
            // center
            dp.pos.sub(0.5f, 0.5f, 0.5f);
            // scale
            dp.pos.mul(Window.baseDim.x*0.5f);

            dp.curPos = new Vector3f(dp.pos);
        }

        time = -1;
        for(DataPoint dp : data) {
            if(time != -1)
                break;
            if(dp.horizontalAccuracy < 2 || dp.verticalAccuracy < 2 || dp.speed <= 0.1)
                continue;
            time = dp.time;
        }
    }

    @Override
    public void OnStart() {
        super.OnStart();
        Shader.DEFAULT = new Shader("phyphox.shader");
        shouldSpin = true;
    }

    @Override
    public void OnUpdate(float dt) {
        super.OnUpdate(dt);
        time += dt*25;

        // spin all positions
        if(shouldSpin) {
            float speed = 4;
            if(acceleratedLeft) {
                speed *= 4;
            }
            if(acceleratedRight) {
                speed *= -4;
            }
            rotation.rotateAxis((float) Math.toRadians(speed*dt), 0, 1, 0);
        }

        for (DataPoint dp : data) {
            dp.curPos.set(dp.pos);
            dp.curPos.rotate(rotation);
        }
    }

    @Override
    public void OnRender() {
        super.OnRender();

        DataPoint prev = data[0];
        for (DataPoint dp : data) {
            if(dp.horizontalAccuracy < 2 || dp.verticalAccuracy < 2 || dp.speed <= 0.1) {
                continue;
            }
            renderer.drawPoint(dp.curPos, (int)(5f*Window.getDifferP1920().x));
            renderer.drawLine(prev.curPos, dp.curPos, dp.distance*10, new Vector4f(1f-dp.normalisedSpeed*7.5f, dp.normalisedSpeed, dp.normalisedSpeed/5f , 1));

            prev = dp;

            if(time < dp.time) {
                break;
            }

        }
    }

    @Override
    public void OnKeyInput(long window, int key, int scancode, int action, int mods) {
        super.OnKeyInput(window, key, scancode, action, mods);
        if(key == GLFW_KEY_SPACE && action == GLFW_PRESS) {
            shouldSpin = !shouldSpin;
        }
        if(key == GLFW_KEY_A && action == GLFW_PRESS) {
            acceleratedLeft = true;
        }
        if(key == GLFW_KEY_A && action == GLFW_RELEASE) {
            acceleratedLeft = false;
        }
        if(key == GLFW_KEY_D && action == GLFW_PRESS) {
            acceleratedRight = true;
        }
        if(key == GLFW_KEY_D && action == GLFW_RELEASE) {
            acceleratedRight = false;
        }

        if(key == GLFW_KEY_W && action == GLFW_PRESS) {
            rotation.rotateAxis((float) Math.toRadians(90), 1, 0, 0);
        }
        if(key == GLFW_KEY_S && action == GLFW_PRESS) {
            rotation.rotateAxis((float) Math.toRadians(-90), 1, 0, 0);
        }
    }


    static class DataPoint {
    // "Time (s)","Latitude (°)","Longitude (°)","Altitude (m)","Altitude WGS84 (m)","Speed (m/s)","Direction (°)","Distance (km)","Horizontal Accuracy (m)","Vertical Accuracy (m)","Satellites"
        float time;
        float latitude;
        float longitude;
        float altitude;
        float altitudeWGS84;
        float speed;
        float normalisedSpeed;
        float direction;
        float distance;
        float horizontalAccuracy;
        float verticalAccuracy;
        float satellites;

        Vector3f pos;
        Vector3f curPos;


        public DataPoint(float time, float latitude, float longitude, float altitude, float altitudeWGS84, float speed, float direction, float distance, float horizontalAccuracy, float verticalAccuracy, float satellites) {
            this.time = time;
            this.latitude = latitude;
            this.longitude = longitude;
            this.altitude = altitude;
            this.altitudeWGS84 = altitudeWGS84;
            this.speed = speed;
            this.direction = direction;
            this.distance = distance;
            this.horizontalAccuracy = horizontalAccuracy;
            this.verticalAccuracy = verticalAccuracy;
            this.satellites = satellites;
        }
    }

}
