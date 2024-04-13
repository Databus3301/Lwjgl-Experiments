package Tests;

import Render.Entity.Camera.Camera;
import Render.Entity.Entity2D;
import Render.MeshData.Model.ObjModel;
import Render.MeshData.Model.ObjModelParser;
import org.joml.Vector2f;
import static org.lwjgl.glfw.GLFW.*;

public class TestCamera extends Test {

    Entity2D origin;
    Entity2D offscreen;
    Camera camera;

    public TestCamera() {
        super();
        renderer.setCamera(camera = new Camera());
        ObjModel model = ObjModelParser.parseOBJ("square.obj");
        origin = new Entity2D(new Vector2f(), model);
        offscreen = new Entity2D(new Vector2f(2000, 0), model);
    }


    @Override
    public void OnUpdate(float dt) {
        super.OnUpdate(dt);
        Vector2f effectiveVelocity = new Vector2f(camera.getVelocity());
        camera.translate(effectiveVelocity.mul(dt));
        //camera.rotate(-100.0f*dt, 2);
        if(camera.getScale().x < 90f)
             camera.scale(new Vector2f(10.0f, 10.0f).mul(dt));
    }

    @Override
    public void OnRender() {
        super.OnRender();
        renderer.draw(origin);
        renderer.draw(offscreen);
    }

    @Override
    public void OnKeyInput(long window, int key, int scancode, int action, int mods) {
        super.OnKeyInput(window, key, scancode, action, mods);

        if (key == GLFW_KEY_A && action == GLFW_PRESS) {
            camera.setVelocity(200f, 0);
        }
        if (key == GLFW_KEY_D && action == GLFW_PRESS) {
            camera.setVelocity(-200f, 0);
        }
        if (key == GLFW_KEY_W && action == GLFW_PRESS) {
            camera.setVelocity(0, -200f);
        }
        if (key == GLFW_KEY_S && action == GLFW_PRESS) {
            camera.setVelocity(0, 200f);
        }
    }

}
