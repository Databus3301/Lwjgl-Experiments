package Tests;

import Render.Entity.Camera.Camera;
import Render.Entity.Entity2D;
import Render.Shader.Shader;
import Render.Vertices.Model.ObjModel;
import Render.Vertices.Model.ObjModelParser;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;

public class TestInstancedRendering extends Test {
    Camera camera;
    Entity2D entity;
    Matrix4f[] instaceMatrices;
    public TestInstancedRendering() {
        super();
        renderer.setCamera(camera = new Camera());

        Shader shader = new Shader("res/shaders/instancing.shader");
        shader.Bind();

        ObjModel model = ObjModelParser.parseOBJ("res/models/square.obj");
        entity = new Entity2D(new Vector2f(200, 0), model, shader);

        /*
            Matrices are correctly passed to the gpu, but the shader is not using them correctly?
            The same matrix that would transform an entity doesn't work when passed as an instance matrix.
            Yet applying instanced rotation works just fine.
         */

        int COUNT = 200;
        instaceMatrices = new Matrix4f[COUNT];
        for(int i = 0; i < COUNT; i++) { // (float)Math.random() * 100 // check multiplication of model and transformation matrix
//           Matrix4f instaceMatrix = new Matrix4f();
//           instaceMatrix.identity();
//           instaceMatrix.translate(new Vector3f(100*(i+1), 0, 0));
//           instaceMatrix.rotate((int)(Math.random() * 1000), new Vector3f(0, 0, 1));
//           instaceMatrix.scale(new Vector3f(2f, 2f, 1));
           instaceMatrices[i] = new Matrix4f(entity.calcModelMatrix()).translate(new Vector3f(100*(i+1), 0, 0));
        }


        // print the second model Matrix through a loop
        for (int i = 0; i < instaceMatrices.length; i++) {
            System.out.println(instaceMatrices[i]);
        }
        System.out.println(entity.calcModelMatrix());
    }
    @Override
    public void OnUpdate(float dt) {
        super.OnUpdate(dt);

        Vector2f effectiveVelocity = new Vector2f(camera.getVelocity());
        camera.translate(effectiveVelocity.mul(dt));
    }
    @Override
    public void OnRender() {
        super.OnRender();

        renderer.DrawInstanced(entity, instaceMatrices);
    }

    @Override
    public void OnClose() {
        super.OnClose();
    }

    @Override
    public void OnKeyInput(long window, int key, int scancode, int action, int mods) {
        super.OnKeyInput(window, key, scancode, action, mods);

        if (key == GLFW_KEY_A && action == GLFW_PRESS) {
            camera.setVelocity(60f, 0);
        }
        if (key == GLFW_KEY_D && action == GLFW_PRESS) {
            camera.setVelocity(-60f, 0);
        }
        if (key == GLFW_KEY_W && action == GLFW_PRESS) {
            camera.setVelocity(0, -60f);
        }
        if (key == GLFW_KEY_S && action == GLFW_PRESS) {
            camera.setVelocity(0, 60f);
        }
    }
}
