package Game.Entities.Projectiles;

import Game.Entities.Living;
import Render.Entity.Entity2D;
import Render.MeshData.Model.ObjModel;
import Render.MeshData.Shader.Shader;
import Render.MeshData.Texturing.Texture;
import org.joml.Vector2f;

import java.util.ArrayList;

public class Homing extends Projectile {

    private boolean homing;
    private float homingDistance;
    private float intensity;
    private Entity2D target;

    public Homing(Entity2D owner, float dmg, Shader shader, ObjModel model, Texture texture) {
        super(owner, dmg, shader, model, texture);
    }

    public Homing() {
        super();
        this.owner = this;
        this.dmg = 100;
        this.model = ObjModel.SQUARE.clone();
        this.shader = Shader.TEXTURING;
        this.pierce = 1;
        this.homing = true;
        this.homingDistance = 200f;
        this.intensity = 100f;
    }


    public Homing clone() {
        Homing p = new Homing(owner, dmg, shader, model, texture);
        p.setScale(scale);
        p.setVelocity(velocity);
        p.setRotation(rotation);
        p.setStatic(isStatic);
        p.setHidden(isHidden);
        p.setOffset(offset);
        p.setColor(color);
        p.setAnimation(animation);

        p.setPierce(pierce);
        p.setArmorPen(armorPen);
        p.setOnHit(onHit);
        p.setOnUpdate(onUpdate);
        p.setHoming(this.homing);
        p.setHomingDistance(this.homingDistance);
        p.setIntensity(this.intensity);

        return p;
    }

    @Override
    public void update(float dt) {
        // move towards target
        if(target != null) {
            Vector2f searchSpace = new Vector2f(-velocity.y, velocity.x);


            float dist = target.getPosition().distance(this.getPosition());
            if(dist < homingDistance * homingDistance) {
                Vector2f direction = new Vector2f(target.getPosition()).sub(this.getPosition()).normalize();
                velocity.add(orthogonalComponent(new Vector2f(velocity), direction.mul(intensity * (1 / (dist+1)))));
            }
        }

        super.update(dt);
    }

    @Override
    public <T extends Living> void collide(ArrayList<T> entities) {
        for (T collider : entities) {
            if (collider.collideRect(this)) {
                boolean gotDamaged = collider.damage(dmg);
                if(gotDamaged) pierce--;
                if(onHit != null) onHit.accept(this, gotDamaged);
                if (pierce <= 0) return;
            }
            if(homing) {
                Vector2f toCollider = new Vector2f(collider.getPosition()).sub(this.getPosition());
                if(velocity.dot(toCollider) < 0) continue; // only consider entities in front of the projectile

                if(target == null) {
                    target = collider;
                } else {
                    if(target.getPosition().distanceSquared(this.getPosition()) > collider.getPosition().distanceSquared(this.getPosition())) {
                        target = collider;
                    }
                }
            }
        }
    }

    // projecting a onto b results in the component of a that is parallel to b (imagine a shadow of a on b)
    // canceling out everything oparralel (i.e the projection) by subtraction leaves the orthogonal component
    private Vector2f orthogonalComponent(Vector2f a, Vector2f b) {
        Vector2f projection = new Vector2f(a).mul(b.dot(a) / a.lengthSquared());
        return new Vector2f(b).sub(projection);
    }

    public boolean isHoming() {
        return homing;
    }
    public float getHomingDistance() {
        return homingDistance;
    }
    public float getIntensity() {
        return intensity;
    }

    public void setHoming(boolean homing) {
        this.homing = homing;
    }
    public void setHomingDistance(float homingDistance) {
        this.homingDistance = homingDistance;
    }
    public void setIntensity(float intensity) {
        this.intensity = intensity;
    }


}
