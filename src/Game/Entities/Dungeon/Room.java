package Game.Entities.Dungeon;

import Audio.AudioSource;
import Game.Action.Ability;
import Game.Action.Waves.EnemySpawner;
import Game.Action.Waves.Wave;
import Game.Entities.Enemies;
import Game.Entities.Enemy;
import Game.Entities.Player;
import Game.Entities.Projectiles.Projectile;
import Game.UI;
import Render.Entity.Entity2D;
import Render.Entity.Interactable.Interactable;
import Render.MeshData.Model.ObjModel;
import Render.MeshData.Shader.Shader;
import Render.MeshData.Texturing.Animation;
import Render.MeshData.Texturing.Texture;
import Render.MeshData.Texturing.TextureAtlas;
import Tests.TestGame;
import org.joml.Math;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.function.BiConsumer;

import static Game.Action.Waves.EnemySpawner.Result.*;
import static Tests.Test.renderer;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_E;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;

/**
 * A room manages the entities and the logic of a game level.
 * It is the parent class for all room types the player might encounter in the dungeon.
 * It is managed by a Dungeon object providing Wave definitions, Routes to other rooms and a reference to the player + scene.
 */
public class Room {

    private final Dungeon dungeon;
    private BiConsumer<Player, ArrayList<Enemy>> onSwitch;

    private final String title;
    private final Dungeon.RoomType type;
    private Dungeon.RoomDesign design;
    private int depth;
    private int floor;


    private Vector2f position = new Vector2f();
    private Vector2f dimensions;
    private final Vector4f collisionRect;


    private final int numOfDoors;
    private Door[] doors;
    private Room[] connectedRooms;
    private final ArrayList<Entity2D> walls;
    private final ArrayList<Interactable> contents;

    private final AudioSource[] audios = new AudioSource[5];


