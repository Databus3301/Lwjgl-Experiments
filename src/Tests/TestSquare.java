package Tests;

import Render.Entity.Entity2D;
import Render.Entity.Texturing.Texture;
import Render.Shader.Shader;
import Render.Vertices.Model.ObjModel;
import Render.Vertices.Model.ObjModelParser;
import org.joml.Vector2f;
import static org.lwjgl.glfw.GLFW.*;

import static org.lwjgl.opengl.GL30.*;

public class TestSquare extends Test {
    Entity2D entity;
    Entity2D entity2;
    Shader shader;
    Texture texture;
    final int DIM = 100;
    Entity2D[] entity2DS = new Entity2D[DIM*DIM];
    public TestSquare() {
        super();
        shader = new Shader("res/shaders/texturing.shader");
        texture = new Texture("res/textures/woodCrate.png", 0);
        shader.setUniform1i("u_Texture", 0);
        texture.bind();
        ObjModel model = ObjModelParser.parseOBJ("res/models/square.obj");
        entity = new Entity2D(new Vector2f(), model, texture, shader);
        entity2 = new Entity2D(new Vector2f(50, 50), model, texture, shader);

        int entityIndex = 0;
        int spacing = 15;
        for (int i = 0; i < DIM; i++) {
            for(int j = 0; j < DIM; j++) {

                entity2DS[entityIndex++] = new Entity2D(new Vector2f(i*spacing-DIM*spacing/2f, j*spacing-DIM*spacing/2f), model, texture, shader);
            }
        }
    }

    @Override
    public void OnUpdate(float dt) {
        super.OnUpdate(dt);
        entity.translate(new Vector2f(entity.getVelocity()).mul(dt));
        Vector2f v = new Vector2f(entity.getPosition());
       entity2.translate(new Vector2f(v.sub(entity2.getPosition()).normalize()).mul(dt).mul(new Vector2f(100,100)));
       for (int i = 0; i < entity2DS.length; i++) {
                Vector2f v2 = new Vector2f(entity.getPosition());
                entity2DS[i].translate(new Vector2f(v2.sub(entity2DS[i].getPosition()).normalize()).mul(dt).mul(new Vector2f(100,100)));
            }
    }

    @Override
    public void OnRender() {
        super.OnRender();
        renderer.drawEntity2D(entity);
        renderer.drawEntity2D(entity2);
        renderer.drawEntities2D(entity2DS);
        glClearColor(0.435f, 0.639f, 0.271f,1);
    }

    @Override
    public void OnKeyInput(long window, int key, int scancode, int action, int mods) {
        super.OnKeyInput(window, key, scancode, action, mods);
        if (key == GLFW_KEY_W && action == GLFW_PRESS) {
            entity.setVelocity(new Vector2f(0, 200));
            entity2.setVelocity(new Vector2f(0, -200));

        }
        if (key == GLFW_KEY_A && action == GLFW_PRESS) {
            entity.setVelocity(new Vector2f(-200, 0));
            entity2.setVelocity(new Vector2f(+200, 0));
        }
        if (key == GLFW_KEY_S && action == GLFW_PRESS) {
            entity.setVelocity(new Vector2f(0, -200));
            entity2.setVelocity(new Vector2f(0, +200));
        }
        if (key == GLFW_KEY_D && action == GLFW_PRESS) {
            entity.setVelocity(new Vector2f(200, 0));
            entity2.setVelocity(new Vector2f(-200, 0));
        }
    }
}
