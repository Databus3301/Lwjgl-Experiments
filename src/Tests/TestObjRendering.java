package Tests;

import Render.Entity.Entity2D;
import Render.Shader.Shader;
import Render.Vertices.Model.ObjModel;
import Render.Vertices.Model.ObjModelParser;
import org.joml.Vector2f;

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

        ObjModel model = ObjModelParser.parseOBJ("big_cube.obj");

        entity = new Entity2D(new Vector2f(0,0), model, shader);
        entity.setScale(new Vector2f(50f, 50f));

//        entity2 = new Entity2D(new Vector2f(300,300), model, shader);
//        entity2.setScale(new Vector2f(200, 200));
//
//        entity3 = new Entity2D(new Vector2f(-300,-300), model, shader);
//        entity3.setScale(new Vector2f(200, 200));
//
//        entity4 = new Entity2D(new Vector2f(-300,300), model, shader);
//        entity4.setScale(new Vector2f(200, 200));
//
//        entity5 = new Entity2D(new Vector2f(300,-300), model, shader);
//        entity5.setScale(new Vector2f(200, 200));
    }

    @Override
    public void OnUpdate(float dt) {
        super.OnUpdate(dt);

//        entity.rotate(10f*dt);
//        entity.scale(new Vector2f(10f*dt, 10f*dt));
//
//        entity2.translate(entity2.getVelocity().mul(dt));
//        entity2.accelaerate(new Vector2f(50f, 50f));
//
//        entity3.translate(entity3.getVelocity().mul(dt));
//        entity3.accelaerate(new Vector2f(-50f, 50f));
//
//        entity4.translate(entity4.getVelocity().mul(dt));
//        entity4.accelaerate(new Vector2f(50f, -50f));
//
//        entity5.translate(entity5.getVelocity().mul(dt));
//        entity5.accelaerate(new Vector2f(-50f, -50f));
    }

    @Override
    public void OnRender() {
        super.OnRender();

        glClearColor(1f, 0.0f, 0.0f, 1.0f);
        renderer.DrawEntity2D(entity);
//        renderer.DrawEntity2D(entity2);
//        renderer.DrawEntity2D(entity3);
//        renderer.DrawEntity2D(entity4);
//        renderer.DrawEntity2D(entity5);
    }

    @Override
    public void OnClose() {
        super.OnClose();
        if(entity.getShader() != null)
            entity.getShader().Delete();
//        if(entity2.getShader() != null)
//            entity2.getShader().Delete();
//        if(entity3.getShader() != null)
//            entity3.getShader().Delete();
//        if(entity4.getShader() != null)
//            entity4.getShader().Delete();
//        if(entity5.getShader() != null)
//            entity5.getShader().Delete();
    }
}
