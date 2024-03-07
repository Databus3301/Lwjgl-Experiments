package Render.Entity;

import Render.Entity.Texturing.Texture;
import Render.Shader.Shader;
import Render.Vertices.Model.ObjModel;
import Render.Window;
import org.joml.Vector2f;

public class Enemy extends Entity2D{
    private int iframes = 0;
    private float health;
    public Enemy (Vector2f position, ObjModel model, Texture texture, Shader shader, float health) {
        super(position, model, texture, shader);
        this.health = health;
    }

    public float getHealth() {
        return health;
    }
    public void setHealth(float health) {
        this.health = health;
    }

    public int getIframes() {
        return iframes;
    }
    public void reduceIframes() {
        if (iframes > 0)
            iframes--;
    }
    public void setIframes(int iframes) {
        this.iframes = iframes;
    }

    public Vector2f worldToCell(Vector2f worldPos, Vector2f cellSize) {
        return new Vector2f((int) ((worldPos.x + 10000) / cellSize.x), (int) ((worldPos.y + 10000) / cellSize.y));
    }

    public int cellToHash(Vector2f cellPos) {
        return (int) (cellPos.x * 3301 + cellPos.y * 1097);
    }
}
