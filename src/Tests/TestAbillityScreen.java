package Tests;

import Game.Action.Ability;
import Render.Entity.Interactable.Button;
import Render.Entity.Interactable.Label;
import Render.MeshData.Shader.Shader;
import Render.MeshData.Texturing.ColorReplacement;
import Render.MeshData.Texturing.Font;
import Render.MeshData.Texturing.Texture;
import Render.Window;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.util.ArrayList;


public class TestAbillityScreen extends Test {
    Button[] abilityButtons;
    public TestAbillityScreen(){
        super();
        ArrayList<Ability> abilities = new ArrayList<>();
        abilities.add(Game.Action.Abilities.SHOOT);
        abilities.add(Game.Action.Abilities.HOMING);
        abilities.add(Game.Action.Abilities.CIRCLESHOOT);
        abilityButtons = new Button[abilities.size()];
        for (int i = 0; i < abilityButtons.length; i++) {
            abilityButtons[i] = new Button(this, new Vector2f((float) Window.dim.x / abilityButtons.length * i - (float) Window.dim.x / 2 + (float) Window.dim.x / 10 + 100, 0));
            Ability ability = abilities.get(i);
            Label l = new Label(Font.RETRO_TRANSPARENT_WHITE, ability.getName());
            l.setScale(1f);
            abilityButtons[i].setLabel(l);
            abilityButtons[i].scale(250, 85);
            abilityButtons[i].setTooltip(ability.getDescription());
            abilityButtons[i].setTexture(new Texture("input.png", 0));
            abilityButtons[i].setShader(Shader.TEXTURING);
            ColorReplacement cr = new ColorReplacement();
            cr.swap(new Vector4f(1, 1, 1, 1), new Vector4f(0.122f, 0.224f, 0.6f, 1));
            abilityButtons[i].setColorReplacement(cr);

            abilityButtons[i].setPressedCallback((button) -> {
                Window.changeTest(new TestGame());
            });
        }
        

        
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
        for (Button button : abilityButtons) {
            renderer.draw(button);
        }
    }
    @Override
    public void OnKeyInput(long window, int key, int scancode, int action, int mods) {
        super.OnKeyInput(window, key, scancode, action, mods);
    }
}

