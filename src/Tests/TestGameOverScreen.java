package Tests;

import Render.Entity.Interactable.Button;
import Render.Entity.Interactable.Label;
import Render.MeshData.Shader.Shader;
import Render.MeshData.Texturing.ColorReplacement;
import Render.MeshData.Texturing.Font;
import Render.MeshData.Texturing.Texture;
import Render.Renderer;
import Render.Window;
import org.joml.Vector2f;
import org.joml.Vector4f;

import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;

public class TestGameOverScreen extends Test {
    Button exit;
    public TestGameOverScreen() {
        super();
        init();
    }

    public void init() {
        Vector2f d = Window.getDifferP1920(); // res diff
        float bc = 3;                         // button count
        float bw = (float) Window.baseDim.x / bc * d.x; // button with   max
        float bh = Window.baseDim.y / (bc+1f) * d.y;    // button height max
        float tw = 250 * d.x;                       // target width
        float th = 85  * d.y;                        // target height
        float bo = bh/bc/3;                   // button offset
        if(tw > bw) tw = bw;
        if(th > bh) th = bh;



        {
            int bn = 2;
            exit = new Button(this, new Vector2f(0, (th*-2-bo) * bn + (th*2+bo) * (bc/2f - 0.5f) + 5*bn - 5*(bc/2f)));
            Label l = new Label(Font.RETRO_TRANSPARENT_WHITE, " Exit");
            l.setScale(1000f);
            exit.setLabel(l);
            exit.scale(tw, th);
            exit.setTooltip("Exit the game");
            exit.setTexture(new Texture("input.png", 0));
            exit.setShader(Shader.TEXTURING);

            ColorReplacement cr = new ColorReplacement();
            cr.swap(new Vector4f(1, 1, 1, 1), new Vector4f(0.122f, 0.224f, 0.6f, 1));
            exit.setColorReplacement(cr);

            exit.setPressedCallback((button) -> {
                glfwSetWindowShouldClose(Window.getWindowPtr(), true);
            });
        }
    }

    @Override
    public void OnStart() {
        super.OnStart();
        renderer.cursorShow();
        renderer.setPostProcessingShader(Shader.TEXTURING);
    }
    @Override
    public void OnUpdate(float dt) {
        super.OnUpdate(dt);
    }
    @Override
    public void OnRender() {
        super.OnRender();
        String text = "> GAME OVER <";
        float size = 20;
        renderer.drawText(text, new Vector2f(), size, Font.RETRO, Shader.TEXTURING, Font::centerFirstLine_UI, null, null);
        renderer.draw(exit);
    }
    @Override
    public void OnKeyInput(long window, int key, int scancode, int action, int mods) {
        super.OnKeyInput(window, key, scancode, action, mods);
    }

    @Override public void OnResize(int width, int height) {
        super.OnResize(width, height);
        exit = null;
        renderer = new Renderer();
        init();
        OnStart();
    }
}
