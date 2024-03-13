package Render.Entity;

import Render.Entity.Texturing.Texture;
import Render.Shader.Shader;
import Render.Vertices.Model.ObjModel;
import Render.Window;
import org.joml.Vector2f;
import org.joml.Vector2fc;

public class Projectile extends Entity2D {
    private Entity2D owner;
    private float dmg;
    private float armorPen;

    public Projectile(Entity2D owner, float dmg, Shader shader, ObjModel model, Texture texture) {
        super(owner.getPosition(), model, texture, shader);
        this.owner = owner;
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
