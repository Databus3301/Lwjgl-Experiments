package Tests;

import Render.Camera.Camera;
import Render.Renderer;
import Render.Shader.Shader;
import Render.Vertices.IndexBuffer;
import Render.Vertices.VertexArray;
import Render.Vertices.VertexBuffer;
import Render.Vertices.VertexBufferLayout;
import org.joml.*;

import java.lang.Math;
import java.util.Random;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.opengl.GL15.*;

public class TestPong extends Test {
    // center of each entity
    private Vector2f ballPos, wallPosLeft, wallPosRight;
    // velocity
    private Vector2f changeBallPos, changeWallPosLeft, changeWallPosRight;

    private float[] vertices = {
            // position		colour
            -50f, -50f,   	1.0f, 1.0f, 1.0f, 1.0f, // 0
            +50f, -50f,		1.0f, 1.0f, 1.0f, 1.0f, // 1
            +50f, +50f,		1.0f, 1.0f, 1.0f, 1.0f, // 2
            -50f, +50f,		1.0f, 1.0f, 1.0f, 1.0f  // 3
    };

    private int[] indices = {
            0, 1, 2,
            2, 3, 0,
    };

    private Shader shader;
    private VertexArray va;
    private IndexBuffer ib;
    private Camera camera;
    private Renderer renderer;

    private Vector2i dim = Render.Window.Window.dim;
    java.util.Random r = new Random();
    float lastDt = 1;




    public TestPong() {
        ballPos = new Vector2f();
        wallPosLeft = new Vector2f(-dim.x / 2f, 0);
        wallPosRight = new Vector2f(+dim.x / 2f, 0);
        changeBallPos = new Vector2f(250f,0.0f); // pixels per second
        changeWallPosLeft = new Vector2f();
        changeWallPosRight = new Vector2f();

        renderer = new Renderer();

        shader = new Shader("res/shaders/basic2.shader");
        shader.Bind();

        va = new VertexArray();
        VertexBuffer vb = new VertexBuffer(vertices);
        VertexBufferLayout layout = new VertexBufferLayout();

        layout.PushF(2);
        layout.PushF(4);
        va.AddBuffer(vb, layout);

        ib = new IndexBuffer(indices);
        camera = new Camera(new Vector2f());
    }

    @Override
    public void OnUpdate(float dt) {
        super.OnUpdate(dt);

        if(dt == 0) return;
        changeBallPos.div(lastDt);
        changeBallPos.mul(dt);

        ballPos.add(changeBallPos);
        wallPosRight.add(changeWallPosRight);
        wallPosLeft.add(changeWallPosLeft);

        lastDt = dt;

        collideBall(dt);
    }
    public void collideBall(float dt) {
        // if the ball
        // 600 - ballWidth - wallWidth
        if(ballPos.x > 600- 100*0.5*0.5 - 100*0.7*0.5 || ballPos.x < (600- 100*0.5*0.5 - 100*0.7*0.5)*-1) {
            boolean collided = collideBallWithWalls(wallPosRight, dt) || collideBallWithWalls(wallPosLeft, dt);
            if(!collided) // no wall collision -> player missed
                resetBall(dt);
        }
        // 450 - ballWidth
        if(ballPos.y > 450 - 100*0.5*0.5 || ballPos.y < -450 + 100*0.5*0.5)
            collideBallWithLevel();
    }

    public boolean collideBallWithWalls(Vector2f wallPos, float dt) {
        // if the ball hit the <wallPos> wall
        //   bP.y     + baseSize * scalar * fromCenter  |  wP.y +         wallWidth * fromCenter  && …
        if((ballPos.y + 100      *  0.5   *  0.5) <=    (wallPos.y + 100*3.5    *    0.5) && (ballPos.y - 100*0.5*0.5) >= (wallPos.y - 100*3.5*0.5)) {
            // if under the speed cap
            if((Math.abs(changeBallPos.x) + Math.abs(changeBallPos.y)) < (1500 * dt)) {
                // add speed while keeping direction
                Vector2f normalized = new Vector2f();
                changeBallPos.normalize(normalized);
                float addedSpeed = 50f * dt;
                changeBallPos.add(new Vector2f(addedSpeed, addedSpeed).mul(normalized));
                ballPos.add(normalized.negate().mul(3));
            }
            // invert the direction
            changeBallPos.set(changeBallPos.x*-1, changeBallPos.y);
            return true;
        }

        return false;
    }

