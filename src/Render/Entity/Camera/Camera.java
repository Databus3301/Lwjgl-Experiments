package Render.Entity.Camera;

import Render.Entity.Entity2D;
import Render.Shader.Shader;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import static Render.Window.dim;

public class Camera extends Entity2D {
    private Matrix4f viewMatrix, projectionMatrix;

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
    // VIEW
    public Matrix4f calcViewMatrix() {
        Vector3f cameraFront = new Vector3f(0.0f, 0.0f, -1.0f);
        Vector3f cameraUp = new Vector3f(0.0f, 1.0f, 0.0f);
        this.viewMatrix.identity();
        this.viewMatrix.lookAt(new Vector3f(position.x*-1, position.y*-1, 20f),
                cameraFront.add(position.x*-1, position.y*-1 , 0.0f),
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


    @Override
    public void rotate(float degrees, int axis) {
       super.rotate(degrees, axis);
    }
}
