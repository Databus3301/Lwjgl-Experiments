package Game;

import Game.Action.Ability;
import Game.Action.Upgrade;
import Game.Entities.Dungeon.Room;
import Game.Entities.Player;
import Render.Entity.Interactable.Button;
import Tests.Test;
import org.joml.Vector2f;

public class UI {
    private static Button[] upgradeButtons;

    public static void onLvlUp(Player player, Test scene, Room room, int numButtons) {
        if(upgradeButtons == null) {
            upgradeButtons = new Button[numButtons];
            for (int i = 0; i < numButtons; i++) {
                upgradeButtons[i] = new Button(scene, new Vector2f(room.getWitdh() / numButtons * i - room.getWitdh() / 2 + room.getWitdh() / 10, 0));
                upgradeButtons[i].scale(room.getWitdh() / 10, room.getHeight() / 10);
                upgradeButtons[i].getLabel().setText("Button " + i);
                upgradeButtons[i].setColor(0.5f, 0.5f, 0.5f, 1);
                upgradeButtons[i].setReleasedCallback((button) -> {
                    //TODO: field design
                    /*
                    - white rect-border with text inside
                    - possibly one icon

                    - Upgrades
                        - large ability name plus colon
                        - small upgrade description
                    - Abilities
                        - large ability name
                        - small ability description

                    - on hover: border color darkens
                    - on click: border color lightens
                    - on release: upgrade is applied

                     */

                    Ability cs = player.getAbilities().get(1);
                    Upgrade u = cs.getRndmUpgrade();
                    u.applyTo(cs);

                    upgradeButtons = null;
                });
            }
        }
    }



    public static void draw() {
        if(upgradeButtons != null)
            Test.renderer.draw(upgradeButtons);
    }

}
