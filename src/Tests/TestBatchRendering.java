package Tests;

import Render.Batch;
import Render.Entity.Entity2D;
import Render.Shader.Shader;
import Render.Vertices.Model.ObjModel;
import Render.Vertices.Model.ObjModelParser;
import Render.Window.Window;
import org.joml.Vector2f;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;

public class TestBatchRendering extends Test {

    Entity2D[] entities;
    Batch b;

    public TestBatchRendering() {
        super();
        ObjModel model = ObjModelParser.parseOBJ("res/models/untitled.obj");
        Shader shader = new Shader("res/shaders/batching.shader");

        entities = new Entity2D[20*20];
        int index = 0;
        for(int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                entities[index] = new Entity2D(new Vector2f(20*i, 20*j), model);
                entities[index].setScale(new Vector2f(10f, 10f));
                index++;
            }
        }
//        entities = new Entity2D[6];
//        entities[0] = new Entity2D(new Vector2f(-25, 0), model, shader);
//        entities[0].setScale(new Vector2f(25f, 25f));
//        entities[1] = new Entity2D(new Vector2f(25, 0), model, shader);
//        entities[1].setScale(new Vector2f(25f, 25f));
//        entities[2] = new Entity2D(new Vector2f(25, 50), model, shader);
//        entities[2].setScale(new Vector2f(25f, 25f));
//        entities[3] = new Entity2D(new Vector2f(-25, 50), model, shader);
//        entities[3].setScale(new Vector2f(25f, 25f));
//        entities[4] = new Entity2D(new Vector2f(25, -50), model, shader);
//        entities[4].setScale(new Vector2f(25f, 25f));
//        entities[5] = new Entity2D(new Vector2f(-25, -50), model, shader);
//        entities[5].setScale(new Vector2f(25f, 25f));


        b = renderer.SetupBatch(entities);
        Entity2D.getCamera().setScale(new Vector2f(5f, 5f));
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
//        for (Entity2D entity : entities) {
//            renderer.DrawEntity2D(entity);
//        }

        renderer.DrawBatch(b);
    }

    @Override
    public void OnKeyInput(long window, int key, int scancode, int action, int mods) {
        super.OnKeyInput(window, key, scancode, action, mods);

        if (key == GLFW_KEY_A && action == GLFW_PRESS) {
            Entity2D.getCamera().setVelocity(200f, 0);
        }
        if (key == GLFW_KEY_D && action == GLFW_PRESS) {
            Entity2D.getCamera().setVelocity(-200f, 0);
        }
        if (key == GLFW_KEY_W && action == GLFW_PRESS) {
            Entity2D.getCamera().setVelocity(0, -200f);
        }
        if (key == GLFW_KEY_S && action == GLFW_PRESS) {
            Entity2D.getCamera().setVelocity(0, 200f);
        }
    }
}
