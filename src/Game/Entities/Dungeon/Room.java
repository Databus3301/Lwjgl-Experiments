package Game.Entities.Dungeon;

import Game.Action.Ability;
import Game.Action.Waves.EnemySpawner;
import Game.Action.Waves.Wave;
import Game.Entities.Enemies;
import Game.Entities.Enemy;
import Game.Entities.Player;
import Game.Entities.Projectiles.Projectile;
import Render.Entity.Entity2D;
import Render.Entity.Interactable.Interactable;
import Render.MeshData.Model.ObjModel;
import Render.MeshData.Shader.Shader;
import Render.MeshData.Texturing.Texture;
import Render.Renderer;
import Render.Window;
import Tests.Test;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.function.BiConsumer;

import static Game.Action.Waves.EnemySpawner.Result.*;
import static Tests.Test.renderer;

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


    private Vector2f position = new Vector2f();
    private Vector2f dimensions;
    private final Vector4f collisionRect;


    private final int numOfDoors;
    private Door[] doors;
    private Room[] connectedRooms;
    private final ArrayList<Entity2D> walls;
    private final ArrayList<Interactable> contents;

    public Room(Player player, Dungeon.RoomType type, String title, int numOfDoors, Dungeon.RoomDesign design, Dungeon dungeon) {
        assert numOfDoors >= 0 : "A room can't have a negative number of doors";

        this.dungeon = dungeon;
        this.type = type;
        this.title = title;
        this.numOfDoors = numOfDoors;
        this.design = design;
        this.dimensions = new Vector2f(20, 10);
        this.onSwitch = (p, e) -> {
            System.out.println("Switched to room: " + title);
        };

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
            wall.setPosition(new Vector2f(i * scale * 2 + position.x  -offset.x, position.y -offset.y));
            walls.add(wall);

            Entity2D wall2 = wall.clone();
            wall2.translate(0, (dimensions.y - 1) * scale * 2);
            wall2.rotate(180, 2);
            walls.add(wall2);

            boolean door;
            door = (doorIndices.length > 0 && doorIndex < doorIndices.length && i == doorIndices[doorIndex]);
            if (door) {
                doors[doorIndex].setPosition(new Vector2f(i * scale * 2 + position.x - offset.x, position.y + (dimensions.y - 1) * scale * 2 - offset.y));
                doors[doorIndex].lock();
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

    public void onSwitch(Player player, ArrayList<Enemy> enemies, EnemySpawner spawner) {
        // change contents to inlcude interactables like blacksmith, shop, etc.
        // call specific room callback
        switch (type) {
            case START -> {
                renderer.setPostProcessingShader(new Shader("post_processing.shader"));

                for(Door d : doors) {
                    d.open();
                }
            }
            case END -> {
            }
            case BOSS -> {
                // add boss to room
            }
            case SHOP -> {
                // add shop to room
            }
            case SMITH -> {
                // add blacksmith to room
            }
            case NORMAL -> {
                // init enemy Spawner to appropriate wave
                //spawner.setProbabilityDistribution(new float[]{0.5f, 0.5f});

                System.out.println((1f/(depth*depth)) * dungeon.getDepth() * dungeon.getDepth() * dungeon.getDepth() * dungeon.getDepth()); // TODO: better wave generation
                Wave w = new Wave(depth, (int)((1f/(depth*depth)) * dungeon.getDepth() * dungeon.getDepth() * dungeon.getDepth() * dungeon.getDepth()), 0.5f);
                spawner.setCurrentWave(w);

            }
        }
        onSwitch.accept(player, enemies);
    }

    public Room update(float dt, EnemySpawner spawner, Player player, ArrayList<Enemy> enemies, ArrayList<Projectile> projectiles) {

        if (spawner.getLastResult() == WAVE_OVER) { // TODO: update WAVE_OVER status to signal when all enemies are killed not spawned
            for (Door door : doors) {
                door.open();
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
                spawner.getCurrentWave().setWaveOver(false);
                renderer.setPostProcessingShader(new Shader("texturing_plain.shader"));

                // init new room
                room.onSwitch(player, enemies, spawner); // TODO: IMPLEMENT THIS
                break;
            }
        }
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
}
