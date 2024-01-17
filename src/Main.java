import Render.Window.Window;
import org.joml.Vector2i;

public class Main {

    public static void main(String[] args) {
        Window window;
        if(args.length >= 2)
            window = new Window(args[1], new Vector2i(1920,1024));
        else
            window = new Window();

        window.init();
        window.run();
        window.destroy();
    }
}
