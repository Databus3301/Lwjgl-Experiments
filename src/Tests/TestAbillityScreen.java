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

import static Game.Action.Abilities.abilities;


public class TestAbillityScreen extends Test {
    Button[] abilityButtons;
    public TestAbillityScreen(){
        super();

        int bc = abilities.length; // button count
        int bw = Window.dim.x / bc; // button width
        abilityButtons = new Button[bc];


        for (int i = 0; i < abilityButtons.length; i++) {
            abilityButtons[i] = new Button(this, new Vector2f(bw * i - bw * (bc/2f), 0));
            Ability ability = abilities[i];

            abilityButtons[i].scale(bw, 85);
            abilityButtons[i].setTooltip(ability.getDescription());
            abilityButtons[i].setLabel(ability.getName());
            abilityButtons[i].setTexture(new Texture("input.png", 0));
            abilityButtons[i].setShader(Shader.TEXTURING);

            abilityButtons[i].setPressedCallback((button) -> {
                TestGame tg = new TestGame();
                Window.changeTest(tg);
                tg.getPlayer().addAbility(ability);
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