    public Room(Player player, Dungeon.RoomType type, String title, int numOfDoors, Dungeon.RoomDesign design, Dungeon dungeon, Vector2f dimensions, int floor) {
        assert numOfDoors >= 0 : "A room can't have a negative number of doors";

        this.dungeon = dungeon;
        this.type = type;
        this.title = title;
        this.floor = floor;
        this.numOfDoors = numOfDoors;
        this.design = design;
        this.dimensions = dimensions;
        this.onSwitch = (p, e) -> {
            System.out.println("Switched to room: " + title + " - " + type);
        };
        for (int i = 0; i < audios.length; i++) {
            audios[i] = new AudioSource();
            audios[i].setVolume(Dungeon.EFFECT_VOLUME);
        }

        contents = new ArrayList<>();
        walls = new ArrayList<>((int) (dimensions.x + dimensions.y) * 2 + numOfDoors + 3);
        float scale = 32 * Dungeon.SCALE;

        // calc offset -> to have <position> be centered
        Vector2f offset = new Vector2f((dimensions.x) * scale, (dimensions.y) * scale);
        // create collision rect -> safe on individual wall collisions
        collisionRect = new Vector4f(position.x + scale - offset.x, position.y + scale - offset.y, (dimensions.x-2) * scale * 2, (dimensions.y-2) * scale * 2);

        //create doors
        this.doors = new Door[numOfDoors];
        for (int i = 0; i < numOfDoors; i++) {
            doors[i] = new Door(dungeon.getScene());
        }
        addDoorTrigger(player);
        // equally spread out the doors along the x-axis
        int[] doorIndices = new int[doors.length];
        int doorIndex = 0;
        float spacing = dimensions.x / (doorIndices.length + 1f);
        for (int i = 1; i < doorIndices.length + 1; i++) {
            doorIndices[i - 1] = (int) (spacing * i);
        }

        for (int i = 1; i < dimensions.x - 1; i++) {
            Entity2D wall = new Entity2D();
            wall.setModel(ObjModel.SQUARE.clone());
            wall.setShader(Shader.TEXTURING);
            wall.scale(scale);
            wall.rotate(-90, 2);
            wall.setTexture(new Texture("rooms/" + design.getDesign() + "Wall" + (i % 2 == 0 ? 1 : 2) + ".png"));
            wall.setPosition(new Vector2f(i * scale * 2 + position.x - offset.x, position.y - offset.y));
            walls.add(wall);

            Entity2D wall2 = wall.clone();
            wall2.translate(0, (dimensions.y - 1) * scale * 2);
            wall2.rotate(180, 2);
            walls.add(wall2);

            boolean door;
            door = (doorIndices.length > 0 && doorIndex < doorIndices.length && i == doorIndices[doorIndex]);
            if (door) {
                doors[doorIndex].setPosition(new Vector2f(i * scale * 2 + position.x - offset.x, position.y + (dimensions.y - 1) * scale * 2 - offset.y));
                doors[doorIndex].close();
                //walls.remove(walls.size()-1);
                walls.add(doors[doorIndex++]);
            }
        }

        for (int i = 1; i < dimensions.y - 1; i++) {
            Entity2D wall = new Entity2D();
            wall.setModel(ObjModel.SQUARE.clone());
            wall.setShader(Shader.TEXTURING);
            wall.scale(scale);
            wall.setTexture(new Texture("rooms/" + design.getDesign() + "Wall" + (i % 2 == 0 ? 1 : 2) + ".png"));
            wall.setPosition(new Vector2f(position.x - offset.x, i * scale * 2 + position.y - offset.y));
            wall.rotate(180, 2);
            wall.setStatic(true);
            walls.add(wall);

            Entity2D wall2 = wall.clone();
            wall2.translate((dimensions.x - 1) * scale * 2, 0);
            wall2.rotate(180, 2);
            walls.add(wall2);
        }

        // add corner tiles
        Entity2D corner = new Entity2D();
        corner.setModel(ObjModel.SQUARE.clone());
        corner.setShader(Shader.TEXTURING);
        corner.scale(scale);
        corner.setTexture(new Texture("rooms/" + design.getDesign() + "Corner.png"));
        corner.setPosition(new Vector2f(position.x - offset.x, position.y - offset.y));
        corner.rotate(180, 2);
        walls.add(corner);

        corner = corner.clone();
        corner.translate((dimensions.x - 1) * scale * 2, 0);
        corner.rotate(90, 2);
        walls.add(corner);

        corner = corner.clone();
        corner.translate(0, (dimensions.y - 1) * scale * 2);
        corner.rotate(90, 2);
        walls.add(corner);

        corner = corner.clone();
        corner.translate(-(dimensions.x - 1) * scale * 2, 0);
        corner.rotate(90, 2);
        walls.add(corner);

    }

