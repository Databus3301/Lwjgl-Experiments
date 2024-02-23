package Tests;

import Render.Entity.Entity2D;
import Render.Shader.Shader;
import Render.Entity.Texturing.Texture;
import Render.Vertices.Model.ObjModel;
import Render.Vertices.Model.ObjModelParser;
import org.joml.Vector2f;

public class TestTextures extends Test {

    protected Entity2D e;
    protected Shader shader;

    public TestTextures() {
        super();

        shader = new Shader("res/shaders/texturing.shader");
        shader.bind();

        //Texture texture = new Texture("res/textures/fonts/oldschool_white.png", 0);
        Texture texture = new Texture("res/textures/cam2.jpg", 0);
        Texture texture2 = new Texture("res/textures/cam3.jpg", 1);

        ObjModel model = ObjModelParser.parseOBJ("res/models/cam.obj");
        e = new Entity2D(new Vector2f(0, 0), model, texture, shader);
        e.scale(300);
    }

    @Override
    public void OnUpdate(float dt) {
        super.OnUpdate(dt);
        //square.rotate(10f * dt, 0);
        e.rotate(10f * dt, 1);
        //square.rotate(10f * dt, 2);

    }

    @Override
    public void OnRender() {
        super.OnRender();
        shader.setUniform1i("u_Texture", 1);
        Entity2D e2 = e.instantiate();
        renderer.drawEntity2D(e2);
        shader.setUniform1i("u_Texture", 0);
        renderer.drawEntity2D(e);
    }

    @Override
    public void OnClose() {
        super.OnClose();
        shader.delete();
    }
}
