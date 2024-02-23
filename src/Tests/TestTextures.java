package Tests;

import Render.Entity.Entity2D;
import Render.Shader.Shader;
import Render.Entity.Texturing.Texture;
import Render.Vertices.Model.ObjModel;
import Render.Vertices.Model.ObjModelParser;
import org.joml.Vector2f;

public class TestTextures extends Test {

    protected Entity2D square;
    protected Shader shader;

    public TestTextures() {
        super();

        shader = new Shader("res/shaders/texturing.shader");
        shader.bind();

        Texture texture = new Texture("res/textures/fonts/oldschool_white.png", 0);
        //Texture texture = new Texture("res/textures/woodCrate.png", 0);
        shader.setUniform1i("u_Texture", 0);

        ObjModel model = ObjModelParser.parseOBJ("res/models/square.obj");
        square = new Entity2D(new Vector2f(0, 0), model, texture, shader);
        square.scale(150);
    }

    @Override
    public void OnUpdate(float dt) {
        super.OnUpdate(dt);
    }

    @Override
    public void OnRender() {
        super.OnRender();
        renderer.drawEntity2D(square);
    }

    @Override
    public void OnClose() {
        super.OnClose();
        shader.delete();
    }
}
