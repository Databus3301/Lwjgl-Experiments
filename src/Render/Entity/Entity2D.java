package Render.Entity;

import Render.Entity.Texturing.Texture;
import Render.Shader.Shader;
import Render.Vertices.Model.ObjModel;
import Render.Vertices.Model.ObjModelParser;
import Render.Vertices.Vertex;
import Render.Vertices.VertexArray;
import org.joml.*;

import java.lang.Math;
import java.util.Objects;

/**
 * Entity2D is a class that represents a 2D entity in the world
 * it has a position, rotation, and scale
 * it also has methods to translate, rotate, and scale the entity
 * it is used as a base class for all 2D entities
 */
public class Entity2D {
    protected Matrix4f modelMatrix = new Matrix4f();
    protected ObjModel model;
    protected Texture texture;
    protected Shader shader;

    protected Vector2f position;  // x y position
    protected Vector2f center; // x y center
    protected Quaternionf rotation; // rotation in degrees
    protected Vector2f scale; // x y scale
    protected Vector2f velocity; // pixels per second

    protected boolean isStatic; // if the entity is static, it will not be updated every frame
    protected VertexArray va = new VertexArray(); // hence why the vertex array may be final

    public Entity2D(Vector2f position, ObjModel model, Texture texture, Shader shader) {
        this(position, model, texture);
        this.shader = shader;
    }

    public Entity2D(Vector2f position, ObjModel model, Texture texture) {
        this(position, model);
        this.texture = texture;
    }
    public Entity2D(Vector2f position, ObjModel model, Shader shader) {
        this(position, model);
        this.shader = shader;
    }
    public Entity2D(Vector2f position, ObjModel model) {
        this(position);

        assert model != null: "[ERROR] (Render.Entity.Entity2D) Model is null";

        this.model = model;
        //va.AddBuffer(model.getVertexBuffer(), Vertex.GetLayout());
    }
    public Entity2D(int x, int y, ObjModel model) {
        this(new Vector2f(x, y));
        assert model != null: "[ERROR] (Render.Entity.Entity2D) Model is null";

        this.model = model;
        //va.AddBuffer(model.getVertexBuffer(), Vertex.GetLayout());
    }
    public Entity2D(Vector2f position) {
        this();
        this.position = position;
    }
    public Entity2D(ObjModel model) {
        this();
        this.model = model;
    }

    public Entity2D() {
        this.position = new Vector2f(0, 0);
        this.rotation = new Quaternionf();
        this.scale = new Vector2f(1, 1);
        this.velocity = new Vector2f();
        this.model = null;
        this.isStatic = false;
        this.center = new Vector2f(0, 0);
    }

