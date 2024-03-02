package Tests;

import Render.Entity.Entity2D;
import Render.Vertices.Model.ObjModel;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;

public class TestMouseInput extends Test {

    Entity2D tracker;
    public TestMouseInput() {
        super();
        tracker = new Entity2D(ObjModel.SQUARE);
        tracker.scale(5);
    }

    @Override
    public void OnUpdate(float dt) {
        super.OnUpdate(dt);
        tracker.setPosition(renderer.screenToWorldCoords(mousePos));
    }

    @Override
    public void OnRender() {
        super.OnRender();
        renderer.drawEntity2D(tracker);
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
