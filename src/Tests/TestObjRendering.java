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

    protected float[] vertices = {
            // position		texture coord (uv?)
            -0.5f, -0.5f,   	0.0f, 0.0f, // 0
            +0.5f, -0.5f,		1.0f, 0.0f, // 1
            +0.5f, +0.5f,		1.0f, 1.0f, // 2
            -0.5f, +0.5f,		0.0f, 1.0f, // 3
    };

    protected int[] indices = {
            0, 1, 2,
            2, 3, 0,
    };

    public TestObjRendering() {
        super();
        ObjModel model = ObjModelParser.parseOBJ("res/models/testModel3.obj");
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
