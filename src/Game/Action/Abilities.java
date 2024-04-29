package Game.Action;

import Game.Entities.Player;
import Game.Entities.Projectiles.Homing;
import Game.Entities.Projectiles.Projectile;
import Render.MeshData.Shader.Shader;
import Render.MeshData.Texturing.Texture;
import org.joml.Vector2f;

import java.util.concurrent.atomic.AtomicBoolean;

public class Abilities {
    public static Ability SHOOT = getSHOOT();
    public static Ability HOMING = getHOMING();
    public static Ability[] SALVE = getSALVE();
    public static Ability DASH = getDASH();
    public static Ability SHIELD = getSHIELD();

    public static Ability getSHOOT() {
        Projectile[] projectiles = new Projectile[1];
        projectiles[0] = new Projectile();
        projectiles[0].setDmg(50);
        projectiles[0].setScale(10f);
        projectiles[0].setTexture(new Texture("fireball.png", 0));
        projectiles[0].setPierce(40);
        SHOOT = new Ability(projectiles, 0.25f);
        SHOOT.setOnTrigger((ability, dt, mousePos, targetPos, origin, scene) -> {
            Projectile projectile = ability.getProjectileTypes()[0].clone();
            projectile.setPosition(origin.getPosition());
            projectile.accelerateTowards(targetPos, 350);
            projectile.setOnHit((p, damaged) -> {
                if (damaged)
                    projectile.getScale().mul(1);
            });
            ability.getProjectiles().add(projectile);
        });

        return SHOOT;
    }

    public static Ability getDASH() {
        Projectile[] projectiles = new Projectile[0];
        DASH = new Ability(projectiles, Float.MAX_VALUE - 2);
        DASH.setOnTrigger((ability, dt, mousePos, targetPos, origin, scene) -> {

            origin.translate(new Vector2f(origin.getVelocity()).mul(200));

        });

        return DASH;
    }
    public static Ability getSHIELD() {
        Projectile[] projectiles = new Projectile[0];
        SHIELD = new Ability(projectiles, 2f);
        AtomicBoolean shielded = new AtomicBoolean(true);
        SHIELD.setOnTrigger((ability, dt, mousePos, targetPos, origin, scene) -> {
            ((Player) origin).setiSeconds(2);
            origin.setShader(new Shader("texturing.player.shader"));
            if(!shielded.get()) {
                origin.setColor(0, 0, 1f, 0);
                shielded.set(true);
            } else {
                origin.setColor(0, 0, 0, 0);
                shielded.set(false);
            }
        });

        return SHIELD;
    }
    public static Ability getHOMING() {
        Projectile[] projectiles = new Homing[1];
        projectiles[0] = new Homing();
        projectiles[0].setDmg(100);
        projectiles[0].setScale(5);
        projectiles[0].setTexture(new Texture("fireball.png", 0));
        projectiles[0].setPierce(1);
        HOMING = new Ability(projectiles, 0.5f);
        HOMING.setOnTrigger((ability, dt, mousePos, targetPos, origin, scene) -> {
            Homing projectile = (Homing) ability.getProjectileTypes()[0].clone();
            projectile.setPosition(origin.getPosition());
            projectile.accelerateTowards(targetPos, 100);
            ability.getProjectiles().add(projectile);
        });

        return HOMING;
    }


    public static Ability[] getSALVE() {
        return new Ability[]{getSHOOT().setCurrentCooldown(0.1f), getSHOOT().setCurrentCooldown(0.2f), getSHOOT().setCurrentCooldown(0.3f)};

    }

}
