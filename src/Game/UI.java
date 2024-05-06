package Game;

import Game.Action.Ability;
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
                    Ability circle_shoot = player.getAbilities().get(1);
                    circle_shoot.upgrades.get(0).applyTo(circle_shoot);
                    upgradeButtons = null;
                    System.out.println("Button " + " pressed");
                });
            }
        }
    }



    public static void draw() {
        if(upgradeButtons != null)
            Test.renderer.draw(upgradeButtons);
    }

}
