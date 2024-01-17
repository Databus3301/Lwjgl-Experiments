package Tests;

import Render.Entity.Entity2D;
import Render.Vertices.*;
import Render.Vertices.Model.ObjModel;
import Render.Vertices.Model.ObjModelParser;
import org.joml.Vector2f;
import static org.lwjgl.opengl.GL30.*;


public class TestObjRendering extends Test {
    Entity2D entity;
    VertexArray va;
    IndexBuffer ib;


    public TestObjRendering() {
        super();
        ObjModel model = ObjModelParser.parseOBJ("res/models/untitled.obj");
        entity = new Entity2D(new Vector2f(0, 0), model);

        va = new VertexArray();
        va.AddBuffer(model.getVertexBuffer(), Vertex.GetLayout());
        ib = model.getIndexBuffer();
    }

    @Override
    public void OnUpdate(float dt) {
        super.OnUpdate(dt);
        entity.setVelocity(new Vector2f(10, 0));
        renderer.Draw(va, ib);
    }

    @Override
    public void OnRender() {
        super.OnRender();
    }

    @Override
    public void OnClose() {
        super.OnClose();
    }
}
