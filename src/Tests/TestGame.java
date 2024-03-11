package Tests;

import Render.Entity.Camera.Camera;
import Render.Entity.Enemy;
import Render.Entity.Entity2D;
import Render.Entity.Projectile;
import Render.Entity.Texturing.Texture;
import Render.Shader.Shader;
import Render.Vertices.Model.ObjModel;
import Render.Vertices.Model.ObjModelParser;
import Render.Window;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL30.glClearColor;

public class TestGame extends Test {
    private final Entity2D player;
    private int livePoints;
    private final int maxLP = 500000;

    private final Shader shader;
    private final Camera camera;
    private final int[] keyArr = new int[4];

    private final ArrayList<Projectile> projectiles = new ArrayList<>();
    private final Texture projectileTexture;
    private float timeBetweenShot = 0;
    private final Entity2D target;

    private final ArrayList<Enemy> enemies = new ArrayList<>();

    public TestGame() {
        super();
        renderer.setCamera(camera = new Camera(new Vector2f(0, 0)));

        int numOfEnemies = 500;
        float scale = 3f;

        Texture entityTexture = new Texture("res/textures/woodCrate.png", 0);
        projectileTexture = new Texture("res/textures/fireball.png", 0);

        shader = new Shader("res/shaders/texturing.shader");
        shader.setUniform1i("u_Texture", 0);

        ObjModel model = ObjModelParser.parseOBJ("res/models/square.obj");

        player = new Entity2D(new Vector2f(0, 0), model, entityTexture, shader);
        player.scale(scale*(4+numOfEnemies/100f));

        target = new Entity2D(new Vector2f(), model, entityTexture, shader);
        target.scale(scale);

        for (int i = 0; i < numOfEnemies; i++) {
            enemies.add(new Enemy(new Vector2f((float) Math.random() * Window.dim.x - Window.dim.x / 2f, (float) Math.random() * Window.dim.y - Window.dim.y / 2f), model, entityTexture, shader, 50));
            enemies.get(i).scale(scale*i/100);
        }

        livePoints = maxLP;
    }

    @Override
    public void OnUpdate(float dt) {
        super.OnUpdate(dt);

        // move player
        player.translate(new Vector2f(player.getVelocity()).mul(300*dt));
        camera.centerOn(player);
        // move target to mouse
        target.setPosition(renderer.screenToWorldCoords(mousePos));

        for (Enemy enemy : enemies) {
            // mark entity closest to cursor
            if(enemy.collideRect(target))
                renderer.drawText("Health: " + enemy.getHealth(), new Vector2f(enemy.getPosition().x - enemy.getScale().x/2f, enemy.getPosition().y + 15), new Vector2f(5));


            if (player.collideRect(enemy) && livePoints > 0) {
                // push away from player
                Vector2f v1 = new Vector2f(player.getPosition());
                Vector2f v2 = new Vector2f(enemy.getPosition());
                v2.add( (float) Math.random()*20f-1f, (float) Math.random()*20f-1f); // randomize a bit (to avoid getting stuck in a loop of pushing each other back and forth
                Vector2f v3 = new Vector2f(v1.sub(v2).normalize().mul(-1));
                enemy.translate(v3);
                // damage player
                livePoints -= 1;
            }
            // damage enemies
            for (Projectile projectile : projectiles) {
                if (enemy.getIframes() == 0 && projectile.collideAABB(enemy)) {
                    enemy.setHealth(enemy.getHealth() - projectile.getDmg());
                    enemy.setIframes(200);
                }
            }

            // push away from each other
            for(Enemy enemy1 : enemies) {
                // TODO: check the performance on the distance checks vs pure collision
                if(enemy != enemy1 && enemy.getCenter().distance(enemy1.getCenter()) > enemy.getScale().maxComponent() && enemy.collideCircle(enemy1)) {
                    Vector2f v1 = new Vector2f(enemy.getPosition());
                    Vector2f v2 = new Vector2f(enemy1.getPosition());
                    Vector2f v3 = new Vector2f(v1.sub(v2).normalize());

                    enemy.translate(v3);
                }
            }
            // move enemy to player
            enemy.translateTowards(player, 100*dt);
        }

        if (livePoints <= 0) {
            System.out.println("Game Over");
            System.exit(0);
        }

        //enemies dead?
        for (int i = 0; i < enemies.size(); i++) {
            enemies.get(i).reduceIframes();
            if (enemies.get(i).getHealth() <= 0) {
                Enemy last = enemies.get(enemies.size()-1);
                enemies.set(i, last);
                enemies.remove(enemies.size()-1);
            }
        }

        for(int i = 0; i < projectiles.size(); i++) {
            Projectile projectile = projectiles.get(i);
            projectile.translate(projectile.getVelocity().mul(dt, new Vector2f()));
        if (projectile.getPosition().x < -Window.dim.x / 2f + player.getPosition().x  + 100 || projectile.getPosition().x > Window.dim.x / 2f + player.getPosition().x + 100 || projectile.getPosition().y < -Window.dim.y / 2f + player.getPosition().y  + 100|| projectile.getPosition().y > Window.dim.y / 2f + player.getPosition().y  + 100) {
                Projectile last = projectiles.get(projectiles.size()-1);
                projectiles.set(i, last);
                projectiles.remove(projectiles.size()-1);
            }
        };

        timeBetweenShot += dt;
        if (timeBetweenShot > 2.0f) { // shoot every 2 seconds
            // direction to target
            Vector2f v3 = new Vector2f(target.getPosition());
            Vector2f projectileVelocity = new Vector2f(v3.sub(player.getPosition()).normalize()).mul(new Vector2f(300, 300));
            // shoot new projectile
            projectiles.add(new Projectile(new Vector2f(player.getPosition()), player.getModel(), projectileTexture, shader, player, projectileVelocity, 20));
            projectiles.getLast().scale(20);

            // reset timer
            //timeBetweenShot = 0;
        }
    }

