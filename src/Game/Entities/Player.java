package Game.Entities;

import Game.Action.Abilities;
import Game.Action.Ability;
import Game.Entities.Dungeon.Door;
import Game.Entities.Dungeon.Room;
import Render.Entity.Entity2D;
import Render.MeshData.Model.ObjModel;
import Render.MeshData.Texturing.Animation;
import Render.Window;
import Tests.Test;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static Tests.Test.renderer;


public class Player extends Living implements Able {
    private Test scene;
    private final ArrayList<Ability> abilities = new ArrayList<>();
    private final Map<String, Animation> animations = new HashMap<>();
    private final Entity2D collider = new Entity2D(ObjModel.SQUARE);

    public Player(Entity2D player, int maxLivePoints, Map<String, Animation> animations) {
        player.clone(this);
        this.maxLP = maxLivePoints;
        this.LP = maxLivePoints;
        this.animations.putAll(animations);
    }

    public <T extends Test> Player(T scene, Entity2D player, int maxLivePoints) {
        scene.addUpdateListener(this::update);
        player.clone(this);
        this.scene = scene;
        this.maxLP = maxLivePoints;
        this.LP = maxLivePoints;

        // --TEMP-- add shooting ability as a default
        addAbility(Abilities.getDASH());
        addAbility(Abilities.getHOMING());
        addAbility(Abilities.getSHOOT());
    }

    public void update(float dt, Vector2f mousePos) {
        if (animation != null)
            animation.update(dt);

        for (Ability ability : abilities) {
            ability.update(dt, mousePos, this);
            // remove out-of-view projectiles
            ability.getProjectiles().removeIf(projectile -> projectile.getPosition().x < -Window.dim.x / 2f + position.x - 100 || projectile.getPosition().x > Window.dim.x / 2f + position.x + 100 || projectile.getPosition().y < -Window.dim.y / 2f + position.y - 100 || projectile.getPosition().y > Window.dim.y / 2f + position.y + 100);
        }
    }

    public <T extends Living> void collide(ArrayList<T> entities) {
        for (Living collider : entities) {
            // push away from player
            if (collider.collideRect(this)) {
                collider.translate(
                        new Vector2f(position).sub(collider.getPosition())
                                .sub((float) Math.random() * 20f - 1f, (float) Math.random() * 20f - 1f) // randomize a bit (to avoid getting stuck in a loop of pushing each other back and forth
                                .normalize().mul(-5)
                );
                damage();
            }
        }
        // collide projectiles / custom ability colliders
        abilities.forEach(ability -> ability.collide(entities));
    }

    public <T extends Room> void collide(T room) {
        this.collider.setScale(this.scale.x / 1.5f, this.scale.y / 2f);
        this.collider.setPosition(this.position.x, this.position.y - this.scale.y / 2f);


        // collide the player with the room.getCollisionRect()
        if (!collider.containedByRect(room.getCollisionRect())) {
            // get each axis and reset to closest axis intercept
            Vector4f rect = room.getCollisionRect();
            Vector2f scale = collider.getScale();
            if (collider.getPosition().x - scale.x / 2f < rect.x) position.x = rect.x + scale.x / 2f;
            if (collider.getPosition().x + scale.x / 2f > rect.z) position.x = rect.z - scale.x / 2f;
            if (collider.getPosition().y - scale.y / 2f < rect.y) position.y = rect.y + scale.y / 2f;
            if (collider.getPosition().y + scale.y / 2f > rect.w) position.y = rect.w - scale.y / 2f;

            //translate(new Vector2f(position).sub(room.getPosition()).normalize().mul(-2));
        }

        // TODO: handle collision independently for each direction, don't make it depend on the room's center (vertical rooms are fucked)



    }

    public void addAnimation(String name, Animation animation) {
        animations.put(name, animation);
    }

    public void removeAnimation(String name) {
        animations.remove(name);
    }

    public void switchAnimation(String name) {
        animation = animations.get(name);
    }

    public ArrayList<Ability> getAbilities() {
        return abilities;
    }
    public Map<String, Animation> getAnimations() {
        return animations;
    }
    public Entity2D getCollider() {
        return collider;
    }
}
