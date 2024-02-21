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

        Texture texture = new Texture("res/textures/woodCrate.png", 0);
        shader.setUniform1i("u_Texture", 0);

        ObjModel model = ObjModelParser.parseOBJ("res/models/square.obj");
        square = new Entity2D(new Vector2f(0, 0), model, texture, shader);
        square.scale(50);
    }

    @Override
    public void OnUpdate(float dt) {
        super.OnUpdate(dt);
        square.rotate(100.0f*dt, 0);
        square.rotate(100.0f*dt, 1);
        square.rotate(100.0f*dt, 2);
        System.out.println(new Vector2f(square.getPosition()));
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
