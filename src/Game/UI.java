package Game;

import Game.Entities.Player;
import Tests.Test;
import org.joml.Vector2f;

public class UI {
    public static void onLvlUp(Player player) {
        Test.renderer.cursorShow();
    }

}
