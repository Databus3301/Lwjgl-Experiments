package Render.Entity;

import Render.MeshData.Texturing.Animation;
import Render.MeshData.Texturing.ColorReplacement;
import Render.MeshData.Texturing.Texture;
import Render.MeshData.Shader.Shader;
import Render.MeshData.Model.ObjModel;
import org.joml.*;

import java.lang.Math;
import java.util.HashMap;
import java.util.Objects;

/**
 * Entity2D is a class that represents a 2D entity in the world
 * it has a position, rotation, and scale
 * it also has methods to translate, rotate, and scale the entity
 * it is used as a base class for all 2D entities
 */
public class Entity2D {
    public Vector4f rectangle = new Vector4f();

    protected Matrix4f modelMatrix = new Matrix4f(); // TODO: keep up to date, throughout method calls like scale() and rotate() to avoid recalculating everything every frame
    protected ObjModel model;
    protected Texture texture;
    protected Animation animation;

    protected HashMap<String, Texture> textures;
    protected Shader shader;

    protected Vector4f color;
    protected ColorReplacement colorReplacement;

    protected Vector2f position;  // x y position
    protected Vector2f offset; // x y offset
    protected Quaternionf rotation; // rotation in degrees
    protected Vector2f scale; // x y scale
    protected Vector2f velocity; // pixels per second

