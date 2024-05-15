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
    public static Ability CIRCLESHOOT = getCIRCLESHOOT();

    public static Ability[] abilities = defineAbilities();
    public static Ability[] defineAbilities() {
        return new Ability[]{SHOOT, HOMING, DASH, SHIELD, CIRCLESHOOT};
    }

    public static Ability getSHOOT() {
        Projectile[] projectiles = new Projectile[1];
        projectiles[0] = new Projectile();
        projectiles[0].setDmg(50);
        projectiles[0].setScale(10f);
        projectiles[0].setTexture(new Texture("fireball.png", 0));

        SHOOT = new Ability(projectiles, 0.25f);
        SHOOT.setDescription("Shoots a projectile in direction of cursor}\n every 0.25 seconds dealing 50 damage");
        SHOOT.addUpgrades(Upgrades.getDefaults());
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
        DASH = new Ability(projectiles, 1000); // Float.MAX_VALUE /3f - 2
        DASH.setName("dash");
        DASH.setDescription("Dashes in the current walking direction\n with a cooldown of 2 seconds\n (can be stacked)");

        DASH.stats.put("cooldown", 2f);
        DASH.addUpgrade(Upgrades.getFlatCooldownStats());
        DASH.addUpgrade(Upgrades.getHalfCooldownStats());

        DASH.setOnTrigger((ability, dt, mousePos, targetPos, origin, scene) -> {
            origin.translate(new Vector2f(origin.getVelocity()).mul(200));
        });
        return DASH;
    }

    public static Ability getSHIELD() {
        Projectile[] projectiles = new Projectile[0];
        SHIELD = new Ability(projectiles, 2f);
        AtomicBoolean shielded = new AtomicBoolean(true);
        SHIELD.setDescription("Gives the player a shield for 2 seconds\n that blocks all incoming damage");

        SHIELD.setOnTrigger((ability, dt, mousePos, targetPos, origin, scene) -> {
            ((Player) origin).setiSeconds(2);
            origin.setShader(new Shader("texturing.player.shader"));
            if (!shielded.get()) {
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
        HOMING.setDescription("Shoots a homing projectile\n following the closest enemy in front of it\n every 0.5 seconds dealing 100 damage");
        HOMING.addUpgrades(Upgrades.getDefaults());
        HOMING.setOnTrigger((ability, dt, mousePos, targetPos, origin, scene) -> {
            Homing projectile = (Homing) ability.getProjectileTypes()[0].clone();
            projectile.setPosition(origin.getPosition());
            projectile.accelerateTowards(targetPos, 100);
            ability.getProjectiles().add(projectile);
        });

        return HOMING;
    }


    public static Ability[] getSALVE() {
        // TODO: rework this to use the UUID system to collectively upgrade things
        return new Ability[]{getSHOOT().setCurrentCooldown(0.1f), getSHOOT().setCurrentCooldown(0.2f), getSHOOT().setCurrentCooldown(0.3f)};
    }

    public static Ability getCIRCLESHOOT() {
        Projectile[] projectiles = new Projectile[1];
        projectiles[0] = new Projectile();
        projectiles[0].setDmg(50);
        projectiles[0].setScale(10f);
        projectiles[0].setTexture(new Texture("fireball.png", 0));
        projectiles[0].setPierce(1);
        projectiles[0].setSpeed(200f);
        CIRCLESHOOT = new Ability(projectiles, 2f);
        CIRCLESHOOT.stats.put("projectileCount", 6f);
        CIRCLESHOOT.setDescription("Shoots " + CIRCLESHOOT.stats.get("projectileCount") + "projectiles in all directions\n every 2 seconds dealing 50 damage each");
        CIRCLESHOOT.addUpgrade(Upgrades.getDoubleProjectiles());
        CIRCLESHOOT.addUpgrade(Upgrades.getFlatProjectiles());
        CIRCLESHOOT.addUpgrades(Upgrades.getDefaults());

        CIRCLESHOOT.setOnTrigger((ability, dt, mousePos, targetPos, origin, scene) -> {
            float pc = ability.stats.get("projectileCount");

            Projectile[] circle = new Projectile[(int) pc];
            for (int i = 0; i < circle.length; i++) {
                circle[i] = ability.getProjectileTypes()[0].clone();
                circle[i].setPosition(origin.getPosition());

                Vector2f vector = new Vector2f((float) Math.cos(i * Math.PI / (circle.length/2f)), (float) Math.sin(i * Math.PI / (circle.length/2f))).mul(350);
                circle[i].accelerateTowards(vector.add(origin.getPosition()), circle[i].getSpeed());

                ability.getProjectiles().add(circle[i]);
            }
        });

        return CIRCLESHOOT;
    }

}
