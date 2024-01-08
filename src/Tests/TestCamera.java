package Tests;

import org.joml.Vector2f;
import static org.lwjgl.glfw.GLFW.*;

public class TestCamera extends TestTextures {

    Vector2f change;
    public TestCamera() {
        super();
        change = new Vector2f();
    }

    @Override
    public void OnUpdate(float dt) {
        super.OnUpdate(dt);
    }

    @Override
    public void OnRender() {
        super.OnRender();
        camera.position.x += change.x;
        camera.position.y += change.y;
    }

    @Override
    public void OnKeyInput(long window, int key, int scancode, int action, int mods) {
        super.OnKeyInput(window, key, scancode, action, mods);

        if (key == GLFW_KEY_A && action == GLFW_PRESS) {
            change.set(-2f, 0);
        }
        if (key == GLFW_KEY_D && action == GLFW_PRESS) {
            change.set(2f, 0);
        }
        if (key == GLFW_KEY_W && action == GLFW_PRESS) {
            change.set(0, 2f);
        }
        if (key == GLFW_KEY_S && action == GLFW_PRESS) {
            change.set(0, -2f);
        }
    }

}
