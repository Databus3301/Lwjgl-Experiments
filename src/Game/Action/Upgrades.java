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
            return ability.setDescription("Increases the projectile count of the ability from\n" + ability.stats.get("projectileCount") + " to " + (ability.stats.get("projectileCount") + inc));
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
            return ability.setDescription("Increases the scale of each projectile by " + inc + ",\npreviously " + ((int)(Arrays.stream(ability.getProjectileTypes()).mapToDouble(projectile -> projectile.getScale().length()).average().orElse(0))*100)/100f + " (average)");
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
            return ability.setDescription("Increases each projectiles pierce by " + inc + ",\npreviously " + ((int)(Arrays.stream(ability.getProjectileTypes()).mapToDouble(Projectile::getPierce).average().orElse(0))*100)/100f + " (average)");
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
            return ability.setDescription("Increases the speed of each projectile by " + inc + " pixels per second,\npreviously " + ((int)(Arrays.stream(ability.getProjectileTypes()).mapToDouble(Entity2D::getSpeed).average().orElse(0))*100)/100f + " (average)");
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
            return ability.setDescription("Decreases the cooldown of the ability by " + dec + "seconds");
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
            return ability.setDescription("Decreases the cooldown of the ability\n by " + dec + " from " + ability.stats.get("cooldown") + " seconds");
        });
        cd.setOnApply((ability, upgrade) -> {
            ability.stats.put("cooldown", ability.stats.get("cooldown") - dec);
        });
        return cd;
    }

    // DOUBLES
    public static Upgrade getDoubleProjectiles() {
        Upgrade pc = new Upgrade("Projectile Count");
        pc.setRarity(0.05f);

        pc.setGenerateDescription(ability -> {
            return ability.setDescription("Doubles the projectile count of the ability\nfrom " + ability.stats.get("projectileCount") + " to " + ability.stats.get("projectileCount") * 2);
        });
        pc.setOnApply((ability, upgrade) -> {
            ability.stats.put("projectileCount", ability.stats.get("projectileCount") * 2);
        });
        return pc;
    }
    public static Upgrade getDoubleDmg() {
        Upgrade dmg = new Upgrade("Damage");
        dmg.setRarity(0.05f);

        dmg.setGenerateDescription(ability -> {
            Float pc = ability.stats.getOrDefault("projectileCount", 1f);
            return ability.setDescription("Doubles each projectiles damage from a collective\n" + Arrays.stream(ability.getProjectileTypes()).mapToInt(projectile -> (int) projectile.getDmg()).sum()/ability.getProjectileTypes().length*pc + "~ to " + Arrays.stream(ability.getProjectileTypes()).mapToInt(projectile -> (int) projectile.getDmg()).sum()/ability.getProjectileTypes().length*pc * 2);
        });
        dmg.setOnApply((ability, upgrade) -> {
            Arrays.stream(ability.getProjectileTypes()).forEach(projectile -> projectile.setDmg(projectile.getDmg() * 2));
        });
        return dmg;
    }

    public static Upgrade getDoubleScale() {
        Upgrade scale = new Upgrade("Scale");
        scale.setRarity(0.05f);

        scale.setGenerateDescription(ability -> {
            return ability.setDescription("Doubles the scale of each projectile from\n" + ((int)(Arrays.stream(ability.getProjectileTypes()).mapToDouble(projectile -> projectile.getScale().length()).average().orElse(0))*100)/100f + "~ to " + ((int)(Arrays.stream(ability.getProjectileTypes()).mapToDouble(projectile -> projectile.getScale().length()).average().orElse(0) * 2)*100)/100f);
        });
        scale.setOnApply((ability, upgrade) -> {
            Arrays.stream(ability.getProjectileTypes()).forEach(projectile -> projectile.setScale(projectile.getScale().mul(2, new Vector2f())));
        });
        return scale;
    }

    public static Upgrade getDoublePierce() {
        Upgrade pierce = new Upgrade("Pierce");
        pierce.setRarity(0.05f);

        pierce.setGenerateDescription(ability -> {
            return ability.setDescription("Doubles each projectiles pierce from\n" + ((int)(Arrays.stream(ability.getProjectileTypes()).mapToDouble(Projectile::getPierce).average().orElse(0)*100)/100f + "~ to " + ((int)(Arrays.stream(ability.getProjectileTypes()).mapToDouble(Projectile::getPierce).average().orElse(0) * 2)*100)/100f));
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
            return ability.setDescription("Doubles the speed of each projectile from\n" + ((int)(Arrays.stream(ability.getProjectileTypes()).mapToDouble(Entity2D::getSpeed).average().orElse(0)*100)/100f + "~ to " + ((int)(Arrays.stream(ability.getProjectileTypes()).mapToDouble(Entity2D::getSpeed).average().orElse(0) * 2)*100)/100f));
        });
        speed.setOnApply((ability, upgrade) -> {
            Arrays.stream(ability.getProjectileTypes()).forEach(projectile -> projectile.setSpeed(projectile.getSpeed() * 2));
        });
        return speed;
    }

    public static Upgrade getHalfCooldown() {
        Upgrade cd = new Upgrade("Cooldown");
        cd.setRarity(0.05f);

        cd.setGenerateDescription(ability -> {
            return ability.setDescription("Halves the cooldown of the ability from\n" + ability.getCooldown() + " to " + ability.getCooldown() / 2);
        });
        cd.setOnApply((ability, upgrade) -> {
            ability.setCooldown(ability.getCooldown() / 2);
        });
        return cd;
    }
    public static Upgrade getHalfCooldownStats() {
        Upgrade cd = new Upgrade("Cooldown");
        cd.setRarity(0.25f);

        float dec = (2f - (1f / cd.getLevel())) / 10f;

        cd.setGenerateDescription(ability -> {
            return ability.setDescription("Halves the cooldown of the ability\n by " + dec + " from " + ability.stats.get("cooldown") + " seconds");
        });
        cd.setOnApply((ability, upgrade) -> {
            ability.stats.put("cooldown", ability.stats.get("cooldown") / 2f);
        });
        return cd;
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

}
