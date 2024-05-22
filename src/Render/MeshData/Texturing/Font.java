package Render.MeshData.Texturing;

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
    public static Vector2f centerLongestLine(TextPosParams p) {
        int longestLine = getLongestLine(p);
        return new Vector2f((p.font.getCharWidth() * -longestLine)*p.size.x/(p.font.getCharHeight()+1f) - p.offset.x, p.font.getCharHeight()*p.size.y/((p.font.getCharWidth()+1)/2f) - p.offset.y);
    }
    public static Vector2f centerLongestLine_UI(TextPosParams p) {
        p.offset.set(Test.renderer.getCamera().getPosition());
        return centerLongestLine(p);
    }
    public static Vector2f centerFirstLine(TextPosParams p) {
        int lineLength   = p.text.split("\n")[0].length();
        float xComponent = -getSizedWidth(lineLength, p.font, p.size) + p.pos.x / 2 - p.offset.x;
        float yComponent = p.font.getCharHeight() * p.size.y / ((p.font.getCharWidth() + 1) / 2f) + p.pos.y + p.font.getCharWidth() - p.offset.y ;

        return new Vector2f(xComponent, yComponent);
    }
    public static Vector2f centerFirstLine_UI(TextPosParams p) {
        p.offset.set(Test.renderer.getCamera().getPosition());
        return centerFirstLine(p);
    }

    public static Vector2f centerFirstLine_UI_MaxLength(TextPosParams p) {
        p.offset.set(Test.renderer.getCamera().getPosition());

        int lineLength = p.text.split("\n")[0].length();
        float sizedWidth = getSizedWidth(lineLength, p.font, p.size);
        if(sizedWidth > p.maxWidth)
            p.size.set(p.size.x * p.maxWidth / sizedWidth);

        float xComponent = -getSizedWidth(lineLength, p.font, p.size) + p.pos.x - p.offset.x;
        float yComponent = p.font.getCharHeight() * p.size.y / ((p.font.getCharWidth() + 1) / 2f) + p.pos.y + p.font.getCharWidth() - p.offset.y ;

        return new Vector2f(xComponent, yComponent);
    }

    public static Vector2f centerLongestLine_UI_MaxLength(TextPosParams p) {
        p.offset.set(Test.renderer.getCamera().getPosition());

        int longestLine = getLongestLine(p)+1;
        float sizedWidth = getSizedWidth(longestLine, p.font, p.size);
        if(sizedWidth > p.maxWidth)
            p.size.set(p.size.x * p.maxWidth / sizedWidth);

        float xComponent = -getSizedWidth(longestLine, p.font, p.size) + p.pos.x - p.offset.x;
        float yComponent = p.font.getCharHeight() * p.size.y / ((p.font.getCharWidth() + 1) / 2f) + p.pos.y + p.font.getCharWidth() - p.offset.y ;

        return new Vector2f(xComponent, yComponent);
    }


    public static int getLongestLine(TextPosParams p){
        int longestLine = 0;
        for (String line : p.text.split("\n")) {
            if (line.length() > longestLine)
                longestLine = line.length();
        }
        return longestLine;
    }
    public static int getWidth(int length, Font font) {
        return length * font.getCharWidth();
    }
    public static int getSizedWidth(int length, Font font, Vector2f size) {
        return (int) (length * font.getCharWidth() * size.x / (font.getCharHeight()+0.5f));
    }
    public static int getSizedWidth(TextPosParams p) {
        return (int) (p.text.length() * p.font.getCharWidth() * p.size.x / (p.font.getCharHeight() + 1f));
    }
    public int getWidth(String text) {
        return text.length() * getCharWidth();
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
        return tileAspect;
    }
    public Texture getTexture() {
        return atlas;
    }
}
