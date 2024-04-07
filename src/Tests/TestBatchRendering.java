package Tests;

import Render.Batch;
import Render.Entity.Camera.Camera;
import Render.Entity.Entity2D;
import Render.MeshData.Shader.Shader;
import Render.MeshData.Model.ObjModel;
import Render.MeshData.Model.ObjModelParser;
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

    boolean batching, spin; // debug vars


    public TestBatchRendering() {
        super();
        // DEBUG
        batching = true;
        spin = false;
        int DIM = 200;
        //
        renderer.setCurrentShader(new Shader("batching.shader"));
        renderer.setCamera(camera = new Camera());
        camera.setScale(new Vector2f(200f/DIM, 200f/DIM));

        ObjModel[] models = new ObjModel[] {
                ObjModelParser.parseOBJ("square.obj"),
                ObjModelParser.parseOBJ("sphere.obj"),
                ObjModelParser.parseOBJ("circle.obj"),
                ObjModelParser.parseOBJ("testModel3.obj"),
        };

        /* TODO: investigate weird 1:20 ratio of entity position between batching and non-batching
           UPDATE:
           TODO: The 1:20 ratio was due to the camera scaling being set to 200f/DIM, with DIM being 10 while testing resulting in a scale of 20f in turn causing this ratio,
           TODO: which is why it works just fine at DIM=200f
           TODO: so what should really be investigated is the camera scaling in relation to entity rendering
        */
        // spread entities out in a grid using above models
        entities = new Entity2D[DIM*DIM];
        int index = 0;
        for(int i = 0; i < DIM; i++) {
            for (int j = 0; j < DIM; j++) {
                entities[index] = new Entity2D(new Vector2f(2*i-DIM, 2*j-DIM).mul(1.5f), models[index % models.length]);
                entities[index].setScale(new Vector2f(5/4f, 5/4f));

                index++;
            }
        }
        System.out.println("Entities: " + index);

        if(batching)
             b = renderer.setupBatch(entities);
    }

    @Override
    public void OnUpdate(float dt) {
        super.OnUpdate(dt);
        Vector2f effectiveVelocity = new Vector2f(camera.getVelocity());
        camera.translate(effectiveVelocity.mul(dt));

        if(spin) {
            camera.rotate(10f*dt, 0);
            camera.rotate(10f*dt, 1);
            camera.rotate(10f*dt, 2);
        }
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