    public void resetBall(float dt) {
        if(ballPos.x > 600- 100*0.5*0.5 - 100*0.7*0.5 || ballPos.x < (600- 100*0.5*0.5 - 100*0.7*0.5)*-1) {
            // if the ball missed the <wallPos> wall
            float x = (r.nextFloat(200) + 200f) * (r.nextBoolean() ? 1 : -1) * dt;
            float y = r.nextFloat(130f) * (r.nextBoolean() ? 1 : -1) * dt;
            changeBallPos.set(x, y);
            ballPos.set(0, 0);
        }
    }

    public void collideBallWithLevel() {
        changeBallPos.set(changeBallPos.x, changeBallPos.y*-1);
    }

    @Override
    public void OnRender() {
        super.OnRender();
        shader.Bind();
        setUniforms();

        Vector3f scalar = new Vector3f(0.7f, 3.5f, 0f);
        // Set up left and right walls
        camera.scaleModelMatrix(scalar);
        camera.translateModelMatrix(new Vector3f(wallPosLeft.x / scalar.x, wallPosLeft.y / scalar.y, 0));
        shader.SetUniformMat4f("uModel", camera.getModelMatrix());
        renderer.Draw(va, ib, shader);

        camera.initModelMatrix();
        camera.scaleModelMatrix(scalar);
        camera.translateModelMatrix(new Vector3f(wallPosRight.x / scalar.x, wallPosRight.y / scalar.y, 0));
        shader.SetUniformMat4f("uModel", camera.getModelMatrix());
        renderer.Draw(va, ib, shader);

        // reset matrices
        camera.initViewMatrix();
        camera.initProjectionMatrix();

        // Set up ball
        scalar.set(0.5f, 0.5f,0 );
        camera.initModelMatrix();
        camera.scaleModelMatrix(new Vector3f(scalar.x, scalar.y, 0f));
        camera.translateModelMatrix(new Vector3f(ballPos.x / scalar.x, ballPos.y / scalar.y, 0));
        shader.SetUniformMat4f("uModel", camera.getModelMatrix());
        renderer.Draw(va, ib, shader);

        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);


    }

    @Override
    public void OnKeyInput(long window, int key, int scancode, int action, int mods) {
        super.OnKeyInput(window, key, scancode, action, mods);

        if (key == GLFW_KEY_W && action == GLFW_PRESS) {
            changeWallPosLeft.set(0, 2f);
        }
        if (key == GLFW_KEY_S && action == GLFW_PRESS) {
            changeWallPosLeft.set(0, -2f);
        }

        // halt on release
        if (key == GLFW_KEY_W && action == GLFW_RELEASE) {
            changeWallPosLeft.set(0, 0);
        }
        if (key == GLFW_KEY_S && action == GLFW_RELEASE) {
            changeWallPosLeft.set(0, 0);
        }

        if (key == GLFW_KEY_UP && action == GLFW_PRESS) {
            changeWallPosRight.set(0, 2f);
        }
        if (key == GLFW_KEY_DOWN && action == GLFW_PRESS) {
            changeWallPosRight.set(0, -2f);
        }

        // halt on release
        if (key == GLFW_KEY_UP && action == GLFW_RELEASE) {
            changeWallPosRight.set(0, 0);
        }
        if (key == GLFW_KEY_DOWN && action == GLFW_RELEASE) {
            changeWallPosRight.set(0, 0);
        }
    }

    @Override
    public void OnClose() {
        super.OnClose();
        shader.Delete();
    }

    protected void setUniforms() {
        camera.initModelMatrix();

        shader.SetUniformMat4f("uProj", camera.getProjectionMatrix());
        shader.SetUniformMat4f("uView", camera.getViewMatrix());
        shader.SetUniformMat4f("uModel", camera.getModelMatrix());
    }
}
