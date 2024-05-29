package Game.Action;

import Game.Entities.Projectiles.Homing;
import Game.Entities.Projectiles.Projectile;
import Render.Entity.Entity2D;
import org.joml.Vector2f;

import java.util.Arrays;

public class Upgrades {
    // BLANK
    public static Upgrade getBlank() {
        Upgrade u = new Upgrade();
        u.setDescription("");
        u.setOnApply((ability, upgrade) -> {});
        u.setGenerateDescription((ability, upgrade) -> {return "";});
        return u;
    }

    // FLATS
    public static Upgrade getFlatProjectiles(){
        Upgrade pc = new Upgrade("Projectile Count");
        pc.setRarity(0.25f);

        int inc = pc.getRndmLevel();

        pc.setGenerateDescription((ability, upgrade) -> {
            return  upgrade.setDescription("Increases the projectile count of the ability\n from " + ability.stats.get("projectileCount") + " to " + (ability.stats.get("projectileCount") + inc));
        });
        pc.setOnApply((ability, upgrade) -> {
            ability.stats.put("projectileCount", ability.stats.get("projectileCount") + inc);
        });
        return pc;
    }
    public static Upgrade getFlatDmg() {
        Upgrade dmg = new Upgrade("Damage");
        dmg.setRarity(0.25f);

        int inc = dmg.getRndmLevel() * 20;

        dmg.setGenerateDescription((ability, upgrade) -> {
            return  upgrade.setDescription("Increases each projectiles damage by " + inc + ",\npreviously " + Arrays.stream(ability.getProjectileTypes()).mapToInt(projectile -> (int) projectile.getDmg()).sum() + " (total)");
        });
        dmg.setOnApply((ability, upgrade) -> {
            Arrays.stream(ability.getProjectileTypes()).forEach(projectile -> projectile.setDmg(projectile.getDmg() + inc));
        });
        return dmg;
    }
    public static Upgrade getFlatScale() {
        Upgrade scale = new Upgrade("Scale");
        scale.setRarity(0.25f);

        int inc = scale.getRndmLevel() * 5;

        scale.setGenerateDescription((ability, upgrade) -> {
            return  upgrade.setDescription("Increases the scale of each projectile by " + inc + ",\npreviously " + toDecimal(Arrays.stream(ability.getProjectileTypes()).mapToDouble(projectile -> projectile.getScale().length()).average().orElse(0)) + " (average)");
        });
        scale.setOnApply((ability, upgrade) -> {
            Arrays.stream(ability.getProjectileTypes()).forEach(projectile -> projectile.getScale().add(inc, inc));
        });
        return scale;
    }
    public static Upgrade getFlatPierce() {
        Upgrade pierce = new Upgrade("Pierce");
        pierce.setRarity(0.25f);

        int inc = pierce.getRndmLevel();

        pierce.setGenerateDescription((ability, upgrade) -> {
            return  upgrade.setDescription("Increases each projectiles pierce by " + inc + ",\npreviously " + toDecimal(Arrays.stream(ability.getProjectileTypes()).mapToDouble(Projectile::getPierce).average().orElse(0)) + " (average)");
        });
        pierce.setOnApply((ability, upgrade) -> {
            Arrays.stream(ability.getProjectileTypes()).forEach(projectile -> projectile.setPierce(projectile.getPierce() + inc));
        });
        return pierce;
    }
    public static Upgrade getFlatSpeed() {
        Upgrade speed = new Upgrade("Speed");
        speed.setRarity(0.25f);

        int inc =  20 * speed.getRndmLevel();

        speed.setGenerateDescription((ability, upgrade) -> {
            return  upgrade.setDescription("Increases the speed of each projectile by " + inc + " pixels per second,\npreviously " + toDecimal(Arrays.stream(ability.getProjectileTypes()).mapToDouble(Entity2D::getSpeed).average().orElse(0)) + " (average)");
        });
        speed.setOnApply((ability, upgrade) -> {
            Arrays.stream(ability.getProjectileTypes()).forEach(projectile -> projectile.setSpeed(projectile.getSpeed() + inc));
        });
        return speed;
    }
    public static Upgrade getFlatCooldown() {
        Upgrade cd = new Upgrade("Cooldown");
        cd.setRarity(0.25f);

        float dec = (2f - (1f / cd.getRndmLevel())) / 15f;

        cd.setGenerateDescription((ability, upgrade) -> {
            return  upgrade.setDescription("Decreases the cooldown of the ability\n by " + toDecimal(dec) + " from " + ability.getCooldown() + " seconds");
        });
        cd.setOnApply((ability, upgrade) -> {
            ability.setCooldown(ability.getCooldown() - dec);
        });
        return cd;
    }
    public static Upgrade getFlatCooldownStats() {
        Upgrade cd = new Upgrade("Cooldown");
        cd.setRarity(0.25f);

        float dec = (2f - (1f / cd.getRndmLevel())) / 15f;

        cd.setGenerateDescription((ability, upgrade) -> {
            return  upgrade.setDescription("Decreases the cooldown of the ability\n by " + toDecimal(dec) + " from " + toDecimal(ability.stats.get("cooldown")) + " seconds");
        });
        cd.setOnApply((ability, upgrade) -> {
            ability.stats.put("cooldown", ability.stats.get("cooldown") - dec);
        });
        return cd;
    }
    public static Upgrade getFlatReach() {
        Upgrade reach = new Upgrade("Reach");
        reach.setRarity(0.25f);

        int inc = reach.getRndmLevel() * 50;

        reach.setGenerateDescription((ability, upgrade) -> {
            return  upgrade.setDescription("Increases the reach of the ability by " + inc + " pixels");
        });
        reach.setOnApply((ability, upgrade) -> {
            ability.stats.put("reach", ability.stats.get("reach") + inc);
        });
        return reach;
    }

