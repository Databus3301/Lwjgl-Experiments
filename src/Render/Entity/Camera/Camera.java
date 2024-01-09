package Render.Entity.Camera;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Camera {
    private Matrix4f modelMatrix, viewMatrix, projectionMatrix;
    public Vector2f position;


    public Camera(Vector2f position) {
        this.position = position;
        modelMatrix = new Matrix4f();
        viewMatrix = new Matrix4f();
        projectionMatrix = new Matrix4f();
        initModelMatrix();
        initViewMatrix();
        initProjectionMatrix();
    }

    // MODEL
    public Matrix4f initModelMatrix() {
        modelMatrix.identity();
        return modelMatrix;
    }
    public Matrix4f translateModelMatrix(Vector3f translation) {
        modelMatrix.translate(translation);
        return modelMatrix;
    }
    public Matrix4f scaleModelMatrix(Vector3f scalor) {
        modelMatrix.scale(scalor);
        return modelMatrix;
    }
    public Matrix4f getModelMatrix() {
        return modelMatrix;
    }

    // VIEW
    public Matrix4f initViewMatrix() {
        Vector3f cameraFront = new Vector3f(0.0f, 0.0f, -1.0f);
        Vector3f cameraUp = new Vector3f(0.0f, 1.0f, 0.0f);
        this.viewMatrix.identity();
        this.viewMatrix.lookAt(new Vector3f(position.x, position.y, 20f),
                cameraFront.add(position.x, position.y , 0.0f),
                cameraUp);
        return this.viewMatrix;
    }

    public Matrix4f getViewMatrix() {
        return this.viewMatrix;
    }

    // PROJECTION
    public Matrix4f initProjectionMatrix() {
        projectionMatrix.identity();
        projectionMatrix.ortho(-600f, 600f, -450f, 450f, 0.01f, 100f);
        return projectionMatrix;
    }
    public Matrix4f getProjectionMatrix() {
        return this.projectionMatrix;
    }

}
