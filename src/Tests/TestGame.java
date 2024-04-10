package Tests;

import Game.Action.Waves.EnemySpawner;
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
import java.util.Iterator;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL30.glClearColor;

public class TestGame extends Test { //TODO: move things into a player class
    private final Player player;
    private final Entity2D target;
    private final ArrayList<Enemy> enemies = new ArrayList<>();
    private final ArrayList<Projectile> projectiles = new ArrayList<>();
    private final Camera camera;


    private final EnemySpawner spawner = new EnemySpawner();
    private final ColorReplacement colorReplacement = new ColorReplacement();
    private final int[] keyArr = new int[4];
    private boolean shouldSimulate = true;


    public TestGame() {
        super();
        // take cam control
        renderer.setCamera(camera = new Camera());

        int numOfEnemies = 100;
        float scale = 3f;

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
        player.addAnimation("idleDown", new Animation(anims, 0, 0, 2,  3, 3));
        player.addAnimation("idleLeft", new Animation(anims, 1, 0, 2,  3, 3));
        player.addAnimation("idleUp",   new Animation(anims, 2, 0, 1,  1, 3));
        player.addAnimation("idleRight",new Animation(anims, 3, 0, 2,  3, 3));
        player.switchAnimation("idleDown");
        player.getEntity().scale(scale*(4+numOfEnemies/(numOfEnemies/10f)));

        // TODO: Spawner class
        // track mouse and indicate cursor position
        Texture cursor = new Texture("woodCrate.png", 0);
        target = new Entity2D(new Vector2f(), ObjModel.SQUARE, cursor, Shader.TEXTURING);
        target.scale(scale);
        target.setColor(1, 0, 0,1);

        // define text colors
        colorReplacement.swap(new Vector4f(1, 1, 1, 1), new Vector4f(0, 1, 1, 1));
        colorReplacement.swap(new Vector4f(0, 0, 0, 1), new Vector4f(0, 0, 1, 1));

        // enemy spawning rules
        spawner.setProbabilityDistribution(new float[]{0.7f, 0.3f});
        spawner.setTracker(player.getEntity());
    }

    @Override
    public void OnUpdate(float dt) {
        super.OnUpdate(dt);

        // game over
        if (player.getLP() <= 0) {
            String text = "> GAME OVER <";
            float size = 20;
            renderer.drawText(text, new Vector2f(), size, Font.RETRO, Shader.TEXTURING, Font::centerFirstLine_UI, colorReplacement, null);
            shouldSimulate = false;
        }

        if(!shouldSimulate) return;
        // move player
        Entity2D playerEntity = player.getEntity();
        playerEntity.translate(new Vector2f(playerEntity.getVelocity()).mul(300*dt));
        camera.centerOn(playerEntity);
        // move target to mouse
        target.setPosition(renderer.screenToWorldCoords(mousePos));
        // collide player and its fields
        player.collide(enemies);
        // spawn enemies
        spawner.update(dt, enemies);

        Iterator<Enemy> enemyIterator = enemies.iterator();
        while(enemyIterator.hasNext()) {
            Enemy enemy = enemyIterator.next();
            // move enemy to player
            enemy.translateTowards(playerEntity, 100*dt);
            // push away from each other
            for(Enemy enemy1 : enemies) {
                if(enemy != enemy1 && enemy.collideCircle(enemy1))
                    enemy.translateTowards(enemy1, -100*dt); // negated "towards" becomes "away"
            }
            // kill enemiess
            enemy.reduceISeconds(dt);
            if(enemy.getLP() <= 0)
                enemyIterator.remove();
            // print debug info if on cursor
            if(enemy.collideRect(target))
                renderer.drawText("LivePoints: " + enemy.getLP(), new Vector2f(enemy.getPosition().x - enemy.getScale().x/2f, enemy.getPosition().y + 15), 5);
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

        // debug
        renderer.drawRect(player.getEntity().rectangle);
        renderer.drawCollisionRect(player.getEntity());
    }

    @Override
    public void OnKeyInput(long window, int key, int scancode, int action, int mods) {
        super.OnKeyInput(window, key, scancode, action, mods);

        // release keys
        if (key == GLFW_KEY_W && action == GLFW_RELEASE) {
            keyArr[0] = 0;
            player.switchAnimation("idleUp");
        }
        if (key == GLFW_KEY_A && action == GLFW_RELEASE) {
            keyArr[1] = 0;
            player.switchAnimation("idleLeft");
        }
        if (key == GLFW_KEY_S && action == GLFW_RELEASE) {
            keyArr[2] = 0;
            player.switchAnimation("idleDown");
        }
        if (key == GLFW_KEY_D && action == GLFW_RELEASE) {
            keyArr[3] = 0;
            player.switchAnimation("idleRight");
        }

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

