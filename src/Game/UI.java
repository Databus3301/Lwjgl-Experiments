package Game;

import Render.Window;
import Tests.Test;
import org.joml.Vector2f;

import java.util.function.Consumer;

public class UI {
    public static Consumer<Test> onLvlUp = (test) -> {
        Test.renderer.cursorShow();
        System.out.println("Level up!");
    };

}
