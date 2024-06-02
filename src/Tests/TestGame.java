package Tests;

import Audio.AudioClip;
import Audio.AudioSource;
import Game.Action.Ability;
import Game.Action.Waves.EnemySpawner;
import Game.Action.Waves.Wave;
import Game.Entities.Collectible;
import Game.Entities.Dungeon.Door;
import Game.Entities.Dungeon.Dungeon;
import Game.Entities.Dungeon.Room;
import Game.Entities.Enemy;
import Game.Entities.Player;
import Game.Entities.Projectiles.Projectile;
import Game.UI;
import Render.Entity.Camera.Camera;
import Render.Entity.Entity2D;
import Render.Entity.Interactable.Interactable;
import Render.MeshData.Model.ObjModel;
import Render.MeshData.Shader.Shader;
import Render.MeshData.Texturing.*;
import Render.Window;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static Game.Action.Waves.EnemySpawner.Result.WAVE_OVER;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL30.glClearColor;

public class TestGame extends Test {
    private final Player player;
    private Dungeon dungeon;
    private Room room;
    private final Entity2D cursor, bg;


    private final EnemySpawner spawner = new EnemySpawner();
    private final ArrayList<Enemy> enemies = new ArrayList<>();
    private final ArrayList<Projectile> projectiles = new ArrayList<>();
    private final ArrayList<Interactable> props = new ArrayList<>();

    private final Camera camera;
    private final ColorReplacement colorReplacement = new ColorReplacement();
    private final int[] keyArr = new int[4];
    private boolean shouldSimulate = true;
    private boolean shouldAdvanceFloor = false;


    public TestGame() {
        super();
        // take cam control
        renderer.setCamera(camera = new Camera());

        // init player
        player = new Player(
                this,
                new Entity2D(new Vector2f(0, 0), ObjModel.SQUARE.clone(), Shader.TEXTURING),
                500
        );
        TextureAtlas anims = new TextureAtlas(new Texture("LinkAnim.png", 0), 10, 8, 10, 120, 130, 0);
        player.addAnimation("walkDown", new Animation(anims, 4, 0, 20, 10));
        player.addAnimation("walkLeft", new Animation(anims, 5, 0, 20, 10));
        player.addAnimation("walkUp", new Animation(anims, 6, 0, 20, 10));
        player.addAnimation("walkRight", new Animation(anims, 7, 0, 20, 10));
        player.addAnimation("idleDown", new Animation(anims, 0, 0, 2, 3, 3));
        player.addAnimation("idleLeft", new Animation(anims, 1, 0, 2, 3, 3));
        player.addAnimation("idleUp", new Animation(anims, 2, 0, 1, 1, 3));
        player.addAnimation("idleRight", new Animation(anims, 3, 0, 2, 3, 3));
        player.switchAnimation("idleDown");
        player.scale(30.0f * Dungeon.SCALE);

        // track mouse and indicate cursor position
        Texture cursor = new Texture("crosshair.png", 0);
        this.cursor = new Entity2D(new Vector2f(), ObjModel.SQUARE, cursor, Shader.TEXTURING);
        this.cursor.scale(16*Dungeon.SCALE);

        // define text colors
        colorReplacement.swap(new Vector4f(1, 1, 1, 1), new Vector4f(0, 1, 1, 1));
        colorReplacement.swap(new Vector4f(0, 0, 0, 1), new Vector4f(0, 0, 1, 1));

        // enemy spawning rules
        spawner.setProbabilityDistribution(new float[]{0.3f, 0.3f, 0.4f, 0.1f});
        spawner.setTracker(player);

        bg = new Entity2D(new Vector2f(Window.dim.div(-2f, new Vector2i())), ObjModel.SQUARE, Shader.TEXTURING) {{
            scale(3000);
            setColor(0.15f, 0.15f, 0.15f, 0.1f);
        }};
    }

    @Override
    public void OnStart() {
        super.OnStart();
        renderer.cursorHide();

        // init dungeon
        int floor = dungeon == null ? 0 : dungeon.getFloor();
        dungeon = new Dungeon(player, this, Dungeon.DEFAULT_DEPTH+floor*2, floor+1);
        room = dungeon.getStart();
        props.clear();
        player.setPosition(room.getPosition());
        room.onSwitch(player, enemies, spawner, props);

        shouldAdvanceFloor = false;
        if(floor > 1)
            dungeon.setStartedPlaying(true);

        Window.baseDim = new Vector2i(Window.dim);
    }

