package Tests;

import Render.Entity.Camera.Camera;
import Render.Renderer;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

// TODO: rework into interface?

/**
Serves as a template for all tests.
Tests are used to test the functionality of the engine.
 **/
public class Test {

    public static Renderer renderer;
    public Vector2f mousePos = new Vector2f(0, 0);

    public Test() {
        renderer = new Renderer();
    }
    public void OnUpdate(float dt) {}
    public void OnRender() {
        renderer.getCamera().onRender();
        renderer.clear();
    }
    public void OnClose() {
        renderer.getCurrentShader().delete();
    }
    public void OnKeyInput(long window, int key, int scancode, int action, int mods) {}


    public static Renderer getRenderer() {
        return renderer;
    }
}
