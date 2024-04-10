package Game.Action;

import Game.Entities.Projectile;
import Render.MeshData.Texturing.Texture;
import Tests.Test;

public class Abilities {
    public static Ability SHOOT = getSHOOT();


    private static Ability getSHOOT() {
        Projectile[] projectiles = new Projectile[1];
        projectiles[0] = new Projectile();
        projectiles[0].setDmg(100);
        projectiles[0].setScale(10f);
        projectiles[0].setTexture(new Texture("fireball.png", 0));
        projectiles[0].setPierce(1);
        SHOOT = new Ability(projectiles, 0.25f);
        SHOOT.setOnTrigger((ability, dt, mousePos, entity, scene) -> {
            Projectile projectile = ability.getProjectileTypes()[0].instantiate();
            projectile.setPosition(entity.getPosition());
            projectile.accelerateTowards(Test.getRenderer().screenToWorldCoords(mousePos), 350);
            projectile.setOnHit((p, damaged) -> {
                if(damaged)
                    projectile.getScale().mul(1);
            });
            ability.getProjectiles().add(projectile);
        });

        return SHOOT;
    }
}
