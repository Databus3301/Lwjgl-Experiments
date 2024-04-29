package Game.Entities;

import Game.Action.Abilities;
import Game.Action.Ability;
import org.joml.Vector4f;

import java.util.ArrayList;

public class Enemies {
    public static final Enemy BASIC = getBASIC();
    public static final Enemy TANK = getTANK();
    public static final Enemy SHOOTER = getSHOOTER();

    public static final ArrayList<Enemy> enemies = defineEnemies();


    public static ArrayList<Enemy> defineEnemies() {
        ArrayList<Enemy> enemies = new ArrayList<>();
        enemies.add(BASIC);
        enemies.add(TANK);
        enemies.add(SHOOTER);

        return enemies;
    }

    //TODO: IMPLEMENT basic.setSpeed(1);
    public static Enemy getBASIC() {
        // BASIC ENEMY
        Enemy basic = new Enemy();
        basic.setMaxLP(50);
        basic.setLP(50);
        basic.setiSeconds(0.02f);
        basic.setColor(new Vector4f(1, 0, 0, 1));
        basic.setScale(10);
        return basic;
    }

    public static Enemy getTANK() {
        // BASIC ENEMY 2
        Enemy tank = new Enemy();
        tank.setMaxLP(200);
        tank.setLP(200);
        tank.setiSeconds(0.02f);
        tank.setColor(new Vector4f(0, 1, 0, 1));
        tank.setScale(10, 15);
        return tank;
    }

    public static Enemy getSHOOTER() {
        // BASIC ENEMY 2
        Enemy shooter = new Enemy();
        shooter.setMaxLP(200);
        shooter.setLP(200);
        shooter.setiSeconds(0.02f);
        shooter.setColor(new Vector4f(0, 0, 1, 1));
        shooter.setScale(10, 12);
        Ability shoot = Abilities.getSHOOT();
        shoot.getProjectileTypes()[0].setScale(5f);
        shooter.addAbility(shoot);

        return shooter;
    }
}
