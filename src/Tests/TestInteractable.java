package Tests;

import Render.Entity.Interactable;
import Render.Vertices.Model.ObjModel;

public class TestInteractable extends Test {

    Interactable interactable;
    int lastState = 0;
    public TestInteractable() {
        super();
        interactable = new Interactable(ObjModel.SQUARE);
        interactable.scale(30);
    }
    @Override
    public void OnUpdate(float dt) {
        super.OnUpdate(dt);
        if(interactable.getState() != lastState) {
            lastState = interactable.getState();
            System.out.println(interactable.getState());
        }
        interactable.updateStates(mousePos);
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
