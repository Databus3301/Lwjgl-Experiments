package Game.Entities;

import Game.Action.Ability;
import Render.Entity.Entity2D;
import Render.MeshData.Texturing.Texture;
import Render.MeshData.Shader.Shader;
import Render.MeshData.Model.ObjModel;
import Render.Window;
import org.joml.Quaternionf;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.util.ArrayList;

public class Enemy extends Living implements Able{

    private final ArrayList<Ability> abilities = new ArrayList<>();

    public Enemy (Vector2f position, ObjModel model, Texture texture, Shader shader, float LP) {
        super(position, model, texture, shader);
        this.maxLP = (int)LP;
        this.LP = LP;
    }

    public Enemy (Vector2f position, ObjModel model, Texture texture, Shader shader) {
        this(position, model, texture, shader, 100);
    }

    public Enemy() {
        this(new Vector2f(), ObjModel.SQUARE.clone(), null, Shader.TEXTURING);
    }

    public void update(float dt, Vector2f mousePos) {
        for (Ability ability : abilities) {
            ability.update(dt, mousePos, this);
            // remove out-of-view projectiles
            ability.getProjectiles().removeIf(projectile -> projectile.getPosition().x < -Window.dim.x / 2f + getPosition().x - 100 || projectile.getPosition().x > Window.dim.x / 2f + getPosition().x + 100 || projectile.getPosition().y < -Window.dim.y / 2f + getPosition().y - 100 || projectile.getPosition().y > Window.dim.y / 2f + getPosition().y + 100);
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

        return e;
    }

    public ArrayList<Ability> getAbilities() {
        return abilities;
    }
}
