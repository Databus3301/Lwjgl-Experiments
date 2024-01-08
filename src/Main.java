import Render.Window.Window;

public class Main {

    public static void main(String[] args) {
        Window window;
        if(args.length >= 2)
            window = new Window(args[1]);
        else
            window = new Window();

        window.init();
        window.run();
        window.destroy();
    }
}
