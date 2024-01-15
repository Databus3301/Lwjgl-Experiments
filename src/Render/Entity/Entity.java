package Render.Entity;

import org.joml.*;

/**
 * Work in Progress for possible future 3D entities
 */
public class Entity {
    private Vector3f position;
    private Vector3f rotation;
    private Vector3f scale;

    public Entity(Vector3f position, Vector3f rotation, Vector3f scale) {
        this.position = position;
        this.rotation = rotation;
        this.scale = scale;
    }

    public Entity(Vector3f position) {
        this.position = position;
        this.rotation = new Vector3f(0, 0, 0);
        this.scale = new Vector3f(1, 1, 1);
    }

    public Entity() {
        this.position = new Vector3f(0, 0, 0);
        this.rotation = new Vector3f(0, 0, 0);
        this.scale = new Vector3f(1, 1, 1);
    }

    /**
     * translate the entity by the given vector
     * @param translation
     */
    public void translate(Vector3f translation) {
        this.position.add(translation);
    }

    /**
     * rotate the entity by the given rotation
     * @param rotation
     */

    public void rotate(Vector3f rotation) {
        this.rotation.add(rotation);
    }
    /**
     * scale the entity by the given scale
     * @param scale
     */

    public void scale(Vector3f scale) {
        this.scale.add(scale);
    }

    public Vector3f getPosition() {
        return position;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public Vector3f getScale() {
        return scale;
    }
}
