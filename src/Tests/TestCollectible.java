package Tests;

import Game.Entities.Collectible;
import Game.Entities.Living;
import Render.Entity.Entity2D;
import Render.MeshData.Model.ObjModel;
import Render.MeshData.Shader.Shader;
import Render.MeshData.Texturing.Texture;
import Render.Window;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.GLFW_REPEAT;

public class TestCollectible extends Test {

    ArrayList<Collectible> collectibles;
    Living player;

    public TestCollectible() {
        super();
    }

    @Override
    public void OnStart() {
        super.OnStart();
        player = new Living(new Vector2f(), ObjModel.SQUARE, null, Shader.TEXTURING);
        player.setScale(50);
        player.setColor(new Vector4f(1, 1, 1, 0.5f));
        player.setSpeed(300f);

        Texture tex = new Texture("xp.png", 0);
        collectibles = new ArrayList<>(100);
        for (int i = 0; i < 100; i++) {
            Vector2f pos = new Vector2f((float) Math.random() * Window.dim.x - Window.dim.x/2f, (float) Math.random() * Window.dim.y - Window.dim.y/2f);
            collectibles.add(new Collectible(this, pos, ObjModel.SQUARE, tex, Shader.TEXTURING));
            collectibles.get(i).setScale(5);
            collectibles.get(i).setIntensity(700);
            collectibles.get(i).setTarget(player);
            collectibles.get(i).setHomingDistance(10);
        }

    }

    @Override
    public void OnUpdate(float dt) {
        super.OnUpdate(dt);
        player.translate(player.getVelocity().mul(dt*player.getSpeed(), new Vector2f()));

        collectibles.removeIf(c -> c.collideCircle(player));
    }

    @Override
    public void OnRender() {
        super.OnRender();
        collectibles.forEach(c -> renderer.draw(c));
        renderer.draw(player);
    }

    @Override
    public void OnKeyInput(long window, int key, int scancode, int action, int mods) {
        super.OnKeyInput(window, key, scancode, action, mods);
        player.setVelocity(0, 0);


        // player wasd control
        if (key == 87 && (action == 1 || action == GLFW_REPEAT)) player.accelerate(new Vector2f(+0, +1));
        if (key == 83 && (action == 1 || action == GLFW_REPEAT)) player.accelerate(new Vector2f(+0, -1));
        if (key == 65 && (action == 1 || action == GLFW_REPEAT)) player.accelerate(new Vector2f(-1, +0));
        if (key == 68 && (action == 1 || action == GLFW_REPEAT)) player.accelerate(new Vector2f(+1, +0));
    }
}
