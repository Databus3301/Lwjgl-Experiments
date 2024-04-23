package Game.Entities.Dungeon;

import Render.Entity.Entity2D;
import Render.Entity.Interactable.Interactable;
import Render.MeshData.Model.ObjModel;
import Render.MeshData.Shader.Shader;
import Render.MeshData.Texturing.Texture;
import Tests.TestGame;
import org.joml.Vector2f;

import static Tests.Test.renderer;
import static org.lwjgl.glfw.GLFW.*;

public class Door extends Interactable {

    private Dungeon.RoomType type;
    public final Entity2D typeSign;

    public final Room connectedRoom;

    private final Texture lockedT, unlockedT;
    boolean locked = false;

    public <T extends TestGame> Door(T scene, Room connectedRoom) {
        super(scene);
        this.type = connectedRoom.getType();
        this.connectedRoom = connectedRoom;

        setModel(ObjModel.SQUARE.clone());
        scale(32, 48);
        scale.mul(Dungeon.SCALE);
        setTexture(lockedT = new Texture("rooms/door/locked.png"));
        unlockedT = new Texture("rooms/door/unlocked.png");
        setShader(Shader.TEXTURING);

        typeSign = new Entity2D();
        typeSign.setModel(ObjModel.SQUARE.clone());
        typeSign.scale(32, 32);
        typeSign.getScale().mul(Dungeon.SCALE);
        typeSign.setTexture(new Texture("rooms/door/signs/" + type.getTextureName()));

        setTriggerDistance(50);
        setKeyCallback((interactable, key, scancode, action, mousePos) -> {
            Vector2f pos = scene.getPlayer().getPosition();
            if(pos.distance(interactable.getPosition()) > interactable.getTriggerDistance())
                return;

            if(key == GLFW_KEY_E && action == GLFW_PRESS) {
                interactable.setColor(1, 0, 1, 1);
            }
            if(key == GLFW_KEY_E && action == GLFW_RELEASE) {
                interactable.setColor(1, 1, 1, 1);
            }
        });
    }





    public void lock() {
        setTexture(lockedT);
        locked = true;
    }
    public void open() {
        setTexture(unlockedT);
        locked = false;
    }
}