    @Override
    public void OnUpdate(float dt) {
        if(shouldAdvanceFloor)
            OnStart();
        // game over
        if (player.getLP() <= 0) {
            // clean up scene
            for(Ability ability : player.getAbilities()) {
                ability.getProjectiles().clear();
            }
            projectiles.clear();
            enemies.clear();
            props.clear();

            Window.changeTest(new TestGameOverScreen());
            shouldSimulate = false;
        }
        // wave over
        if (spawner.getLastResult() == WAVE_OVER) {
            shouldSimulate = true;
            dungeon.getMusicAudioSource().stopSound();
            UI.onRoomCompletion(player, this, 3);
        }
        // move target to mouse
        cursor.setPosition(renderer.screenToWorldCoords(mousePos));

        // restart music
        if(dungeon.hasStartedPlayingMusic() && !dungeon.getMusicAudioSource().isPlaying()) {
            dungeon.getMusicAudioSource().resumeSound();
        }


        if (!shouldSimulate) return;
        // move player
        player.translate(new Vector2f(player.getVelocity()).mul(player.getSpeed() * dt));
        camera.centerOn(player);
        // collide player and its fields
        player.collide(enemies);
        player.collide(room);
        player.collıde(props);
        // spawn enemies
        spawner.update(dt, enemies);
        // change room
        room = room.update(dt, spawner, player, enemies, projectiles, props);


        Iterator<Enemy> enemyIterator = enemies.iterator();
        while (enemyIterator.hasNext()) {
            Enemy enemy = enemyIterator.next();
            // movement, abilities, iFrames, etc..
            enemy.update(dt, mousePos, player.getPosition(), room);
            // push away from each other
            enemy.collide(dt, enemies, player);
            // kill enemies
            if (enemy.getLP() <= 0) {
                enemy.spawnXp(this, props, player, room);
                enemyIterator.remove();
            }
            // print debug info if on cursor
            if (cursor.collideRect(enemy))
                renderer.drawText("LivePoints: " + enemy.getLP(), new Vector2f(enemy.getPosition().x - enemy.getScale().x / 2f, enemy.getPosition().y + 15), 5);
        }

        super.OnUpdate(dt);
    }

    @Override
    public void OnRender() {
        super.OnRender();
        glClearColor(0.04f, 0.04f, 0.03f, 1);

        // entities
        renderer.draw(projectiles);
        renderer.draw(cursor);
        renderer.draw(room.getWalls());
        renderer.draw(room.getDoors());
        renderer.draw(enemies);
        renderer.draw(props);
        renderer.draw(player);

        // UI
        // live points
        float widthLP = (float) Window.dim.x / 4f;
        Vector2i differ = Window.getDifferF();
        renderer.fillRect(new Vector2f(Window.dim.x / 2f - widthLP + differ.x / 2f, Window.dim.y / 2f - 25f + differ.y / 2f).sub(camera.getPosition()), new Vector2f(widthLP, 25), new Vector4f(1, 0, 0, 1));
        renderer.fillRect(new Vector2f(Window.dim.x / 2f - widthLP + differ.x / 2f, Window.dim.y / 2f - 25f + differ.y / 2f).sub(camera.getPosition()), new Vector2f(widthLP * ((float) player.getLP() / player.getMaxLP()), 25), new Vector4f(0, 1, 0, 1));

        renderer.drawUI(bg);
        UI.draw();
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

        if (key == GLFW_KEY_Q && action == GLFW_PRESS)
            player.toggleAutoshooting();

        // apply velocity
        player.setVelocity(new Vector2f(0, 0));
        if (keyArr[0] == GLFW_KEY_W)
            player.accelerate(new Vector2f(0, 1));
        if (keyArr[1] == GLFW_KEY_A)
            player.accelerate(new Vector2f(-1, 0));
        if (keyArr[2] == GLFW_KEY_S)
            player.accelerate(new Vector2f(0, -1));
        if (keyArr[3] == GLFW_KEY_D)
            player.accelerate(new Vector2f(1, 0));

        // update animation
        if (keyArr[0] == GLFW_KEY_W)
            player.switchAnimation("walkUp");
        if (keyArr[1] == GLFW_KEY_A)
            player.switchAnimation("walkLeft");
        if (keyArr[2] == GLFW_KEY_S)
            player.switchAnimation("walkDown");
        if (keyArr[3] == GLFW_KEY_D)
            player.switchAnimation("walkRight");

        // Dash
        if (key == GLFW_KEY_SPACE && action == GLFW_PRESS) {
            // iterate over all abilities
            ArrayList<Ability> abilities = player.getAbilities();
            for (int i = 0; i < abilities.size(); i++) {
                Ability a = abilities.get(i);
                // get the first dash ability
                if (a.getName().equals("Dash")) {
                    // that's off cooldown
                    if(a.getCurrentCooldown() <= (a.getCooldown()*2 - a.stats.get("cooldown"))) {
                        // and trigger it
                        a.setCurrentCooldown(0);
                        break;
                    }
                }
            }
        }

        // kill all enemies
        if (key == GLFW_KEY_K && action == GLFW_PRESS) {
            enemies.clear();
        }

    }

    public Room getRoom() {
        return room;
    }
    public EnemySpawner getSpawner() {
        return spawner;
    }

    public void setRoom(Room room) {
        this.room = room;
    }
    public Player getPlayer() {
        return player;
    }
    public void setShouldSimulate(boolean shouldSimulate) {
        this.shouldSimulate = shouldSimulate;
    }
    public void setShouldAdvanceFloor(boolean shouldAdvanceFloor) {
       this.shouldAdvanceFloor = shouldAdvanceFloor;
    }
}

