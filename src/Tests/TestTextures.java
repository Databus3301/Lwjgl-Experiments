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

    public TestTextures() {
        super();

        shader = new Shader("res/shaders/basic.shader");
        shader.Bind();

        Texture texture = new Texture("res/textures/woodCrate.png", 0);
        texture.Bind(0);
        shader.SetUniform1i("u_Texture", 0);

        ObjModel model = ObjModelParser.parseOBJ("res/models/square.obj");
        box = new Entity2D(new Vector2f(0, 0), model, texture, shader);
    }

    @Override
    public void OnUpdate(float dt) {
        super.OnUpdate(dt);
    }

    @Override
    public void OnRender() {
        super.OnRender();
        renderer.DrawEntity2D(box);
    }

    @Override
    public void OnClose() {
        super.OnClose();
        shader.Delete();
    }
}
