package Tests;

import Render.Entity.Entity2D;
import Render.MeshData.Texturing.Texture;
import Render.MeshData.Shader.Shader;
import Render.MeshData.Model.ObjModel;
import Render.MeshData.Model.ObjModelParser;
import org.joml.Vector2f;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL30.glClearColor;

public class TestSquare extends Test {
    Entity2D player;
    Entity2D entity2;
    Shader shader;
    Texture textureEntity;
    Texture textureProjectile;
    final int DIM = 100;
    Entity2D[] entity2DS = new Entity2D[DIM * DIM];
    Vector2f scale = new Vector2f(4, 4);
    int timeBetweenShot=0;
    public TestSquare() {
        super();
        shader = new Shader("res/shaders/texturing.shader");
        textureEntity = new Texture("res/textureEntitys/woodCrate.png", 0);
        textureProjectile = new Texture("res/models/square.png", 0);
        shader.setUniform1i("u_Texture", 0);
        textureEntity.bind();
        ObjModel model = ObjModelParser.parseOBJ("res/models/square.obj");
        player = new Entity2D(new Vector2f(), model, textureEntity, shader);
        player.scale(scale);

        int entityIndex = 0;
        int spacing = 15;
        for (int i = 0; i < DIM; i++) {
            for (int j = 0; j < DIM; j++) {

                entity2DS[entityIndex] = new Entity2D(new Vector2f(i * spacing - DIM * spacing / 2f, j * spacing - DIM * spacing / 2f), model, textureEntity, shader);
                entity2DS[entityIndex++].scale(scale);

            }
        }
    }

    @Override
    public void OnUpdate(float dt) {
        super.OnUpdate(dt);
        player.translate(new Vector2f(player.getVelocity()).mul(dt));
        Vector2f v = new Vector2f(player.getPosition());
        for (int i = 0; i < entity2DS.length; i++) {
            Vector2f v2 = new Vector2f(player.getPosition());
            if (entity2DS[i] == null) continue;
            entity2DS[i].translate(new Vector2f(v2.sub(entity2DS[i].getPosition()).normalize()).mul(dt).mul(new Vector2f(100, 100)));
            if (player.collideRect(entity2DS[i])) {
                entity2DS[i]= null;
            }
        }


    }

    @Override
    public void OnRender() {
        super.OnRender();
        renderer.draw(player);
        renderer.draw(entity2DS);
        glClearColor(0.435f, 0.639f, 0.271f, 1);
    }

    @Override
    public void OnKeyInput(long window, int key, int scancode, int action, int mods) {
        super.OnKeyInput(window, key, scancode, action, mods);
        if (key == GLFW_KEY_W && action == GLFW_PRESS) {
            player.setVelocity(new Vector2f(0, 200));
        }
        if (key == GLFW_KEY_A && action == GLFW_PRESS) {
            player.setVelocity(new Vector2f(-200, 0));
        }
        if (key == GLFW_KEY_S && action == GLFW_PRESS) {
            player.setVelocity(new Vector2f(0, -200));
        }
        if (key == GLFW_KEY_D && action == GLFW_PRESS) {
            player.setVelocity(new Vector2f(200, 0));
        }
    }
}
