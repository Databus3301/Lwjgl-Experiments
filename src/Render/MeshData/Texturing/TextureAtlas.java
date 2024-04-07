package Render.MeshData.Texturing;

import org.joml.Vector2f;

public class TextureAtlas {
    protected Texture atlas;
    protected int tilesPerRow, rows, cols, tileWidth, tileHeight, spacing;
    protected float aspect, tileAspect;

    public TextureAtlas(Texture atlas, int tilesPerRow, int rows, int cols, int tileWidth, int tileHeight, int spacing) {
        this.atlas = atlas;
        this.tilesPerRow = tilesPerRow;
        this.rows = rows;
        this.cols = cols;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        this.spacing = spacing;

        aspect = atlas.getAspect();
        tileAspect = (float)tileWidth/(float)tileHeight;
    }

    public TextureAtlas copy() {
        return new TextureAtlas(atlas, tilesPerRow, rows, cols, tileWidth, tileHeight, spacing);
    }

    public float[][] getTexCoords(int tile) {
        Vector2f atlasDim = atlas.getDim();
        int row = (tile)/ tilesPerRow;
        int col = (tile)% tilesPerRow;
        return new float[][]{
                {(col * (tileWidth + spacing)) / atlasDim.x, (atlasDim.y - (row + 1) * tileHeight - row*spacing) / atlasDim.y}, // top left corner
                {((col + 1) * tileWidth + col*spacing) / atlasDim.x, (atlasDim.y - (row + 1) * tileHeight - row*spacing) / atlasDim.y}, // top right corner
                {((col + 1) * tileWidth + col*spacing) / atlasDim.x, (atlasDim.y - row * (tileHeight + spacing)) / atlasDim.y}, // bottom right corner
                {(col * (tileWidth + spacing)) / atlasDim.x, (atlasDim.y - row * (tileHeight + spacing)) / atlasDim.y}, // bottom left corner
        };
    }

    public float getAspect(){
        return aspect;
    }
    public float getTileAspect(){
        return tileAspect;
    }

    public Texture getTexture() {
        return atlas;
    }

    public int getTileWidth() {
        return tileWidth;
    }

    public int getTileHeight() {
        return tileHeight;
    }

    public int getCols() {
        return cols;
    }
}
