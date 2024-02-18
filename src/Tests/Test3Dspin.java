package Tests;

import Render.Batch;
import Render.Entity.Camera.Camera;
import Render.Entity.Entity2D;
import Render.Shader.Shader;
import Render.Vertices.Model.ObjModel;
import Render.Vertices.Model.ObjModelParser;
import Render.Window.Window;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Test3Dspin extends Test {

    Entity2D[] entities;
    Camera camera;
    ObjModel point = ObjModelParser.parseOBJ("square.obj"); // cylinder and circle are messed up??
    Shader shader = new Shader("res/shaders/objrendering.shader");

    Batch b;

    public Test3Dspin() {
        super();
        renderer.setCamera(camera = new Camera(new Vector2f()));

        ObjModel model = ObjModelParser.parseOBJ("untitled.obj");
        //ObjModel point = ObjModelParser.parseOBJ("square.obj"); // cylinder and circle are messed up??
        entities = new Entity2D[1000];
        for (int i = 0; i < 1; i++) {
            entities[i] = new Entity2D(new Vector2f(i*50, (i-1)*50), model, renderer.defaultShader);
            entities[i].scale(150);
        }

        for (int i = 1; i < entities.length; i++) {
            entities[i] = new Entity2D(new Vector2f(entities[0].getCenter()), point, shader);;
            entities[i].scale(10);
        }

        //b = renderer.SetupBatch(entities);

        new TestObjModelParser(model);
    }

    int refs = 1;
    int fs = 0;
    @Override
    public void OnUpdate(float dt) {
        super.OnUpdate(dt);
//        entities[0].rotate(5*dt, 0);
//        entities[0].rotate(5*dt, 1);
        entities[0].rotate(100*dt, 2);

//        if(refs == entities.length-1)
//            refs = 1;
//        if(fs % 100 == 0) {
//            entities[refs].setPosition(new Vector2f(entities[0].getPosition()));
//            refs++;
//        }
    }

    @Override
    public void OnRender() {
        super.OnRender();
        renderer.DrawEntities2D(entities);
        //renderer.DrawBatch(b);
    }

    @Override
    public void OnKeyInput(long window, int key, int scancode, int action, int mods) {
        super.OnKeyInput(window, key, scancode, action, mods);
    }

}
