package Tests;

import Render.Entity.Entity2D;
import Render.MeshData.Model.ObjModel;
import Render.MeshData.Shader.Shader;
import Render.MeshData.Texturing.Animation;
import Render.MeshData.Texturing.Texture;
import Render.MeshData.Texturing.TextureAtlas;
import org.joml.Vector2f;

public class TestAnimation extends Test {

     Animation walkUp, walkDown, walkLeft, walkRight;
     Entity2D player, player2, player3, player4;

    public TestAnimation() {
        super();

        TextureAtlas atlas = new TextureAtlas(new Texture("LinkAnim.png", 0), 10, 8, 10, 120, 130, 0);
        walkUp    = new Animation(this, atlas, 5-1, 0, 20, 10);
        walkDown  = new Animation(this, atlas, 6-1, 0, 20, 10);
        walkLeft  = new Animation(this, atlas, 7-1, 0, 20, 10);
        walkRight = new Animation(this, atlas, 8-1, 0, 20, 10);

        player  = new Entity2D(new Vector2f(100, 100),   ObjModel.SQUARE, Shader.TEXTURING);
        player2 = new Entity2D(new Vector2f(-100, 100),  ObjModel.SQUARE, Shader.TEXTURING);
        player3 = new Entity2D(new Vector2f(-100, -100), ObjModel.SQUARE, Shader.TEXTURING);
        player4 = new Entity2D(new Vector2f(100, -100),  ObjModel.SQUARE, Shader.TEXTURING);

        player.setAnimation(walkUp);
        player2.setAnimation(walkDown);
        player3.setAnimation(walkLeft);
        player4.setAnimation(walkRight);

        player.scale(50);
        player2.scale(50);
        player3.scale(50);
        player4.scale(50);
    }

    @Override
    public void OnUpdate(float dt) {
        super.OnUpdate(dt);
    }

    @Override
    public void OnRender() {
        super.OnRender();
        renderer.drawEntity2D(player);
        renderer.drawEntity2D(player2);
        renderer.drawEntity2D(player3);
        renderer.drawEntity2D(player4);
    }
}
