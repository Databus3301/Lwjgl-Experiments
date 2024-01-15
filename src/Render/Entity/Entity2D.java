package Render.Entity;

import Render.Vertices.Model.OBJModel;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

/**
 * Entity2D is a class that represents a 2D entity in the world
 * it has a position, rotation, and scale
 * it also has methods to translate, rotate, and scale the entity
 * it is used as a base class for all 2D entities
 */
public class Entity2D {

    protected Matrix4f modelMatrix = new Matrix4f();
    protected OBJModel model;

    protected Vector2f position;  // x y position
    protected float rotation; // rotation in degrees
    protected Vector2f scale; // x y scale

    protected Vector2f velocity; // in pixels per second

    public Entity2D(Vector2f position, float rotation, Vector2f scale, Vector2f velocity, OBJModel model) {
        this(position, rotation, scale, model);
        this.velocity = velocity;
    }
    public Entity2D(Vector2f position, float rotation, Vector2f scale, OBJModel model) {
        this(position, scale, model);
        this.rotation = rotation;
    }

    public Entity2D(Vector2f position,  Vector2f scale, OBJModel model) {
        this(position, model);
        this.scale = scale;
    }
    public Entity2D(Vector2f position, OBJModel model) {
        this(position);
        this.model = model;
    }
    public Entity2D(Vector2f position) {
        this();
        this.position = position;
    }

    public Entity2D() {
        this.position = new Vector2f(0, 0);
        this.rotation = 0;
        this.scale = new Vector2f(1, 1);
        this.velocity = new Vector2f();
        this.model = null;
    }

    /**
     * translate the entity by the given vector
     * @param translation the vector to translate by
     */
    public void translate(Vector2f translation) {
        this.position.add(translation);
    }
    public void translate(float x, float y) {
        this.position.add(new Vector2f(x, y));
    }

    /**
     * rotate the entity by the given rotation
     * @param rotation the rotation to rotate by
     */

    public void rotate(float rotation) {
        this.rotation += rotation;
    }

    /**
     * scale the entity by the given scale
     * @param scale the scale to scale by
     */

    public void scale(Vector2f scale) {
        this.scale.add(scale);
    }

    /**
     * calculate the model matrix for the entity
     * @return
     */
    public Matrix4f calcModelMatrix() {
        modelMatrix.identity();
        modelMatrix.translate(new Vector3f(position.x, position.y, 0));
        modelMatrix.rotate((float)Math.toRadians(rotation), new Vector3f(0, 0, 1));
        modelMatrix.scale(new Vector3f(scale.x, scale.y, 1));
        return modelMatrix;
    }

    public void accelaerate(Vector2f acceleration) {
    	this.velocity.add(acceleration);
    }


    public Vector2f getPosition() {
        return position;
    }

    public float getRotation() {
        return rotation;
    }

    public Vector2f getScale() {
        return scale;
    }

    public Vector2f getVelocity() {
    	return velocity;
    }

    public void setPosition(Vector2f position) {
        this.position = position;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public void setScale(Vector2f scale) {
        this.scale = scale;
    }

    public void setVelocity(Vector2f velocity) {
    	this.velocity = velocity;
    }




}
