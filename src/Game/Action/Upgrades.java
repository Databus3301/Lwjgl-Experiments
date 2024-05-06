package Game.Action;

import Game.Entities.Projectiles.Projectile;
import org.joml.Vector2f;

public class Upgrades {
    // TODO: lvl system for upgrades (diff rarities/levels → diff power)


    // FLATS
    // TODO: add rarities
    public static Upgrade getFlatProjectiles() {
        Upgrade pc = new Upgrade("Projectile Count");
        pc.setRarity(0.05f);

        int inc = pc.getLevel();

        pc.setGenerateDescription(ability -> {
            ability.setDescription("Increases the projectile count of the ability from " + ability.stats.get("projectileCount") + " to " + (ability.stats.get("projectileCount") + inc));
        });
        pc.setOnApply((ability, upgrade) -> {
            ability.stats.put("projectileCount", ability.stats.get("projectileCount") + inc);
        });
        return pc;
    }

    public static Upgrade getFlatDmg() {
        Upgrade dmg = new Upgrade("Damage");
        dmg.setRarity(0.05f);

        int inc = dmg.getLevel() * 10;

        dmg.setGenerateDescription(ability -> {
            ability.setDescription("Increases each projectiles damage by " + inc + " , previously " + ability.getProjectiles().stream().mapToInt(projectile -> (int) projectile.getDmg()).sum() + " (total)");
        });
        dmg.setOnApply((ability, upgrade) -> {
            ability.getProjectiles().forEach(projectile -> projectile.setDmg(projectile.getDmg() + inc));
        });
        return dmg;
    }

    public static Upgrade getFlatScale() {
        Upgrade scale = new Upgrade("Scale");
        scale.setRarity(0.05f);

        int inc = scale.getLevel() * 5;

        scale.setGenerateDescription(ability -> {
            ability.setDescription("Increases the scale of each projectile by " + inc + " , previously " + ability.getProjectiles().stream().mapToDouble(projectile -> projectile.getScale().length()).average().orElse(0) + " (average)");
        });
        scale.setOnApply((ability, upgrade) -> {
            ability.getProjectiles().forEach(projectile -> projectile.getScale().add(inc, inc));
        });
        return scale;
    }

    public static Upgrade getFlatPierce() {
        Upgrade pierce = new Upgrade("Pierce");
        pierce.setRarity(0.05f);

        int inc = pierce.getLevel();

        pierce.setGenerateDescription(ability -> {
            ability.setDescription("Increases each projectiles pierce by " + inc + " , previously " + ability.getProjectiles().stream().mapToDouble(Projectile::getPierce).average().orElse(0) + " (average)");
        });
        pierce.setOnApply((ability, upgrade) -> {
            ability.stats.put("pierce", ability.stats.get("pierce") + inc);
        });
        return pierce;
    }

    public static Upgrade getFlatSpeed() {
        Upgrade speed = new Upgrade("Speed");
        speed.setRarity(0.05f);

        int inc =  20 * speed.getLevel();

        speed.setGenerateDescription(ability -> {
            ability.setDescription("Increases the speed of each projectile by " + inc + " pixels per second, previously " + ability.getProjectiles().stream().mapToDouble(projectile -> projectile.getSpeed().length()).average().orElse(0) + " (average)");
        });
        speed.setOnApply((ability, upgrade) -> {
            ability.stats.put("speed", ability.stats.get("speed") + inc);
        });
        return speed;
    }

    public static Upgrade getFlatCooldown() {
        Upgrade cd = new Upgrade("Cooldown");
        cd.setRarity(0.05f);

        float dec = (2f - (1f / cd.getLevel())) / 10f;

        cd.setGenerateDescription(ability -> {
            ability.setDescription("Decreases the cooldown of the ability by " + dec + "seconds");
        });
        cd.setOnApply((ability, upgrade) -> {
            ability.setCooldown(ability.getCooldown() - dec);
        });
        return cd;
    }

    // DOUBLES
    public static Upgrade getDoubleProjectiles() {
        Upgrade pc = new Upgrade("Projectile Count");
        pc.setRarity(0.05f);

        pc.setGenerateDescription(ability -> {
            ability.setDescription("Doubles the projectile count of the ability from " + ability.stats.get("projectileCount") + " to " + ability.stats.get("projectileCount") * 2);
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
            ability.setDescription("Doubles each projectiles damage from a collective " + ability.getProjectiles().stream().mapToInt(projectile -> (int) projectile.getDmg()).sum() + " to " + ability.getProjectiles().stream().mapToInt(projectile -> (int) projectile.getDmg()).sum() * 2);
        });
        dmg.setOnApply((ability, upgrade) -> {
            ability.getProjectiles().forEach(projectile -> projectile.setDmg(projectile.getDmg() * 2));
        });
        return dmg;
    }

    public static Upgrade getDoubleScale() {
        Upgrade scale = new Upgrade("Scale");
        scale.setRarity(0.05f);

        scale.setGenerateDescription(ability -> {
            ability.setDescription("Doubles the scale of each projectile from " + ability.getProjectiles().stream().mapToDouble(projectile -> projectile.getScale().length()).average().orElse(0) + "~ to " + ability.getProjectiles().stream().mapToDouble(projectile -> projectile.getScale().length()).average().orElse(0) * 2);
        });
        scale.setOnApply((ability, upgrade) -> {
            ability.getProjectiles().forEach(projectile -> projectile.setScale(projectile.getScale().mul(2, new Vector2f())));
        });
        return scale;
    }

    public static Upgrade getDoublePierce() {
        Upgrade pierce = new Upgrade("Pierce");
        pierce.setRarity(0.05f);

        pierce.setGenerateDescription(ability -> {
            ability.setDescription("Doubles each projectiles pierce from " + ability.getProjectiles().stream().mapToDouble(Projectile::getPierce).average().orElse(0) + "~ to " + ability.getProjectiles().stream().mapToDouble(Projectile::getPierce).average().orElse(0) * 2);
        });
        pierce.setOnApply((ability, upgrade) -> {
            ability.stats.put("pierce", ability.stats.get("pierce") * 2);
        });
        return pierce;
    }

    public static Upgrade getDoubleSpeed() {
        Upgrade speed = new Upgrade("Speed");
        speed.setRarity(0.05f);

        speed.setGenerateDescription(ability -> {
            ability.setDescription("Doubles the speed of each projectile from " + ability.getProjectiles().stream().mapToDouble(projectile -> projectile.getSpeed().length()).average().orElse(0) + "~ to " + ability.getProjectiles().stream().mapToDouble(projectile -> projectile.getSpeed().length()).average().orElse(0) * 2);
        });
        speed.setOnApply((ability, upgrade) -> {
            ability.stats.put("speed", ability.stats.get("speed") * 2);
        });
        return speed;
    }

    public static Upgrade getHalfCooldown() {
        Upgrade cd = new Upgrade("Cooldown");
        cd.setRarity(0.05f);

        cd.setGenerateDescription(ability -> {
            ability.setDescription("Halves the cooldown of the ability from " + ability.getCooldown() + " to " + ability.getCooldown() / 2);
        });
        cd.setOnApply((ability, upgrade) -> {
            ability.setCooldown(ability.getCooldown() / 2);
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

}
