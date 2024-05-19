package Game.Action;

import Game.Entities.Projectiles.Projectile;
import Render.Entity.Entity2D;
import org.joml.Vector2f;

import java.util.Arrays;

public class Upgrades {
    // TODO: lvl system for upgrades (diff rarities/levels → diff power)

    // FLATS
    public static Upgrade getFlatProjectiles() {
        Upgrade pc = new Upgrade("Projectile Count");
        pc.setRarity(0.25f);

        int inc = pc.getLevel();

        pc.setGenerateDescription(ability -> {
            return ability.setDescription("Increases the projectile count of the ability\n from" + ability.stats.get("projectileCount") + " to " + (ability.stats.get("projectileCount") + inc));
        });
        pc.setOnApply((ability, upgrade) -> {
            ability.stats.put("projectileCount", ability.stats.get("projectileCount") + inc);
        });
        return pc;
    }
    public static Upgrade getFlatDmg() {
        Upgrade dmg = new Upgrade("Damage");
        dmg.setRarity(0.25f);

        int inc = dmg.getLevel() * 10;

        dmg.setGenerateDescription(ability -> {
            return ability.setDescription("Increases each projectiles damage by " + inc + ",\npreviously " + Arrays.stream(ability.getProjectileTypes()).mapToInt(projectile -> (int) projectile.getDmg()).sum() + " (total)");
        });
        dmg.setOnApply((ability, upgrade) -> {
            Arrays.stream(ability.getProjectileTypes()).forEach(projectile -> projectile.setDmg(projectile.getDmg() + inc));
        });
        return dmg;
    }
    public static Upgrade getFlatScale() {
        Upgrade scale = new Upgrade("Scale");
        scale.setRarity(0.25f);

        int inc = scale.getLevel() * 5;

        scale.setGenerateDescription(ability -> {
            return ability.setDescription("Increases the scale of each projectile by " + inc + ",\npreviously " + toDecimal(Arrays.stream(ability.getProjectileTypes()).mapToDouble(projectile -> projectile.getScale().length()).average().orElse(0)) + " (average)");
        });
        scale.setOnApply((ability, upgrade) -> {
            Arrays.stream(ability.getProjectileTypes()).forEach(projectile -> projectile.getScale().add(inc, inc));
        });
        return scale;
    }
    public static Upgrade getFlatPierce() {
        Upgrade pierce = new Upgrade("Pierce");
        pierce.setRarity(0.25f);

        int inc = pierce.getLevel();

        pierce.setGenerateDescription(ability -> {
            return ability.setDescription("Increases each projectiles pierce by " + inc + ",\npreviously " + toDecimal(Arrays.stream(ability.getProjectileTypes()).mapToDouble(Projectile::getPierce).average().orElse(0)) + " (average)");
        });
        pierce.setOnApply((ability, upgrade) -> {
            Arrays.stream(ability.getProjectileTypes()).forEach(projectile -> projectile.setPierce(projectile.getPierce() + inc));
        });
        return pierce;
    }
    public static Upgrade getFlatSpeed() {
        Upgrade speed = new Upgrade("Speed");
        speed.setRarity(0.25f);

        int inc =  20 * speed.getLevel();

        speed.setGenerateDescription(ability -> {
            return ability.setDescription("Increases the speed of each projectile by " + inc + " pixels per second,\npreviously " + toDecimal(Arrays.stream(ability.getProjectileTypes()).mapToDouble(Entity2D::getSpeed).average().orElse(0)) + " (average)");
        });
        speed.setOnApply((ability, upgrade) -> {
            Arrays.stream(ability.getProjectileTypes()).forEach(projectile -> projectile.setSpeed(projectile.getSpeed() + inc));
        });
        return speed;
    }
    public static Upgrade getFlatCooldown() {
        Upgrade cd = new Upgrade("Cooldown");
        cd.setRarity(0.25f);

        float dec = (2f - (1f / cd.getLevel())) / 10f;

        cd.setGenerateDescription(ability -> {
            return ability.setDescription("Decreases the cooldown of the ability\n by " + dec + " from " + ability.getCooldown() + " seconds");
        });
        cd.setOnApply((ability, upgrade) -> {
            ability.setCooldown(ability.getCooldown() - dec);
        });
        return cd;
    }
    public static Upgrade getFlatCooldownStats() {
        Upgrade cd = new Upgrade("Cooldown");
        cd.setRarity(0.25f);

        float dec = (2f - (1f / cd.getLevel())) / 10f;

        cd.setGenerateDescription(ability -> {
            return ability.setDescription("Decreases the cooldown of the ability\n by " + dec + " from " + toDecimal(ability.stats.get("cooldown")) + " seconds");
        });
        cd.setOnApply((ability, upgrade) -> {
            ability.stats.put("cooldown", ability.stats.get("cooldown") - dec);
        });
        return cd;
    }
    public static Upgrade getFlatReach() {
        Upgrade reach = new Upgrade("Reach");
        reach.setRarity(0.25f);

        int inc = reach.getLevel() * 50;

        reach.setGenerateDescription(ability -> {
            return ability.setDescription("Increases the reach of the ability by " + inc + " pixels");
        });
        reach.setOnApply((ability, upgrade) -> {
            ability.stats.put("reach", ability.stats.get("reach") + inc);
        });
        return reach;
    }

