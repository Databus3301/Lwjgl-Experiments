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
import org.joml.Vector4f;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.IllegalFormatWidthException;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import static Render.Entity.Interactable.Interactable.States.DRAGGED;

public class TestOptions extends Test {

    private static final Path  optionsPath = Path.of("res", "Settings", "settings.txt");
    private final Slider effectVolume, musicVolume;

    private final Button back;

    public TestOptions(float tw, float th, float bo) {
        super();


        effectVolume = new Slider(this, new Vector2f(-Window.dim.x/4f, -th*2 + bo));
        effectVolume.setBarScale(150, 15);
        effectVolume.setLabel("Effect Volume:");

        musicVolume = new Slider(this, new Vector2f(-Window.dim.x/4f, th*2 + bo));
        musicVolume.setBarScale(150, 15);
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
    }

    @Override
    public void OnStart() {
        super.OnStart();
    }

    @Override
    public void OnUpdate(float dt) {
        super.OnUpdate(dt);
        if(effectVolume.getState() != DRAGGED) {
            write(line -> {
                if (line.startsWith("EFFECT_VOLUME= ")) {
                    return "EFFECT_VOLUME= " + effectVolume.getValue();
                }
                return line;
            });
        }
        if(musicVolume.getState() != DRAGGED) {
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
                        Dungeon.ABILITY_VOLUME = Float.parseFloat(line.split("= ")[1]);
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

    @Override
    public void OnRender() {
        super.OnRender();
        renderer.draw(effectVolume);
        renderer.draw(musicVolume);
        renderer.draw(back);
    }

    @Override
    public void OnClose() {
        super.OnClose();
    }
}
