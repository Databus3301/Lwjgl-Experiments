package Render.Entity;

import Render.Shader.Shader;
import Render.Vertices.Model.ObjModel;
import Tests.Test;
import org.joml.Vector2f;

import static org.lwjgl.glfw.GLFW.*;

/**
 * An interactable entity.
 * This class is used to represent entities that can be interacted with.
 * This class is a subclass of Entity2D.
 * Interactions: Mouse click, keyboard input, etc.
 */


public class Interactable extends Entity2D {

    private int state = States.DEFAULT;

    // write all constructors for this class from entity2d
    public Interactable() {
        super();
    }
    public Interactable(Vector2f position) {
        super(position);
    }
    public Interactable(ObjModel model) {
        super(model);
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
        if (state == States.DEFAULT && isHovered(mousePos)) {
            state = States.HOVER;
        } else if (state == States.HOVER && !isHovered(mousePos)) {
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
        public static final int ClICKED = 5;
    }
}
