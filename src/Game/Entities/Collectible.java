package Game.Entities;

import Game.Entities.Projectiles.Projectile;
import Render.Entity.Interactable.Interactable;
import Render.MeshData.Model.ObjModel;
import Render.MeshData.Shader.Shader;
import Render.MeshData.Texturing.Texture;
import Tests.Test;
import org.joml.Vector2f;

import java.util.function.Consumer;

import static Game.Entities.Projectiles.Homing.orthogonalComponent;

public class Collectible extends Interactable {

    private boolean isHoming = true;
    private float homingDistance;
    private float intensity = 100f;
    private Living target;

    protected Consumer<Collectible> onUpdate;

    public <T extends Test> Collectible(T scene, Vector2f position, ObjModel model, Texture texture, Shader shader) {
        super(scene, position, model, texture, shader);
    }

    public void update(float dt) {
        // leave target
        if(target != null && target.getLP() <= 0) target = null;
        // move towards target
        if(target != null && isHoming) {
            float dist = target.getPosition().distance(this.getPosition());
            if(dist < homingDistance * homingDistance) {
                Vector2f direction = new Vector2f(target.getPosition()).sub(this.getPosition()).normalize();
                velocity.add(direction.mul(intensity * (1 / (dist+1))));
            }
        }

        if (this.onUpdate != null)
            this.onUpdate.accept(this);
        translate(velocity.mul(dt, new Vector2f()));
    }


    public void setHoming(boolean homing) {
        isHoming = homing;
    }
    public void setHomingDistance(float homingDistance) {
        this.homingDistance = homingDistance;
    }
    public void setTarget(Living target) {
        this.target = target;
    }

    public void setIntensity(int i) {
        this.intensity = i;
    }
}
