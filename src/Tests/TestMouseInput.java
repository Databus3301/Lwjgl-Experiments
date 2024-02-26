package Tests;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;

public class TestMouseInput extends Test{
    public TestMouseInput() {
        super();
    }

    @Override
    public void OnUpdate(float dt) {
        super.OnUpdate(dt);
    }

    @Override
    public void OnRender() {
        super.OnRender();
    }

    @Override
    public void OnClose() {
        super.OnClose();
    }

    @Override
    public void OnKeyInput(long window, int key, int scancode, int action, int mods) {
        super.OnKeyInput(window, key, scancode, action, mods);
        if ((key == GLFW_MOUSE_BUTTON_1 || key == GLFW_MOUSE_BUTTON_2 || key == GLFW_MOUSE_BUTTON_3 || key == GLFW_MOUSE_BUTTON_4 || key == GLFW_MOUSE_BUTTON_5) && action == GLFW_PRESS) {
            System.out.printf("Mouse button %d pressed\n", key);
        }
    }
}
