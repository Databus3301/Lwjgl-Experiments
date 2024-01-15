package Render.Entity.Camera;

import Render.Entity.Entity2D;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Camera extends Entity2D {
    private Matrix4f viewMatrix, projectionMatrix;
    public Camera(Vector2f position) {
        this.position = position;
        viewMatrix = new Matrix4f();
        projectionMatrix = new Matrix4f();
        calcModelMatrix();
        calcViewMatrix();
        calcProjectionMatrix();
    }
    public Camera() {
        this.position = new Vector2f(0, 0);
        viewMatrix = new Matrix4f();
        projectionMatrix = new Matrix4f();
        calcModelMatrix();
        calcViewMatrix();
        calcProjectionMatrix();
    }

    // VIEW
    public Matrix4f calcViewMatrix() {
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
    public Matrix4f calcProjectionMatrix() {
        projectionMatrix.identity();
        projectionMatrix.ortho(-600f, 600f, -450f, 450f, 0.01f, 100f);
        return projectionMatrix;
    }
    public Matrix4f getProjectionMatrix() {
        return this.projectionMatrix;
    }

}
