package Render.Entity;

import Render.Entity.Texturing.Texture;
import Render.Shader.Shader;
import Render.Vertices.Model.ObjModel;
import org.joml.Vector2f;

public class Projectile extends Entity2D {
    Entity2D owner;


    public Projectile(Vector2f position, ObjModel model, Texture texture, Shader shader, Entity2D owner, Vector2f velocity) {
        super(position, model, texture, shader);
        this.owner = owner;
        this.velocity = velocity;
    }
}
