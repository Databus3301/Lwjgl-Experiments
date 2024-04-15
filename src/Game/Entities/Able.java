package Game.Entities;

import Game.Action.Ability;

import java.util.ArrayList;

public interface Able {

    ArrayList<Ability> getAbilities();

    default void addAbility(Ability ability) {
        getAbilities().add(ability);
    }
    default void addAbilities(Ability... abilities) {
        for (Ability ability : abilities) {
            addAbility(ability);
        }
    }
    default void removeAbility(Ability ability) {
        getAbilities().remove(ability);
    }
}
