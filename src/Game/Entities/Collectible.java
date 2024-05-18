package Game.Entities;

import Render.Entity.Entity2D;
import Render.Entity.Interactable.Interactable;
import Render.MeshData.Model.ObjModel;
import Render.MeshData.Shader.Shader;
import Render.MeshData.Texturing.Texture;
import Tests.Test;
import org.joml.Vector2f;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class Collectible extends Interactable {

    private boolean isHoming = true;
    private float lifeTime = 5f;
    private float homingDistance;
    private float intensity = 100f;
    private Living target;

    protected Consumer<Collectible> onUpdate;
    protected BiConsumer<Collectible, Entity2D> onCollect;

    public <T extends Test> Collectible(T scene, Vector2f position, ObjModel model, Texture texture, Shader shader) {
        super(scene, position, model, texture, shader);
        scene.addUpdateListener(this::update);
        setScale(5);
        setIntensity(700);
        setHomingDistance(10);
    }

    public <T extends Test> Collectible(T scene, Vector2f position) {
        super(scene, position, ObjModel.SQUARE, new Texture("xp.png", 0), Shader.TEXTURING);
        scene.addUpdateListener(this::update);
        setScale(5);
        setIntensity(700);
        setHomingDistance(10);
    }


    public void update(float dt) {
        // leave target
        if(target != null && target.getLP() <= 0) target = null;
        // move towards target
        if(target != null && isHoming) {
            float dist = target.getPosition().distance(this.getPosition());
            if(dist < homingDistance * homingDistance) {
                Vector2f direction = new Vector2f(target.getPosition()).sub(this.getPosition()).normalize();
                // correct overshooting
                if(direction.dot(velocity) < 0) {
                    velocity.add(direction.mul(intensity*15 * (1 / (dist+1))));
                }
                velocity.add(direction.mul(intensity * (1 / (dist+1))));
            }
        }

        lifeTime -= dt;

        if (this.onUpdate != null)
            this.onUpdate.accept(this);
        translate(velocity.mul(dt, new Vector2f()));
    }
    private void update(Float aFloat, Vector2f vector2f) {
        update(aFloat);
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

    public void setOnCollect(BiConsumer<Collectible, Entity2D> onCollect) {
        this.onCollect = onCollect;
    }

    public boolean shouldDisappear() {
        return lifeTime <= 0;
    }
}
