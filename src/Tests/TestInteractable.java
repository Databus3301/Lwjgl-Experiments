package Tests;

import Render.Entity.Interactable;
import Render.Vertices.Model.ObjModel;

public class TestInteractable extends Test {

    Interactable interactable;
    int lastState = 0;
    public TestInteractable() {
        super();
        interactable = new Interactable(ObjModel.SQUARE, this);
        interactable.scale(30);

        interactable.setDraggedCallback((interactable) -> {
            interactable.setPosition(renderer.screenToWorldCoords(mousePos));
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
        interactable.setDefaultCallback((interactable) -> {
            interactable.setColor(1, 1, 1, 1);
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
    }
    @Override
    public void OnClose() {
        super.OnClose();
    }
    @Override
    public void OnKeyInput(long window, int key, int scancode, int action, int mods) {
        super.OnKeyInput(window, key, scancode, action, mods);
        interactable.updateStates(key, scancode, action, mods, mousePos);
    }

}
