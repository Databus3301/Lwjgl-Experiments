package Render.Entity.Camera;

import Render.Entity.Entity2D;
import Render.MeshData.Shader.Shader;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import static Render.Window.dim;

public class Camera extends Entity2D {
    private Matrix4f viewMatrix, projectionMatrix;
    private boolean calculatedModelMatrixForThisFrame = false;

    public Camera(Vector2f position, Shader shader) {
        super(position);
        this.shader = shader;
        init();
    }
    public Camera(Vector2f position) {
        super(position);
        init();
    }
    public Camera(Shader shader) {
        super();
        this.shader = shader;
        init();
    }
    public Camera() {
        super();
        init();
    }

    public void init() {
        viewMatrix = new Matrix4f();
        projectionMatrix = new Matrix4f();
        calcModelMatrix();
        calcViewMatrix();
        calcProjectionMatrix();
    }
    // MODEL
    @Override
    public Matrix4f calcModelMatrix() {
        if(calculatedModelMatrixForThisFrame) return modelMatrix;

        calculatedModelMatrixForThisFrame = true;
        offset = position.mul(-1, new Vector2f());
        return super.calcModelMatrix();
    }

    // VIEW
    public Matrix4f calcViewMatrix() {
        Vector3f cameraFront = new Vector3f(0.0f, 0.0f, -1.0f);
        Vector3f cameraUp = new Vector3f(0.0f, 1.0f, 0.0f);
        this.viewMatrix.identity();
        this.viewMatrix.lookAt(new Vector3f(-position.x, -position.y, 20f),
                cameraFront.add(-position.x, -position.y , 0.0f),
                cameraUp);
        return this.viewMatrix;
    }

    public Matrix4f getViewMatrix() {
        return this.viewMatrix;
    }

    // PROJECTION
    public Matrix4f calcProjectionMatrix() {
        projectionMatrix.identity();
        projectionMatrix.ortho(-dim.x/2f, dim.x/2f, -dim.y/2f, dim.y/2f, -10000f, 10000f);
        return projectionMatrix;
    }
    public Matrix4f getProjectionMatrix() {
        return this.projectionMatrix;
    }


    public void centerOn(Vector2f position) {
        this.position.x = -position.x;
        this.position.y = -position.y;
    }
    public void centerOn(Entity2D entity) {
        float lerpFactor = 0.03f; // determines camera speed
        Vector2f targetPosition = entity.getPosition().mul(-1f, new Vector2f());
        this.position.x += (targetPosition.x - this.position.x) * lerpFactor;
        this.position.y += (targetPosition.y - this.position.y) * lerpFactor;
    }

    public void onRender() {
        calculatedModelMatrixForThisFrame = false;
    }
}
