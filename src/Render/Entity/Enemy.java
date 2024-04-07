package Render.Entity;

import Render.MeshData.Texturing.Texture;
import Render.MeshData.Shader.Shader;
import Render.MeshData.Model.ObjModel;
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
        if (iframes > 0) iframes--;
    }
    public void setIframes(int iframes) {
        this.iframes = iframes;
    }
}
