package Game.Entities;

import Game.Action.Ability;

import java.util.ArrayList;
import java.util.UUID;

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
    default void removeAbility(int index) {
        getAbilities().remove(index);
    }
    default void removeAbility(UUID index) {
        getAbilities().removeIf(a -> a.getUUID().equals(index));
    }

}
