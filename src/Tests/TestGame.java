package Tests;

import Render.Entity.Entity2D;
import Render.Entity.Projectile;
import Render.Entity.Texturing.Texture;
import Render.Shader.Shader;
import Render.Vertices.Model.ObjModel;
import Render.Vertices.Model.ObjModelParser;
import org.joml.Vector2f;
import org.joml.Vector4f;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL30.glClearColor;

public class TestGame extends Test {
    Entity2D player;
    int livePoints = 0;
    Entity2D entity2;
    Shader shader;
    Texture textureEntity;
    Texture textureProjectile;
    final int DIM = 10;
    Entity2D[] entity2DS = new Entity2D[DIM * DIM];
    Vector2f scale = new Vector2f(4, 4);
    float timeBetweenShot = 0;
    Projectile proj;

    public TestGame() {
        super();
        shader = new Shader("res/shaders/texturing.shader");
        textureEntity = new Texture("res/textures/woodCrate.png", 0);
        textureProjectile = new Texture("res/textures/woodCrate.png", 0);
        shader.setUniform1i("u_Texture", 0);
        textureEntity.bind();
        ObjModel model = ObjModelParser.parseOBJ("res/models/square.obj");
        player = new Entity2D(new Vector2f(), model, textureEntity, shader);
        player.scale(scale);
        livePoints = 500;
        entity2 = new Entity2D(new Vector2f(200, 200), model, textureEntity, shader);
        entity2.scale(scale);
       /* int entityIndex = 0;
        int spacing = 15;
        for (int i = 0; i < DIM; i++) {
            for (int j = 0; j < DIM; j++) {

                entity2DS[entityIndex] = new Entity2D(new Vector2f(i * spacing - DIM * spacing / 2f, j * spacing - DIM * spacing / 2f), model, textureEntity, shader);
                entity2DS[entityIndex++].scale(scale);

            }
        }

        */
    }

    @Override
    public void OnUpdate(float dt) {
        super.OnUpdate(dt);
        player.translate(new Vector2f(player.getVelocity()).mul(dt));
        Vector2f v = new Vector2f(player.getPosition());
        entity2.translate(new Vector2f(v.sub(entity2.getPosition()).normalize()).mul(dt).mul(new Vector2f(100, 100)));
        if (proj != null){
            proj.translate(proj.getVelocity());
        }

       /* for (int i = 0; i < entity2DS.length; i++) {
            Vector2f v2 = new Vector2f(player.getPosition());
            if (entity2DS[i] == null) continue;
            entity2DS[i].translate(new Vector2f(v2.sub(entity2DS[i].getPosition()).normalize()).mul(dt).mul(new Vector2f(100, 100)));
            if (player.collideRect(entity2DS[i])) {
                entity2DS[i] = null;
                livePoints -= 1;
            }
        }
*/
        if (player.collideRect(entity2)) {
            livePoints -= 1;
        }
        timeBetweenShot += dt;
        System.out.println(timeBetweenShot);
        if (timeBetweenShot > 1) {
            Vector2f v3 = new Vector2f(player.getPosition());
            Vector2f velocityProjectile = new Vector2f(v3.sub(entity2.getPosition()).normalize()).mul(dt).mul(new Vector2f(100, 100));
            proj = new Projectile(new Vector2f(player.getPosition().x, player.getPosition().y), player.getModel(), textureProjectile, shader, player, velocityProjectile);
            proj.scale(scale);
            timeBetweenShot = 0;
            System.out.println("shot");
        }
    }

    @Override
    public void OnRender() {
        super.OnRender();
        renderer.drawEntity2D(player);
        //renderer.drawEntities2D(entity2DS);
        renderer.drawEntity2D(entity2);
        glClearColor(0.435f, 0.639f, 0.271f, 1);
        //renderer.drawRect(new Vector2f(-1280,440), new Vector2f(500,25), new Vector4f(1,0,0,1));
        renderer.fillRect(new Vector2f(-1280, 615), new Vector2f(livePoints, 25), new Vector4f(1, 0, 0, 1));
        if (proj != null){
            renderer.drawEntity2D(proj);
        }

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