    public Entity2D instantiate() { // TODO: test this
        Entity2D e = new Entity2D(position, model, texture, shader);
        e.scale = scale;
        e.velocity = new Vector2f(velocity);
        e.isStatic = isStatic;
        e.rotation = rotation;
        e.center = new Vector2f(center);

        return e;

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

    public void rotate(Quaternionf rotation) {
        this.rotation.add(rotation);
    }
    public void rotate(float degrees, int axis) {
        if(axis < 0 || axis > 2) throw new IllegalArgumentException("Axis must be 0, 1, 2");
        switch (axis) {
            case 0 -> this.rotation.rotateAxis((float)Math.toRadians(degrees), 1, 0, 0);
            case 1 -> this.rotation.rotateAxis((float)Math.toRadians(degrees), 0, 1, 0);
            case 2 -> this.rotation.rotateAxis((float)Math.toRadians(degrees), 0, 0, 1);
        }
    }
    public void rotate(float degrees, Vector3f axis) {
        if(axis.x != 0 && axis.z != 1) throw new IllegalArgumentException("Axis X must be 0, 1");
        if(axis.y != 0 && axis.y != 1) throw new IllegalArgumentException("Axis Y must be 0, 1");
        if(axis.z != 0 && axis.x != 1) throw new IllegalArgumentException("Axis Z must be 0, 1");

        this.rotation.rotateAxis((float)Math.toRadians(degrees), axis);
    }

    /**
     * scale the entity by the given scale
     * @param scale the scale to scale by
     */

    public void scale(Vector2f scale) {
        this.scale.add(scale);
//        this.position.add(scale); // move the entity to keep the center in the same place
    }
    public void scale(float x, float y) {
        this.scale.x += x;
        this.scale.y += y;

//        this.position.x += x;
//        this.position.y += y; // move the entity to keep the center in the same place
    }
    public void scale(float xy) {
        this.scale.x += xy;
        this.scale.y += xy;

//        this.position.x += xy;
//        this.position.y += xy; // move the entity to keep the center in the same place
    }


    /**
     * calculate the model matrix for the entity
     * @return
     */
    public Matrix4f calcModelMatrix() {
        if(isStatic && !Objects.equals(modelMatrix, new Matrix4f()))
            return modelMatrix;

        modelMatrix.identity();
        modelMatrix.scale(new Vector3f(scale.x, scale.y, 1));
        modelMatrix.translate(new Vector3f(position.x/scale.x, position.y/scale.y, 0));
        modelMatrix.rotateAround(rotation, (getCenter().x-position.x)/scale.x, (getCenter().y-position.y)/scale.y, 0f);

        return modelMatrix;
    }

    /**
     * Basic form of collision detection often referred to as Axis-Aligned Bounding Box (AABB) collision detection. <br>
     * Doesn't work for entities with <b>rotation</b> <br> or models with <b>axis</b> other than <b>1:1</b>
     * @param other entity to colide with
     * @return hasCollided
     */
    public boolean collideAABB(Entity2D other) {
        return Math.abs(getCenter().x - other.getCenter().x) < Math.abs(scale.x + other.scale.x)*1.4 &&
                Math.abs(getCenter().y - other.getCenter().y) < Math.abs(scale.y + other.scale.y)*1.4;
    }

    /**
     * This method checks for collision between this entity and another entity using their bounding rectangles.
     * Unlike the Axis-Aligned Bounding Box (AABB) collision detection method, this method can handle entities with non-1:1 axis ratios.
     * However, it does not account for entity rotation, which can lead to inaccurate collision detection for rotated entities.
     *
     * The method transforms the bounding rectangles of the entities according to their model matrices, and then checks for intersection.
     * The bounding rectangle is defined by a Vector4f where x and y represent the upper left corner, and z (width) and w (height) represent the dimensions.
     *
     * Note: This method can result in false positives for collision when entities are close to each other but not actually intersecting, especially for entities with complex shapes.
     *
     * @param other The other Entity2D to check for collision with.
     * @return true if the bounding rectangles of the entities intersect, false otherwise.
     */
    public boolean collideRect(Entity2D other) {
        Vector4f rect1 = model.getBoundingBox();
        Vector4f rect2 = other.model.getBoundingBox();

        Vector4f trans1 = calcModelMatrix().transform(new Vector4f(rect1.x, rect1.y, 0, 1));
        Vector4f trans2 = other.calcModelMatrix().transform(new Vector4f(rect2.x, rect2.y, 0, 1));
        rect1.x = trans1.x;
        rect1.y = trans1.y;
        rect2.x = trans2.x;
        rect2.y = trans2.y;

        rect1.z *= scale.x;
        rect1.w *= scale.y;
        rect2.z *= other.scale.x;
        rect2.w *= other.scale.y;

        return rect1.x < rect2.x + rect2.z &&
                rect1.x + rect1.z > rect2.x &&
                rect1.y < rect2.y + rect2.w &&
                rect1.y + rect1.w > rect2.y;
    }

    public void accelerate(Vector2f acceleration) {
    	this.velocity.add(acceleration);
    }


    public Vector2f getPosition() {
        return position;
    }
    public Vector2f getCenter() {
        return new Vector2f(position);//.sub(scale).add(1,1);
    }

    public Quaternionf getRotation() {
        return rotation;
    }

    public Vector2f getScale() {
        return scale;
    }

    public Vector2f getVelocity() {
    	return velocity;
    }

    public ObjModel getModel() {
        return model;
    }

    public Texture getTexture() {
    	return texture;
    }
    public Shader getShader() {
    	return shader;
    }

    public boolean isStatic() {
    	return isStatic;
    }

    public VertexArray getVa() {
        assert model != null : "[ERROR] (Render.Renderer.DrawEntity2D) Entity2D has no model";

        if(!isStatic) {
            va = new VertexArray();
            va.addBuffer(model.getVertexBuffer(), Vertex.getLayout());
        }
        return va;
    }

    public void setPosition(Vector2f position) {
        this.position = position;
    }
    public void setPosition(float x, float y) {
        this.position.x = x;
        this.position.y = y;
    }

    public void setRotation(Quaternionf rotation) {
        this.rotation = rotation;
    }
    public void setRotation(float degrees, int axis) {
        if(axis < 0 || axis > 2) throw new IllegalArgumentException("Axis must be 0, 1, 2");
        switch (axis) {
            case 0 -> {
                rotation.x = 0;
                rotation.rotateAxis((float)Math.toRadians(degrees), 1, 0, 0);
            }
            case 1 -> {
                rotation.y = 0;
                rotation.rotateAxis((float)Math.toRadians(degrees), 0, 1, 0);
            }
            case 2 -> {
                rotation.z = 0;
                rotation.rotateAxis((float)Math.toRadians(degrees), 0, 0, 1);
            }
        }
    }

    public void setScale(Vector2f scale) {
        this.scale = scale;
    }

    /**
     * set the velocity in pixels per second
     * @param velocity the velocity to set
     */
    public void setVelocity(Vector2f velocity) {
    	this.velocity = velocity;
    }
    public void setVelocity(float x, float y) {
        this.velocity = new Vector2f(x, y);
    }

    public void setModel(ObjModel model) {
        this.model = model;
    }
    public void setTexture(Texture texture) {
    	this.texture = texture;
    }
    public void setShader(Shader shader) {
    	this.shader = shader;
    }
    public void setStatic(boolean isStatic) {
    	this.isStatic = isStatic;
    }
}
