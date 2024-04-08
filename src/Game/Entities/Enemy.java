package Game.Entities;

import Render.MeshData.Texturing.Texture;
import Render.MeshData.Shader.Shader;
import Render.MeshData.Model.ObjModel;
import org.joml.Vector2f;

public class Enemy extends Living {

    public Enemy (Vector2f position, ObjModel model, Texture texture, Shader shader, float LP) {
        super(position, model, texture, shader);
        this.maxLP = (int)LP;
        this.LP = LP;
    }

}
