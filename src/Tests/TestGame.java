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
    private final Entity2D player;
    private int livePoints;
    private final int maxLP = 15000;
    private final Entity2D target;
    private final Shader shader;
    private final Texture projectileTexture;
    private final ArrayList<Projectile> projectiles = new ArrayList<>();
    private final ArrayList<Entity2D> enemies = new ArrayList<>();
    private float timeBetweenShot = 0;
    private Projectile proj;


    public TestGame() {
        super();

        int numOfEnemies = 1000;
        float scale = 2f;

        Texture entityTexture = new Texture("res/textures/woodCrate.png", 0);
        projectileTexture = new Texture("res/textures/fireball.png", 0);

        shader = new Shader("res/shaders/texturing.shader");
        shader.setUniform1i("u_Texture", 0);

        ObjModel model = ObjModelParser.parseOBJ("res/models/square.obj");

        player = new Entity2D(new Vector2f(), model, entityTexture, shader);
        player.scale(scale);

        target = new Entity2D(new Vector2f(0, 0), model, entityTexture, shader);
        target.scale(scale);

        for (int i = 0; i < numOfEnemies; i++) {
            enemies.add(new Entity2D(new Vector2f((float) Math.random()*Window.dim.x-Window.dim.x/2f, (float) Math.random()*Window.dim.y-Window.dim.y/2f), model, entityTexture, shader));
            enemies.get(i).scale(scale);
        }

        livePoints = maxLP;
    }

    @Override
    public void OnUpdate(float dt) {
        super.OnUpdate(dt);
        // move player
        player.translate(new Vector2f(player.getVelocity()).mul(dt));
        // move target to player
            //Vector2f v = new Vector2f(player.getPosition());
            //target.translate(new Vector2f(v.sub(target.getPosition()).normalize()).mul(dt).mul(new Vector2f(100, 100)));
        // move target to mouse
        target.setPosition(renderer.screenToWorldCoords(mousePos));


        for (Entity2D enemy : enemies) {
            if (enemy == null) continue;
            // move enemy to player
            Vector2f v2 = new Vector2f(player.getPosition());
            enemy.translate(new Vector2f(v2.sub(enemy.getPosition()).normalize()).mul(dt).mul(new Vector2f(250, 250)));
            // damage player
            if (player.collideRect(enemy) && livePoints > 0)
                    livePoints -= 1;
        }

        if (livePoints <= 0) {
             System.out.println("Game Over");
             System.exit(0);
        }

        // damage enemies and move projectiles
        for (int j = 0; j < projectiles.size(); j++) {
            if (projectiles.get(j) != null){
                for (int i = 0; i < enemies.size(); i++) {
                    if (projectiles.get(j).collideRect(enemies.get(i))) {
                        enemies.set(i,null);
                    }
                }
                projectiles.get(j).translate(projectiles.get(j).getVelocity().mul(dt, new Vector2f()));
            }
        }


        timeBetweenShot += dt;
        if (timeBetweenShot > 0.2f) { // shoot every second/5
            // direction to target
            Vector2f v3 = new Vector2f(target.getPosition());
            Vector2f projectileVelocity = new Vector2f(v3.sub(player.getPosition()).normalize()).mul(new Vector2f(300, 300));
            // shoot new projectile
            projectiles.add(new Projectile(new Vector2f(player.getPosition().x, player.getPosition().y), player.getModel(), projectileTexture, shader, player, projectileVelocity));
            for (int i = 0; i < projectiles.size(); i++) {
                projectiles.get(i).scale(20);
            }
            // reset timer
            timeBetweenShot = 0;
        }
    }

    @Override
    public void OnRender() {
        super.OnRender();
        glClearColor(0.435f, 0.639f, 0.271f, 1);

        // entities
        renderer.drawEntity2D(player);
        renderer.drawEntities2D(enemies);
        renderer.drawEntity2D(target);
        renderer.drawProjectiles(projectiles);

        // live points
        renderer.fillRect(new Vector2f(-Window.dim.x/2f, Window.dim.y/2f-25), new Vector2f(player.getScale().x * ((float) maxLP /Window.dim.x), 25), new Vector4f(1, 0, 0, 1));
        renderer.fillRect(new Vector2f(-Window.dim.x/2f, Window.dim.y/2f-25f), new Vector2f(player.getScale().x * ((float) livePoints /Window.dim.x), 25), new Vector4f(0, 1, 0, 1));
        // draw rect above player : player.getPosition().sub(widthLP/2f, player.getScale().y*-6
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

