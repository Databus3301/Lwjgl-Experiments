package Tests;

import Render.*;
import Render.Entity.*;
import Render.Entity.Camera.Camera;
import Render.Shader.*;
import Render.Vertices.*;
import Render.Vertices.Model.ObjModel;
import Render.Vertices.Model.ObjModelParser;
import org.joml.*;
// rewrite the pong test to use the Entity2D class
public class TestPong2 extends Test {
    private Entity2D ball, wallLeft, wallRight;


    public TestPong2() {
        Vector2i dim = Render.Window.Window.dim;

        ObjModel model = ObjModelParser.parseOBJ("res/models/square.obj");

        ball = new Entity2D(new Vector2f(), new Vector2f(100, 100), model);
        wallLeft = new Entity2D(new Vector2f(-dim.x / 2f, 0), new Vector2f(100, 100), model);
        wallRight = new Entity2D(new Vector2f(+dim.x / 2f, 0), new Vector2f(100, 100), model);

        ball.setVelocity(new Vector2f(250f, 0));


        ///////
        renderer = new Renderer();

        final float[] vertices = {
                // position
                -50f, -50f,
                +50f, -50f,
                +50f, +50f,
                -50f, +50f,
        };

        final int[] indices = {
                0, 1, 2,
                2, 3, 0,
        };

        Shader shader = new Shader("res/shaders/pong.shader");
        shader.Bind();

        VertexArray va = new VertexArray();
        VertexBuffer vb = new VertexBuffer(vertices);
        VertexBufferLayout layout = new VertexBufferLayout();

        layout.PushF(3);
        va.AddBuffer(vb, layout);

        IndexBuffer ib = new IndexBuffer(indices);
        Camera camera = new Camera(new Vector2f());

        ///////
    }

    @Override
    public void OnUpdate(float dt) {
        super.OnUpdate(dt);
    }

    @Override
    public void OnRender() {
        super.OnRender();
    }

    @Override
    public void OnKeyInput(long window, int key, int scancode, int action, int mods) {
        super.OnKeyInput(window, key, scancode, action, mods);
    }

    @Override
    public void OnClose() {
        super.OnClose();
      //  shader.Delete();
    }
}
