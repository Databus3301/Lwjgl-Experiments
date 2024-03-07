package Tests;

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
    private final int maxLP = 1000000;
    private final Entity2D target;
    private final Shader shader;
    private final Texture projectileTexture;
    private final ArrayList<Projectile> projectiles = new ArrayList<>();
    private final ArrayList<Enemy> enemies = new ArrayList<>();
    private final int[] enemyGridKeyArr; // spatial lookup
    private final int[] enemyGridArr;
    private final int[] startIndeces;
    private float timeBetweenShot = 0;
    private final int[] keyArr = new int[4];

    public TestGame() {
        super();

        int numOfEnemies = 10000;
        float scale = 7f;

        Texture entityTexture = new Texture("res/textures/woodCrate.png", 0);
        projectileTexture = new Texture("res/textures/fireball.png", 0);

        shader = new Shader("res/shaders/texturing.shader");
        shader.setUniform1i("u_Texture", 0);

        ObjModel model = ObjModelParser.parseOBJ("res/models/square.obj");

        player = new Entity2D(new Vector2f(), model, entityTexture, shader);
        player.scale(scale*2);

        target = new Entity2D(new Vector2f(0, 0), model, entityTexture, shader);
        target.scale(scale);

        for (int i = 0; i < numOfEnemies; i++) {
            enemies.add(new Enemy(new Vector2f((float) Math.random() * Window.dim.x - Window.dim.x / 2f, (float) Math.random() * Window.dim.y - Window.dim.y / 2f), model, entityTexture, shader, 50));
            enemies.get(i).scale(scale);
        }

        livePoints = maxLP;

        enemyGridKeyArr = new int[enemies.size()];
        enemyGridArr = new int[enemies.size()];
        startIndeces = new int[enemies.size()];
    }

    @Override
    public void OnUpdate(float dt) {
        super.OnUpdate(dt);

        // move player
        player.translate(new Vector2f(player.getVelocity()).mul(dt));
        // move target to mouse
        target.setPosition(renderer.screenToWorldCoords(mousePos));

        int enemyIndex = 0;
        for (Enemy enemy : enemies) {
            enemyGridKeyArr[enemyIndex] = enemy.cellToHash(enemy.worldToCell(enemy.getPosition(), new Vector2f(100, 100)))  % enemyGridArr.length;
            enemyGridArr[enemyIndex] = enemyIndex;

            // damage player
            if (player.collideRect(enemy) && livePoints > 0)
                livePoints -= 1;
            // damage enemies
            for (Projectile projectile : projectiles) {
                if (enemy.getIframes() == 0 && projectile.collideAABB(enemy)) {
                    enemy.setHealth(enemy.getHealth() - projectile.getDmg());
                    enemy.setIframes(200);
                    //projectiles.set(projectiles.indexOf(projectile), null);
                }
            }

            //TODO: push them orthongonal their direction as to not have them push back?
            //TODO: Investigate cell coordinate mapping issue

            // push away from each other
            int cellKey = enemyGridKeyArr[enemyIndex];
            int startIndex = startIndeces[cellKey];

            for (int j = startIndex; j < startIndeces.length; j++) {
                if (enemyGridKeyArr[j] != cellKey)
                    break;
                Enemy enemy2 = enemies.get(enemyGridArr[j]);
                if (enemy != enemy2 && enemy.collideCircle(enemy2)) {
                    Vector2f v1 = new Vector2f(enemy.getPosition());
                    Vector2f v2 = new Vector2f(enemy2.getPosition());
                    Vector2f v3 = new Vector2f(v1.sub(v2).normalize());
                    enemy.translate(v3);
                }
            }
            // move enemy to player
            Vector2f v = new Vector2f(player.getPosition());
            enemy.translate(new Vector2f(v.sub(enemy.getPosition()).normalize()).mul(dt).mul(new Vector2f(200, 200)));

            enemyIndex++;
        }

        // sort both arrays
        for (int j = 1; j < enemyGridArr.length; j++) {
            int key = enemyGridKeyArr[j];
            int value = enemyGridArr[j];
            int k = j - 1;
            while (k >= 0 && enemyGridKeyArr[k] > key) {
                enemyGridKeyArr[k + 1] = enemyGridKeyArr[k];
                enemyGridArr[k + 1] = enemyGridArr[k];
                k--;
            }
            enemyGridKeyArr[k + 1] = key;
            enemyGridArr[k + 1] = value;
        }

        int startIndex = 0;
        int lastCellKey = enemyGridKeyArr[0];
        for(int j = 0; j < enemyGridKeyArr.length; j++) {
            if(enemyGridKeyArr[j] != lastCellKey) {
                lastCellKey = enemyGridKeyArr[j];
                startIndex = j;
            }
            startIndeces[enemyGridKeyArr[j]] = startIndex;
        }


        if (livePoints <= 0) {
            System.out.println("Game Over");
            System.exit(0);
        }

        //enemies dead?
        for (int i = 0; i < enemies.size(); i++) {
            enemies.get(i).reduceIframes();
            if (enemies.get(i).getHealth() <= 0) {
                Enemy last = enemies.getLast();
                enemies.set(i, last);
                enemies.removeLast();
            }
        }

        for(int i = 0; i < projectiles.size(); i++) {
            Projectile projectile = projectiles.get(i);
            projectile.translate(projectile.getVelocity().mul(dt, new Vector2f()));
            if (projectile.getPosition().x < -Window.dim.x / 2f || projectile.getPosition().x > Window.dim.x / 2f || projectile.getPosition().y < -Window.dim.y / 2f || projectile.getPosition().y > Window.dim.y / 2f) {
                Projectile last = projectiles.getLast();
                projectiles.set(i, last);
                projectiles.removeLast();
            }
        };

        //timeBetweenShot += dt;
        if (timeBetweenShot > 2.0f) { // shoot every 2 seconds
            // direction to target
            Vector2f v3 = new Vector2f(target.getPosition());
            Vector2f projectileVelocity = new Vector2f(v3.sub(player.getPosition()).normalize()).mul(new Vector2f(300, 300));
            // shoot new projectile
            projectiles.add(new Projectile(new Vector2f(player.getPosition().x, player.getPosition().y), player.getModel(), projectileTexture, shader, player, projectileVelocity, 20));
            projectiles.getLast().scale(20);

            // reset timer
            timeBetweenShot = 0;
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
        renderer.fillRect(new Vector2f(-Window.dim.x / 2f, Window.dim.y / 2f - 25), new Vector2f(player.getScale().x * ((float) maxLP / Window.dim.x), 25), new Vector4f(1, 0, 0, 1));
        renderer.fillRect(new Vector2f(-Window.dim.x / 2f, Window.dim.y / 2f - 25f), new Vector2f(player.getScale().x * ((float) livePoints / Window.dim.x), 25), new Vector4f(0, 1, 0, 1));
        // draw rect above player : player.getPosition().sub(widthLP/2f, player.getScale().y*-6
    }

    @Override
    public void OnKeyInput(long window, int key, int scancode, int action, int mods) {
        super.OnKeyInput(window, key, scancode, action, mods);
        //one Key press
        if (key == GLFW_KEY_W && (action == GLFW_PRESS || action == GLFW_REPEAT)) {
            player.setVelocity(new Vector2f(0, 200));
            keyArr[0] = GLFW_KEY_W;
            if (keyArr[1] == GLFW_KEY_A) player.setVelocity(new Vector2f(-200, 200));
            if (keyArr[3] == GLFW_KEY_D) player.setVelocity(new Vector2f(200, 200));
        }
        if (key == GLFW_KEY_A && (action == GLFW_PRESS || action == GLFW_REPEAT)) {
            player.setVelocity(new Vector2f(-200, 0));
            keyArr[1] = GLFW_KEY_A;
            if (keyArr[0] == GLFW_KEY_W) player.setVelocity(new Vector2f(-200, 200));
            if (keyArr[2] == GLFW_KEY_S) player.setVelocity(new Vector2f(-200, -200));
        }
        if (key == GLFW_KEY_S && (action == GLFW_PRESS || action == GLFW_REPEAT)) {
            player.setVelocity(new Vector2f(0, -200));
            keyArr[2] = GLFW_KEY_S;
            if (keyArr[1] == GLFW_KEY_A) player.setVelocity(new Vector2f(-200, -200));
            if (keyArr[3] == GLFW_KEY_D) player.setVelocity(new Vector2f(200, -200));
        }
        if (key == GLFW_KEY_D && (action == GLFW_PRESS || action == GLFW_REPEAT)) {
            player.setVelocity(new Vector2f(200, 0));
            keyArr[3] = GLFW_KEY_D;
            if (keyArr[0] == GLFW_KEY_W) player.setVelocity(new Vector2f(200, 200));
            if (keyArr[2] == GLFW_KEY_S) player.setVelocity(new Vector2f(200, -200));
        }
        //Release keys
        if (key == GLFW_KEY_W && action == GLFW_RELEASE) {
            if (keyArr[1] == GLFW_KEY_A) player.setVelocity(new Vector2f(-200, 0));
            else if (keyArr[3] == GLFW_KEY_D) player.setVelocity(new Vector2f(200, 0));
            else player.setVelocity(new Vector2f(0, 0));
            keyArr[0] = 0;
        }
        if (key == GLFW_KEY_A && action == GLFW_RELEASE) {
            if (keyArr[0] == GLFW_KEY_W) player.setVelocity(new Vector2f(0, 200));
            else if (keyArr[2] == GLFW_KEY_S) player.setVelocity(new Vector2f(0, -200));
            else player.setVelocity(new Vector2f(0, 0));
            keyArr[1] = 0;
        }
        if (key == GLFW_KEY_S && action == GLFW_RELEASE) {
            if (keyArr[1] == GLFW_KEY_A) player.setVelocity(new Vector2f(-200, 0));
            else if (keyArr[3] == GLFW_KEY_D) player.setVelocity(new Vector2f(200, 0));
            else player.setVelocity(new Vector2f(0, 0));
            keyArr[2] = 0;
        }
        if (key == GLFW_KEY_D && action == GLFW_RELEASE) {
            if (keyArr[0] == GLFW_KEY_W) player.setVelocity(new Vector2f(0, 200));
            else if (keyArr[2] == GLFW_KEY_S) player.setVelocity(new Vector2f(0, -200));
            else player.setVelocity(new Vector2f(0, 0));
            keyArr[3] = 0;
        }

    }

}

