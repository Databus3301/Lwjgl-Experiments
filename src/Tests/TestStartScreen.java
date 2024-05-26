package Tests;

import Render.Entity.Interactable.Button;
import Render.Entity.Interactable.Label;
import Render.MeshData.Shader.Shader;
import Render.MeshData.Texturing.ColorReplacement;
import Render.MeshData.Texturing.Font;
import Render.MeshData.Texturing.Texture;
import Render.Window;
import org.joml.Vector2f;
import org.joml.Vector4f;

import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;

public class TestStartScreen extends Test {

    Button start, options, exit;
    public TestStartScreen() {
        super();

        float bc = 3;                         // button count
        float bw = (float) Window.dim.x / bc; // button with   max
        float bh = Window.dim.y / (bc+1f);    // button height max
        float tw = 250;                       // target width
        float th = 85;                        // target height
        float bo = bh/bc/3;                   // button offset
        if(tw > bw) tw = bw;
        if(th > bh) th = bh;

        {
            int bn = 0;
            start = new Button(this, new Vector2f(0, (th*-2-bo) * bn + (th*2+bo) * (bc/2f - 0.5f) + 5*bn - 5*(bc/2f)));
            Label l = new Label(Font.RETRO_TRANSPARENT_WHITE, " Start");
            l.setScale(1000f);
            start.setLabel(l);
            start.scale(tw, th);
            start.setTooltip("Start the game");
            start.setTexture(new Texture("input.png", 0));
            start.setShader(Shader.TEXTURING);

            ColorReplacement cr = new ColorReplacement();
            cr.swap(new Vector4f(1, 1, 1, 1), new Vector4f(0.122f, 0.224f, 0.6f, 1));
            start.setColorReplacement(cr);

            start.setPressedCallback((button) -> {
                TestOptions.readOptions();
                Window.changeTest(new TestAbillityScreen());
            });
        }
        {
            int bn = 1;
            options = new Button(this, new Vector2f(0, (th*-2-bo) * bn + (th*2+bo) * (bc/2f - 0.5f) + 5*bn - 5*(bc/2f)));
            Label l = new Label(Font.RETRO_TRANSPARENT_WHITE, " Options");
            l.setScale(1000f);
            options.setLabel(l);
            options.scale(tw, th);
            options.setTooltip("Change settings");
            options.setTexture(new Texture("input.png", 0));
            options.setShader(Shader.TEXTURING);

            ColorReplacement cr = new ColorReplacement();
            cr.swap(new Vector4f(1, 1, 1, 1), new Vector4f(0.122f, 0.224f, 0.6f, 1));
            options.setColorReplacement(cr);


            float thf = th;
            float twf = tw;

            options.setPressedCallback((button) -> {
                Window.changeTest(new TestOptions(twf, thf, bo));
            });
        }
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
        renderer.draw(start);
        renderer.draw(options);
        renderer.draw(exit);
    }
    @Override
    public void OnKeyInput(long window, int key, int scancode, int action, int mods) {
        super.OnKeyInput(window, key, scancode, action, mods);
    }
}
