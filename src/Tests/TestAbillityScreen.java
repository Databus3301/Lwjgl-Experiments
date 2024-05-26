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

        int io = 0; // iterator offset
        int skips = 2; // non weapon ability count

        int bc = abilities.length - skips; // button count
        int bw = Window.dim.x / (bc+1); // button width
        int bo = bw/bc; // button offset

        abilityButtons = new Button[bc];

        for (int i = 0; i < abilities.length; i++) {
            Ability ability = abilities[i];
            // exclude non weapon abilities | increment iterator offset
            if((ability.getName() == "Dash" || ability.getName() == "Shield") && ++io > -1)  continue;

            // create and spread out buttons
            abilityButtons[i-io] = new Button(this, new Vector2f((bw+bo) * (i-io) - (bw+bo) * (bc/2f - 0.5f), 0));

            abilityButtons[i-io].scale(bw/2f, 85);
            abilityButtons[i-io].setTooltip(ability.getDescription());
            abilityButtons[i-io].setLabel(" " + ability.getName());
            abilityButtons[i-io].getLabel().setFont(Font.RETRO_TRANSPARENT_WHITE);
            abilityButtons[i-io].getLabel().setScale(15f);
            abilityButtons[i-io].setTexture(new Texture("input.png", 0));
            abilityButtons[i-io].setShader(Shader.TEXTURING);

            // change text colors
//            ColorReplacement cr = new ColorReplacement();
//            cr.swap(new Vector4f(1, 1, 1, 1), new Vector4f(1f, 1f, 0.2f, 1));
//            abilityButtons[i].setColorReplacement(cr);

            abilityButtons[i-io].setPressedCallback((button) -> {
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

