package Render.Entity.Interactable;

import Render.Entity.Entity2D;
import Render.MeshData.Texturing.Texture;
import Render.MeshData.Shader.Shader;
import Render.MeshData.Model.ObjModel;
import Tests.Test;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.function.Consumer;

import static org.lwjgl.glfw.GLFW.*;

/**
 * An interactable entity.
 * This class is used to represent entities that can be interacted with.
 * This class is a subclass of Entity2D.
 * Interactions: Mouse click, keyboard input, etc.
 */


public class Interactable extends Entity2D {

    protected States state = States.DEFAULT;
    private float triggerDistance = 1000;
    private float triggerDistanceSquared = triggerDistance * triggerDistance;
    protected final ArrayList<Vector2f> triggerPos = new ArrayList<>();

    private Consumer<Interactable> defaultCallback = (interactable) -> {};
    private Consumer<Interactable> hoverCallback = (interactable) -> {};
    private Consumer<Interactable> pressedCallback = (interactable) -> {};
    private Consumer<Interactable> releasedCallback = (interactable) -> {};
    private Consumer<Interactable> draggedCallback = (interactable) -> {};
    private QuintConsumer<Interactable, Integer, Integer, Integer, Vector2f>
                                    keyCallback = (interactable, key, scancode, action, mousePos) -> {};

    /**
     * performance optimization: check if the entity is hovered only every n-th frame
     * reduces accuracy but increases performance (the higher the faster)
     */
    private int checkHover = 30;
    private States lastState = States.DEFAULT;
    private boolean changedState = false;



    public <T extends Test> Interactable(T scene) {
        super();
        init(scene);
    }
    public <T extends Test> Interactable(T scene, Vector2f position) {
        super(position);
        init(scene);
    }
    public <T extends Test> Interactable(T scene, ObjModel model) {
        super(model);
        init(scene);
    }
    public <T extends Test> Interactable(T scene, Vector2f position, ObjModel model, Texture texture, Shader shader) {
        super(position, model, texture, shader);
        init(scene);
    }
    public <T extends Test> Interactable(T scene, Vector2f position, ObjModel model) {
        super(position, model);
        init(scene);
    }
    public <T extends Test> void init(T scene) {
        scene.addKeyListener(this::onKeyInput);
        scene.addUpdateListener(this::onUpdate);
    }

    public void onUpdate(float dt, Vector2f mousePos) {
        switch (state) {
            case DEFAULT -> defaultCallback.accept(this);
            case HOVER -> hoverCallback.accept(this);
            case PRESSED -> pressedCallback.accept(this);
            case RELEASED -> releasedCallback.accept(this);
            case DRAGGED -> draggedCallback.accept(this);
        }

        updateStates(mousePos);

        if(this.getState() != lastState) {
            lastState = this.getState();
            changedState = true;
        } else {
            changedState = false;
        }

        if(animation != null)
            animation.update(dt);


    }
    public void onKeyInput(int key, int scancode, int action, int mods, Vector2f mousePos) {
        updateStates(key, scancode, action, mods, mousePos);
        keyCallback.accept(this, key, scancode, action, mousePos);
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

    private int hoverCheck = 0;
    private boolean isHovered = false;
    public void updateStates(Vector2f mousePos) {
        if(hoverCheck++ > checkHover) {
            isHovered = isHovered(mousePos);
            hoverCheck = 0;
        }

        if ((state == States.DEFAULT || state == States.RELEASED) && isHovered) {
            state = States.HOVER;
        }
        else if (state == States.PRESSED && isHovered) {
            state = States.DRAGGED;
        }
        else if (state == States.HOVER && !isHovered) {
            state = States.DEFAULT;
        }
    }
    public void updateStates() {
        updateStates(Test.mousePos);
    }

    public boolean isHovered(Vector2f mousePos) {
        return collideAABB(Test.renderer.screenToWorldCoords(mousePos));
    }


    public enum States {
        DEFAULT,
        HOVER,
        PRESSED,
        RELEASED,
        DRAGGED
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
    public void setKeyCallback(QuintConsumer<Interactable, Integer, Integer, Integer, Vector2f> callback) {
        this.keyCallback = callback;
    }

    public void hover() {
        state = States.HOVER;
        hoverCallback.accept(this);
    }
    public void press() {
        state = States.PRESSED;
        pressedCallback.accept(this);
    }
    public void release() {
        state = States.RELEASED;
        releasedCallback.accept(this);
    }
    public void drag() {
        state = States.DRAGGED;
        draggedCallback.accept(this);
    }

    public void setTriggerDistance(float triggerDistance) {
        this.triggerDistance = triggerDistance;
        this.triggerDistanceSquared = triggerDistance * triggerDistance;
    }
    public void addTriggerPos(Vector2f pos) {
        triggerPos.add(pos);
    }
    public void addTriggerPos(float x, float y) {
        triggerPos.add(new Vector2f(x, y));
    }
    public void clearTriggerPos() {
        triggerPos.clear();
    }


    public ArrayList<Vector2f> getTriggerPos() {
        return triggerPos;
    }
    public boolean hasChangedState() {
        return changedState;
    }
    public float getTriggerDistance() {
        return triggerDistance;
    }
    public float getTriggerDistanceSquared() {
        return triggerDistanceSquared;
    }

    public States getState() {
        return state;
    }

    public void setCheckHover(int checkHover) {
        this.checkHover = checkHover;
    }

    @FunctionalInterface
    public interface QuintConsumer<T, U, V, W, X> {
        void accept(T t, U u, V v, W w, X x);
    }
}
