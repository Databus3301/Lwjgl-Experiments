package Game;

import Game.Entities.Dungeon.Room;
import Game.Entities.Player;
import Render.Entity.Interactable.Button;
import Tests.Test;
import org.joml.Vector2f;

import java.util.ArrayList;

public class UI {
    public static void onLvlUp(Player player, Test test, Room room, int numButtons) {
        Test.renderer.cursorShow();
        ArrayList<Button> buttons = new ArrayList<>();
        for (int i = 0; i < numButtons; i++) {
            buttons.add(new Button(test, new Vector2f(room.getWitdh()/numButtons*i-room.getWitdh()/2, 0)));
            buttons.get(i).scale(room.getWitdh()/10, room.getHeight()/10);
            buttons.get(i).getLabel().setText("Button " + i);
            buttons.get(i).setColor(0.5f, 0.5f, 0.5f, 1);
        }
        Test.renderer.draw(buttons);
    }

}
