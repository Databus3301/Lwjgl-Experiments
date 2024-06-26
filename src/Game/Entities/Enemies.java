package Game.Entities;

import Audio.AudioSource;
import Game.Action.Abilities;
import Game.Action.Ability;
import Render.MeshData.Shader.Shader;
import Render.MeshData.Texturing.Texture;
import Tests.Test;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.util.ArrayList;

public class Enemies {
    public static final Enemy BASIC = getBASIC();
    public static final Enemy TANK = getTANK();
    public static final Enemy SHOOTER = getSHOOTER();
    public static final Enemy MINIBOSS = getMINIBOSS();
    public static final Enemy FAST = getFAST();

    public static final ArrayList<Enemy> enemies = defineEnemies();


    public static ArrayList<Enemy> defineEnemies() {
        ArrayList<Enemy> enemies = new ArrayList<>();
        enemies.add(BASIC);
        enemies.add(TANK);
        enemies.add(SHOOTER);
        enemies.add(MINIBOSS);
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
        shoot.setCooldown(2.5f);
        shoot.getProjectileTypes()[0].setScale(5f);
        shooter.addAbility(shoot);
        shooter.setSpeed(110f);

        shooter.setMovement((enemy, dt, mousepos, target, room) -> {
            // move towards player if distance is greater than 250
            if (enemy.getPosition().distance(target) > 250)
                enemy.translateTowards(target, enemy.getSpeed()*dt);

            // move away from player if distance is less than 250
            if (enemy.getPosition().distance(target) < 250)
                enemy.translateTowards(target, -enemy.getSpeed()*dt);

            // if distance is between 240 and 260, orbit target
            if (enemy.getPosition().distance(target) > 240 && enemy.getPosition().distance(target) < 260) {
                // Calculate the direction from the enemy to the target
                Vector2f direction = enemy.translateTowards(target, 0);
                // Rotate the direction by 90 degrees to get the orbiting direction
                // This will make the enemy move in a direction that is perpendicular to the target, creating an orbiting effect
                Vector2f orbitDirection = new Vector2f(-direction.y, direction.x);
                // Move the enemy in the orbiting direction
                enemy.translate(orbitDirection.mul(enemy.getSpeed()*dt)); // Assuming 'speed' is the speed at which you want the enemy to orbit the target
            }
        });

        return shooter;
    }

    public static Enemy getMINIBOSS() {
        Enemy miniboss = new Enemy();
        miniboss.setScale(10 * 4, 12 * 4);
        miniboss.setColor(new Vector4f(0, 1, 1, 1));

        miniboss.setiSeconds(0.05f);
        miniboss.setMaxLP(1000);
        miniboss.setLP(1000);

        miniboss.setMinXP(10);
        miniboss.setMaxXP(30);

        Ability shoot1 = Abilities.getSHOOT();
        Ability shoot2 = Abilities.getCIRCLESHOOT();
        shoot1.setCooldown(2f);
        shoot2.setCooldown(8f);
        shoot1.getProjectileTypes()[0].setScale(20f);
        shoot2.getProjectileTypes()[0].setScale(20f);
        miniboss.addAbility(shoot1);
        miniboss.addAbility(shoot2);

        miniboss.setSpeed(45f);

        return miniboss;
    }

    public static Enemy getBOSS() {
        // BOSS ENEMY
        Enemy boss = new Enemy();
        boss.setMaxLP(5000);
        boss.setLP(5000);
        boss.setiSeconds(0.06f);
        boss.setScale(80, 96);
        boss.setSpeed(50f);
        boss.setMinXP(25);
        boss.setMaxXP(55);


        Ability shoot = Abilities.getSHOOT();
        shoot.setCooldown(10000f);
        shoot.getProjectileTypes()[0].setScale(20f);

        boss.addAbility(shoot);

        Texture bossT = new Texture("boss0.png");
        Texture bossT2 = new Texture("boss1.png");
        Texture bossT3 = new Texture("boss2.png");

        boss.setMovement((enemy, dt, mousepos, target, room) -> {
            enemy.translateTowards(target, enemy.getSpeed()*dt);

            // should move in large zigs zagging patterns
            float p = enemy.getMovementFuncProgress();
            enemy.setMovementFuncProgress(p+dt);
            if (p < 0)
                enemy.translateTowards(target, enemy.getSpeed()*dt);
            else if (p > 0.5f && p < 2)
                enemy.translateTowards(new Vector2f((target.x-enemy.getPosition().x+5) *5, enemy.getPosition().y - target.y/50), -enemy.getSpeed()*dt*10);
            else if (p > 2)
                enemy.translateTowards(new Vector2f((target.x-enemy.getPosition().x+5)*-10, enemy.getPosition().y - target.y/50), -enemy.getSpeed()*dt*10);
            if (p > 4)
                enemy.setMovementFuncProgress(-0);

            if(p > 0.5f && p < 4f && p % 0.5f < 0.1f)
                enemy.getAbilities().get(0).setCurrentCooldown(0);


            // hijack the movement function to change the texture based on health
            if(enemy.getLP() < boss.getMaxLP()/3f) {
                room.getAudios()[4].playSound("ghast3.wav");
                Test.renderer.setPostProcessingShader(Shader.POST_PROCESSING_SWIZZLE);
                enemy.setTexture(bossT3);
            }
            else if(enemy.getLP() < boss.getMaxLP()/3f*2) {
                room.getAudios()[3].playSound("ghast1.wav");
                Test.renderer.setPostProcessingShader(Shader.POST_PROCESSING_INVERT);
                enemy.setTexture(bossT2);
            }
            else {
                enemy.setTexture(bossT);
            }
        });

        return boss;
    }






}
