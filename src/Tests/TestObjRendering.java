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

        //float[] vertexBuffer = VertexBuffer.parseVertexArray(model.getVertexBuffer());
        //ib = new IndexBuffer(model.getIndexBuffer());
        //        int vertexBuffer_ID = glGenBuffers();
        //        glBindBuffer(GL_ARRAY_BUFFER, vertexBuffer_ID);
        //        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        float[] vertexBuffer = VertexBuffer.parseVertexArray(model.getVertexBuffer());

        va = new VertexArray();
        va.AddBuffer(new VertexBuffer(vertexBuffer), Vertex.GetLayout());

        ib = new IndexBuffer(model.getIndexBuffer());

    }

    @Override
    public void OnUpdate(float dt) {
        super.OnUpdate(dt);
        entity.setVelocity(new Vector2f(10, 0));
        renderer.tmpDraw(va, ib);
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
