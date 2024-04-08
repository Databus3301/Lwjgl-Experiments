package Tests;

import Game.Entities.Player;
import Render.Entity.Camera.Camera;
import Game.Entities.Enemy;
import Render.Entity.Entity2D;
import Game.Entities.Projectile;
import Render.MeshData.Texturing.*;
import Render.MeshData.Shader.Shader;
import Render.MeshData.Model.ObjModel;
import Render.Window;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL30.glClearColor;

public class TestGame extends Test { //TODO: move things into a player class
    private final Player player;
    private final Entity2D target;
    private final ArrayList<Enemy> enemies = new ArrayList<>();
    private final ArrayList<Projectile> projectiles = new ArrayList<>();
    private final Camera camera;



    private final ColorReplacement colorReplacement = new ColorReplacement();
    private final int[] keyArr = new int[4];
    private boolean shouldSimulate = true;


    public TestGame() {
        super();
        // take cam control
        renderer.setCamera(camera = new Camera());

        int numOfEnemies = 100;
        float scale = 3f;

        Texture entityTexture = new Texture("res/textures/woodCrate.png", 0);
        //projectileTexture = new Texture("res/textures/fireball.png", 0);

        // init player
        player = new Player(
                this,
                new Entity2D(new Vector2f(0, 0), ObjModel.SQUARE.clone(), Shader.TEXTURING),
                50000
        );
        TextureAtlas anims = new TextureAtlas(new Texture("LinkAnim.png", 0), 10, 8, 10, 120, 130, 0);
        player.addAnimation("walkDown", new Animation(anims, 4, 0, 20, 10));
        player.addAnimation("walkLeft", new Animation(anims, 5, 0, 20, 10));
        player.addAnimation("walkUp",   new Animation(anims, 6, 0, 20, 10));
        player.addAnimation("walkRight",new Animation(anims, 7, 0, 20, 10));
        player.getEntity().scale(scale*(4+numOfEnemies/(numOfEnemies/10f)));

        // track mouse
        target = new Entity2D(new Vector2f(), ObjModel.SQUARE, entityTexture, Shader.TEXTURING);
        target.scale(scale);

         // spawn enemies
        for (int i = 0; i < numOfEnemies; i++) {
            enemies.add(new Enemy(new Vector2f((float) Math.random() * Window.dim.x - Window.dim.x / 2f, (float) Math.random() * Window.dim.y - Window.dim.y / 2f), ObjModel.SQUARE, entityTexture, Shader.TEXTURING, 50));
            enemies.get(i).scale(scale*i/(numOfEnemies/10f));
        }

        colorReplacement.swap(new Vector4f(1, 1, 1, 1), new Vector4f(0, 1, 1, 1));
        colorReplacement.swap(new Vector4f(0, 0, 0, 1), new Vector4f(0, 0, 1, 1));
    }

    @Override
    public void OnUpdate(float dt) {
        super.OnUpdate(dt);

        // game over
        if (player.getLP() <= 0) {
            String text = "> GAME OVER <";
            float size = 20;
            renderer.drawText(text, new Vector2f(), size, Font.RETRO, Shader.TEXTURING, Font::centerFirstLine_UI, colorReplacement, -1);
            shouldSimulate = false;
        }


        if(!shouldSimulate) return;
        // move player
        Entity2D playerEntity = player.getEntity();
        playerEntity.translate(new Vector2f(playerEntity.getVelocity()).mul(300*dt));
        camera.centerOn(playerEntity);
        // move target to mouse
        target.setPosition(renderer.screenToWorldCoords(mousePos));

        for (Enemy enemy : enemies) {
            // print debug info if on cursor
            if(enemy.collideRect(target))
                renderer.drawText("Health: " + enemy.getHealth(), new Vector2f(enemy.getPosition().x - enemy.getScale().x/2f, enemy.getPosition().y + 15), 5);

            if (playerEntity.collideCircle(enemy) && player.getLP() > 0) {
                // push away from player
                Vector2f v1 = new Vector2f(playerEntity.getPosition());
                Vector2f v2 = new Vector2f(enemy.getPosition());
                v2.add( (float) Math.random()*20f-1f, (float) Math.random()*20f-1f); // randomize a bit (to avoid getting stuck in a loop of pushing each other back and forth
                Vector2f v3 = new Vector2f(v1.sub(v2).normalize().mul(-1));
                enemy.translate(v3);
                // damage player
                player.damage();
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
                if(enemy != enemy1 && enemy.collideCircle(enemy1))
                    enemy.translateTowards(enemy1, -100*dt); // negated "towards" becomes "away"
            }
            // move enemy to player
            enemy.translateTowards(playerEntity, 100*dt);
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

    }

    @Override
    public void OnRender() {
        super.OnRender();
        glClearColor(0.435f, 0.639f, 0.271f, 1);

        // entities
        renderer.drawEntities2D(enemies);
        renderer.drawEntities2D(projectiles);
        renderer.drawPlayer(player);
        renderer.drawEntity2D(target);

        // live points
        float widthLP = (float) Window.dim.x / 4f; // TODO: fix positioning when adjusting viewport || maybe use drawUI instead
        renderer.fillRect(new Vector2f(-Window.dim.x / 2f, Window.dim.y / 2f - 25f).sub(camera.getPosition()), new Vector2f(widthLP, 25), new Vector4f(1, 0, 0, 1));
        renderer.fillRect(new Vector2f(-Window.dim.x / 2f, Window.dim.y / 2f - 25f).sub(camera.getPosition()), new Vector2f(widthLP * ((float) player.getLP() / player.getMaxLP()), 25), new Vector4f(0, 1, 0, 1));
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
        Entity2D playerEntity = player.getEntity();
        playerEntity.setVelocity(new Vector2f(0, 0));
        if(keyArr[0] == GLFW_KEY_W)
            playerEntity.accelerate(new Vector2f(0, 1));
        if(keyArr[1] == GLFW_KEY_A)
            playerEntity.accelerate(new Vector2f(-1, 0));
        if(keyArr[2] == GLFW_KEY_S)
            playerEntity.accelerate(new Vector2f(0, -1));
        if(keyArr[3] == GLFW_KEY_D)
            playerEntity.accelerate(new Vector2f(1, 0));

        // update animation
        if(keyArr[0] == GLFW_KEY_W)
            player.switchAnimation("walkUp");
        if(keyArr[1] == GLFW_KEY_A)
            player.switchAnimation("walkLeft");
        if(keyArr[2] == GLFW_KEY_S)
            player.switchAnimation("walkDown");
        if(keyArr[3] == GLFW_KEY_D)
            player.switchAnimation("walkRight");
    }

}

