package Render.Entity;

import Render.Entity.Texturing.Texture;
import Render.Shader.Shader;
import Render.Vertices.Model.ObjModel;
import Tests.Test;
import org.joml.Vector2f;
import org.w3c.dom.Text;

import java.util.function.Consumer;
import java.util.function.Function;

import static org.lwjgl.glfw.GLFW.*;

/**
 * An interactable entity.
 * This class is used to represent entities that can be interacted with.
 * This class is a subclass of Entity2D.
 * Interactions: Mouse click, keyboard input, etc.
 */


public class Interactable extends Entity2D {

    private int state = States.DEFAULT;

    private Consumer<Interactable> defaultCallback = (interactable) -> {};
    private Consumer<Interactable> hoverCallback = (interactable) -> {};
    private Consumer<Interactable> pressedCallback = (interactable) -> {};
    private Consumer<Interactable> releasedCallback = (interactable) -> {};
    private Consumer<Interactable> draggedCallback = (interactable) -> {};

    private int lastState = States.DEFAULT;
    private boolean changedState = false;

    public Interactable() {
        super();
    }
    public Interactable(Vector2f position) {
        super(position);
    }
    public Interactable(ObjModel model) {
        super(model);
    }
    public Interactable(Vector2f position, ObjModel model, Texture texture) {
        super(position, model, texture);
    }
    public Interactable(Vector2f position, ObjModel model, Texture texture, Shader shader) {
        super(position, model, texture, shader);
    }
    public Interactable(int x, int y, ObjModel model) {
        super(x, y, model);
    }
    public Interactable(Vector2f position, ObjModel model) {
        super(position, model);
    }
    public Interactable(Vector2f position, ObjModel model, Shader shader) {
        super(position, model, shader);
    }


    public void onUpdate(Vector2f mousePos) {
        updateStates(mousePos);

        switch (state) {
            case States.DEFAULT:
                defaultCallback.accept(this);
            break;
            case States.HOVER:
                hoverCallback.accept(this);
            break;
            case States.PRESSED:
                pressedCallback.accept(this);
            break;
            case States.RELEASED:
                releasedCallback.accept(this);
            break;
            case States.DRAGGED:
                draggedCallback.accept(this);
            break;
        }

        if(this.getState() != lastState) {
            lastState = this.getState();
            changedState = true;
        } else {
            changedState = false;
        }
    }

    public void updateStates(int key, int scancode, int action, int mods, Vector2f mousePos) {
        // depending on position and user input, change state
        if (isHovered(mousePos) && key == GLFW_MOUSE_BUTTON_LEFT) {
            if (action == GLFW_PRESS)
                state = States.PRESSED;
            else if (action == GLFW_RELEASE)
                state = States.RELEASED;
        } else {
            state = States.DEFAULT;
        }
    }
    public void updateStates(Vector2f mousePos) {
        if ((state == States.DEFAULT || state == States.RELEASED) && isHovered(mousePos)) {
            state = States.HOVER;
        }
        else if (state == States.PRESSED && isHovered(mousePos)) {
            state = States.DRAGGED;
        }
        else if (state == States.HOVER && !isHovered(mousePos)) {
            state = States.DEFAULT;
        }
    }

    public boolean isHovered(Vector2f mousePos) {
           return collideAABB(Test.renderer.screenToWorldCoords(mousePos));
    }

    public int getState() {
        return state;
    }

    static class States {
        public static final int DEFAULT = 0;
        public static final int HOVER = 1;
        public static final int PRESSED = 2;
        public static final int RELEASED = 3;
        public static final int DRAGGED = 4;

    }


    public void setDefaultCallback(Consumer<Interactable> callback) {
        this.defaultCallback = callback;
    }

    public void setHoverCallback(Consumer<Interactable> callback) {
        this.hoverCallback = callback;
    }

    public void setPressedCallback(Consumer<Interactable> callback) {
        this.pressedCallback = callback;
    }

    public void setReleasedCallback(Consumer<Interactable> callback) {
        this.releasedCallback = callback;
    }

    public void setDraggedCallback(Consumer<Interactable> callback) {
        this.draggedCallback = callback;
    }

    public boolean hasChangedState() {
        return changedState;
    }
}
