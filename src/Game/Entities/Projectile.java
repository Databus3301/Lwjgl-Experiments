package Game.Entities;

import Render.Entity.Entity2D;
import Render.MeshData.Texturing.Texture;
import Render.MeshData.Shader.Shader;
import Render.MeshData.Model.ObjModel;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class Projectile extends Entity2D {
    private Entity2D owner;
    private float dmg;
    private float armorPen;
    private int pierce;

    private BiConsumer<Projectile, Boolean> onHit;
    private Consumer<Projectile> onUpdate;

    public Projectile(Entity2D owner, float dmg, Shader shader, ObjModel model, Texture texture) {
        super(owner.getPosition(), model, texture, shader);
        this.owner = owner;
        this.dmg = dmg;
    }
    public Projectile(Entity2D owner, int dmg) {
        super(owner.getPosition());
        this.dmg = dmg;
    }
    public Projectile(Player owner, int dmg) {
        this(owner.getEntity(), dmg);
    }
    public Projectile(Player owner) {
        this(owner.getEntity());
    }
    public Projectile(Entity2D owner) {
        super(owner.getPosition());
        this.owner = owner;
        this.dmg = 100;
        this.model = ObjModel.SQUARE.clone();
        this.shader = Shader.TEXTURING;
        this.pierce = 1;
    }
    public Projectile() {
        super();
        this.owner = this;
        this.dmg = 100;
        this.model = ObjModel.SQUARE.clone();
        this.shader = Shader.TEXTURING;
        this.pierce = 1;
    }

    public Projectile instantiate() {
        Projectile projectile = new Projectile(owner, dmg, shader, model, texture);
        projectile.setScale(scale);
        projectile.setVelocity(velocity);
        projectile.setPierce(pierce);
        // TODO: properly clone this by copying more (Entity2D) fields
        return projectile;
    }

    public void update(float dt) {
        if (this.onUpdate != null)
            this.onUpdate.accept(this);
        translate(velocity.mul(dt, new Vector2f()));
    }
    public <T extends Living> void collide(ArrayList<T> entities) {
        for (T collider : entities) {
            if (collider.collideRect(this)) {
                boolean gotDamaged = collider.damage(dmg);
                if(gotDamaged) pierce--;
                if(onHit != null) onHit.accept(this, gotDamaged);
                if (pierce <= 0) return;
            }
        }
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
    public int getPierce() {
        return pierce;
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
    public void setOnHit(BiConsumer<Projectile, Boolean> onHit) {
        this.onHit = onHit;
    }
    public void setOnUpdate(Consumer<Projectile> onUpdate) {
        this.onUpdate = onUpdate;
    }
    public void setPierce(int pierce) {
        this.pierce = pierce;
    }
}