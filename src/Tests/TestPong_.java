package Tests;

import Render.Entity.Camera.Camera;
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

public class TestPong_ extends Test {
    // center of each entity
    private Vector2f ballPos, wallPosLeft, wallPosRight;
    // velocity
    private Vector2f changeBallPos, changeWallPosLeft, changeWallPosRight;
    private final float ballWidth = 100f;
    private final float wallWidth = 100f;


    private final float[] vertices = {
            // position		colour
            -50f, -50f,   	1.0f, 1.0f, 1.0f, 1.0f, // 0
            +50f, -50f,		1.0f, 1.0f, 1.0f, 1.0f, // 1
            +50f, +50f,		1.0f, 1.0f, 1.0f, 1.0f, // 2
            -50f, +50f,		1.0f, 1.0f, 1.0f, 1.0f  // 3
    };

    private final int[] indices = {
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




    public TestPong_() {
        ballPos = new Vector2f();
        wallPosLeft = new Vector2f(-dim.x / 2f, 0);
        wallPosRight = new Vector2f(+dim.x / 2f, 0);
        changeBallPos = new Vector2f(250f,0.0f); // pixels per second
        changeWallPosLeft = new Vector2f();
        changeWallPosRight = new Vector2f();

        renderer = new Renderer();

        shader = new Shader("res/shaders/pong.shader");
        shader.bind();

        va = new VertexArray();
        VertexBuffer vb = new VertexBuffer(vertices);
        VertexBufferLayout layout = new VertexBufferLayout();

        layout.pushF(2);
        layout.pushF(4);
        va.addBuffer(vb, layout);

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
        float wallBoundX = wallPosRight.x - ballWidth*0.5f*0.5f - wallWidth*0.7f*0.5f;
        // if ball inside wall bounds
        if(ballPos.x > wallBoundX || ballPos.x < wallBoundX*-1) {
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
        float ballRadius = ballWidth * 0.5f * 0.5f;
        float wallRadius = wallWidth * 3.5f * 0.5f;
        // balls outer bound max(y)      | wall outer bound max(y)   | balls outer bound min(y)        |      wall outer bound min(y)
        if((ballPos.y + ballRadius*0.5f) <= (wallPos.y + wallRadius) && (ballPos.y - ballRadius * 0.5f) >= (wallPos.y - wallRadius)) {
            if((Math.abs(changeBallPos.x) + Math.abs(changeBallPos.y)) < (1500 * dt)) { // if under the speed cap
                // add speed while keeping direction
                Vector2f normalized = new Vector2f();
                changeBallPos.normalize(normalized);
                float addedSpeed = 50f * dt;
                changeBallPos.add(new Vector2f(addedSpeed, addedSpeed).mul(normalized));
                // clip ball out of wall
                float wallClipBoundX = wallPosRight.x - ballRadius - wallRadius;
                System.out.println(changeBallPos.x / 100f);
                System.out.println(normalized.x);
                if(ballPos.x >= wallClipBoundX || ballPos.x <= wallClipBoundX*-1) {
                    ballPos.set(wallClipBoundX * normalized.x, ballPos.y);
                }
            }
            // invert the direction
            changeBallPos.set(changeBallPos.x*-1, changeBallPos.y);
            return true;
        }

        return false;
    }

    public void resetBall(float dt) {
        float wallBoundX = wallPosRight.x - ballWidth*0.5f*0.5f - wallWidth*0.7f*0.5f;
        if(ballPos.x > wallBoundX || ballPos.x < wallBoundX*-1) {
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
        shader.bind();
        setUniforms();

        Vector2f scalar = new Vector2f(0.7f, 3.5f);
        // Set up left and right walls
        camera.scale(scalar);
        camera.translate(new Vector2f(wallPosLeft.x / scalar.x, wallPosLeft.y / scalar.y));
        shader.setUniformMat4f("uModel", camera.calcModelMatrix());
        renderer.draw(va, ib, shader);

        camera.calcModelMatrix();
        camera.scale(scalar);
        camera.translate(new Vector2f(wallPosRight.x / scalar.x, wallPosRight.y / scalar.y));
        shader.setUniformMat4f("uModel", camera.calcModelMatrix());
        renderer.draw(va, ib, shader);

        // reset matrices
        camera.calcViewMatrix();
        camera.calcProjectionMatrix();

        // Set up ball
        scalar.set(0.5f, 0.5f);
        camera.calcModelMatrix();
        camera.scale(new Vector2f(scalar.x, scalar.y));
        camera.translate(new Vector2f(ballPos.x / scalar.x, ballPos.y / scalar.y));
        shader.setUniformMat4f("uModel", camera.calcViewMatrix());
        renderer.draw(va, ib, shader);

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
        shader.delete();
    }

    protected void setUniforms() {
        camera.calcModelMatrix();

        shader.setUniformMat4f("uProj", camera.getProjectionMatrix());
        shader.setUniformMat4f("uView", camera.getViewMatrix());
        shader.setUniformMat4f("uModel", camera.calcModelMatrix());
    }
}
