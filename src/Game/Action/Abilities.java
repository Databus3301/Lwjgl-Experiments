package Game.Action;

import Audio.AudioLoader;
import Game.Entities.Living;
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
        projectiles[0].setSpeed(350f);

        SHOOT = new Ability(projectiles, 0.25f);
        SHOOT.setName("Shoot");
        SHOOT.setDescription("Shoots a projectile in direction of cursor\n every 0.25 seconds dealing 50 damage");
        SHOOT.setOnTrigger((ability, dt, mousePos, targetPos, origin, scene) -> {
            Projectile projectile = ability.getProjectileTypes()[0].clone();
            projectile.setPosition(origin.getPosition());
            projectile.accelerateTowards(targetPos, projectile.getSpeed());
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
        DASH.setName("Dash");
        DASH.setDescription("Dashes in the current walking direction\nwith a cooldown of 2 seconds\n(can be stacked)");

        DASH.stats.put("cooldown", 2f);
        DASH.stats.put("reach", 150f);
        DASH.setOnUpgradeGen((a) -> {
            a.getUpgrades().clear();
            a.addUpgrade(Upgrades.getFlatCooldownStats());
            a.addUpgrade(Upgrades.getHalfCooldownStats());
            a.addUpgrade(Upgrades.getFlatReach());
            a.addUpgrade(Upgrades.getDoubleReach());
        });

        DASH.setSound(AudioLoader.loadWavFileSafe("dash.wav"));

        DASH.setOnTrigger((ability, dt, mousePos, targetPos, origin, scene) -> {
            float reach = ability.stats.get("reach");
            origin.translate(new Vector2f(origin.getVelocity()).mul(reach));
        });
        return DASH;
    }

    public static Ability getSHIELD() {
        Projectile[] projectiles = new Projectile[0];
        SHIELD = new Ability(projectiles, 2f);
        SHIELD.setName("Shield");
        SHIELD.setDescription("Gives the player a shield for 2 seconds\nthat blocks all incoming damage");

        SHIELD.stats.put("duration", 2f);

        SHIELD.setOnUpgradeGen((a) -> {
            a.getUpgrades().clear();
            a.addUpgrade(Upgrades.getDoubleDuration());
            a.addUpgrade(Upgrades.getFlatDuration());
        });

        AtomicBoolean shielded = new AtomicBoolean(true);
        SHIELD.setOnTrigger((ability, dt, mousePos, targetPos, origin, scene) -> {
            float dur = ability.stats.get("duration");
            ((Player) origin).setiSeconds(dur);

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
        HOMING.setName("Homing");
        HOMING.setDescription("Shoots a homing projectile\nfollowing the closest enemy in front of it\nevery 0.5 seconds dealing 100 damage");
        HOMING.setOnUpgradeGen((a) -> {
            a.getUpgrades().clear();
            a.addUpgrades(Upgrades.getDefaults());
            a.addUpgrade(Upgrades.getFlatIntensity());
        });
        HOMING.setSound(AudioLoader.loadWavFileSafe("shoot.wav"));

        HOMING.setOnTrigger((ability, dt, mousePos, targetPos, origin, scene) -> {
            Homing projectile = (Homing) ability.getProjectileTypes()[0].clone();
            projectile.setPosition(origin.getPosition());
            projectile.accelerateTowards(targetPos, projectile.getSpeed());
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

        CIRCLESHOOT.setName("Circle-Shoot");
        CIRCLESHOOT.setDescription("Shoots " + CIRCLESHOOT.stats.get("projectileCount") + " projectiles in all directions\nevery 2 seconds dealing 50 damage each");
        CIRCLESHOOT.setSound(AudioLoader.loadWavFileSafe("shoot.wav"));

        CIRCLESHOOT.setOnUpgradeGen((a) -> {
            a.getUpgrades().clear();
            a.addUpgrades(Upgrades.getDefaults());
            a.addUpgrade(Upgrades.getDoubleProjectiles());
            a.addUpgrade(Upgrades.getFlatProjectiles());
        });

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

    public static Ability getSPEED() {
        Projectile[] projectiles = new Projectile[1];
        Ability SPEED = new Ability(projectiles, 0.5f);
        SPEED.setName("Speed");

        SPEED.stats.put("speed", 350f);
        SPEED.setOnUpgradeGen((a) -> {
            a.getUpgrades().clear();
            a.addUpgrade(Upgrades.getFlatSpeedPlayer());
        });

        SPEED.setOnTrigger((ability, dt, mousePos, targetPos, origin, scene) -> {
            origin.setSpeed(ability.stats.get("speed"));
            System.out.println("Speed: " + origin.getSpeed());
        });

        return SPEED;
    }

    public static Ability getMAXHP() {
        Projectile[] projectiles = new Projectile[1];
        Ability MAX_HEALTH = new Ability(projectiles, 0.5f);
        MAX_HEALTH.setName("Max Health");

        MAX_HEALTH.stats.put("maxHealth", 1000f);
        MAX_HEALTH.setOnUpgradeGen((a) -> {
            a.getUpgrades().clear();
            a.addUpgrade(Upgrades.getFlatMaxHealthPlayer());
        });

        MAX_HEALTH.setOnTrigger((ability, dt, mousePos, targetPos, origin, scene) -> {
            Living l = (Living) origin;
            float max_lp_diff = ability.stats.get("maxHealth") - l.getMaxLP();
            l.setMaxLP((ability.stats.get("maxHealth").intValue()));
            l.heal((int)max_lp_diff);
        });

        MAX_HEALTH.setOnApplyToAble((ability, target) -> {
            Living l = (Living) target;
            l.setMaxLP((ability.stats.get("maxHealth").intValue()));
            l.setLP(l.getMaxLP());
        });

        return MAX_HEALTH;
    }

}
