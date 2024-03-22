package Render.Entity.Texturing;

import Tests.Test;
import org.joml.Vector2f;

public class Font extends TextureAtlas {
    public static final Font RETRO = new Font(new Texture("fonts/oldschool_white.png", 0), 32, 18, 5, 18, 7, 9, 0);
    public static final Font RETRO_TRANSPARENT_WHITE = new Font(new Texture("fonts/oldschool_transparent_white.png", 0), 32, 18, 5, 18, 7, 9, 0);
    private int asciiOffset; // account for fonts that start at an ascii value other than 0;


    public Font(Texture font, int asciiOffset, int charsPerRow, int rows, int cols, int charWidth, int charHeight, int charSpacing) {
        super(font, charsPerRow, rows, cols, charWidth, charHeight, charSpacing);
        this.asciiOffset = asciiOffset;
    }

    public Font copy() {
        return new Font(atlas, asciiOffset, tilesPerRow, rows, cols, tileWidth, tileHeight, spacing);
    }

    public float[][] getTexCoords(int tile) {
        int offset = asciiOffset;
        return super.getTexCoords(tile - offset);
    }

    // TODO: investigate direct characterAspect access in contrast to possible recalculating it here
    public Vector2f centerLongestLine(TextPosParams p) {
        int longestLine = 0;
        for (String line : p.text.split("\n")) {
            if (line.length() > longestLine)
                longestLine = line.length();
        }

       return new Vector2f((p.font.getCharWidth() * - longestLine)*p.size.x/(p.font.getCharHeight()+1f), p.font.getCharHeight()*p.size.y/((p.font.getCharWidth()+1)/2f));
    }
    public Vector2f centerLongestLineUI(TextPosParams p) {
        Vector2f cam = Test.renderer.getCamera().getPosition();

        int longestLine = 0;
        for (String line : p.text.split("\n")) {
            if (line.length() > longestLine)
                longestLine = line.length();
        }

        return new Vector2f((p.font.getCharWidth() * - longestLine)*p.size.x/(p.font.getCharHeight()+1f) - cam.x, p.font.getCharHeight()*p.size.y/((p.font.getCharWidth()+1)/2f) - cam.y);
    }
    public static Vector2f centerFirstLine(TextPosParams p) {
        int lineLength = p.text.split("\n")[0].length();
        float xComponent = (p.font.getCharWidth() * - lineLength) * p.size.x / (p.font.getCharHeight() + 1f) + p.pos.x / 2;
        float yComponent = p.font.getCharHeight() * p.size.y / ((p.font.getCharWidth() + 1) / 2f) + p.pos.y + p.font.getCharWidth();

        return new Vector2f(xComponent, yComponent);
    }
    public static Vector2f centerFirstLineUI(TextPosParams p) {
        Vector2f cam = Test.renderer.getCamera().getPosition();

        int lineLength = p.text.split("\n")[0].length();
        float xComponent = (p.font.getCharWidth() * - lineLength) * p.size.x / (p.font.getCharHeight() + 1f) + p.pos.x / 2 - cam.x;
        float yComponent = p.font.getCharHeight() * p.size.y / ((p.font.getCharWidth() + 1) / 2f) + p.pos.y + p.font.getCharWidth() - cam.y ;

        return new Vector2f(xComponent, yComponent);
    }

    public int getAsciiOffset() {
        return asciiOffset;
    }

    public void setAsciiOffset(int asciiOffset) {
        this.asciiOffset = asciiOffset;
    }

    public int getCharWidth() {
        return tileWidth;
    }

    public int getCharHeight() {
        return tileHeight;
    }

    public float getCharacterAspect() {
        return (float)tileWidth/tileHeight;
    }

    public Texture getTexture() {
        return atlas;
    }
}
