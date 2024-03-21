package Tests;

import Render.Entity.*;
import Render.Entity.Camera.Camera;
import Render.Shader.*;
import Render.Vertices.Model.ObjModel;
import Render.Vertices.Model.ObjModelParser;
import Render.Window;
import org.joml.*;

import java.lang.Math;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;

// rewrite the pong test to use the Entity2D class
public class TestPong extends Test {
    private final Entity2D ball, wallLeft, wallRight;

    public TestPong() {
        Vector2i dim = Window.dim;


        ObjModel model = ObjModel.SQUARE;
        int SCALE = 15;

        ball = new Entity2D(new Vector2f(), model);
        wallLeft = new Entity2D(new Vector2f(-dim.x / 2f, 0), model);
        wallRight = new Entity2D(new Vector2f(+dim.x / 2f, 0), model);

        ball.scale(SCALE);
        wallLeft.scale(new Vector2f(SCALE, dim.y/4f));
        wallRight.scale(new Vector2f(SCALE, dim.y/4f));

        ball.setVelocity(new Vector2f((float) (Math.random() * 1000f), (float) (Math.random() * 50f)));
    }

    long lastCollision = System.currentTimeMillis();
    @Override
    public void OnUpdate(float dt) {
        super.OnUpdate(dt);
        ball.translate(new Vector2f(ball.getVelocity()).mul(dt));
        wallLeft.translate(new Vector2f(wallLeft.getVelocity()).mul(dt));
        wallRight.translate(new Vector2f(wallRight.getVelocity()).mul(dt));

        if (ball.collideRect(wallLeft) || ball.collideRect(wallRight)) {
            if (System.currentTimeMillis() - lastCollision > 100) {
                lastCollision = System.currentTimeMillis();
                ball.setVelocity(-ball.getVelocity().x, ball.getVelocity().y + (float) Math.random() * 100 - 50);
            }
        }
        if (ball.getPosition().x < -Window.dim.x / 2f || ball.getPosition().x > Window.dim.x / 2f) {
            ball.setPosition(new Vector2f());
        }
        if (ball.getPosition().y < -Window.dim.y / 2f + ball.getScale().y || ball.getPosition().y > Window.dim.y / 2f - ball.getScale().y) {
            ball.setVelocity(ball.getVelocity().x, -ball.getVelocity().y);
            ball.translateIn(ball.getVelocity(), 10);
        }
    }

    @Override
    public void OnRender() {
        super.OnRender();
        renderer.drawEntities2D(new Entity2D[]{ball, wallLeft, wallRight});
    }

    @Override
    public void OnKeyInput(long window, int key, int scancode, int action, int mods) {
        super.OnKeyInput(window, key, scancode, action, mods);

        if (key == GLFW_KEY_W && action == GLFW_PRESS) {
            wallLeft.setVelocity(new Vector2f(0, 200f));
        }
        if (key == GLFW_KEY_S && action == GLFW_PRESS) {
            wallLeft.setVelocity(new Vector2f(0, -200f));
        }
        if (key == GLFW_KEY_UP && action == GLFW_PRESS) {
            wallRight.setVelocity(new Vector2f(0, 200f));
        }
        if (key == GLFW_KEY_DOWN && action == GLFW_PRESS) {
            wallRight.setVelocity(new Vector2f(0, -200f));
        }
    }

    @Override
    public void OnClose() {
        super.OnClose();
    }
}
