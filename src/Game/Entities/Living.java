package Game.Entities;

import Render.Entity.Entity2D;
import Render.MeshData.Model.ObjModel;
import Render.MeshData.Shader.Shader;
import Render.MeshData.Texturing.Texture;
import org.joml.Vector2f;

public class Living extends Entity2D {
    protected float LP;
    protected int maxLP;
    protected float iSeconds;
    protected float currentIseconds;

    public Living(Vector2f position, ObjModel model, Texture texture, Shader shader) {
        super(position, model, texture, shader);
        LP = 100;
        maxLP = 100;
        iSeconds = 60;
        currentIseconds = 0;
    }
    public Living(Vector2f position) {
        super(position);
        LP = 100;
        maxLP = 100;
        iSeconds = 60;
        currentIseconds = 0;
    }
    public Living() {
        super();
        LP = 100;
        maxLP = 100;
        iSeconds = 60;
        currentIseconds = 0;
    }



    public boolean damage() {
        return damage(1);
    }
    public void heal() {
        LP++;
    }
    public boolean damage(float damage) {
        if(currentIseconds == 0) {
            LP -= damage;
            currentIseconds = iSeconds;
            return true;
        }
        return false;
    }
    public void heal(int heal) {
        LP += heal;
    }

    public void reduceISeconds() {
        if (currentIseconds > 0) currentIseconds -= 0.001f; //reduce by 1ms
    }
    public void reduceISeconds(float dt) {
        if (currentIseconds > 0) currentIseconds -= dt; //reduce by dt
    }

    public float getLP() {
        return LP;
    }
    public int getMaxLP() {
        return maxLP;
    }
    public float getiSeconds() {
        return iSeconds;
    }
    public float getCurrentIseconds() {
        return currentIseconds;
    }

    public void setLP(float LP) {
        this.LP = LP;
    }
    public void setMaxLP(int maxLP) {
        this.maxLP = maxLP;
    }
    public void setiSeconds(float iSeconds) {
        this.iSeconds = iSeconds;
    }
    public void setCurrentIseconds(float currentIseconds) {
        this.currentIseconds = currentIseconds;
    }
}
