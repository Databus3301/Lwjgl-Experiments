package Tests;

import Render.Entity.Camera.Camera;
import Render.Entity.Entity2D;
import Render.Renderer;
import Render.Shader.Shader;
import Render.Vertices.Model.ObjModel;
import Render.Vertices.Model.ObjModelParser;
import org.joml.Matrix4f;
import org.joml.Vector2f;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;

public class TestInstancedRendering extends Test {
    Camera camera;
    Entity2D entity;
    Matrix4f[] modelMatrices;
    public TestInstancedRendering() {
        super();
        renderer.setCamera(camera = new Camera());

        Shader shader = new Shader("res/shaders/instancing.shader");
        shader.Bind();

        ObjModel model = ObjModelParser.parseOBJ("res/models/untitled.obj");
        entity = new Entity2D(new Vector2f(50, -30), model, shader);
        entity.setScale(new Vector2f(2000, 2000));


        float[] m = entity.calcModelMatrix().get(new float[16]);
        System.out.println("m: ");
        for (int j = 0; j < 16; j++) {
            System.out.print(m[j] + " ");
        }
        System.out.println("");


        int COUNT = 2;
        modelMatrices = new Matrix4f[COUNT];
        for(int i = 0; i < COUNT; i++) {
            modelMatrices[i] = new Matrix4f().identity().translate((float)Math.random() * 100, (float)Math.random() * 100, 0);
        }

        float[] m2 = entity.calcModelMatrix().mul(modelMatrices[0]).get(new float[16]);
        System.out.println("m2: ");
        for (int j = 0; j < 16; j++) {
            System.out.print(m2[j] + " ");
        }
        System.out.println("");


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

//        renderer.DrawEntity2D(entity);
        renderer.DrawInstanced(entity, modelMatrices);
    }

    @Override
    public void OnClose() {
        super.OnClose();
    }

    @Override
    public void OnKeyInput(long window, int key, int scancode, int action, int mods) {
        super.OnKeyInput(window, key, scancode, action, mods);

        if (key == GLFW_KEY_A && action == GLFW_PRESS) {
            camera.setVelocity(2f, 0);
        }
        if (key == GLFW_KEY_D && action == GLFW_PRESS) {
            camera.setVelocity(-2f, 0);
        }
        if (key == GLFW_KEY_W && action == GLFW_PRESS) {
            camera.setVelocity(0, -2f);
        }
        if (key == GLFW_KEY_S && action == GLFW_PRESS) {
            camera.setVelocity(0, 2f);
        }
    }
}
