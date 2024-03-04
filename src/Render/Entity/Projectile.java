package Render.Entity;

import Render.Entity.Texturing.Texture;
import Render.Shader.Shader;
import Render.Vertices.Model.ObjModel;
import org.joml.Vector2f;

public class Projectile extends Entity2D {
    private Entity2D owner;

    private float dmg;
    private float armorPen;

    public Projectile(Vector2f position, ObjModel model, Texture texture, Shader shader, Entity2D owner, Vector2f velocity, float dmg) {
        super(position, model, texture, shader);
        this.owner = owner;
        this.velocity = velocity;
        this.dmg = dmg;
    }


    public Entity2D getOwner() {
        return owner;
    }
    public float getDmg() {
        return dmg;
    }
    public float getArmorPen() {
        return armorPen;
    }
    public void setDmg(float dmg) {
        this.dmg = dmg;
    }
    public void setOwner(Entity2D owner) {
        this.owner = owner;
    }
    public void setArmorPen(float armorPen) {
        this.armorPen = armorPen;
    }

}
