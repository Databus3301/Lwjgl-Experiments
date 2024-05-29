package Game.Entities.Dungeon;

import Audio.AudioSource;
import Game.Entities.Player;
import Render.Window;
import Tests.Test;
import org.joml.Vector2f;

public class Dungeon { // TODO: load settings file
    public static float SCALE = 2.5f * Window.getDifferP1920().length();
    // DEFAULTS
    public static final int DEFAULT_DEPTH = 5;
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

    private int rc = 1;

    private final AudioSource asMusic = new AudioSource();
    private boolean startedPlaying = false;

    public Dungeon(Player player, Test scene) {
        this(player, scene, DEFAULT_DEPTH);
    }
    public Dungeon(Player player, Test scene, int depth) {
        this.scene = scene;
        this.player = player;
        this.depth = depth;
        asMusic.setVolume(MUSIC_VOLUME);

        start = new Room(player, RoomType.START, "Start", DEFAULT_START_CONNECTIONS, RoomDesign.STONE, this, new Vector2f(10, 10));
        start.setConnectedRooms(generate(depth, DEFAULT_MAX_DOORS, DEFAULT_START_CONNECTIONS));
        System.out.println("Room count: " + rc);
    }

    private Room[] generate(int depth, int maxDoors, int connections) {
        Vector2f dim = new Vector2f((int) (Math.random() * (MAX_ROOM_SIZE.x - MIN_ROOM_SIZE.x) + MIN_ROOM_SIZE.x), (int) (Math.random() * (MAX_ROOM_SIZE.y - MIN_ROOM_SIZE.y) + MIN_ROOM_SIZE.y));

        if(depth <= 0) {
            rc++;
            return new Room[]{new Room(player, RoomType.BOSS, "End", 0, RoomDesign.STONE, this, dim)};
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
            RoomType type = rndmRoomType();
            //RoomType type = RoomType.SMITH;
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


    public void setStartedPlaying(boolean startedPlaying) {
        this.startedPlaying = startedPlaying;
    }
}
