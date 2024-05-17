package Game.Entities;

import Game.Action.Abilities;
import Game.Action.Ability;
import Render.Entity.Entity2D;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.util.ArrayList;

public class Enemies {
    public static final Enemy BASIC = getBASIC();
    public static final Enemy TANK = getTANK();
    public static final Enemy SHOOTER = getSHOOTER();
    public static final Enemy BOSS = getBOSS();
    public static final Enemy FAST = getFAST();

    public static final ArrayList<Enemy> enemies = defineEnemies();


    public static ArrayList<Enemy> defineEnemies() {
        ArrayList<Enemy> enemies = new ArrayList<>();
        enemies.add(BASIC);
        enemies.add(TANK);
        enemies.add(SHOOTER);
        enemies.add(BOSS);
        enemies.add(FAST);
        return enemies;
    }

    public static Enemy getBASIC() {
        // BASIC ENEMY
        Enemy basic = new Enemy();
        basic.setMaxLP(50);
        basic.setLP(50);
        basic.setiSeconds(0.02f);
        basic.setColor(new Vector4f(1, 0, 0, 1));
        basic.setScale(10);
        basic.setSpeed(120f);

        return basic;
    }
    public static Enemy getFAST() {
        // FAST ENEMY
        Enemy fast = new Enemy();
        fast.setMaxLP(50);
        fast.setLP(50);
        fast.setiSeconds(0.02f);
        fast.setColor(new Vector4f(1, 0, 0, 1));
        fast.setScale(10);
        fast.setSpeed(250f);

        return fast;
    }

    public static Enemy getTANK() {
        Enemy tank = new Enemy();
        tank.setMaxLP(200);
        tank.setLP(300);
        tank.setiSeconds(0.02f);
        tank.setColor(new Vector4f(0, 1, 0, 1));
        tank.setScale(10, 15);
        tank.setMinXP(2);
        tank.setMaxXP(4);
        tank.setSpeed(30f);

        return tank;
    }

    public static Enemy getSHOOTER() {
        Enemy shooter = new Enemy();
        shooter.setMaxLP(200);
        shooter.setLP(200);
        shooter.setiSeconds(0.02f);
        shooter.setColor(new Vector4f(0, 0, 1, 1));
        shooter.setScale(10, 12);
        shooter.setMinXP(2);
        shooter.setMaxXP(6);

        Ability shoot = Abilities.getSHOOT();
        shoot.getProjectileTypes()[0].setScale(5f);
        shooter.addAbility(shoot);
        shooter.setSpeed(110f);

        return shooter;
    }

    public static Enemy getBOSS() {
        Enemy boss = new Enemy();
        boss.setScale(10 * 4, 12 * 4);
        boss.setColor(new Vector4f(0, 1, 1, 1));

        boss.setiSeconds(0.02f);
        boss.setMaxLP(2000);
        boss.setLP(2000);

        boss.setMinXP(10);
        boss.setMaxXP(30);

        Ability shoot = Abilities.getSHOOT();
        Ability shoot2 = Abilities.getCIRCLESHOOT();
        shoot2.setCooldown(4f);
        shoot .getProjectileTypes()[0].setScale(20f);
        shoot2.getProjectileTypes()[0].setScale(20f);
        boss.addAbility(shoot);
        boss.addAbility(shoot2);

        boss.setSpeed(90f);

        return boss;
    }

}