    public void onSwitch(Player player, ArrayList<Enemy> enemies, EnemySpawner spawner, ArrayList<Interactable> props) {
        // change contents to include interactables like blacksmith, shop, etc.
        // call specific room callback
        switch (type) {
            case START -> {
                renderer.setPostProcessingShader(new Shader("post_processing.shader"));
                player.setAutoshooting(false);

                audios[0].playSound("weird.wav");
                audios[1].playSound("heartbeat.wav");
            }
            case BOSS -> {
                // add boss to room
                player.setAutoshooting(true);
                Enemy b = Enemies.getBOSS();
                enemies.add(b);
                b.setPosition(position.x, position.y + 128 * Dungeon.SCALE);
                b.translate(1,1);

                audios[2].playSound("ghast2.wav");

            }
            case SMITH -> {
                player.setAutoshooting(false);
                for(Door d : doors) {
                    d.open();
                }

                // add blacksmith to room
                TextureAtlas smithAtlas = new TextureAtlas(new Texture("SmithAnim.png", 0), 3, 1, 3, 32, 32, 0);
                Animation smithIdle = new Animation(smithAtlas, 0, 0, 3, 10);

                Interactable smith = new Interactable(dungeon.getScene());
                smith.setModel(ObjModel.SQUARE.clone());
                smith.setShader(Shader.TEXTURING);
                smith.setAnimation(smithIdle);
                smith.scale(32 * Dungeon.SCALE * 1.2f);
                smith.setPosition(position);
                smith.setStatic(true);
                smith.setKeyCallback((interactable, key, scancode, action, mousePos) -> {
                    // open shop
                    if(key == GLFW_KEY_E && action == GLFW_PRESS)
                        System.out.println("Smithy");
                });

                props.add(smith);

                // add anvil to room
                Interactable anvil = new Interactable(dungeon.getScene());
                anvil.setModel(ObjModel.SQUARE.clone());
                anvil.setShader(Shader.TEXTURING);
                anvil.setTexture(new Texture("anvil.png"));
                anvil.scale(32 * Dungeon.SCALE * 1.2f);
                anvil.setPosition(position.x + 12 * Dungeon.SCALE, position.y - 26 * Dungeon.SCALE);
                anvil.setStatic(true);

                props.add(anvil);

            }
            case NORMAL -> {
                // init enemy Spawner to appropriate wave
                //spawner.setProbabilityDistribution(new float[]{0.5f, 0.5f});


                // TODO: better wave generation
                Wave w = new Wave(depth, (int)((1f/(depth*depth)) * dungeon.getDepth() * dungeon.getDepth() * dungeon.getDepth()), 0.5f);
                spawner.setCurrentWave(w);

                player.setAutoshooting(true);
            }
        }
        onSwitch.accept(player, enemies);
    }

    public Room update(float dt, EnemySpawner spawner, Player player, ArrayList<Enemy> enemies, ArrayList<Projectile> projectiles, ArrayList<Interactable> props) {

        if (spawner.getLastResult() == WAVE_OVER) {
            for (Door door : doors) {
                door.open();
            }
        }

        // animate door opening based on audio playback progress
        if(type == Dungeon.RoomType.START) {
            if(audios[0] != null) {
                if (audios[0].getPlaybackPercentage() > 0.6f) {
                    for (Door d : doors) {
                        d.setToHalfClosedT();
                    }
                }
                if (!audios[0].isPlaying()) {
                    for (Door d : doors) {
                        d.open();
                    }
                }
            }
        }


        if (type == Dungeon.RoomType.BOSS) {
            if (enemies.isEmpty() && props.isEmpty()) {
                Interactable stairs = new Interactable(dungeon.getScene());
                stairs.setModel(ObjModel.SQUARE.clone());
                stairs.setShader(Shader.TEXTURING);
                stairs.setTexture(new Texture("stairs.png"));
                stairs.scale(32 * Dungeon.SCALE);
                stairs.setPosition(position.x, position.y);
                stairs.setTriggerDistance(110f);

                stairs.setKeyCallback((interactable, key, scancode, action, mousePos) -> {
                    if (key == GLFW_KEY_E && action == GLFW_PRESS) {
                        if(player.getPosition().distanceSquared(interactable.getPosition()) < interactable.getTriggerDistanceSquared())
                         ((TestGame)dungeon.getScene()).setShouldAdvanceFloor(true);
                    }
                });

                props.add(stairs);


                // health collectible healing player by half max lp add to props
                // add health collectible to room
                Interactable health = new Interactable(dungeon.getScene());
                health.setModel(ObjModel.SQUARE.clone());
                health.setShader(Shader.TEXTURING);
                //health.setTexture(new Texture("health.png"));
                health.setColor(0, 1, 0, 1);
                health.scale(8 * Dungeon.SCALE);
                health.setPosition(position.x + (float)(32 * Math.random()), position.y + (float)(32 * Math.random() + 96 * Dungeon.SCALE));
                health.setTriggerDistance(75f);

                health.setKeyCallback((interactable, key, scancode, action, mousePos) -> {
                    if (key == GLFW_KEY_E && action == GLFW_PRESS) {
                        if (player.getPosition().distanceSquared(interactable.getPosition()) < interactable.getTriggerDistanceSquared()) {
                            System.out.println(player.getPosition().distanceSquared(interactable.getPosition()));
                            player.heal(player.getMaxLP() / 2);
                            interactable.setColor(0.2f, 0.2f, 0.2f, 1);
                            interactable.setKeyCallback((i, k, s, a, m) -> {});
                        }
                    }

                });

                props.add(health);

            }
        }

        Room room = this;
        for(int i = 0; i< doors.length; i++) {
            if(!doors[i].isOpen()) continue;

            if(doors[i].collideRect(player.getCollider())) {
                room = doors[i].getConnectedRoom();
                player.setPosition(doors[i].getConnectedRoom().getPosition());
                // clean up scene
                for(Ability ability : player.getAbilities()) {
                    ability.getProjectiles().clear();
                }
                projectiles.clear();
                enemies.clear();
                props.clear();
                spawner.getCurrentWave().setFinishedSpawning(false);
                if(UI.getAbilityButtons() != null)
                    UI.getAbilityButtons()[0].release();
                renderer.setPostProcessingShader(new Shader("texturing_plain.shader"));

                for(AudioSource audio : audios) {
                    audio.cleanup();
                }
                // if you leave the start room, start the music
                if(this.type == Dungeon.RoomType.START) {
                    dungeon.getMusicAudioSource().playSound("normal_music.wav");
                    dungeon.setStartedPlaying(true);
                }

                // init new room
                room.onSwitch(player, enemies, spawner, props);
                break;
            }
        }
        if(!props.isEmpty())
            renderer.drawTriggerDistance(props.get(0));

        return room;
    }





