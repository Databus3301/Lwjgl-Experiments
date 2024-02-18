package Tests;

import Render.Entity.Entity2D;
import Render.Shader.Shader;
import Render.Entity.Texturing.Texture;
import Render.Vertices.Model.ObjModel;
import Render.Vertices.Model.ObjModelParser;
import org.joml.Vector2f;

public class TestTextures extends Test {

    // 100*100 box centered around 0,0 with texture coords
    protected Entity2D box;

    protected Shader shader;

    public TestTextures() {
        super();

        shader = new Shader("res/shaders/basic.shader");
        shader.bind();

        Texture texture = new Texture("res/textures/woodCrate.png", 0);
        texture.Bind(0);
        shader.setUniform1i("u_Texture", 0);

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
        renderer.drawEntity2D(box);
    }

    @Override
    public void OnClose() {
        super.OnClose();
        shader.delete();
    }
}
