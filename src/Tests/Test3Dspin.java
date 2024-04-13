package Tests;

import Render.Entity.Camera.Camera;
import Render.Entity.Entity2D;
import Render.MeshData.Shader.Shader;
import Render.MeshData.Model.ObjModel;
import Render.MeshData.Model.ObjModelParser;
import org.joml.Vector2f;
import org.joml.Vector4f;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.opengl.GL11.GL_LINE;

public class Test3Dspin extends Test {

    Entity2D[] entities;
    Entity2D main;
    float[][] positions;
    Camera camera;
    ObjModel point = ObjModelParser.parseOBJ("square.obj"); // cylinder and circle are messed up??

    public Test3Dspin() {
        super();
        renderer.setMode(GL_LINE); // wireframe mode

        renderer.setCamera(camera = new Camera(new Vector2f()));

        Shader shader = new Shader("res/shaders/batching.shader");
        ObjModel model = ObjModelParser.parseOBJ("testModel3.obj");
        positions = model.getPositions();


        entities = new Entity2D[model.getPositions().length + 1];
        // rotating entity
        main = new Entity2D(new Vector2f(0, 0), model, shader);
        main.scale(300);
        // mark entity center
        entities[0] = new Entity2D(new Vector2f(), point);;
        entities[0].scale(2);


        // instantiate a new entity at each vertex position of the model
        for(int i = 1; i < entities.length; i++) {
            entities[i] = new Entity2D(new Vector2f(), point);
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
            Vector4f pos = new Vector4f(positions[i% positions.length][0], positions[i% positions.length][1], positions[i% positions.length][2], 1);
            Vector4f transformed = main.calcModelMatrix().transform(pos);

            entities[i].setPosition(transformed.x, transformed.y);
        }

        entities[0].setPosition(main.getPosition());

        main.translate(camera.getVelocity().mul(dt, new Vector2f()));

    }

    @Override
    public void OnRender() {
        super.OnRender();
        renderer.draw(entities);
        renderer.draw(main);
    }

    @Override
    public void OnKeyInput(long window, int key, int scancode, int action, int mods) {
        super.OnKeyInput(window, key, scancode, action, mods);

        if (key == GLFW_KEY_A && action == GLFW_PRESS) {
            camera.setVelocity(-200f, 0);
        }
        if (key == GLFW_KEY_D && action == GLFW_PRESS) {
            camera.setVelocity(+200f, 0);
        }
        if (key == GLFW_KEY_W && action == GLFW_PRESS) {
            camera.setVelocity(0, +200f);
        }
        if (key == GLFW_KEY_S && action == GLFW_PRESS) {
            camera.setVelocity(0, -200f);
        }
    }

}
