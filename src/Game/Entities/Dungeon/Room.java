package Game.Entities.Dungeon;

import Render.Entity.Entity2D;
import Render.MeshData.Model.ObjModel;
import Render.MeshData.Shader.Shader;
import Render.MeshData.Texturing.Texture;
import Tests.Test;
import org.joml.Vector2f;

import java.util.ArrayList;

/**
 * A room manages the entities and the logic of a game level.
 * It is the parent class for all room types the player might encounter in the dungeon.
 * It is managed by a Dungeon object providing Wave definitions, Routes to other rooms and a reference to the player + scene.
 */
public class Room {
    private final Test scene;
    private final Dungeon dungeon;
    private final Dungeon.RoomType type;
    private Dungeon.RoomDesign design;
    private final String title;

    private Vector2f position = new Vector2f();

    private Vector2f dimensions;
    private final ArrayList<Entity2D> walls;
    private final Door[] doors;

    public Room(Test scene, Dungeon dungeon, Dungeon.RoomType type, String title, Door[] doors) {
        this.dungeon = dungeon;
        this.type = type;
        this.title = title;
        this.scene = scene;
        this.doors = doors;
        this.design = Dungeon.RoomDesign.WATER;
        this.dimensions = new Vector2f(10, 10);

        walls = new ArrayList<>((int) (dimensions.x + dimensions.y)*2+5);
        float scale = 32 * Dungeon.SCALE;

        // equally spread out the doors along the x-axis (indices where not to draw normal walls, but doors instead)
        int[] doorIndices = new int[doors == null ? 0 : doors.length];
        int doorIndex = 0;
        float spacing = dimensions.x / (doorIndices.length + 1f);
        for (int i = 1; i < doorIndices.length+1; i++) {
            doorIndices[i-1] = (int) (spacing * i);
        }

        for(int i = 1; i < dimensions.x-1; i++) {
            Entity2D wall = new Entity2D();
            wall.setModel(ObjModel.SQUARE.clone());
            wall.setShader(Shader.TEXTURING);
            wall.scale(scale);
            wall.rotate(-90, 2);
            wall.setTexture(new Texture("rooms/" + design.getDesign() + "Wall" + (i % 2 == 0 ? 1 : 2) + ".png"));
            wall.setPosition(new Vector2f(i * scale*2 + position.x, position.y));
            walls.add(wall);

            Entity2D wall2 = wall.clone();
            wall2.translate(0, (dimensions.y-1)*scale*2);
            wall2.rotate(180, 2);
            walls.add(wall2);

            boolean door;
            door = (doorIndices.length > 0 &&  doorIndex < doorIndices.length && i == doorIndices[doorIndex]);
            if(door) {
                doors[doorIndex].setPosition(new Vector2f(i*scale*2 + position.x, position.y + (dimensions.y-1)*scale*2));
                doors[doorIndex].lock();
                //walls.remove(walls.size()-1);
                walls.add(doors[doorIndex++]);
            }
        }

        for(int i = 1; i < dimensions.y-1; i++) {
            Entity2D wall = new Entity2D();
            wall.setModel(ObjModel.SQUARE.clone());
            wall.setShader(Shader.TEXTURING);
            wall.scale(scale);
            wall.setTexture(new Texture("rooms/" + design.getDesign() + "Wall" + (i%2 == 0 ? 1 : 2) + ".png"));
            wall.setPosition(new Vector2f(position.x, i*scale*2 + position.y));
            wall.rotate(180, 2);
            walls.add(wall);

            Entity2D wall2 = wall.clone();
            wall2.translate((dimensions.x-1)*scale*2, 0);
            wall2.rotate(180, 2);
            walls.add(wall2);
        }

        // add corner tiles
        Entity2D corner = new Entity2D();
        corner.setModel(ObjModel.SQUARE.clone());
        corner.setShader(Shader.TEXTURING);
        corner.scale(scale);
        corner.setTexture(new Texture("rooms/" + design.getDesign() + "Corner.png"));
        corner.setPosition(new Vector2f(position.x, position.y));
        corner.rotate(180, 2);
        walls.add(corner);

        corner = corner.clone();
        corner.translate((dimensions.x-1)*scale*2, 0);
        corner.rotate(90, 2);
        walls.add(corner);

        corner = corner.clone();
        corner.translate(0, (dimensions.y-1)*scale*2);
        corner.rotate(90, 2);
        walls.add(corner);

        corner = corner.clone();
        corner.translate(-(dimensions.x-1)*scale*2, 0);
        corner.rotate(90, 2);
        walls.add(corner);

    }

    public void update(float dt) {

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

    public void setDimensions(Vector2f dimensions) {
        this.dimensions = dimensions;
    }
    public void setPosition(Vector2f position) {
        this.position = position;
    }
    public void setDesign(Dungeon.RoomDesign design) {
        this.design = design;
    }
}