    public static Upgrade getFlatIntensity() {
        Upgrade intensity = new Upgrade("Intensity");
        intensity.setRarity(0.25f);

        int inc = intensity.getRndmLevel() * 15;

        intensity.setGenerateDescription((ability, upgrade) -> {
            return  upgrade.setDescription("Increases the homing intensity by " + inc);
        });
        intensity.setOnApply((ability, upgrade) -> {
            for (Projectile p : ability.getProjectiles()) {
                Homing hp = (Homing) p;
                hp.setIntensity(hp.getIntensity() + inc);
            }
        });
        return intensity;
    }

    // DOUBLES
    public static Upgrade getDoubleProjectiles() {
        Upgrade pc = new Upgrade("Projectile Count");
        pc.setRarity(0.03f);

        // 70% chance of getting a debuff alongside the double
        Upgrade debuff = getBlank();
        if (pc.getRndmLevel() < 3)
            debuff = getDebuffs()[(int) (Math.random() * getDebuffs().length)];

        Upgrade fDebuff = debuff;
        pc.setGenerateDescription((ability, upgrade) -> {
            return  upgrade.setDescription("Doubles the projectile count of the ability\nfrom " + toDecimal(ability.stats.get("projectileCount")) + " to " + toDecimal(ability.stats.get("projectileCount") * 2f)
                                            + (fDebuff.genDescription(ability).isEmpty() ? "" : "\n\n" + fDebuff.genDescription(ability))
            );
        });

        pc.setOnApply((ability, upgrade) -> {
            ability.stats.put("projectileCount", ability.stats.get("projectileCount") * 2f);
            fDebuff.applyTo(ability);
        });
        return pc;
    }
    public static Upgrade getDoubleDmg() {
        Upgrade dmg = new Upgrade("Damage");
        dmg.setRarity(0.03f);

        // 70% chance of getting a debuff alongside the double
        Upgrade debuff = getBlank();
        if (dmg.getRndmLevel() < 3)
            debuff = getDebuffs()[(int) (Math.random() * getDebuffs().length)];

        Upgrade fDebuff = debuff;
        dmg.setGenerateDescription((ability, upgrade) -> {
            Float pc = ability.stats.getOrDefault("projectileCount", 1f);
            return  upgrade.setDescription("Doubles each projectiles damage from a collective\n" + toDecimal((float) Arrays.stream(ability.getProjectileTypes()).mapToInt(projectile -> (int) projectile.getDmg()).sum() /ability.getProjectileTypes().length*pc) + "~ to " + toDecimal((float) Arrays.stream(ability.getProjectileTypes()).mapToInt(projectile -> (int) projectile.getDmg()).sum() /ability.getProjectileTypes().length*pc * 2f)
                                            + (fDebuff.genDescription(ability).isEmpty() ? "" : "\n\n" + fDebuff.genDescription(ability))
            );
        });
        dmg.setOnApply((ability, upgrade) -> {
            Arrays.stream(ability.getProjectileTypes()).forEach(projectile -> projectile.setDmg(projectile.getDmg() * 2f));
            fDebuff.applyTo(ability);
        });
        return dmg;
    }
    public static Upgrade getDoubleScale() {
        Upgrade scale = new Upgrade("Scale");
        scale.setRarity(0.03f);

        // 70% chance of getting a debuff alongside the double
        Upgrade debuff = getBlank();
        if (scale.getRndmLevel() < 3)
            debuff = getDebuffs()[(int) (Math.random() * getDebuffs().length)];

        Upgrade fDebuff = debuff;
        scale.setGenerateDescription((ability, upgrade) -> {
            return  upgrade.setDescription("Doubles the scale of each projectile from\n" + toDecimal(Arrays.stream(ability.getProjectileTypes()).mapToDouble(projectile -> projectile.getScale().length()).average().orElse(0))) + "~ to " + toDecimal(Arrays.stream(ability.getProjectileTypes()).mapToDouble(projectile -> projectile.getScale().length()).average().orElse(0) * 2f) +
                    (fDebuff.genDescription(ability).isEmpty() ? "" : "\n" + fDebuff.genDescription(ability)
                );
        });
        scale.setOnApply((ability, upgrade) -> {
            Arrays.stream(ability.getProjectileTypes()).forEach(projectile -> projectile.setScale(projectile.getScale().mul(2f, new Vector2f())));
            fDebuff.applyTo(ability);
        });
        return scale;
    }
    public static Upgrade getDoublePierce() {
        Upgrade pierce = new Upgrade("Pierce");
        pierce.setRarity(0.03f);

        // 70% chance of getting a debuff alongside the double
        Upgrade debuff = getBlank();
        if (pierce.getRndmLevel() < 3)
            debuff = getDebuffs()[(int) (Math.random() * getDebuffs().length)];

        Upgrade fDebuff = debuff;
        pierce.setGenerateDescription((ability, upgrade) -> {
            return  upgrade.setDescription("Doubles each projectiles pierce from\n" + toDecimal(Arrays.stream(ability.getProjectileTypes()).mapToDouble(Projectile::getPierce).average().orElse(0))+ "~ to " + toDecimal(Arrays.stream(ability.getProjectileTypes()).mapToDouble(Projectile::getPierce).average().orElse(0) * 2f)
                                            + (fDebuff.genDescription(ability).isEmpty() ? "" : "\n\n" + fDebuff.genDescription(ability))
            );
        });
        pierce.setOnApply((ability, upgrade) -> {
            Arrays.stream(ability.getProjectileTypes()).forEach(projectile -> projectile.setPierce(projectile.getPierce() * 2));
            fDebuff.applyTo(ability);
        });
        return pierce;
    }
    public static Upgrade getDoubleSpeed() {
        Upgrade speed = new Upgrade("Speed");
        speed.setRarity(0.03f);

        // 70% chance of getting a debuff alongside the double
        Upgrade debuff = getBlank();
        if (speed.getRndmLevel() < 3)
            debuff = getDebuffs()[(int) (Math.random() * getDebuffs().length)];

        Upgrade fDebuff = debuff;
        speed.setGenerateDescription((ability, upgrade) -> {
            return  upgrade.setDescription("Doubles the speed of each projectile from\n" + toDecimal((Arrays.stream(ability.getProjectileTypes()).mapToDouble(Entity2D::getSpeed).average().orElse(0))) + "~ to " + toDecimal(Arrays.stream(ability.getProjectileTypes()).mapToDouble(Entity2D::getSpeed).average().orElse(0) * 2f)
                                            + (fDebuff.genDescription(ability).isEmpty() ? "" : "\n\n" + fDebuff.genDescription(ability))
            );
        });
        speed.setOnApply((ability, upgrade) -> {
            Arrays.stream(ability.getProjectileTypes()).forEach(projectile -> projectile.setSpeed(projectile.getSpeed() * 2f));
            fDebuff.applyTo(ability);
        });
        return speed;
    }
    public static Upgrade getHalfCooldown() {
        Upgrade cd = new Upgrade("Cooldown");
        cd.setRarity(10000.03f);

        // 70% chance of getting a debuff alongside the double
        Upgrade debuff = getBlank();
        if (cd.getRndmLevel() < 3)
            debuff = getDebuffs()[(int) (Math.random() * getDebuffs().length)];

        Upgrade fDebuff = debuff;
        cd.setGenerateDescription((ability, upgrade) -> {
            return  upgrade.setDescription("Halves the cooldown of the ability from\n" + toDecimal(ability.getCooldown()) + " to " + toDecimal(ability.getCooldown() / 2f)  + " seconds"
                                            + (fDebuff.genDescription(ability).isEmpty() ? "" : "\n\n" + fDebuff.genDescription(ability))
            );
        });
        cd.setOnApply((ability, upgrade) -> {
            ability.setCooldown(ability.getCooldown() / 2f);
            fDebuff.applyTo(ability);
        });
        return cd;
    }
    public static Upgrade getHalfCooldownStats() {
        Upgrade cd = new Upgrade("Cooldown");
        cd.setRarity(0.25f);

        // 70% chance of getting a debuff alongside the double
        Upgrade debuff = getBlank();
        if (cd.getRndmLevel() < 3)
            debuff = Math.random() > 0.5 ? getFlatCooldownStatsIncrease() : getFlatReachReduction();

        Upgrade fDebuff = debuff;
        cd.setGenerateDescription((ability, upgrade) -> {
            return  upgrade.setDescription("Halves the cooldown of the ability from\n" + toDecimal(ability.stats.get("cooldown")) + " to " + toDecimal(ability.stats.get("cooldown")/2f) + " seconds"
                                            + (fDebuff.genDescription(ability).isEmpty() ? "" : "\n\n" + fDebuff.genDescription(ability))
            );
        });
        cd.setOnApply((ability, upgrade) -> {
            ability.stats.put("cooldown", ability.stats.get("cooldown") / 2f);
            fDebuff.applyTo(ability);
        });
        return cd;
    }
    public static Upgrade getDoubleReach() {
        Upgrade reach = new Upgrade("Reach");
        reach.setRarity(0.03f);

        // 70% chance of getting a debuff alongside the double
        Upgrade debuff = getBlank();
        if (reach.getRndmLevel() < 3)
            debuff = Math.random() > 0.5 ? getFlatCooldownStatsIncrease() : getFlatReachReduction();

        Upgrade fDebuff = debuff;
        reach.setGenerateDescription((ability, upgrade) -> {
            return  upgrade.setDescription("Doubles the reach of the ability from\n" + toDecimal(ability.stats.get("reach")) + " to " + toDecimal(ability.stats.get("reach") * 2f) + " pixels"
                                            + (fDebuff.genDescription(ability).isEmpty() ? "" : "\n\n" + fDebuff.genDescription(ability))
            );
        });
        reach.setOnApply((ability, upgrade) -> {
            ability.stats.put("reach", ability.stats.get("reach") * 2f);
            fDebuff.applyTo(ability);
        });
        return reach;
    }

