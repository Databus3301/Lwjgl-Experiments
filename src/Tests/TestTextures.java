package Tests;

import Render.Entity.Camera.Camera;
import Render.Entity.Entity2D;
import Render.Renderer;
import Render.Shader.Shader;
import Render.Entity.Texturing.Texture;
import Render.Vertices.*;
import Render.Vertices.Model.ObjModel;
import Render.Vertices.Model.ObjModelParser;
import org.joml.Vector2f;

import static org.lwjgl.opengl.GL30.*;

public class TestTextures extends Test {

    // 100*100 box centered around 0,0 with texture coords
    protected Entity2D box;

    protected Shader shader;
    protected Texture texture;
    protected VertexArray va;
    protected IndexBuffer ib;
    protected Camera camera;

    public TestTextures() {
        super();
        ObjModel model = ObjModelParser.parseOBJ("res/models/square.obj");
        box = new Entity2D(new Vector2f(0, 0), model);

        shader = new Shader("res/shaders/basic.shader");
        shader.Bind();

        texture = new Texture("res/textures/woodCrate.png");
        texture.Bind(0);
        shader.SetUniform1i("u_Texture", 0);

        va = new VertexArray();
        float[] vertices = VertexBuffer.parseVertexArray(model.getVertexBuffer());
        VertexBuffer vb = new VertexBuffer(vertices);

        va.AddBuffer(vb, Vertex.GetLayout());

        ib = new IndexBuffer(model.getIndexBuffer());
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
