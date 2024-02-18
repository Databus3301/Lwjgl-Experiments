package Tests;

import Render.Entity.Entity2D;
import Render.Entity.Texturing.Texture;
import Render.Shader.Shader;
import Render.Vertices.Model.ObjModel;
import Render.Vertices.Model.ObjModelParser;
import org.joml.Vector2f;

import static org.lwjgl.glfw.GLFW.*;

public class TestSquare extends Test {
    Entity2D entity;
    Entity2D entity2;
    Shader shader;
    Texture texture;

    public TestSquare() {
        super();
        shader = new Shader("res/shaders/basic.shader");
        texture = new Texture("res/textures/woodCrate.png", 0);
        shader.setUniform1i("u_Texture", 0);
        texture.Bind();
        ObjModel model = ObjModelParser.parseOBJ("res/models/square.obj");
        entity = new Entity2D(new Vector2f(), model, texture, shader);
        entity2 = new Entity2D(new Vector2f(50, 50), model, texture, shader);

    }

    @Override
    public void OnUpdate(float dt) {
        super.OnUpdate(dt);
        entity.translate(new Vector2f(entity.getVelocity()).mul(dt));
        Vector2f v = new Vector2f(entity.getPosition());
       entity2.translate(new Vector2f(v.sub(entity2.getPosition()).normalize()).mul(dt).mul(new Vector2f(30,30)));
    }

    @Override
    public void OnRender() {
        super.OnRender();
        renderer.drawEntity2D(entity);
        renderer.drawEntity2D(entity2);
    }

    @Override
    public void OnKeyInput(long window, int key, int scancode, int action, int mods) {
        super.OnKeyInput(window, key, scancode, action, mods);
        if (key == GLFW_KEY_W && action == GLFW_PRESS) {
            entity.setVelocity(new Vector2f(0, 100));
            entity2.setVelocity(new Vector2f(0, -100));
        }
        if (key == GLFW_KEY_A && action == GLFW_PRESS) {
            entity.setVelocity(new Vector2f(-100, 0));
            entity2.setVelocity(new Vector2f(+100, 0));
        }
        if (key == GLFW_KEY_S && action == GLFW_PRESS) {
            entity.setVelocity(new Vector2f(0, -100));
            entity2.setVelocity(new Vector2f(0, +100));
        }
        if (key == GLFW_KEY_D && action == GLFW_PRESS) {
            entity.setVelocity(new Vector2f(100, 0));
            entity2.setVelocity(new Vector2f(-100, 0));
        }
    }
}
