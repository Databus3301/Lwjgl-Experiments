package Tests;

import Render.Entity.Entity2D;
import Render.Entity.Interactable.Button;
import Render.Entity.Interactable.Label;
import Render.MeshData.Model.ObjModel;
import Render.MeshData.Shader.Shader;
import Render.MeshData.Texturing.ColorReplacement;
import Render.MeshData.Texturing.Font;
import Render.MeshData.Texturing.Texture;
import Render.Window;
import org.joml.Vector2f;
import org.joml.Vector4f;

import static org.lwjgl.opengl.GL11C.*;

public class TestStartScreen extends Test {

    Button b;
    public TestStartScreen() {
        super();
        b = new Button(this, new Vector2f());
        Label l = new Label(Font.RETRO_TRANSPARENT_WHITE, "Start");
        l.setScale(100f);
        b.setLabel(l);
        b.scale(100, 50);
        b.setTooltip("Start the game");
        b.setTexture(new Texture("input.png", 0));
        b.setShader(Shader.TEXTURING);

        ColorReplacement cr = new ColorReplacement();
        cr.swap(new Vector4f(1, 1, 1, 1), new Vector4f(0.122f, 0.224f, 0.6f, 1));
        b.setColorReplacement(cr);
    }

    @Override
    public void OnStart() {
        super.OnStart();
        renderer.cursorShow();
    }
    @Override
    public void OnUpdate(float dt) {
        super.OnUpdate(dt);
    }
    @Override
    public void OnRender() {
        super.OnRender();
        renderer.draw(b);
    }
    @Override
    public void OnKeyInput(long window, int key, int scancode, int action, int mods) {
        super.OnKeyInput(window, key, scancode, action, mods);
    }
}