    // NEGATIVE FLATS

    public static Upgrade getFlatDmgReduction() {
        Upgrade dmg = new Upgrade("Damage Reduction");

        int inc = dmg.getRndmLevel() * 10;

        dmg.setGenerateDescription((ability, upgrade) -> {
            return  upgrade.setDescription("Reduces each projectiles damage by " + inc + ",\npreviously " + Arrays.stream(ability.getProjectileTypes()).mapToInt(projectile -> (int) projectile.getDmg()).sum() + " (total)");
        });
        dmg.setOnApply((ability, upgrade) -> {
            Arrays.stream(ability.getProjectileTypes()).forEach(projectile -> projectile.setDmg(projectile.getDmg() - inc));
        });
        return dmg;
    }
    public static Upgrade getFlatScaleReduction() {
        Upgrade scale = new Upgrade("Scale Reduction");

        int inc = scale.getRndmLevel() * 5;

        scale.setGenerateDescription((ability, upgrade) -> {
            return  upgrade.setDescription("Reduces the scale of each projectile by " + inc + ",\npreviously " + toDecimal(Arrays.stream(ability.getProjectileTypes()).mapToDouble(projectile -> projectile.getScale().length()).average().orElse(0)) + " (average)");
        });
        scale.setOnApply((ability, upgrade) -> {
            Arrays.stream(ability.getProjectileTypes()).forEach(projectile -> {
                projectile.getScale().sub(inc, inc);
                if(projectile.getScale().x <= 0 && projectile.getScale().y <= 0) projectile.getScale().set(2, 2);
            });
        });
        return scale;
    }
    public static Upgrade getFlatPierceReduction() {
        Upgrade pierce = new Upgrade("Pierce Reduction");

        int inc = pierce.getRndmLevel();

        pierce.setGenerateDescription((ability, upgrade) -> {
            return  upgrade.setDescription("Reduces each projectiles pierce by " + inc + ",\npreviously " + toDecimal(Arrays.stream(ability.getProjectileTypes()).mapToDouble(Projectile::getPierce).average().orElse(0)) + " (average)");
        });
        pierce.setOnApply((ability, upgrade) -> {
            Arrays.stream(ability.getProjectileTypes()).forEach(projectile -> {
                projectile.setPierce(projectile.getPierce() - inc);
                if (projectile.getPierce() <= 0) projectile.setPierce(1);
            });
        });
        return pierce;
    }
    public static Upgrade getFlatSpeedReduction() {
        Upgrade speed = new Upgrade("Speed Reduction");

        int inc =  20 * speed.getRndmLevel();

        speed.setGenerateDescription((ability, upgrade) -> {
            return  upgrade.setDescription("Reduces the speed of each projectile\n by " + inc + " pixels per second, previously " + toDecimal(Arrays.stream(ability.getProjectileTypes()).mapToDouble(Entity2D::getSpeed).average().orElse(0)) + " (average)");
        });
        speed.setOnApply((ability, upgrade) -> {
            Arrays.stream(ability.getProjectileTypes()).forEach(projectile -> projectile.setSpeed(projectile.getSpeed() - inc));
        });
        return speed;
    }
    public static Upgrade getFlatCooldownIncrease() {
        Upgrade cd = new Upgrade("Cooldown Increase");

        float dec = (2f - (1f / cd.getRndmLevel())) / 15f;

        cd.setGenerateDescription((ability, upgrade) -> {
            return  upgrade.setDescription("Increases the cooldown of the ability\n by " + toDecimal(dec) + " from " + ability.getCooldown() + " seconds");
        });
        cd.setOnApply((ability, upgrade) -> {
            ability.setCooldown(ability.getCooldown() + dec);
        });
        return cd;
    }
    public static Upgrade getFlatCooldownStatsIncrease() {
        Upgrade cd = new Upgrade("Cooldown Increase");

        float dec = (2f - (1f / cd.getRndmLevel())) / 10f;

        cd.setGenerateDescription((ability, upgrade) -> {
            return  upgrade.setDescription("Increases the cooldown of the ability\n by " + toDecimal(dec) + " from " + toDecimal(ability.stats.get("cooldown")) + " seconds");
        });
        cd.setOnApply((ability, upgrade) -> {
            ability.stats.put("cooldown", ability.stats.get("cooldown") + dec);
        });
        return cd;
    }
    public static Upgrade getFlatReachReduction() {
        Upgrade reach = new Upgrade("Reach Reduction");

        int inc = reach.getRndmLevel() * 50;

        reach.setGenerateDescription((ability, upgrade) -> {
            return  upgrade.setDescription("Reduces the reach of the ability by " + inc + " pixels");
        });
        reach.setOnApply((ability, upgrade) -> {
            ability.stats.put("reach", ability.stats.get("reach") - inc);
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
    public static Upgrade[] getDebuffs() {
        return new Upgrade[]{getFlatScaleReduction()};//, getFlatDmgReduction(),  getFlatPierceReduction(), getFlatSpeedReduction(), getFlatCooldownIncrease()};
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
