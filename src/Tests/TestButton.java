package Tests;

import Render.MeshData.Texturing.Texture;
import Render.Entity.Interactable.Button;
import Render.MeshData.Shader.Shader;
import org.joml.Vector2f;

public class TestButton extends Test {
    Button b;
    public TestButton() {
        super();
        Texture icon = new Texture("woodCrate.png", 0);
        b = new Button(this, new Vector2f());

        b.scale(500); // TODO: Make label scaling more consistent with different button sizes
        b.setLabel("This is a button\nwith a label");
        b.getLabel().setScale(10000);
        b.setTooltip("This is a tooltip");
        b.setIcon(icon);
        b.setShader(Shader.TEXTURING);
        b.setHitTime(1000);
    }

    @Override
    public void OnUpdate(float dt) {
        super.OnUpdate(dt);
        b.scale(-15f*dt);
    }
    @Override
    public void OnRender() {
        super.OnRender();
        renderer.drawButton(b);
        renderer.drawCollisionRect(b);
    }
    @Override
    public void OnClose() {
        super.OnClose();
    }
    @Override
    public void OnKeyInput(long window, int key, int scancode, int action, int mods) {
        super.OnKeyInput(window, key, scancode, action, mods);
    }
}
