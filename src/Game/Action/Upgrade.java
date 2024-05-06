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

    private BiConsumer<Ability, Upgrade> onApply = (ability, upgrade) -> {};


    public Upgrade(String name, String description, int level) {
        this.name = name;
        this.description = description;
        this.level = level;
    }
    public Upgrade() {
        this.name = "NO NAME";
        this.description = "NO DESCRIPTION";
        this.level = 0;
    }

    public void applyTo(Ability ability) {
        onApply.accept(ability, this);
    }

    public void setOnApply(BiConsumer<Ability, Upgrade> onApply) {
        this.onApply = onApply;
    }
    public void setLevel(int level) {
        this.level = level;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setName(String name) {
        this.name = name;
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
}
