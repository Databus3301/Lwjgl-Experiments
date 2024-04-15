package Game.Action;

import Game.Entities.Projectiles.Homing;
import Game.Entities.Projectiles.Projectile;
import Render.MeshData.Texturing.Texture;

public class Abilities {
    public static Ability SHOOT = getSHOOT();
    public static Ability HOMING = getHOMING();


    public static Ability getSHOOT() {
        Projectile[] projectiles = new Projectile[1];
        projectiles[0] = new Projectile();
        projectiles[0].setDmg(50);
        projectiles[0].setScale(10f);
        projectiles[0].setTexture(new Texture("fireball.png", 0));
        projectiles[0].setPierce(40);
        SHOOT = new Ability(projectiles, 0.25f);
        SHOOT.setOnTrigger((ability, dt, mousePos, targetPos, origin, scene) -> {
            Projectile projectile = ability.getProjectileTypes()[0].clone();
            projectile.setPosition(origin.getPosition());
            projectile.accelerateTowards(targetPos, 350);
            projectile.setOnHit((p, damaged) -> {
                if(damaged)
                    projectile.getScale().mul(1);
            });
            ability.getProjectiles().add(projectile);
        });

        return SHOOT;
    }

    public static Ability getHOMING() {
        Projectile[] projectiles = new Homing[1];
        projectiles[0] = new Homing();
        projectiles[0].setDmg(100);
        projectiles[0].setScale(5);
        projectiles[0].setTexture(new Texture("fireball.png", 0));
        projectiles[0].setPierce(1);
        HOMING = new Ability(projectiles, 0.5f);
        HOMING.setOnTrigger((ability, dt, mousePos, targetPos, origin, scene) -> {
            Homing projectile = (Homing) ability.getProjectileTypes()[0].clone();
            projectile.setPosition(origin.getPosition());
            projectile.accelerateTowards(targetPos, 100);
            ability.getProjectiles().add(projectile);
        });

        return HOMING;
    }

}
