package Game.Entities.Dungeon;

import Audio.AudioSource;
import Game.Entities.Player;
import Render.Window;
import Tests.Test;
import org.joml.Vector2f;

public class Dungeon { // TODO: load settings file
    public static float SCALE = 2.5f * Window.getDifferP1920().length();
    // DEFAULTS
    public static final int DEFAULT_DEPTH = 1; // 5
    public static final int DEFAULT_MAX_DOORS = 4;
    public static final int DEFAULT_START_CONNECTIONS = 2;
    public static final int DEFAULT_MIN_DOORS = 2;                          
    public static final Vector2f MIN_ROOM_SIZE = new Vector2f(8, 8);
    public static final Vector2f MAX_ROOM_SIZE = new Vector2f(16, 16);

    public static float EFFECT_VOLUME = 0.2f;
    public static float MUSIC_VOLUME = 0.2f;


    private final Test scene;

    private Player player;
    private Room start;

    private final int depth;
    private int floor = 1;

    private int rc = 1;

    private final AudioSource asMusic = new AudioSource();
    private boolean startedPlaying = false;

    public Dungeon(Player player, Test scene) {
        this(player, scene, DEFAULT_DEPTH);
    }
    public Dungeon(Player player, Test scene, int depth) {
        this(player, scene, depth, 1);
    }
    public Dungeon(Player player, Test scene, int depth, int floor) {
        this.scene = scene;
        this.player = player;
        this.depth = depth;
        this.floor = floor;
        asMusic.setVolume(MUSIC_VOLUME);

        start = new Room(player, RoomType.START, "Start", DEFAULT_START_CONNECTIONS, RoomDesign.values()[floor % RoomDesign.values().length], this, new Vector2f(10, 10), floor);
        start.setConnectedRooms(generate(depth, DEFAULT_MAX_DOORS, DEFAULT_START_CONNECTIONS));
        System.out.println("Room count: " + rc);
    }

    private Room[] generate(int depth, int maxDoors, int connections) {
        Vector2f dim = new Vector2f((int) (Math.random() * (MAX_ROOM_SIZE.x - MIN_ROOM_SIZE.x) + MIN_ROOM_SIZE.x), (int) (Math.random() * (MAX_ROOM_SIZE.y - MIN_ROOM_SIZE.y) + MIN_ROOM_SIZE.y));

        if(depth <= 0) {
            rc++;
            return new Room[]{new Room(player, RoomType.BOSS, "End", 0, RoomDesign.values()[floor % RoomDesign.values().length], this, dim.add(2, 2), floor)};
        }

        int newDoors = (int) (Math.random() * (maxDoors-(DEFAULT_MIN_DOORS-1)) + DEFAULT_MIN_DOORS);
        if(depth == 1)
            newDoors = 1;

        //System.out.println("Depth: " + "*".repeat(depth) + "\t\tnewDoors: " + newDoors + "  connections: " + connections);

        Room[] rooms = new Room[connections];
        for (int j = 0; j < connections; j++) {
            // generate random design
            RoomDesign design = RoomDesign.values()[floor % RoomDesign.values().length];
            // generate random type
            RoomType type = rndmRoomType();
            //RoomType type = RoomType.SMITH;
            String name = "Room " + depth + "-" + j;

            rooms[j] = new Room(player, type, name, newDoors, design, this, dim, floor);
            rooms[j].setDepth(depth);
            rc++;
        }

        for (Room room : rooms) {
            room.setConnectedRooms(generate(depth - 1, maxDoors, newDoors));
        }

        return rooms;
    }
    public Room generate() {
        start = new Room(player, RoomType.START, "Start", 1, RoomDesign.values()[floor % RoomDesign.values().length], this, new Vector2f(10, 10), floor);
        start.setConnectedRooms(generate(5, 4, 1));
        return start;
    }

    public Room generate(float depth, int maxDoors, int connections) {
        start = new Room(player, RoomType.START, "Start", 1, RoomDesign.values()[floor % RoomDesign.values().length], this, new Vector2f(10, 10), floor);
        start.setConnectedRooms(generate((int)depth, maxDoors, connections));
        return start;
    }

    public RoomType rndmRoomType() {
        // random room type excluding START and END,,, NORMAL should be the most common ↓
        RoomType[] type = new RoomType[(RoomType.values().length-2)          *          2];
        for(int i = 0; i < type.length; i++) {
            if(i < RoomType.values().length-2)
                type[i] =  RoomType.values()[i+2];
            else
                type[i] = RoomType.NORMAL;
        }
        return type[(int) (Math.random() * type.length)];
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
    public int getFloor() {
        return floor;
    }
    public AudioSource getMusicAudioSource() {
        return asMusic;
    }

    public boolean hasStartedPlayingMusic() {
        return startedPlaying;
    }

    public enum RoomType {
        START ("start.png"),
        BOSS  ("boss.png"),
        //SHOP  ("shop.png"),
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
        WATER("Water"),
        STONE("Stone");


        private final String design;

        RoomDesign(String design) {
            this.design = design;
        }
        public String getDesign() {
            return design;
        }
    }


    public void setStartedPlaying(boolean startedPlaying) {
        this.startedPlaying = startedPlaying;
    }
    public void setFloor(int floor) {
        this.floor = floor;
    }
}
