package Game.Entities;

import Game.Action.Ability;
import Game.Entities.Dungeon.Room;
import Render.Entity.Entity2D;
import Render.Entity.Interactable.Interactable;
import Render.MeshData.Model.ObjModel;
import Render.MeshData.Shader.Shader;
import Render.MeshData.Texturing.Texture;
import Render.Window;
import Tests.Test;
import org.joml.Quaternionf;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.util.ArrayList;

public class Enemy extends Living implements Able {

    private final ArrayList<Ability> abilities = new ArrayList<>();
    private int minXP = 1;
    private int maxXP = 3;

    public QuintConsumer<Enemy, Float, Vector2f, Vector2f, Room> movement = (enemy, dt, mousepos, target, room) -> {
        enemy.translateTowards(target, enemy.getSpeed()*dt);
    };

    public Enemy(Vector2f position, ObjModel model, Texture texture, Shader shader, float LP) {
        super(position, model, texture, shader);
        this.maxLP = (int) LP;
        this.LP = LP;
        speed = 100;
    }

    public Enemy(Vector2f position, ObjModel model, Texture texture, Shader shader) {
        this(position, model, texture, shader, 100);
    }

    public Enemy() {
        this(new Vector2f(), ObjModel.SQUARE.clone(), null, Shader.TEXTURING);

    }

    public void update(float dt, Vector2f mousePos) {
        update(dt, mousePos, mousePos, null);
    }

    public void update(float dt, Vector2f mousePos, Vector2f target, Room room) {
        for (Ability ability : abilities) {
            ability.update(dt, mousePos, target, this);
            // remove out-of-view projectiles
            ability.getProjectiles().removeIf(projectile -> projectile.getPosition().x < -Window.dim.x / 2f + getPosition().x - 100 || projectile.getPosition().x > Window.dim.x / 2f + getPosition().x + 100 || projectile.getPosition().y < -Window.dim.y / 2f + getPosition().y - 100 || projectile.getPosition().y > Window.dim.y / 2f + getPosition().y + 100);

            movement.accept(this, dt, mousePos, target, room);
        }
    }

    public Enemy clone() {
        Enemy e = new Enemy();

        e.rotation = new Quaternionf(rotation);
        e.scale = new Vector2f(scale);
        e.velocity = new Vector2f(velocity);
        e.isStatic = isStatic;
        e.isHidden = isHidden;
        e.offset = new Vector2f(offset);
        e.color = new Vector4f(color);
        e.texture = texture;

        abilities.forEach(a -> e.addAbility(a.clone()));

        e.LP = LP;
        e.maxLP = maxLP;
        e.iSeconds = iSeconds;
        e.currentIseconds = currentIseconds;
        e.minXP = minXP;
        e.maxXP = maxXP;

        return e;
    }

    public void spawnXp(Test scene, ArrayList<Interactable> props, Player player, Room room) {
        int xp = (int) (Math.random() * (maxXP - minXP) + minXP);
        for(int i = 0; i < xp; i++){
            Collectible xpCollectible = new Collectible(scene, position);
            xpCollectible.setScale(5);
            xpCollectible.setTarget(player);
            xpCollectible.setIntensity(500);
            xpCollectible.setHomingDistance(15);

            xpCollectible.setOnCollect((c, p) -> {
                ((Player)p).addXP(1);
            });
            // rndm pos in a radius of radius
            int radius = 20;
            xpCollectible.translate((float) Math.random() * radius*2 - radius, (float) Math.random() * radius*2 - radius);
            // keep xp in room
            xpCollectible.onUpdate = c -> {
                if(!c.containedByRect(room.getCollisionRect())) {
                    c.setVelocity(0, 0);
                    c.translate(new Vector2f(room.getCollisionRect().x + room.getCollisionRect().z / 2f - c.getPosition().x, room.getCollisionRect().y + room.getCollisionRect().w / 2f - c.getPosition().y).normalize().mul((int)(100*Math.random())));
                }
            };

            props.add(xpCollectible);
        }
    }

    public ArrayList<Ability> getAbilities() {
        return abilities;
    }
    public int getMinXP() {
        return minXP;
    }
    public int getMaxXP() {
        return maxXP;
    }


    public void setMaxXP(int maxXP) {
        this.maxXP = maxXP;
    }
    public void setMinXP(int minXP) {
        this.minXP = minXP;
    }

    public void setMovement(QuintConsumer<Enemy, Float, Vector2f, Vector2f, Room> movement) {
        this.movement = movement;
    }
    @FunctionalInterface
    public interface QuintConsumer<T, U, V, W, Y> {
        void accept(T t, U u, V v, W w, Y y);
    }
}
