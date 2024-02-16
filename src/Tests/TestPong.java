package Tests;

import Render.Batch;
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
    private Entity2D ball, wallLeft, wallRight;
    private Camera camera;


    public TestPong() {
        Vector2i dim = Render.Window.Window.dim;

        ObjModel model = ObjModelParser.parseOBJ("res/models/square.obj");
        Shader shader = new Shader("res/shaders/objrendering.shader");
        //shader.Bind();

        ball = new Entity2D(new Vector2f(), model);
        wallLeft = new Entity2D(new Vector2f(-dim.x / 2f, 0), model);
        wallRight = new Entity2D(new Vector2f(+dim.x / 2f, 0), model);

//        Vector2f scale = new Vector2f(100, 100);
//        ball.setScale(scale);
//        wallLeft.setScale(scale);
//        wallRight.setScale(scale);
        ball.setVelocity(new Vector2f(250f, 0));

        renderer.setCamera(camera = new Camera(new Vector2f(), shader));
    }

    @Override
    public void OnUpdate(float dt) {
        super.OnUpdate(dt);
        ball.translate(new Vector2f(ball.getVelocity()).mul(dt));
    }

    @Override
    public void OnRender() {
        super.OnRender();
        renderer.DrawEntities2D(new Entity2D[]{ball, wallLeft, wallRight});
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
            camera.getShader().Delete();
    }
}
