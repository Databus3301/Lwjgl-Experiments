package Tests;

import Render.Entity.Entity2D;
import Render.Entity.Texturing.Texture;
import Render.Shader.Shader;
import Render.Vertices.Model.ObjModel;
import Render.Vertices.Model.ObjModelParser;
import org.joml.Vector2f;
import org.joml.Vector4f;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL30.glClearColor;

public class TestGame extends Test {
    Entity2D entity;
    int livePoints = 0;
    Entity2D entity2;
    Shader shader;
    Texture texture;
    final int DIM = 10;
    Entity2D[] entity2DS = new Entity2D[DIM * DIM];
    Vector2f scale = new Vector2f(4, 4);

    public TestGame() {
        super();
        shader = new Shader("res/shaders/texturing.shader");
        texture = new Texture("res/textures/woodCrate.png", 0);
        shader.setUniform1i("u_Texture", 0);
        texture.bind();
        ObjModel model = ObjModelParser.parseOBJ("res/models/square.obj");
        entity = new Entity2D(new Vector2f(), model, texture, shader);
        entity.scale(scale);
        livePoints = 500;
        entity2 = new Entity2D(new Vector2f(200, 200), model, texture, shader);
        entity2.scale(scale);
       /* int entityIndex = 0;
        int spacing = 15;
        for (int i = 0; i < DIM; i++) {
            for (int j = 0; j < DIM; j++) {

                entity2DS[entityIndex] = new Entity2D(new Vector2f(i * spacing - DIM * spacing / 2f, j * spacing - DIM * spacing / 2f), model, texture, shader);
                entity2DS[entityIndex++].scale(scale);

            }
        }

        */
    }

    @Override
    public void OnUpdate(float dt) {
        super.OnUpdate(dt);
        entity.translate(new Vector2f(entity.getVelocity()).mul(dt));
        Vector2f v = new Vector2f(entity.getPosition());
        entity2.translate(new Vector2f(v.sub(entity2.getPosition()).normalize()).mul(dt).mul(new Vector2f(100, 100)));
       /* for (int i = 0; i < entity2DS.length; i++) {
            Vector2f v2 = new Vector2f(entity.getPosition());
            if (entity2DS[i] == null) continue;
            entity2DS[i].translate(new Vector2f(v2.sub(entity2DS[i].getPosition()).normalize()).mul(dt).mul(new Vector2f(100, 100)));
            if (entity.collideRect(entity2DS[i])) {
                entity2DS[i] = null;
                livePoints -= 1;
            }
        }
*/
        if (entity.collideRect(entity2)) {
            livePoints -= 1;
        }
    }

    @Override
    public void OnRender() {
        super.OnRender();
        renderer.drawEntity2D(entity);
        renderer.drawEntities2D(entity2DS);
       // renderer.drawEntity2D(entity2);
        glClearColor(0.435f, 0.639f, 0.271f, 1);
        //renderer.drawRect(new Vector2f(-1280,440), new Vector2f(500,25), new Vector4f(1,0,0,1));
        renderer.fillRect(new Vector2f(-1280, 615), new Vector2f(livePoints, 25), new Vector4f(1, 0, 0, 1));
    }

    @Override
    public void OnKeyInput(long window, int key, int scancode, int action, int mods) {
        super.OnKeyInput(window, key, scancode, action, mods);
        if (key == GLFW_KEY_W && action == GLFW_PRESS) {
            entity.setVelocity(new Vector2f(0, 200));
        }
        if (key == GLFW_KEY_A && action == GLFW_PRESS) {
            entity.setVelocity(new Vector2f(-200, 0));
        }
        if (key == GLFW_KEY_S && action == GLFW_PRESS) {
            entity.setVelocity(new Vector2f(0, -200));
        }
        if (key == GLFW_KEY_D && action == GLFW_PRESS) {
            entity.setVelocity(new Vector2f(200, 0));
        }
    }

    public class Bullet {
        Vector2f position;
        Vector2f velocity;
        Entity2D shotBy;
        Texture texture;
        Shader shader;

        public Bullet(Vector2f position, Vector2f velocity, Entity2D shotBy, Texture texture, Shader shader) {
            this.shotBy = shotBy;
            this.position = position;
            this.velocity = velocity;
            this.texture = texture;
            this.shader = shader;
        }
    }
}

