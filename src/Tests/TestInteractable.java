package Tests;

import Render.Entity.Interactable.Interactable;
import Render.MeshData.Model.ObjModel;
import org.joml.Vector2f;

import static org.lwjgl.glfw.GLFW.*;

public class TestInteractable extends Test {

    Interactable general;
    Interactable button;
    public TestInteractable() {
        super();
        ObjModel model = new ObjModel("circle.obj");
        general = new Interactable(this, new Vector2f(), model);
        general.scale(70);
        button = new Interactable(this, new Vector2f(0, 200), model);
        button.scale(70, 20);

        button.setReleasedCallback((interactable -> {
            interactable.setColor((float)(Math.random()), (float)(Math.random()), (float)(Math.random()), 1f);
            System.out.println("clicked");
        }));

        general.setDraggedCallback((interactable) -> {
            interactable.setPosition(renderer.screenToWorldCoords(mousePos));
        });

        general.setDefaultCallback((interactable) -> {
            //interactable.setColor(1, 1, 1, 1);
        });
        general.setHoverCallback((interactable) -> {
            interactable.setColor(0, 1, 0, 1);
        });


        general.setPressedCallback((interactable) -> { // TODO: delay till it counts as dragged to avoid accidental drags and actually show this?
            interactable.setColor(1, 0, 0, 1);
        });
        general.setReleasedCallback((interactable) -> {
            interactable.setColor(0, 0, 1, 1);
        });

        general.setTriggerDistance(300);
        general.setKeyCallback((interactable, key, scancode, action, mousePos) -> {
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
        if(general.hasChangedState())
            System.out.println("State: " + general.getState());
    }
    @Override
    public void OnRender() {
        super.OnRender();
        renderer.drawEntity2D(general);
        renderer.drawCollisionAABB(general);
        renderer.drawEntity2D(button);
        renderer.drawCollisionAABB(button);
    }
    @Override
    public void OnClose() {
        super.OnClose();
    }
    @Override
    public void OnKeyInput(long window, int key, int scancode, int action, int mods) {
        super.OnKeyInput(window, key, scancode, action, mods);
    }

}
