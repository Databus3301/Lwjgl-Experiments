package Game.Entities;

import Render.Entity.Entity2D;
import Render.MeshData.Model.ObjModel;
import Render.MeshData.Shader.Shader;
import Render.MeshData.Texturing.Texture;
import org.joml.Quaternionf;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class Living extends Entity2D {
    protected float LP;
    protected int maxLP;
    protected float iSeconds;
    protected float currentIseconds;

    public Living(Vector2f position, ObjModel model, Texture texture, Shader shader) {
        super(position, model, texture, shader);
        LP = 100;
        maxLP = 100;
        iSeconds = 0.4f;
        currentIseconds = 0;
    }
    public Living(Vector2f position) {
        this(position, ObjModel.SQUARE.clone(), new Texture("res/textures/woodCrate.png", 0), Shader.TEXTURING);
    }
    public Living() {
        this(new Vector2f());
    }

    public boolean damage() {
        return damage(100);
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
        reduceISeconds(0.001f); //reduce by 1ms
    }
    public void reduceISeconds(float dt) {
        if (currentIseconds > 0f) currentIseconds -= dt; //reduce by dt
        else currentIseconds = 0;
    }

    public Living clone() {
        Living l = new Living();

        l.rotation = new Quaternionf(rotation);
        l.scale = new Vector2f(scale);
        l.velocity = new Vector2f(velocity);
        l.isStatic = isStatic;
        l.isHidden = isHidden;
        l.offset = new Vector2f(offset);
        l.color = new Vector4f(color);

        l.LP = LP;
        l.maxLP = maxLP;
        l.iSeconds = iSeconds;
        l.currentIseconds = currentIseconds;

        return l;
    }

    public float getLP() {
        return LP;
    }
    public int getMaxLP() {
        return maxLP;
    }
    public float getIseconds() {
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
