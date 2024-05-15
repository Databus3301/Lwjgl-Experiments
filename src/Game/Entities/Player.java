package Game.Entities;

import Game.Action.Abilities;
import Game.Action.Ability;
import Game.Entities.Dungeon.Room;
import Render.Entity.Entity2D;
import Render.Entity.Interactable.Interactable;
import Render.MeshData.Model.ObjModel;
import Render.MeshData.Texturing.Animation;
import Render.Window;
import Tests.Test;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class Player extends Living implements Able {
    private Test scene;
    private final ArrayList<Ability> abilities = new ArrayList<>();
    private final Map<String, Animation> animations = new HashMap<>();
    private final Entity2D collider = new Entity2D(ObjModel.SQUARE);
    private int xp = 0;

    private boolean isAutoshooting = false;

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
//        addAbility(Abilities.getDASH());
//        addAbility(Abilities.getCIRCLESHOOT());
//        addAbility(Abilities.getHOMING());
//        addAbility(Abilities.getHOMING().setCurrentCooldown(0.1f));
//        addAbility(Abilities.getHOMING().setCurrentCooldown(0.2f));
//        addAbility(Abilities.getHOMING().setCurrentCooldown(0.3f));
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
            Vector4f rect = room.getCollisionRect();
            Vector2f scale = collider.getScale();
            Vector2f pos = collider.getPosition();

            if (pos.x - scale.x / 2f < rect.x)          position.x = rect.x + scale.x / 2f;
            if (pos.x + scale.x / 2f > rect.z + rect.x) position.x = rect.z + rect.x - scale.x / 2f;
            if (pos.y - scale.y / 2f < rect.y)          position.y = rect.y + scale.y / 2f + (position.y - pos.y);
            if (pos.y + scale.y / 2f > rect.w + rect.y) position.y = rect.w + rect.y - scale.y / 2f + (position.y - pos.y);
        }

    }

    public <T extends Interactable> void collıde(ArrayList<T> props) {
        Iterator<T> iterator = props.iterator();
        while(iterator.hasNext()) {
            Entity2D prop = iterator.next();
            if(prop instanceof Collectible collectible) {
                if(collider.collideCircle(collectible)) {
                    ((Collectible) prop).onCollect.accept((Collectible) prop, this);
                    iterator.remove();
                    System.out.println("Player xp: " + xp);
                }
            }
        }
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

    public void addXP(int xp) {
        this.xp += xp;
    }


    public void setAutoshooting(boolean autoshooting) {
        this.isAutoshooting = autoshooting;
    }
    public void toggleAutoshooting() {
        this.isAutoshooting = !isAutoshooting;
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
    public int getXP() {
        return xp;
    }

    public boolean getAutoshooting() {
        return isAutoshooting;
    }
}
