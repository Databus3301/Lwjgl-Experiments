package Tests;

import Render.Batch;
import Render.Entity.Camera.Camera;
import Render.Entity.Entity2D;
import Render.Shader.Shader;
import Render.Vertices.Model.ObjModel;
import Render.Vertices.Model.ObjModelParser;
import org.joml.Matrix3f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Test3Dspin extends Test {

    Entity2D[] entities;
    Entity2D main;
    float[][] positions;

    Camera camera;
    ObjModel point = ObjModelParser.parseOBJ("square.obj"); // cylinder and circle are messed up??

    public Test3Dspin() {
        super();
        renderer.setCamera(camera = new Camera(new Vector2f()));

        Shader shader = new Shader("res/shaders/batching.shader");
        ObjModel model = ObjModelParser.parseOBJ("sphere.obj");
        positions = model.getPositions();


        entities = new Entity2D[1000];
        // rotating entity
        main = new Entity2D(new Vector2f(-100, -100), model, shader);
        main.scale(100);
        // mark entity center
        entities[0] = new Entity2D(new Vector2f(), point);;
        entities[0].scale(2);


        // instantiate a new entity at each vertex position of the model
        for(int i = 1; i < entities.length; i++) {
            entities[i] = new Entity2D(new Vector2f(positions[i% positions.length][0]*main.getScale().x+main.getPosition().x, positions[i% positions.length][1]*main.getScale().y+main.getPosition().y), point);
            entities[i].scale(2);
        }
    }

    @Override
    public void OnUpdate(float dt) {
        super.OnUpdate(dt);
        main.rotate(50*dt, 0);
        main.rotate(50*dt, 1);
        main.rotate(50*dt, 2);


        for (int i = 1; i < entities.length; i++) {
            //Vector3f pos = new Vector3f(positions[i% positions.length][0]*150+150, positions[i% positions.length][1]*150+150, positions[i% positions.length][2]*150+150);
            Vector3f pos = new Vector3f(
                    positions[i% positions.length][0]*main.getScale().x+main.getPosition().x,
                    positions[i% positions.length][1]*main.getScale().y+main.getPosition().y,
                    positions[i% positions.length][2]*main.getScale().x+main.getScale().x
            );

            Vector3f mainPos = new Vector3f(main.getPosition(), +main.getScale().x);
            Vector3f relativePos = pos.sub(mainPos, new Vector3f()); // Translate so main entity is at origin

            Matrix3f rotationMatrix = main.getRotation().get(new Matrix3f());
            relativePos.mul(rotationMatrix); // Rotate around origin

            Vector3f rotatedPos = relativePos.add(mainPos); // Translate back
            entities[i].setPosition(rotatedPos.x, rotatedPos.y);
        }

        entities[0].setPosition(main.getCenter());
    }

    @Override
    public void OnRender() {
        super.OnRender();
        renderer.drawEntities2D(entities);
        renderer.drawEntity2D(main);
    }

    @Override
    public void OnKeyInput(long window, int key, int scancode, int action, int mods) {
        super.OnKeyInput(window, key, scancode, action, mods);
    }

}
