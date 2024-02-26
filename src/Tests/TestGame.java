package Tests;

import Render.Entity.Entity2D;
import Render.Entity.Projectile;
import Render.Entity.Texturing.Texture;
import Render.Shader.Shader;
import Render.Vertices.Model.ObjModel;
import Render.Vertices.Model.ObjModelParser;
import Render.Window.Window;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL30.glClearColor;

public class TestGame extends Test {
    Entity2D player;
    int livePoints;
    int maxLP = 15000;
    Entity2D entity2;
    Shader shader;
    Texture textureEntity;
    Texture textureProjectile;
    final int DIM = 10;
    ArrayList<Entity2D> entity2DS = new ArrayList<>();
    Vector2f scale = new Vector2f(4, 4);
    float timeBetweenShot = 0;
    Projectile proj;
    int numOfEnemies = 100;


    public TestGame() {
        super();
        shader = new Shader("res/shaders/texturing.shader");
        textureEntity = new Texture("res/textures/woodCrate.png", 0);
        textureProjectile = new Texture("res/models/square.png", 0);
        shader.setUniform1i("u_Texture", 0);
        textureEntity.bind();
        ObjModel model = ObjModelParser.parseOBJ("res/models/square.obj");
        player = new Entity2D(new Vector2f(), model, textureEntity, shader);
        player.scale(scale);
        livePoints = maxLP;
        entity2 = new Entity2D(new Vector2f(200, 200), model, textureEntity, shader);
        entity2.scale(scale);
        for (int i = 0; i < numOfEnemies; i++) {
            entity2DS.add(new Entity2D(new Vector2f(200+i*10, 100), model, textureEntity, shader));
            entity2DS.get(i).scale(scale);
            System.out.println("Entity created");
        }
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

        for (int i = 0; i < entity2DS.size(); i++) {
            Vector2f v2 = new Vector2f(player.getPosition());
            if (entity2DS.get(i) == null) continue;
            entity2DS.get(i).translate(new Vector2f(v2.sub(entity2DS.get(i).getPosition()).normalize()).mul(dt).mul(new Vector2f(100, 100)));
            if (player.collideRect(entity2DS.get(i))) {
                //entity2DS.set(i,null);
                //entity2DS.remove(i);
                if(livePoints > 0)
                    livePoints -= 1;
            }
        }
           if (livePoints <= 0) {
                System.out.println("Game Over");
                System.exit(0);
            }
        for (int i = 0; i < entity2DS.size(); i++) {
            if (player.collideRect(entity2DS.get(i))) {
                if(livePoints > 0)
                    livePoints -= 1;
            }
        }
         if (proj != null){
                for (int i = 0; i < entity2DS.size(); i++) {
                 if (proj.collideRect(entity2DS.get(i))) {
                      entity2DS.set(i,null);
                 }
                }
          }
        timeBetweenShot += dt;
        if (timeBetweenShot > 1) {
            Vector2f v3 = new Vector2f(entity2.getPosition());
            Vector2f velocityProjectile = new Vector2f(v3.sub(player.getPosition()).normalize()).mul(dt).mul(new Vector2f(100, 100));
            proj = new Projectile(new Vector2f(player.getPosition().x, player.getPosition().y), player.getModel(), textureProjectile, shader, player, velocityProjectile);
            proj.scale(scale);
            timeBetweenShot = 0;
        }
    }

    @Override
    public void OnRender() {
        super.OnRender();
        renderer.drawEntity2D(player);
        renderer.drawEntities2D(entity2DS);
        renderer.drawEntity2D(entity2);
        glClearColor(0.435f, 0.639f, 0.271f, 1);
        // draw rect above player : player.getPosition().sub(widthLP/2f, player.getScale().y*-6
        renderer.fillRect(new Vector2f(-Window.dim.x/2f, Window.dim.y/2f-25), new Vector2f(player.getScale().x * (maxLP/Window.dim.x), 25), new Vector4f(1, 0, 0, 1));
        renderer.fillRect(new Vector2f(-Window.dim.x/2f, Window.dim.y/2f-25f), new Vector2f(player.getScale().x * (livePoints/Window.dim.x), 25), new Vector4f(0, 1, 0, 1));

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

