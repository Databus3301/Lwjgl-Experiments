package Tests;

import Render.Entity.Camera.Camera;
import org.joml.Vector2f;
import static org.lwjgl.glfw.GLFW.*;

public class TestCamera extends TestTextures {

    Camera camera;

    public TestCamera() {
        super();
        renderer.setCamera(camera = new Camera());
    }


    @Override
    public void OnUpdate(float dt) {
        super.OnUpdate(dt);
        Vector2f effectiveVelocity = new Vector2f(camera.getVelocity());
        camera.translate(effectiveVelocity.mul(dt));
        camera.rotate(10.0f * dt, 2);
        if(camera.getScale().x < 6f)
             camera.scale(new Vector2f(1.0f, 1.0f).mul(dt));
    }

    @Override
    public void OnRender() {
        super.OnRender();
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
