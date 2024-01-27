package Tests;

import Render.Batch;
import Render.Entity.Entity2D;
import Render.Vertices.Model.ObjModel;
import Render.Vertices.Model.ObjModelParser;
import org.joml.Vector2f;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;

/**
 * Batch rendering takes a lot of memory and pre-processing, but is much faster than normal rendering, when dealing with a lot of entities
 * A combination of Instanced Rendering and Batch Rendering would be ideal, depending on the situation
 */

public class TestBatchRendering extends Test {

    Entity2D[] entities;
    Batch b;

    Boolean batching; // test var

    public TestBatchRendering() {
        super();
        batching = true;
        int DIM = 20;

        ObjModel[] model = new ObjModel[] { ObjModelParser.parseOBJ("res/models/testModel3.obj"), ObjModelParser.parseOBJ("res/models/untitled.obj"), ObjModelParser.parseOBJ("res/models/testModel.obj") };
        entities = new Entity2D[DIM*DIM];
        int index = 0;
        for(int i = 0; i < DIM; i++) {
            for (int j = 0; j < DIM; j++) {
                entities[index] = new Entity2D(new Vector2f(5*i, 5*j), model[index%3]);
                entities[index].setScale(new Vector2f(5/2f, 5/2f));
                index++;
            }
        }
        if(batching)
             b = renderer.SetupBatch(entities);
        Entity2D.getCamera().setScale(new Vector2f(8f, 8f));
    }

    @Override
    public void OnUpdate(float dt) {
        super.OnUpdate(dt);
        Vector2f effectiveVelocity = new Vector2f(Entity2D.getCamera().getVelocity());
        Entity2D.getCamera().translate(effectiveVelocity.mul(dt));
    }

    @Override
    public void OnRender() {
        super.OnRender();

        if(batching)
            renderer.DrawBatch(b);
        else
            for (Entity2D e : entities)
                renderer.DrawEntity2D(e);
    }

    @Override
    public void OnKeyInput(long window, int key, int scancode, int action, int mods) {
        super.OnKeyInput(window, key, scancode, action, mods);

        if (key == GLFW_KEY_A && action == GLFW_PRESS) {
            Entity2D.getCamera().setVelocity(400f, 0);
        }
        if (key == GLFW_KEY_D && action == GLFW_PRESS) {
            Entity2D.getCamera().setVelocity(-400f, 0);
        }
        if (key == GLFW_KEY_W && action == GLFW_PRESS) {
            Entity2D.getCamera().setVelocity(0, -400f);
        }
        if (key == GLFW_KEY_S && action == GLFW_PRESS) {
            Entity2D.getCamera().setVelocity(0, 400f);
        }
    }
}
