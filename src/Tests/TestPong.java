package Tests;

import Render.Entity.*;
import Render.Entity.Camera.Camera;
import Render.Shader.*;
import Render.Vertices.Model.ObjModel;
import Render.Vertices.Model.ObjModelParser;
import org.joml.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;

// rewrite the pong test to use the Entity2D class
public class TestPong extends Test {
    private final Entity2D ball, wallLeft, wallRight;
    private final Camera camera;


    public TestPong() {
        Vector2i dim = Render.Window.Window.dim;


        ObjModel model = ObjModelParser.parseOBJ("square.obj");
        Shader shader = new Shader("res/shaders/objrendering.shader");
        //shader.Bind();
        int SCALE = 15;

        ball = new Entity2D(new Vector2f(), model);
        wallLeft = new Entity2D(new Vector2f(-dim.x / 2f, 0), model);
        wallRight = new Entity2D(new Vector2f(+dim.x / 2f, 0), model);

        ball.scale(SCALE);
        wallLeft.scale(SCALE);
        wallRight.scale(SCALE);
        //ball.setVelocity(new Vector2f(250f, 0));
        new TestObjModelParser(model);

        renderer.setCamera(camera = new Camera(new Vector2f(), shader));
    }

    @Override
    public void OnUpdate(float dt) {
        super.OnUpdate(dt);
        ball.translate(new Vector2f(ball.getVelocity()).mul(dt));
        System.out.println(ball.getCenter());
    }

    @Override
    public void OnRender() {
        super.OnRender();
        renderer.drawEntities2D(new Entity2D[]{ball, wallLeft, wallRight});
    }

    @Override
    public void OnKeyInput(long window, int key, int scancode, int action, int mods) {
        super.OnKeyInput(window, key, scancode, action, mods);

        if (key == GLFW_KEY_A && action == GLFW_PRESS) {
            ball.setVelocity(-200f, 0);
        }
        if (key == GLFW_KEY_D && action == GLFW_PRESS) {
            ball.setVelocity( 200f, 0);
        }
    }

    @Override
    public void OnClose() {
        super.OnClose();
        if(camera.getShader() != null)
            camera.getShader().delete();
    }
}
