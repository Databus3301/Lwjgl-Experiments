package Tests;

import Render.Entity.Interactable.Interactable;
import Render.Renderer;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

// TODO: rework into interface?

/**
Serves as a template for all tests.
Tests are used to test the functionality of the engine.
 **/
public class Test {

    public static Renderer renderer;
    public Vector2f mousePos = new Vector2f(0, 0);


    private final ArrayList<Interactable.QuintConsumer<Integer, Integer, Integer, Integer, Vector2f>> keyCallbacks;
    private final ArrayList<BiConsumer<Float, Vector2f>> updateCallbacks;

    public Test() {
        renderer = new Renderer();
        keyCallbacks = new ArrayList<>();
        updateCallbacks = new ArrayList<>();
    }

    /**
     * Called after Window/OpenGL/OpenAL initialization.
     * Called on the first frame of the test.
     */
    public void OnStart() {

    }
    /**
     * Called every frame.
     * @param dt Delta time.
     */
    public void OnUpdate(float dt) {
        for(int i = 0; i < updateCallbacks.size(); i++) {
            updateCallbacks.get(i).accept(dt, mousePos);
        }
    }
    /**
     * Called every frame before OnUpdate.
     */
    public void OnRender() {
        renderer.getCamera().onRender();
        renderer.clear();
    }

    public void OnClose() {
        renderer.getCurrentShader().delete();
        removeAllKeyListeners();
    }
    public void OnKeyInput(long window, int key, int scancode, int action, int mods) {
        keyCallbacks.forEach(callback -> {
            callback.accept(key, scancode, action, mods, mousePos);
        });
    }


    public static Renderer getRenderer() {
        return renderer;
    }
    public void addKeyListener(Interactable.QuintConsumer<Integer, Integer, Integer, Integer, Vector2f> callback) {
        keyCallbacks.add(callback);
    }
    public void removeKeyListener(Interactable.QuintConsumer<Integer, Integer, Integer, Integer, Vector2f> callback) {
        keyCallbacks.remove(callback);
    } // TODO: fix non removal, because of new lambda creation every this::method access (add an id?)
    public void removeAllKeyListeners() {
        keyCallbacks.clear();
    }

    public void addUpdateListener(BiConsumer<Float, Vector2f> callback) {
        updateCallbacks.add(callback);
    }
    public void removeUpdateListener(BiConsumer<Float, Vector2f> callback) {
       updateCallbacks.remove(callback);
    }  // TODO: fix non removal, because of new lambda creation every this::method access (add an id?)
    public void removeAllUpdateListeners() {
        updateCallbacks.clear();
    }
}
