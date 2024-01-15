package Tests;

import Render.Entity.Camera.Camera;
import Render.Renderer;
import Render.Shader.Shader;
import Render.Entity.Texturing.Texture;
import Render.Vertices.IndexBuffer;
import Render.Vertices.VertexArray;
import Render.Vertices.VertexBuffer;
import Render.Vertices.VertexBufferLayout;
import org.joml.Vector2f;

import static org.lwjgl.opengl.GL30.*;

public class TestTextures extends Test {

    // 100*100 box centered around 0,0 with texture coords
    protected float[] vertices = {
            // position		texture coord (uv?)
            -50f, -50f,   	0.0f, 0.0f, // 0
            +50f, -50f,		1.0f, 0.0f, // 1
            +50f, +50f,		1.0f, 1.0f, // 2
            -50f, +50f,		0.0f, 1.0f, // 3
    };

    protected int[] indices = {
            0, 1, 2,
            2, 3, 0,
    };

    protected Shader shader;
    protected Texture texture;
    protected VertexArray va;
    protected IndexBuffer ib;
    protected Camera camera;
    protected Renderer renderer;

    public TestTextures() {
        renderer = new Renderer();

        shader = new Shader("res/shaders/basic.shader");
        shader.Bind();

        texture = new Texture("res/woodCrate.png");
        texture.Bind(0);
        shader.SetUniform1i("u_Texture", 0);

        va = new VertexArray();
        VertexBuffer vb = new VertexBuffer(vertices);
        VertexBufferLayout layout = new VertexBufferLayout();

        layout.PushF(2);
        layout.PushF(2);
        va.AddBuffer(vb, layout);

        ib = new IndexBuffer(indices);
        camera = new Camera(new Vector2f());
    }

    @Override
    public void OnUpdate(float dt) {
        super.OnUpdate(dt);
    }

    @Override
    public void OnRender() {
        super.OnRender();

        shader.Bind();
        setUniforms();

        renderer.Draw(va, ib, shader);
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);
    }

    protected void setUniforms() {
        shader.SetUniformMat4f("uProj", camera.getProjectionMatrix());
        shader.SetUniformMat4f("uView", camera.getViewMatrix());
        shader.SetUniformMat4f("uModel", camera.calcModelMatrix());
    }

    @Override
    public void OnClose() {
        super.OnClose();
        shader.Delete();
    }
}
