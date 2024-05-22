package Game.Entities.Dungeon;

import Game.Entities.Player;
import Tests.Test;
import org.joml.Vector2f;

public class Dungeon { // TODO: load settings file
    public static final float SCALE = 1.2f;
    // DEFAULTS
    public static final int DEFAULT_DEPTH = 5;
    public static final int DEFAULT_MAX_DOORS = 4;
    public static final int DEFAULT_START_CONNECTIONS = 2;
    public static final int DEFAULT_MIN_DOORS = 2;
    public static final Vector2f MIN_ROOM_SIZE = new Vector2f(8, 8);
    public static final Vector2f MAX_ROOM_SIZE = new Vector2f(16, 16);

    public static float ABILITY_VOLUME = 0.2f;
    public static float ENTITY_VOLUME = 0.5f;


    private final Test scene;

    private Player player;
    private Room start;

    private final int depth;

    private int rc = 1;

    public Dungeon(Player player, Test scene) {
        this(player, scene, DEFAULT_DEPTH);
    }
    public Dungeon(Player player, Test scene, int depth) {
        this.scene = scene;
        this.player = player;
        this.depth = depth;

        start = new Room(player, RoomType.START, "Start", DEFAULT_START_CONNECTIONS, RoomDesign.STONE, this, new Vector2f(10, 10));
        start.setConnectedRooms(generate(depth, DEFAULT_MAX_DOORS, DEFAULT_START_CONNECTIONS));
        System.out.println("Room count: " + rc);
    }

    private Room[] generate(int depth, int maxDoors, int connections) {
        Vector2f dim = new Vector2f((int) (Math.random() * (MAX_ROOM_SIZE.x - MIN_ROOM_SIZE.x) + MIN_ROOM_SIZE.x), (int) (Math.random() * (MAX_ROOM_SIZE.y - MIN_ROOM_SIZE.y) + MIN_ROOM_SIZE.y));

        if(depth <= 0) {
            rc++;
            return new Room[]{new Room(player, RoomType.END, "End", 0, RoomDesign.STONE, this, dim)};
        }

        int newDoors = (int) (Math.random() * (maxDoors-(DEFAULT_MIN_DOORS-1)) + DEFAULT_MIN_DOORS);
        if(depth == 1)
            newDoors = 1;

        //System.out.println("Depth: " + "*".repeat(depth) + "\t\tnewDoors: " + newDoors + "  connections: " + connections);

        Room[] rooms = new Room[connections];
        for (int j = 0; j < connections; j++) {
            // generate random design
            RoomDesign design = RoomDesign.values()[(int) (Math.random() * RoomDesign.values().length)];
            // generate random type
            //RoomType type = RoomType.values()[(int) (Math.random() * (RoomType.values().length-1)+1)];
            RoomType type = RoomType.NORMAL;
            String name = "Room " + depth + "-" + j;

            rooms[j] = new Room(player, type, name, newDoors, design, this, dim);
            rooms[j].setDepth(depth);
            rc++;
        }

        for (Room room : rooms) {
            room.setConnectedRooms(generate(depth - 1, maxDoors, newDoors));
        }

        return rooms;
    }
    public Room generate() {
        start = new Room(player, RoomType.START, "Start", 1, RoomDesign.STONE, this, new Vector2f(10, 10));
        start.setConnectedRooms(generate(5, 4, 1));
        return start;
    }

    public Room getStart() {
        return start;
    }
    public int getDepth() {
        return depth;
    }
    public Test getScene() {
        return scene;
    }

    public enum RoomType {
        START ("start.png"),
        END   ("end.png"),
        BOSS  ("boss.png"),
        SHOP  ("shop.png"),
        SMITH ("smith.png"),
        NORMAL("normal.png");

        private final String texture;

        RoomType(String texture) {
            this.texture = texture;
        }

        public String getTextureName() {
            return texture;
        }
    }
    public enum RoomDesign {
        STONE("Stone"),
        WATER("Water");


        private final String design;

        RoomDesign(String design) {
            this.design = design;
        }
        public String getDesign() {
            return design;
        }
    }




}
