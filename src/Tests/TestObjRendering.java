package Tests;

import Render.Entity.Camera.Camera;
import Render.Entity.Entity;
import Render.Entity.Entity2D;
import Render.Shader.Shader;
import Render.Vertices.*;
import Render.Vertices.Model.ObjModel;
import Render.Vertices.Model.ObjModelParser;
import org.joml.Vector2f;
import org.joml.Vector3f;

import static org.lwjgl.opengl.GL30.*;


public class TestObjRendering extends Test {
    Entity2D entity;
    Entity2D entity2;
    Entity2D entity3;
    Entity2D entity4;
    Entity2D entity5;
    Shader shader;

    public TestObjRendering() {
        super();

        shader = new Shader("res/shaders/objrendering.shader");
        shader.Bind();

        ObjModel model = ObjModelParser.parseOBJ("res/models/untitled.obj");

        entity = new Entity2D(new Vector2f(0,0), model, shader);
        entity.setScale(new Vector2f(300, 300));

        entity2 = new Entity2D(new Vector2f(300,300), model, shader);
        entity2.setScale(new Vector2f(200, 200));

        entity3 = new Entity2D(new Vector2f(-300,-300), model, shader);
        entity3.setScale(new Vector2f(200, 200));

        entity4 = new Entity2D(new Vector2f(-300,300), model, shader);
        entity4.setScale(new Vector2f(200, 200));

        entity5 = new Entity2D(new Vector2f(300,-300), model, shader);
        entity5.setScale(new Vector2f(200, 200));
    }

    @Override
    public void OnUpdate(float dt) {
        super.OnUpdate(dt);

        entity.setPosition(entity.getVelocity());
        entity.accelaerate(new Vector2f(0.1f, 0.1f));
    }

    @Override
    public void OnRender() {
        super.OnRender();

        renderer.DrawEntity2D(entity);
        renderer.DrawEntity2D(entity2);
        renderer.DrawEntity2D(entity3);
        renderer.DrawEntity2D(entity4);
        renderer.DrawEntity2D(entity5);
    }

    @Override
    public void OnClose() {
        super.OnClose();
    }
}
