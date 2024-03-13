package Tests;

import Render.Entity.Camera.Camera;
import Render.Renderer;
import org.joml.Vector2f;

// TODO: rework into interface?

/**
Serves as a template for all tests.
Tests are used to test the functionality of the engine.
 **/
public class Test {

    public static Renderer renderer;
    public Vector2f mousePos = new Vector2f(0, 0);
    private Camera camera;

    public Test() {
        renderer = new Renderer();
        renderer.setCamera(camera = new Camera());
    }
    public void OnUpdate(float dt) {
    }
    public void OnRender() {
        camera.onRender();
        renderer.clear();
    }
    public void OnClose() {}
    public void OnKeyInput(long window, int key, int scancode, int action, int mods) {}


    public static Renderer getRenderer() {
        return renderer;
    }
}