    // DOUBLES
    public static Upgrade getDoubleProjectiles() {
        Upgrade pc = new Upgrade("Projectile Count");
        pc.setRarity(0.05f);

        pc.setGenerateDescription(ability -> {
            return ability.setDescription("Doubles the projectile count of the ability\nfrom " + toDecimal(ability.stats.get("projectileCount")) + " to " + toDecimal(ability.stats.get("projectileCount") * 2f));
        });
        pc.setOnApply((ability, upgrade) -> {
            ability.stats.put("projectileCount", ability.stats.get("projectileCount") * 2f);
        });
        return pc;
    }
    public static Upgrade getDoubleDmg() {
        Upgrade dmg = new Upgrade("Damage");
        dmg.setRarity(0.05f);

        dmg.setGenerateDescription(ability -> {
            Float pc = ability.stats.getOrDefault("projectileCount", 1f);
            return ability.setDescription("Doubles each projectiles damage from a collective\n" + toDecimal((float) Arrays.stream(ability.getProjectileTypes()).mapToInt(projectile -> (int) projectile.getDmg()).sum() /ability.getProjectileTypes().length*pc) + "~ to " + toDecimal((float) Arrays.stream(ability.getProjectileTypes()).mapToInt(projectile -> (int) projectile.getDmg()).sum() /ability.getProjectileTypes().length*pc * 2f));
        });
        dmg.setOnApply((ability, upgrade) -> {
            Arrays.stream(ability.getProjectileTypes()).forEach(projectile -> projectile.setDmg(projectile.getDmg() * 2f));
        });
        return dmg;
    }
    public static Upgrade getDoubleScale() {
        Upgrade scale = new Upgrade("Scale");
        scale.setRarity(0.05f);

        scale.setGenerateDescription(ability -> {
            return ability.setDescription("Doubles the scale of each projectile from\n" + toDecimal(Arrays.stream(ability.getProjectileTypes()).mapToDouble(projectile -> projectile.getScale().length()).average().orElse(0))) + "~ to " + toDecimal(Arrays.stream(ability.getProjectileTypes()).mapToDouble(projectile -> projectile.getScale().length()).average().orElse(0) * 2f);
        });
        scale.setOnApply((ability, upgrade) -> {
            Arrays.stream(ability.getProjectileTypes()).forEach(projectile -> projectile.setScale(projectile.getScale().mul(2f, new Vector2f())));
        });
        return scale;
    }
    public static Upgrade getDoublePierce() {
        Upgrade pierce = new Upgrade("Pierce");
        pierce.setRarity(0.05f);

        pierce.setGenerateDescription(ability -> {
            return ability.setDescription("Doubles each projectiles pierce from\n" + toDecimal(Arrays.stream(ability.getProjectileTypes()).mapToDouble(Projectile::getPierce).average().orElse(0))+ "~ to " + toDecimal(Arrays.stream(ability.getProjectileTypes()).mapToDouble(Projectile::getPierce).average().orElse(0) * 2f));
        });
        pierce.setOnApply((ability, upgrade) -> {
            Arrays.stream(ability.getProjectileTypes()).forEach(projectile -> projectile.setPierce(projectile.getPierce() * 2));
        });
        return pierce;
    }
    public static Upgrade getDoubleSpeed() {
        Upgrade speed = new Upgrade("Speed");
        speed.setRarity(0.05f);

        speed.setGenerateDescription(ability -> {
            return ability.setDescription("Doubles the speed of each projectile from\n" + toDecimal((Arrays.stream(ability.getProjectileTypes()).mapToDouble(Entity2D::getSpeed).average().orElse(0))) + "~ to " + toDecimal(Arrays.stream(ability.getProjectileTypes()).mapToDouble(Entity2D::getSpeed).average().orElse(0) * 2f));
        });
        speed.setOnApply((ability, upgrade) -> {
            Arrays.stream(ability.getProjectileTypes()).forEach(projectile -> projectile.setSpeed(projectile.getSpeed() * 2f));
        });
        return speed;
    }
    public static Upgrade getHalfCooldown() {
        Upgrade cd = new Upgrade("Cooldown");
        cd.setRarity(0.05f);

        cd.setGenerateDescription(ability -> {
            return ability.setDescription("Halves the cooldown of the ability from\n" + toDecimal(ability.getCooldown()) + " to " + toDecimal(ability.getCooldown() / 2f));
        });
        cd.setOnApply((ability, upgrade) -> {
            ability.setCooldown(ability.getCooldown() / 2f);
        });
        return cd;
    }
    public static Upgrade getHalfCooldownStats() {
        Upgrade cd = new Upgrade("Cooldown");
        cd.setRarity(0.25f);

        cd.setGenerateDescription(ability -> {
            return ability.setDescription("Halves the cooldown of the ability from\n" + toDecimal(ability.stats.get("cooldown")) + " to " + toDecimal(ability.stats.get("cooldown")/2f) + "seconds");
        });
        cd.setOnApply((ability, upgrade) -> {
            ability.stats.put("cooldown", ability.stats.get("cooldown") / 2f);
        });
        return cd;
    }
    public static Upgrade getDoubleReach() {
        Upgrade reach = new Upgrade("Reach");
        reach.setRarity(0.05f);

        reach.setGenerateDescription(ability -> {
            return ability.setDescription("Doubles the reach of the ability from\n" + toDecimal(ability.stats.get("reach")) + " to " + toDecimal(ability.stats.get("reach") * 2f) + " pixels");
        });
        reach.setOnApply((ability, upgrade) -> {
            ability.stats.put("reach", ability.stats.get("reach") * 2f);
        });
        return reach;
    }


