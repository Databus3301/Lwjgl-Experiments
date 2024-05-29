package Tests;

import Game.Entities.Dungeon.Dungeon;
import Render.Entity.Interactable.Button;
import Render.Entity.Interactable.Label;
import Render.Entity.Interactable.Slider;
import Render.MeshData.Model.ObjModel;
import Render.MeshData.Shader.Shader;
import Render.MeshData.Texturing.ColorReplacement;
import Render.MeshData.Texturing.Font;
import Render.MeshData.Texturing.Texture;
import Render.Window;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector4f;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Function;

import static Render.Entity.Interactable.Interactable.States.DRAGGED;

public class TestOptions extends Test {

    private static final Path  optionsPath = Path.of("res", "Settings", "settings.txt");
    private Slider effectVolume, musicVolume;
    private Button back;
    private Label controls;

    public TestOptions() {
        super();
        init();
    }

    public void init() {
        Vector2f d = Window.getDifferP1920(); // res diff
        float bc = 3;                         // button count
        float bw = (float) Window.baseDim.x / bc * d.x; // button with   max
        float bh = Window.baseDim.y / (bc+1f)    * d.y;    // button height max
        float tw = 250 * d.x;                       // target width
        float th = 85  * d.y;                        // target height
        float bo = bh/bc/3;                   // button offset
        if(tw > bw) tw = bw;
        if(th > bh) th = bh;

        effectVolume = new Slider(this, new Vector2f(-Window.dim.x/4f, -th*2 + bo));
        effectVolume.setBarScale(tw/2, tw/20);
        effectVolume.setLabel("Effect Volume:");

        musicVolume = new Slider(this, new Vector2f(-Window.dim.x/4f, th*2 + bo));
        musicVolume.setBarScale(tw/2, tw/20);
        musicVolume.setLabel("Music Volume:");

        back = new Button(this, new Vector2f(0, -Window.dim.y/2f + th*2), ObjModel.SQUARE, new Texture("input.png", 0), Shader.TEXTURING);
        back.scale(tw, th);
        back.setPressedCallback((button) -> {
            Window.changeTest(new TestStartScreen());
        });
        ColorReplacement cr = new ColorReplacement();
        cr.swap(new Vector4f(1, 1, 1, 1), new Vector4f(0.122f, 0.224f, 0.6f, 1));
        back.setColorReplacement(cr);
        Label l = new Label(Font.RETRO_TRANSPARENT_WHITE, "  Back", 1000);
        back.setLabel(l);

        controls = new Label(Font.RETRO_TRANSPARENT_WHITE, "Controls:\n\nWASD - Move\nSpace - Dash\nE - Interact\nQ - Autoshoot\nEsc - Exit", (int)(effectVolume.getBar().getScale().y *1.2f));
        int longestLine = controls.getFont().getLongestLine(controls.getText());
        maxWidth = controls.getFont().getWidth(" ".repeat(longestLine));
    }

    @Override
    public void OnStart() {
        super.OnStart();
        renderer.setPostProcessingShader(Shader.TEXTURING);
        readOptions();
        effectVolume.setValue(Dungeon.EFFECT_VOLUME);
        musicVolume.setValue(Dungeon.MUSIC_VOLUME);
    }

    @Override
    public void OnUpdate(float dt) {
        super.OnUpdate(dt);
        if(effectVolume.getState() != DRAGGED && effectVolume.changedSinceLastRead()) {
            write(line -> {
                if (line.startsWith("EFFECT_VOLUME= ")) {
                    return "EFFECT_VOLUME= " + effectVolume.getValue();
                }
                return line;
            });
        }
        if(musicVolume.getState() != DRAGGED && musicVolume.changedSinceLastRead()) {
            write(line -> {
                if (line.startsWith("MUSIC_VOLUME= ")) {
                    return "MUSIC_VOLUME= " + musicVolume.getValue();
                }
                return line;
            });
        }
    }

    public void write(Function<String, String> function) {
        try {
            // Read all lines from the file
            List<String> lines = Files.readAllLines(optionsPath);
            // Process the lines
            List<String> processedLines = lines.stream()
                    .map(function)
                    .toList();
            // Write the processed lines back to the file
            Files.write(optionsPath, processedLines);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void readOptions() {
        try {
            List<String> lines = Files.readAllLines(optionsPath);
            for(String line : lines) {
                switch (line.split("= ")[0]) {
                    case "EFFECT_VOLUME":
                        Dungeon.EFFECT_VOLUME = Float.parseFloat(line.split("= ")[1]);
                        break;
                    case "MUSIC_VOLUME":
                        Dungeon.MUSIC_VOLUME = Float.parseFloat(line.split("= ")[1]);
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    float maxWidth = 0;
    @Override
    public void OnRender() {
        super.OnRender();
        renderer.draw(effectVolume);
        renderer.draw(musicVolume);
        renderer.draw(controls, pos.set(Window.dim.x/4f - maxWidth*3f, musicVolume.getBar().getPosition().y + musicVolume.getScale().y *5f));
        renderer.draw(back);
    }

    @Override public void OnResize(int width, int height) {
        renderer = new Render.Renderer();
        effectVolume = musicVolume = null;
        back = null;
        controls = null;
        init();
        OnStart();
    }



    @Override
    public void OnClose() {
        super.OnClose();
    }


    Vector2f pos = new Vector2f(0, 0);
}
