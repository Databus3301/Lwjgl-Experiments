package Game.Entities.Dungeon;

import Render.Entity.Entity2D;
import Render.Entity.Interactable.Interactable;
import Render.MeshData.Model.ObjModel;
import Render.MeshData.Shader.Shader;
import Render.MeshData.Texturing.Texture;
import Tests.Test;

import static org.lwjgl.glfw.GLFW.*;

public class Door extends Interactable {

    private Entity2D connectedRoomDisplay;
    private Room connectedRoom;

    private final Texture lockedT, unlockedT;
    private boolean isLocked, isOpen = false;

    public <T extends Test> Door(T scene) {
        super(scene);
        setModel(ObjModel.SQUARE.clone());

        setTexture(lockedT = new Texture("rooms/door/locked.png"));
        unlockedT = new Texture("rooms/door/unlocked.png");
        setShader(Shader.TEXTURING);

        scale(32 * Dungeon.SCALE);
        float aspect = texture.getAspect();
        scale.y = scale.x * aspect;

        connectedRoomDisplay = new Entity2D();
        connectedRoomDisplay.setModel(ObjModel.SQUARE.clone());
        connectedRoomDisplay.scale(32, 32);
        connectedRoomDisplay.getScale().mul(Dungeon.SCALE);
        connectedRoomDisplay.setTexture(new Texture("rooms/door/signs/" + Dungeon.RoomType.NORMAL.getTextureName()));



        setTriggerDistance(80);
        setKeyCallback((door, key, scancode, action, mousePos) -> {

            if(key == GLFW_KEY_E && action == GLFW_PRESS) {
                float smallestDist;
                // use triggerPos to determine the closest point to the door
                smallestDist = triggerPos.stream().map(pos -> pos.distanceSquared(getPosition())).min(Float::compareTo).orElse(Float.MAX_VALUE);

                if(smallestDist < getTriggerDistanceSquared()) {
                    if (isLocked)
                        unlock();
                    else
                        lock();
                }
            }
        });
    }


    public void lock() {
        setTexture(lockedT);
        isLocked = true;
        isOpen = false;
    }
    public void unlock() {
        setTexture(unlockedT);
        isLocked = false;
    }

    // TODO: visual indicators
    public void open() {
        isOpen = true;
    }
    public void close() {

        isOpen = false;
    }


    public Entity2D getConnectedRoomDisplay() {
        return connectedRoomDisplay;
    }
    public Room getConnectedRoom() {
        return connectedRoom;
    }
    public boolean isLocked() {
        return isLocked;
    }
    public boolean isOpen() {
        return isOpen;
    }

    public void setConnectedRoomDisplay(Dungeon.RoomType connectedRoomDisplay) {
        this.connectedRoomDisplay = new Entity2D();
        this.connectedRoomDisplay.setModel(ObjModel.SQUARE.clone());
        this.connectedRoomDisplay.scale(32, 32);
        this.connectedRoomDisplay.getScale().mul(Dungeon.SCALE);
        this.connectedRoomDisplay.setTexture(new Texture("rooms/door/signs/" + connectedRoomDisplay.getTextureName()));
    }
    public void setConnectedRoomDisplay(Entity2D connectedRoomDisplay) {
        this.connectedRoomDisplay = connectedRoomDisplay;
    }
    public void setConnectedRoom(Room connectedRoom) {
        this.connectedRoom = connectedRoom;
    }
}