    public ArrayList<Entity2D> getWalls() {
        return walls;
    }
    public Door[] getDoors() {
        return doors;
    }
    public Vector2f getDimensions() {
        return dimensions;
    }
    public String getTitle() {
        return title;
    }
    public Vector2f getPosition() {
        return position;
    }
    public Dungeon.RoomType getType() {
        return type;
    }
    public Room[] getConnectedRooms() {
        return connectedRooms;
    }
    public Vector4f getCollisionRect() {
        return collisionRect;
    }
    public int getDepth() {
        return depth;
    }


    public void setDimensions(Vector2f dimensions) {
        this.dimensions = dimensions;
    }
    public void setPosition(Vector2f position) {
        for (Entity2D wall : walls)
            wall.translate(new Vector2f(position).sub(this.position));

        this.position = position;
    }
    public void setDesign(Dungeon.RoomDesign design) {
        this.design = design;

        // update wall textures
        for (Entity2D wall : walls) {
            wall.setTexture(new Texture("rooms/" + design.getDesign() + "Wall" + (wall.getPosition().x % 2 == 0 ? 1 : 2) + ".png"));
        }

    }
    public void setDoors(Door[] doors) {
        assert doors.length == numOfDoors : "The number of doors must match the number of doors set in the constructor";
        this.doors = doors;
    }
    public void setOnSwitch(BiConsumer<Player, ArrayList<Enemy>> onSwitch) {
        this.onSwitch = onSwitch;
    }
    public void addDoorTrigger(Entity2D trigger) {
        for(Door door : doors) {
            door.addTriggerPos(trigger.getPosition());
        }
    }
    public void setConnectedRooms(Room[] connectedRooms) {
        assert connectedRooms.length == numOfDoors : "The number of connected rooms must match the number of doors set in the constructor → " + numOfDoors + " != " + connectedRooms.length;
        this.connectedRooms = connectedRooms;

        for (int i = 0; i < doors.length; i++) {
            doors[i].setConnectedRoomDisplay(connectedRooms[i].getType());
            doors[i].setConnectedRoom(connectedRooms[i]);
        }
    }
    public void setDepth(int depth) {
        this.depth = depth;
    }
    public float getWitdh() {
        return dimensions.x*32*Dungeon.SCALE;
    }
    public float getHeight() {
        return dimensions.y*32*Dungeon.SCALE;
    }

    public AudioSource[] getAudios() {
        return audios;
    }

}