    protected boolean isStatic; // if the entity is static, it will not be updated every frame
    protected boolean isHidden; // if the entity is hidden, it will not be drawn

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
        this.position.set(position);
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
        this.isHidden = false;
        this.offset = new Vector2f(0, 0);
        this.color = new Vector4f(0.976f, 0.164f, 0.976f, 1.0f);
        this.animation = null;
    }

    public Entity2D instantiate() { // TODO: test this
        Entity2D e = new Entity2D(new Vector2f(position), model, texture, shader);
        e.rotation = new Quaternionf(rotation);
        e.scale = new Vector2f(scale);
        e.velocity = new Vector2f(velocity);
        e.isStatic = isStatic;
        e.isHidden = isHidden;
        e.offset = new Vector2f(offset);
        e.color = new Vector4f(color);

        return e;
    }

    /**
     * translate/move the entity by the given vector
     * @param translation the vector to translate by
     */
    public void translate(Vector2f translation) {
        this.position.add(translation);
    }
    public void translate(float x, float y) {
        this.position.add(new Vector2f(x, y));
    }
    public Vector2f translateTowards(Vector2f target, float by) {
        Vector2f direction = new Vector2f(target).sub(position).normalize();
        this.position.add(direction.x * by, direction.y * by);
        return direction;
    }
    public Vector2f translateTowards(Entity2D target, float by) {
        Vector2f direction = new Vector2f(target.getPosition()).sub(position).normalize();
        this.position.add(direction.x * by, direction.y * by);
        return direction;
    }
    public void translateIn(Vector2f direction, float by) {
        this.position.add(new Vector2f(direction).normalize().mul(by));
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
    }
    public void scale(float x, float y) {
        this.scale.x += x;
        this.scale.y += y;
    }
    public void scale(float xy) {
        this.scale.x += xy;
        this.scale.y += xy;
    }


    /**
     * calculate the model matrix for the entity
     * @return
     */
    public Matrix4f calcModelMatrix() {
        if(isStatic && !Objects.equals(modelMatrix, new Matrix4f()))
            return modelMatrix;

        position.add(offset);

        modelMatrix.identity();
        modelMatrix.scale(new Vector3f(scale.x, scale.y, 1));
        modelMatrix.translate(new Vector3f(position.x/scale.x, position.y/scale.y, 0));
        if(!rotation.equals(new Quaternionf()))
            modelMatrix.rotateAround(rotation, (getPosition().x-position.x)/scale.x, (getPosition().y-position.y)/scale.y, 0f);

        position.sub(offset);

        return modelMatrix;
    }
    public Matrix4f calcModelMatrixNoRotation() {
        if(isStatic && !Objects.equals(modelMatrix, new Matrix4f()))
            return modelMatrix;

        modelMatrix.identity();
        modelMatrix.scale(new Vector3f(scale.x, scale.y, 1));
        modelMatrix.translate(new Vector3f(position.x/scale.x, position.y/scale.y, 0));

        return modelMatrix;
    }

    /**
     * Basic form of collision detection often referred to as Axis-Aligned Bounding Box (AABB) collision detection. <br>
     * Doesn't work for entities with <b>rotation</b> <br> or models with <b>axis</b> other than <b>1:1</b>
     * @param other entity to collide with
     * @return hasCollided
     */
    public boolean collideAABB(Entity2D other) {
        return Math.abs(getPosition().x - other.getPosition().x) < Math.abs(scale.x + other.scale.x)  &&
                Math.abs(getPosition().y - other.getPosition().y) < Math.abs(scale.y + other.scale.y);
    }
    public boolean collideAABB(Vector2f pos2) {
        return Math.abs(getPosition().x - pos2.x) < Math.abs(scale.x)  &&
                Math.abs(getPosition().y - pos2.y) < Math.abs(scale.y);
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
        if(other == null || other.model == null || model == null) return false;

        Vector4f rect1 = model.getBoundingBox();
        Vector4f rect2 = other.model.getBoundingBox();

        trans1.x = rect1.x;
        trans1.y = rect1.y;
        trans2.x = rect2.x;
        trans2.y = rect2.y;

        calcModelMatrix().transform(trans1);
        other.calcModelMatrix().transform(trans2);
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
    private final Vector4f trans1 = new Vector4f(); // save memory by declaring
    private final Vector4f trans2 = new Vector4f(); // them outside the method

    public boolean collideRect(Vector4f rect1) {
        if(model == null || rect1 == null) return false;

        Vector4f rect2 = model.getBoundingBox();

        trans2.x = rect2.x;
        trans2.y = rect2.y;

        calcModelMatrix().transform(trans2);
        rect2.x = trans2.x;
        rect2.y = trans2.y;

        rect2.z *= scale.x;
        rect2.w *= scale.y;

        return rect1.x < rect2.x + rect2.z &&
                rect1.x + rect1.z > rect2.x &&
                rect1.y < rect2.y + rect2.w &&
                rect1.y + rect1.w > rect2.y;
    }

    public boolean collideCircle(Entity2D other) {
        Vector2f p2 = other.position;
        return position.distanceSquared(p2.x, p2.y) < (scale.x + other.scale.x) * (scale.x + other.scale.x);
    }

    public float distance(Entity2D other) {
        return position.distance(other.position);
    }
    public float distanceToNearestEdge(Vector2f point) {
        if(this.model == null) return Float.MAX_VALUE;
        // Get the bounding box of the Interactable object
        Vector4f boundingBox = this.model.getBoundingBox();

        // Calculate the distances to each edge
        float left = Math.abs(boundingBox.x - point.x);
        float right = Math.abs(boundingBox.x + boundingBox.z - point.x);
        float bottom = Math.abs(boundingBox.y - point.y);
        float top = Math.abs(boundingBox.y + boundingBox.w - point.y);

        // Return the smallest distance
        return Math.min(Math.min(left, right), Math.min(bottom, top));
    }

    public void accelerate(Vector2f acceleration) {
    	this.velocity.add(acceleration);
    }
    public void accelerate(float x, float y) {
        this.velocity.add(x, y);
    }
    public void accelerateTowards(Vector2f target, float speed) {
        Vector2f direction = new Vector2f(target).sub(position).normalize().mul(speed);
        this.velocity.add(direction);
    }
    public void accelerateTowards(Entity2D target, float speed) {
        Vector2f direction = new Vector2f(target.getPosition()).sub(position).normalize().mul(speed);
        this.velocity.add(direction);
    }
    public void slow(Vector2f deceleration) {
        // prevent the entity from moving backwards and stop at 0, 0
        if(velocity.x > 0) velocity.x = Math.max(0, velocity.x - deceleration.x);
        else if(velocity.x < 0) velocity.x = Math.min(0, velocity.x + deceleration.x);
        if(velocity.y > 0) velocity.y = Math.max(0, velocity.y - deceleration.y);
        else if(velocity.y < 0) velocity.y = Math.min(0, velocity.y + deceleration.y);
    }
    public void offset(Vector2f offset) {
        this.offset.add(offset);
    }

    public Vector2f getPosition() {
        return position;
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
    public boolean isHidden() {
        return isHidden;
    }
    public Vector2f getOffset() {
        return offset;
    }

    public String getDescription() {
        return "Position: " + position + "\n" +
                "Rotation: " + rotation + "\n" +
                "Scale: " + scale + "\n" +
                "Velocity: " + velocity + "\n" +
                "Model: " + model + "\n" +
                "Texture: " + texture + "\n" +
                "Shader: " + shader + "\n" +
                "Is Static: " + isStatic + "\n" +
                "Offset: " + offset + "\n";
    }
    public Vector4f getColor() {
        return color;
    }

    public ColorReplacement getColorReplacement() {
        return colorReplacement;
    }

    public Animation getAnimation() {
        return animation;
    }

    public void setPosition(Vector2f position) {
        this.position.set(position);
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
        this.scale.set(scale);
    }
    public void setScale(float x, float y) {
        this.scale.x = x;
        this.scale.y = y;
    }
    public void setScale(float d) {
        this.scale.x = d;
        this.scale.y = d;
    }

    /**
     * set the velocity in pixels per second
     * @param velocity the velocity to set
     */
    public void setVelocity(Vector2f velocity) {
    	this.velocity.set(velocity);
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
    public void setAnimation(Animation animation) {
        this.animation = animation;
    }
    public void setShader(Shader shader) {
    	this.shader = shader;
    }
    public void setStatic(boolean isStatic) {
    	this.isStatic = isStatic;
    }
    public void hide() {
        isHidden = true;
    }
    public void show() {
        isHidden = false;
    }
    public void setHidde(boolean isHidden) {
        this.isHidden = isHidden;
    }
    public void setOffset(Vector2f offset) {
        this.offset = offset;
    }
    public void setOffset(float x, float y) {
        this.offset.x = x;
        this.offset.y = y;
    }
    public void setColor(float r, float g, float b, float a) {
        if(color == null) color = new Vector4f(r, g, b, a);
        else color.set(r, g, b, a);
    }
    public void setColor(Vector4f color) {
        this.color = color;
    }
    public void setColorReplacement(ColorReplacement colorReplacement) {
        this.colorReplacement = colorReplacement;
    }

}
