package Game.Action;

import java.util.function.BiConsumer;

/**
 * Upgrade for an Ability able to be obtained by anything Able. <br>
 * Each Ability has a list of Upgrades that can be applied to it. <br>
 * Said list is generated in Abilities on Ability definition.<br>
 * It holds common upgrades, but also custom ones.<br>
 * This is realised through functional interfaces.<br>
 *
 * @see Game.Entities.Able
 * @see Game.Action.Ability
 * @see Game.Entities.Player
 */
public class Upgrade {
    private String name;
    private String description;
    private int level;
    /**
     * The lower, the rarer. <br>
     * Ideally between 0 and 1. <br>
     * but the true rarity is determined by the relation to the rarity of the other upgrades.
     *
     */
    private float rarity;

    private BiConsumer<Ability, Upgrade> onApply = (ability, upgrade) -> {};
    private DescGenerator<Ability, Upgrade> generateDescription = (ability, upgrade) -> {return "";};


    public Upgrade(String name, String description, int level) {
        this.name = name;
        this.description = description;
        this.level = level;
    }
    public Upgrade(String name) {
        this(name, "NO DESCRIPTION", 1);
    }
    public Upgrade() {
        this("NO NAME", "NO DESCRIPTION", 1);
    }

    public void applyTo(Ability ability) {
        onApply.accept(ability, this);
    }
    public String genDescription(Ability ability) {
        return generateDescription.accept(ability, this);
    }

    public void setOnApply(BiConsumer<Ability, Upgrade> onApply) {
        this.onApply = onApply;
    }
    public void setGenerateDescription(DescGenerator<Ability, Upgrade> generateDescription) {
        this.generateDescription = generateDescription;
    }
    public void setLevel(int level) {
        this.level = level;
    }
    public String setDescription(String description) {
        return this.description = description;
    }
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Set the rarity of the upgrade. <br>
     * Rarity is a float between 0 and 1. <br>
     * the lower, the rarer.
     * @param rarity
     */
    public void setRarity(float rarity) {
        //assert rarity >= 0 && rarity <= 1 : "Rarity must be between 0 and 1";
        this.rarity = rarity;
    }

    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
    public int getLevel() {
        return level;
    }
    public int getRndmLevel() {
        double d = Math.random();
        if(d < 0.4) return 1 + level;
        if(d < 0.7) return 2 + level;
        if(d < 0.9) return 3 + level;
        return 4 + level;
    }
    public float getRarity() {
        return rarity;
    }

    @FunctionalInterface
    public interface DescGenerator<A, B> {
        String accept(A a, B b);
    }
}
