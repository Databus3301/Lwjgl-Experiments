package Tests;

import Render.Batch;
import Render.Entity.Camera.Camera;
import Render.Entity.Entity2D;
import Render.Shader.Shader;
import Render.Vertices.Model.ObjModel;
import Render.Vertices.Model.ObjModelParser;
import org.joml.Vector2f;

public class Test3Dspin extends Test {

    Entity2D[] entities;
    Camera camera;
    ObjModel point = ObjModelParser.parseOBJ("square.obj"); // cylinder and circle are messed up??

    public Test3Dspin() {
        super();
        renderer.setCamera(camera = new Camera(new Vector2f()));

        Shader def = new Shader("res/shaders/default.shader");
        renderer.defaultShader = def;
        Shader special = new Shader("res/shaders/batching.shader");

        ObjModel model = ObjModelParser.parseOBJ("circle.obj");
        entities = new Entity2D[1000];
        entities[0] = new Entity2D(new Vector2f(100, 100), model, special);
        entities[0].scale(150);

        entities[1] = new Entity2D(new Vector2f(), point);;
        entities[1].scale(2);

//        for (int i = 2; i < entities.length; i++) {
//            entities[i] = entities[1].instantiate();
//        }
    }

    @Override
    public void OnUpdate(float dt) {
        super.OnUpdate(dt);
        entities[0].rotate(50*dt, 0);
        entities[0].rotate(50*dt, 1);
        entities[0].rotate(50*dt, 2);

        entities[1].setPosition(entities[0].getCenter());

    }

    @Override
    public void OnRender() {
        super.OnRender();
        renderer.drawEntities2D(entities);
    }

    @Override
    public void OnKeyInput(long window, int key, int scancode, int action, int mods) {
        super.OnKeyInput(window, key, scancode, action, mods);
    }

}
