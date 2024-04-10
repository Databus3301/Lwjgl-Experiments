package Game.Entities;

import Render.Entity.Entity2D;
import Render.MeshData.Texturing.Texture;
import Render.MeshData.Shader.Shader;
import Render.MeshData.Model.ObjModel;
import org.joml.Quaternionf;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class Enemy extends Living {

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

    @Override
    public Enemy instantiate() {
        Enemy e = new Enemy();

        e.rotation = new Quaternionf(rotation);
        e.scale = new Vector2f(scale);
        e.velocity = new Vector2f(velocity);
        e.isStatic = isStatic;
        e.isHidden = isHidden;
        e.offset = new Vector2f(offset);
        e.color = new Vector4f(color);

        e.LP = LP;
        e.maxLP = maxLP;
        e.iSeconds = iSeconds;
        e.currentIseconds = currentIseconds;

        return e;
    }
}
