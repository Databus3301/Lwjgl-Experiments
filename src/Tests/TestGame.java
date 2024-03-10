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
    private final int maxLP = 500000;

    private final Shader shader;
    private final int[] keyArr = new int[4];

    private final ArrayList<Projectile> projectiles = new ArrayList<>();
    private final Texture projectileTexture;
    private float timeBetweenShot = 0;
    private final Entity2D target;

    private final ArrayList<Enemy> enemies = new ArrayList<>();
    private final int[] spatialLookUp; // for spatial partitioning
    private final int[] enemyCellPositions;
    private final int[] startIndeces;
    private final Vector2f cellSize = new Vector2f(200);

    public TestGame() {
        super();

        int numOfEnemies = 500;
        float scale = 3f;

        Texture entityTexture = new Texture("res/textures/woodCrate.png", 0);
        projectileTexture = new Texture("res/textures/fireball.png", 0);

        shader = new Shader("res/shaders/texturing.shader");
        shader.setUniform1i("u_Texture", 0);

        ObjModel model = ObjModelParser.parseOBJ("res/models/square.obj");

        player = new Entity2D(new Vector2f(), model, entityTexture, shader);
        player.scale(scale*4);

        target = new Entity2D(new Vector2f(0, 0), model, entityTexture, shader);
        target.scale(scale);

        for (int i = 0; i < numOfEnemies; i++) {
            enemies.add(new Enemy(new Vector2f((float) Math.random() * Window.dim.x - Window.dim.x / 2f, (float) Math.random() * Window.dim.y - Window.dim.y / 2f), model, entityTexture, shader, 50));
            enemies.get(i).scale(scale*i/100);
        }

        livePoints = maxLP;

        spatialLookUp = new int[enemies.size()];
        enemyCellPositions = new int[enemies.size()];
        startIndeces = new int[enemies.size()];
    }

    @Override
    public void OnUpdate(float dt) {
        super.OnUpdate(dt);

        // move player
        player.translate(new Vector2f(player.getVelocity()).mul(300*dt));
        // move target to mouse
        target.setPosition(renderer.screenToWorldCoords(mousePos));

        int enemyIndex = 0;
        for (Enemy enemy : enemies) {

            spatialLookUp[enemyIndex] = enemy.cellToHash(enemy.worldToCell(enemy.getPosition(), cellSize))  % enemyCellPositions.length; // save cell hashes to array
            enemyCellPositions[enemyIndex] = enemyIndex;                                                                                 // save enemy index to array

            // mark entity closest to cursor
            if(enemy.collideRect(target))
                renderer.drawText("Cell: " + spatialLookUp[enemyIndex] + "\nCollisions: " + enemy.getCollisions() + "\nChecks: " + enemy.getChecks(), new Vector2f(enemy.getPosition().x - enemy.getScale().x/2f, enemy.getPosition().y + 15), new Vector2f(5));


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
            int[] cellKeys = new int[5];
            cellKeys[0] = enemy.cellToHash(enemy.worldToCell(enemy.getPosition(), cellSize))  % enemyCellPositions.length;
            cellKeys[1] = enemy.cellToHash(enemy.worldToCell(enemy.getPosition().add(0, cellSize.y),  cellSize))  % enemyCellPositions.length;
            cellKeys[2] = enemy.cellToHash(enemy.worldToCell(enemy.getPosition().add(0, -cellSize.y), cellSize)) % enemyCellPositions.length;
            cellKeys[3] = enemy.cellToHash(enemy.worldToCell(enemy.getPosition().add(cellSize.x, 0),  cellSize))  % enemyCellPositions.length;
            cellKeys[4] = enemy.cellToHash(enemy.worldToCell(enemy.getPosition().add(-cellSize.x, 0), cellSize)) % enemyCellPositions.length;
//            for(int cellKey: cellKeys) {
//                int startIndex = startIndeces[cellKey];
//
//                for (int j = startIndex; j < startIndeces.length; j++) {
//                    if (spatialLookUp[j] != cellKey || enemyCellPositions[j] > enemies.size() - 1)
//                        break;
//                    Enemy enemy2 = enemies.get(enemyCellPositions[j]);
//                    enemy.addCheck();
//                    enemy2.addCheck();
//                    if (enemy != enemy2 && enemy.collideCircle(enemy2)) {
//                        Vector2f v1 = new Vector2f(enemy.getPosition());
//                        Vector2f v2 = new Vector2f(enemy2.getPosition());
//                        Vector2f v3 = new Vector2f(v1.sub(v2));
//                        enemy.addCollision();
//                        enemy2.addCollision();
//
//                        float overlap = enemy.getScale().x + enemy2.getScale().x - v3.length();
//                        if (overlap > 0) {
//                            v3.normalize().mul(overlap*0.1f);
//                            enemy.translateIn(v3, 100*dt);
//                            enemy2.translateIn(v3.mul(-1), 100*dt);
//                        }
//                    }
//                }
//            }
            for(Enemy enemy1 : enemies) {
                enemy1.addCheck();
                enemy.addCheck();
                if(enemy != enemy1 && enemy.collideCircle(enemy1)) {
                    Vector2f v1 = new Vector2f(enemy.getPosition());
                    Vector2f v2 = new Vector2f(enemy1.getPosition());
                    Vector2f v3 = new Vector2f(v1.sub(v2).normalize());

                    enemy.translate(v3);

                    enemy.addCollision();
                    enemy1.addCollision();
                }
            }
            // move enemy to player
            enemy.translateTowards(player, 100*dt);
            enemyIndex++;
        }

        // sort both arrays
        for (int j = 1; j < enemyCellPositions.length; j++) {
            int key = spatialLookUp[j];
            int value = enemyCellPositions[j];
            int k = j - 1;
            while (k >= 0 && spatialLookUp[k] > key) {
                spatialLookUp[k + 1] = spatialLookUp[k];
                enemyCellPositions[k + 1] = enemyCellPositions[k];
                k--;
            }
            spatialLookUp[k + 1] = key;
            enemyCellPositions[k + 1] = value;
        }

        int startIndex = 0;
        int lastCellKey = spatialLookUp[0];
        for(int j = 0; j < spatialLookUp.length; j++) {
            if (spatialLookUp[j] != lastCellKey) {
                lastCellKey = spatialLookUp[j];
                startIndex = j;
            }
            startIndeces[spatialLookUp[j]] = startIndex;
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

