import Render.Window;
import org.joml.Vector2i;

import static java.lang.Integer.parseInt;

public class Main {

    public static void main(String[] args) {
        Window window;
        if(args.length == 2)
            window = new Window(args[1], new Vector2i(2560,1280));
        else if(args.length == 4)
            window = new Window(args[1], new Vector2i(parseInt(args[2]),parseInt(args[3])));
        else
            window = new Window();

        window.init();
        window.run();
        window.destroy();
    }
}
