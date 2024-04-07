package Tests;

import Render.Entity.Entity2D;
import Render.MeshData.Shader.Shader;
import Render.MeshData.Texturing.Texture;
import Render.MeshData.Model.ObjModel;
import Render.MeshData.Model.ObjModelParser;
import org.joml.Vector2f;

import static org.lwjgl.opengl.GL11C.glClearColor;

public class TestTextures extends Test {

    protected Entity2D e;
    protected Shader shader;

    public TestTextures() {
        super();

        shader = new Shader("res/shaders/texturing.shader");
        shader.bind();

        Texture texture = new Texture("res/textures/cam2.jpg", 0);

        ObjModel model = ObjModelParser.parseOBJ("res/models/square.obj");
        e = new Entity2D(new Vector2f(0, 0), model, texture, shader);
        e.scale(300);

    }

    @Override
    public void OnUpdate(float dt) {
        super.OnUpdate(dt);
        e.rotate(10f * dt, 1);
    }

    @Override
    public void OnRender() {
        super.OnRender();

        glClearColor(0.1f, 0.1f, 0.1f, 1.0f);
        renderer.drawEntity2D(e);
    }

    @Override
    public void OnClose() {
        super.OnClose();
        shader.delete();
    }
}
