package Tests;

import Render.Entity.Entity2D;
import Render.Entity.Texturing.Font;
import Render.Entity.Texturing.Texture;
import Render.Shader.Shader;
import Render.Vertices.Model.ObjModel;
import Render.Vertices.Model.ObjModelParser;
import org.joml.Vector2f;

import java.util.concurrent.atomic.AtomicInteger;

public class TestFont extends Test{

    String s = "Hallo Cornelius!";
    Font font = new Font(new Vector2f(128, 64), 32, 18, 5, 18, 7, 9, 0);
    Shader shader = new Shader("res/shaders/texturing.shader");
    Texture texture = new Texture("res/textures/fonts/oldschool_black.png", 0);

    public TestFont() {
        super();
    }
    public void OnUpdate(float dt) {
        super.OnUpdate(dt);
    }
    public void OnRender() {
        super.OnRender();
        //TODO: move to <renderer> class
        //TODO: auto aspect ratio
        //TODO: make this work on the same model (save original and changed texture coordinates)

        AtomicInteger index = new AtomicInteger();
        s.chars().forEach(c -> {
            ObjModel model = ObjModelParser.parseOBJ("res/models/square.obj");
            Entity2D eChar = new Entity2D(new Vector2f(7* index.get() *5-225, 0), model, texture, shader);
            eChar.scale(new Vector2f(35/2f, 45/2f));
            eChar.getModel().setTextures(font.getCharTexCoords(c));
            renderer.drawEntity2D(eChar);
            index.getAndIncrement();
        });
    }
    public void OnClose() {
        super.OnClose();
    }
    public void OnKeyInput(long window, int key, int scancode, int action, int mods) {
        super.OnKeyInput(window, key, scancode, action, mods);
    }

}
