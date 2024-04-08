package Game.Action;

import Game.Entities.Projectile;
import Render.MeshData.Texturing.Texture;
import Tests.Test;
import org.joml.Vector2f;

public class Abilities {
    public static Ability SHOOT = getSHOOT();


    private static Ability getSHOOT() {
        Projectile[] projectiles = new Projectile[1];
        projectiles[0] = new Projectile();
        projectiles[0].setDmg(100);
        projectiles[0].setScale(100f);
        projectiles[0].setTexture(new Texture("fireball.png", 0));
        SHOOT = new Ability(projectiles, 1f);
        SHOOT.setOnTrigger((ability, dt, mousePos, entity, scene) -> {
            Projectile projectile = ability.getProjectileTypes()[0].instantiate();
            projectile.setPosition(entity.getPosition());
            projectile.accelerateTowards(Test.getRenderer().screenToWorldCoords(mousePos), 1000);
            ability.getProjectiles().add(projectile);
        });
        return SHOOT;
    }
}