    // ideas
//    public static Upgrade getDoubleDuration() {
//        Upgrade dur = new Upgrade("Duration", "Doubles the duration of the ability", 0);
//        dur.setOnApply((ability, upgrade) -> {
//            ability.stats.put("duration", ability.stats.get("duration") * 2);
//        });
//        return dur;
//    }
//
//    public static Upgrade getDoubleRadius() {
//        Upgrade rad = new Upgrade("Radius", "Doubles the radius of the ability", 0);
//        rad.setOnApply((ability, upgrade) -> {
//            ability.stats.put("radius", ability.stats.get("radius") * 2);
//        });
//        return rad;
//    }

    public static Upgrade[] getDefaults() {
        return new Upgrade[]{getFlatDmg(), getFlatScale(), getFlatPierce(), getFlatSpeed(), getFlatCooldown(), getDoubleDmg(), getDoubleScale(), getDoublePierce(), getDoubleSpeed(), getHalfCooldown()};
    }

    public static float toDecimal(float f, int decimal) {
        return (float) ((int)(f * Math.pow(10, decimal)) / Math.pow(10, decimal));
    }
    public static float toDecimal(float f) {
        return ((int)(f * 100f) / 100f);
    }
    public static float toDecimal(double f, int decimal) {
        return (float) ((int)(f * Math.pow(10, decimal)) / Math.pow(10, decimal));
    }
    public static float toDecimal(double f) {
        return ((int)(f * 100f) / 100f);
    }
}
