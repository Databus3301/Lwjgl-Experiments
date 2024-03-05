package Tests;

import Render.Entity.Entity2D;
import Render.Shader.Shader;
import Render.Vertices.Model.ObjModel;
import Render.Vertices.Model.ObjModelParser;
import org.joml.Vector2f;

import static org.lwjgl.opengl.GL30.*;


public class TestObjRendering extends Test {
    Entity2D entity;
    Shader shader;

    public TestObjRendering() {
        super();

        shader = new Shader("res/shaders/default.shader");
        shader.bind();

        ObjModel model = ObjModelParser.parseOBJ("cam.obj");

        entity = new Entity2D(new Vector2f(0,0), model, shader);
        entity.setScale(new Vector2f(50f, 50f));

        new TestObjModelParser(entity.getModel());
    }

    @Override
    public void OnUpdate(float dt) {
        super.OnUpdate(dt);
    }

    @Override
    public void OnRender() {
        super.OnRender();

        glClearColor(1f, 0.0f, 0.0f, 1.0f);
        renderer.drawEntity2D(entity);

    }

    @Override
    public void OnClose() {
        super.OnClose();
        if(entity.getShader() != null)
            entity.getShader().delete();
    }

    @Override
    public void OnKeyInput(long window, int key, int scancode, int action, int mods) {
        super.OnKeyInput(window, key, scancode, action, mods);
    }
}