    @Override
    public void OnRender() {
        super.OnRender();
        glClearColor(0.435f, 0.639f, 0.271f, 1);

        // entities
        renderer.drawEntities2D(enemies);
        renderer.drawEntity2D(target);
        renderer.drawProjectiles(projectiles);
        renderer.drawEntity2D(player);

        // live points
        float widthLP = (float) Window.dim.x / 4f;
        renderer.fillRect(new Vector2f(-Window.dim.x / 2f, Window.dim.y / 2f - 25f), new Vector2f(widthLP, 25), new Vector4f(1, 0, 0, 1));
        renderer.fillRect(new Vector2f(-Window.dim.x / 2f, Window.dim.y / 2f - 25f), new Vector2f(widthLP * ((float) livePoints / maxLP), 25), new Vector4f(0, 1, 0, 1));
    }

    @Override
    public void OnKeyInput(long window, int key, int scancode, int action, int mods) {
        super.OnKeyInput(window, key, scancode, action, mods);

        // release keys
        if (key == GLFW_KEY_W && action == GLFW_RELEASE)
            keyArr[0] = 0;
        if (key == GLFW_KEY_A && action == GLFW_RELEASE)
            keyArr[1] = 0;
        if (key == GLFW_KEY_S && action == GLFW_RELEASE)
            keyArr[2] = 0;
        if (key == GLFW_KEY_D && action == GLFW_RELEASE)
            keyArr[3] = 0;

        // register keys
        if (key == GLFW_KEY_W && (action == GLFW_PRESS || action == GLFW_REPEAT))
            keyArr[0] = GLFW_KEY_W;
        if (key == GLFW_KEY_A && (action == GLFW_PRESS || action == GLFW_REPEAT))
            keyArr[1] = GLFW_KEY_A;
        if (key == GLFW_KEY_S && (action == GLFW_PRESS || action == GLFW_REPEAT))
            keyArr[2] = GLFW_KEY_S;
        if (key == GLFW_KEY_D && (action == GLFW_PRESS || action == GLFW_REPEAT))
            keyArr[3] = GLFW_KEY_D;

        // apply velocity
        player.setVelocity(new Vector2f(0, 0));
        if(keyArr[0] == GLFW_KEY_W)
            player.accelerate(new Vector2f(0, 1));
        if(keyArr[1] == GLFW_KEY_A)
            player.accelerate(new Vector2f(-1, 0));
        if(keyArr[2] == GLFW_KEY_S)
            player.accelerate(new Vector2f(0, -1));
        if(keyArr[3] == GLFW_KEY_D)
            player.accelerate(new Vector2f(1, 0));
    }

}

