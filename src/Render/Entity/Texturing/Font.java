package Render.Entity.Texturing;

import org.joml.Vector2f;

public class Font {
    public static final Font RETRO = new Font(new Texture("fonts/oldschool_white.png", 0), 32, 18, 5, 18, 7, 9, 0);
    private Vector2f bitmapSize;
    private int asciiOffset;
    private int charsPerRow;
    private int rows;
    private int cols;
    private int charWidth;
    private int charHeight;
    private int charSpacing;
    private Texture fontTexture;

    public Font(Texture font, int asciiOffset, int charsPerRow, int rows, int cols, int charWidth, int charHeight, int charSpacing) {
        this.bitmapSize = new Vector2f(font.getWidth(), font.getHeight());
        this.asciiOffset = asciiOffset;
        this.charsPerRow = charsPerRow;
        this.rows = rows;
        this.cols = cols;
        this.charWidth = charWidth;
        this.charHeight = charHeight;
        this.charSpacing = charSpacing;
        this.fontTexture = font;
    }

    public Font copy() {
        return new Font(fontTexture, asciiOffset, charsPerRow, rows, cols, charWidth, charHeight, charSpacing);
    }

    public float[][] getCharTexCoords(int c) {
        int offset = asciiOffset;
        int row = (c-offset)/charsPerRow;
        int col = (c-offset)%charsPerRow;
        return new float[][]{
                {(col * charWidth) / bitmapSize.x, (bitmapSize.y - (row + 1) * charHeight) / bitmapSize.y}, // top left corner
                {((col + 1) * charWidth) / bitmapSize.x, (bitmapSize.y - (row + 1) * charHeight) / bitmapSize.y}, // top right corner
                {((col + 1) * charWidth) / bitmapSize.x, (bitmapSize.y - row * charHeight) / bitmapSize.y}, // bottom right corner
                {(col * charWidth) / bitmapSize.x, (bitmapSize.y - row * charHeight) / bitmapSize.y}, // bottom left corner
        };
    }

    public Vector2f getBitmapSize() {
        return bitmapSize;
    }

    public void setBitmapSize(Vector2f bitmapSize) {
        this.bitmapSize = bitmapSize;
    }

    public int getAsciiOffset() {
        return asciiOffset;
    }

    public void setAsciiOffset(int asciiOffset) {
        this.asciiOffset = asciiOffset;
    }

    public int getCharsPerRow() {
        return charsPerRow;
    }

    public void setCharsPerRow(int charsPerRow) {
        this.charsPerRow = charsPerRow;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getCols() {
        return cols;
    }

    public void setCols(int cols) {
        this.cols = cols;
    }

    public int getCharWidth() {
        return charWidth;
    }

    public void setCharWidth(int charWidth) {
        this.charWidth = charWidth;
    }

    public int getCharHeight() {
        return charHeight;
    }

    public void setCharHeight(int charHeight) {
        this.charHeight = charHeight;
    }

    public int getCharSpacing() {
        return charSpacing;
    }

    public void setCharSpacing(int charSpacing) {
        this.charSpacing = charSpacing;
    }

    public float getCharacterAspect() {
        return (float)charWidth/charHeight;
    }

    public Texture getTexture() {
        return fontTexture;
    }
}
