package Render.Entity;

import Render.Entity.Texturing.Texture;
import Render.Vertices.Model.ObjModel;
import org.joml.*;

import java.lang.Math;

/**
 * ___________DEPRECATED___________
 * Work in Progress for possible future 3D entities
 */
public class Entity {
    protected Matrix4f modelMatrix = new Matrix4f();
    protected ObjModel model;
    protected Texture texture;

    protected Vector3f position;  // x y z position
    protected Vector3f rotation; // rotation in degrees
    protected Vector3f scale; // x y z scale
    protected Vector3f velocity; // pixels per second

    public Entity(Vector3f position, ObjModel model, Texture texture) {
        this(position, model);
        this.texture = texture;
    }
    public Entity(Vector3f position, ObjModel model) {
        this(position);
        this.model = model;
    }
    public Entity(Vector3f position) {
        this();
        this.position = position;
    }

    public Entity() {
        this.position = new Vector3f();
        this.rotation = new Vector3f();
        this.scale = new Vector3f(1, 1, 1);
        this.velocity = new Vector3f();
        this.model = null;
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

    public Matrix4f calcModelMatrix() {
        modelMatrix.identity();
        modelMatrix.translate(new Vector3f(position.x, position.y, 0));
        modelMatrix.rotate((float) Math.toRadians(rotation.x), new Vector3f(1, 0, 0));
        modelMatrix.rotate((float) Math.toRadians(rotation.y), new Vector3f(0, 1, 0));
        modelMatrix.rotate((float) Math.toRadians(rotation.z), new Vector3f(0, 0, 1));
        modelMatrix.scale(new Vector3f(scale.x, scale.y, 1));
        return modelMatrix;
    }

    public void accelaerate(Vector3f acceleration) {
        this.velocity.add(acceleration);
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

    public Vector3f getVelocity() {
        return velocity;
    }

    public ObjModel getModel() {
        return model;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public void setRotation(Vector3f rotation) {
        this.rotation = rotation;
    }

    public void setScale(Vector3f scale) {
        this.scale = scale;
    }

    /**
     * set the velocity in pixels per second
     * @param velocity the velocity to set
     */
    public void setVelocity(Vector3f velocity) {
        this.velocity = velocity;
    }
    public void setVelocity(float x, float y, float z) {
        this.velocity = new Vector3f(x, y, z);
    }
}
