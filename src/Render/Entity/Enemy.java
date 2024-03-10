package Render.Entity;

import Render.Entity.Texturing.Texture;
import Render.Shader.Shader;
import Render.Vertices.Model.ObjModel;
import Render.Window;
import org.joml.Vector2f;

public class Enemy extends Entity2D{
    private int iframes = 0;
    private float health;
    private int collisions = 0;
    private int checks = 0;
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
        return new Vector2f((int) ((worldPos.x + 100000) / cellSize.x), (int) ((worldPos.y + 100000) / cellSize.y));
    }

    public Vector2f cellToWorld(Vector2f cellPos, Vector2f cellSize) {
        return new Vector2f(cellPos.x * cellSize.x - 100000, cellPos.y * cellSize.y - 100000);
    }

    public int cellToHash(Vector2f cellPos) {
        return (int) (cellPos.x * 3301 + cellPos.y * 1097);
    }

    public int getCollisions() {
        return collisions;
    }
    public int addCollision() {
        return collisions++;
    }

    public int getChecks() {
        return checks;
    }
    public int addCheck() {
        return checks++;
    }
}
