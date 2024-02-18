package Tests;

import Render.Batch;
import Render.Entity.Camera.Camera;
import Render.Entity.Entity2D;
import Render.Vertices.Model.ObjModel;
import Render.Vertices.Model.ObjModelParser;
import org.joml.Vector2f;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL43.*;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;

/**
 * Batch rendering takes a lot of (memory and) pre-processing, but is much faster than normal rendering, when dealing with a lot of entities
 * A combination of Instanced Rendering and Batch Rendering would be ideal, depending on the situation
 */

public class TestBatchRendering extends Test {

    Entity2D[] entities;
    Camera camera;
    Batch b;

    Boolean batching; // test var

    public TestBatchRendering() {
        super();
        // DEBUG
        batching = true;
        int DIM = 200;
        //

        renderer.setCamera(camera = new Camera());
        camera.setScale(new Vector2f(200f/DIM, 200f/DIM));

        ObjModel[] models = new ObjModel[] {
                ObjModelParser.parseOBJ("res/models/testModel3.obj"),
                ObjModelParser.parseOBJ("res/models/sphere.obj"),
                ObjModelParser.parseOBJ("res/models/square.obj"),
                ObjModelParser.parseOBJ("res/models/circle.obj")
        };

        // spread entites out in a grid using above models
        entities = new Entity2D[DIM*DIM];
        int index = 0;
        for(int i = 0; i < DIM; i++) {
            for (int j = 0; j < DIM; j++) {
                entities[index] = new Entity2D(new Vector2f(5*i-DIM*2, 5*j-DIM*2), models[index%4]);
                entities[index].setScale(new Vector2f(5/2f, 5/2f));
                index++;
            }
        }
        System.out.println("Entities: " + index);
        // print out the first entities model data


        if(batching)
             b = renderer.setupBatch(entities);

    }

    @Override
    public void OnUpdate(float dt) {
        super.OnUpdate(dt);
        Vector2f effectiveVelocity = new Vector2f(camera.getVelocity());
        camera.translate(effectiveVelocity.mul(dt));
    }

    @Override
    public void OnRender() {
        super.OnRender();

        glClearColor(0.3f, 0.7f, 0.6f, 1.0f);

        if(batching)
            renderer.drawBatch(b);
        else
            renderer.drawEntities2D(entities);
    }

    @Override
    public void OnKeyInput(long window, int key, int scancode, int action, int mods) {
        super.OnKeyInput(window, key, scancode, action, mods);

        if (key == GLFW_KEY_A && action == GLFW_PRESS) {
            camera.setVelocity(200f, 0);
        }
        if (key == GLFW_KEY_D && action == GLFW_PRESS) {
            camera.setVelocity(-200f, 0);
        }
        if (key == GLFW_KEY_W && action == GLFW_PRESS) {
            camera.setVelocity(0, -200f);
        }
        if (key == GLFW_KEY_S && action == GLFW_PRESS) {
            camera.setVelocity(0, 200f);
        }
    }
}
