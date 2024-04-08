package Game.Action;

import Game.Entities.Living;
import Game.Entities.Projectile;
import Render.Entity.Entity2D;
import Render.Entity.Interactable.Interactable;
import Tests.Test;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.Iterator;

public class Ability {
    private Test scene;
    private Projectile[] projectileTypes;
    private ArrayList<Projectile> projectiles = new ArrayList<>();
    /**
     * Cooldown in seconds
     */
    private float cooldown;
    private float currentCooldown;

    public Interactable.QuintConsumer<Ability, Float, Vector2f, Entity2D, Test> onTrigger;
    public Ability(Projectile[] projectileTypes, float cooldown) {
        this.projectileTypes = projectileTypes;
        this.cooldown = cooldown;
        this.currentCooldown = .5f;
    }

    public void update(float dt, Vector2f mousePos, Entity2D trigger) {
        if (currentCooldown > 0) {
            currentCooldown -= dt;
        } else {
            currentCooldown = cooldown*2;
            onTrigger.accept(this, dt, mousePos, trigger, scene);
        }
        projectiles.forEach(projectile -> projectile.update(dt));
        projectiles.removeIf(projectile -> projectile.getPierce() <= 0);
    }
    public <T extends Living> void collide(ArrayList<T> entities) {
        projectiles.forEach(projectile -> projectile.collide(entities));
    }


    public float getCooldown() {
        return cooldown;
    }
    public float getCurrentCooldown() {
        return currentCooldown;
    }
    public Projectile[] getProjectileTypes() {
        return projectileTypes;
    }
    public ArrayList<Projectile> getProjectiles() {
        return projectiles;
    }

    public void setCooldown(float cooldown) {
        this.cooldown = cooldown;
    }
    public void setOnTrigger(Interactable.QuintConsumer<Ability, Float, Vector2f, Entity2D, Test> onTrigger) {
        this.onTrigger = onTrigger;
    }
    public void setProjectileTypes(Projectile[] projectileTypes) {
        this.projectileTypes = projectileTypes;
    }
    public Ability setScene(Test scene) {
        this.scene = scene;
        return this;
    }

}
