package Game.Entities.Dungeon;

import Game.Entities.Player;
import Tests.Test;

public class Dungeon {
    public static final float SCALE = 1.2f;
    private final Test scene;

    private Player player;
    private Room start;

    private final int depth;

    private int rc = 1;

    public Dungeon(Player player, Test scene) {
        this(player, scene, 5);
    }
    public Dungeon(Player player, Test scene, int depth) {
        this.scene = scene;
        this.player = player;
        this.depth = depth;

        start = new Room(player, RoomType.START, "Start", 2, RoomDesign.STONE, this);
        start.setConnectedRooms(generate(depth, 4, 2));
        System.out.println("Room count: " + rc);
    }

    private Room[] generate(int depth, int maxDoors, int connections) {
        if(depth <= 0) {
            rc++;
            return new Room[]{new Room(player, RoomType.END, "End", 0, RoomDesign.STONE, this)};
        }

        int newDoors = (int) (Math.random() * (maxDoors-1) + 2);
        if(depth == 1)
            newDoors = 1;

        System.out.println("Depth: " + "*".repeat(depth) + "\t\tnewDoors: " + newDoors + "  connections: " + connections);


        Room[] rooms = new Room[connections];
        for (int j = 0; j < connections; j++) {
            // generate random design
            RoomDesign design = RoomDesign.values()[(int) (Math.random() * RoomDesign.values().length)];
            // generate random type
            //RoomType type = RoomType.values()[(int) (Math.random() * (RoomType.values().length-1)+1)];
            RoomType type = RoomType.NORMAL;
            String name = "Room " + depth + "-" + j;

            rooms[j] = new Room(player, type, name, newDoors, design, this);
            rooms[j].setDepth(depth);
            rc++;
        }

        for (Room room : rooms) {
            room.setConnectedRooms(generate(depth - 1, maxDoors, newDoors));
        }

        return rooms;
    }
    public Room generate() {
        start = new Room(player, RoomType.START, "Start", 1, RoomDesign.STONE, this);
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
