package Tests;

import Render.Entity.Interactable;
import Render.Vertices.Model.ObjModel;
import org.joml.Vector2f;

import static org.lwjgl.glfw.GLFW.*;

public class TestInteractable extends Test {

    Interactable interactable;
    public TestInteractable() {
        super();
        ObjModel model = new ObjModel("circle.obj");
        interactable = new Interactable(new Vector2f(), model);
        interactable.scale(70);

        interactable.setDraggedCallback((interactable) -> {
            interactable.setPosition(renderer.screenToWorldCoords(mousePos));
        });

        interactable.setDefaultCallback((interactable) -> {
            //interactable.setColor(1, 1, 1, 1);
        });
        interactable.setHoverCallback((interactable) -> {
            interactable.setColor(0, 1, 0, 1);
        });


        interactable.setPressedCallback((interactable) -> { // TODO: delay till it counts as dragged to avoid accidental drags and actually show this?
            interactable.setColor(1, 0, 0, 1);
        });
        interactable.setReleasedCallback((interactable) -> {
            interactable.setColor(0, 0, 1, 1);
        });

        interactable.setTriggerDistance(300);
        interactable.setKeyCallback((interactable, key, scancode, action, mousePos) -> {
            if(renderer.screenToWorldCoords(mousePos).distance(interactable.getPosition()) > interactable.getTriggerDistance())
                return;

            if(key == GLFW_KEY_E && action == GLFW_PRESS) {
                interactable.setColor(1, 0, 1, 1);
            }
            if(key == GLFW_KEY_E && action == GLFW_RELEASE) {
                interactable.setColor(1, 1, 1, 1);
            }
        });

    }
    @Override
    public void OnUpdate(float dt) {
        super.OnUpdate(dt);
        if(interactable.hasChangedState())
            System.out.println("State: " + interactable.getState());
        interactable.onUpdate(mousePos);
    }
    @Override
    public void OnRender() {
        super.OnRender();
        renderer.drawEntity2D(interactable);
        renderer.drawCollisionAABB(interactable);
    }
    @Override
    public void OnClose() {
        super.OnClose();
    }
    @Override
    public void OnKeyInput(long window, int key, int scancode, int action, int mods) {
        super.OnKeyInput(window, key, scancode, action, mods);
        interactable.onKeyInput(key, scancode, action, mods, mousePos);
    }

}
