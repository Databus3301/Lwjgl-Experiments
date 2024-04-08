package Render.MeshData.Texturing;

import Tests.Test;

/**
 * A class to handle animations <br>
 * Works with <i>TextureAtlas</i>'ses where each tile is a frame of the animation <br>
 */
public class Animation {
    private final TextureAtlas atlas;
    private int frame, frames, offset, row;
    private float time, fps;

    boolean loop = true;
    boolean paused = false;

    public <T extends Test> Animation(T scene, TextureAtlas atlas, int row, int offset, float fps, int frames) {
        scene.addUpdateListener((dt, mousePos) -> update(dt));
        this.fps = fps;
        this.row = row;
        this.offset = offset;
        this.frames = frames;
        this.atlas = atlas;
    }
    public Animation(TextureAtlas atlas, int row, int offset, float fps, int frames) {
        this.fps = fps;
        this.row = row;
        this.offset = offset;
        this.frames = frames;
        this.atlas = atlas;
    }

    public void update(float dt){
        if(paused) return;

        time += dt;
        if(time > 1/fps) {
            time = 0;
            frame++;
        }

        if(frame >= frames){
            if(loop)
                frame = 0;
            else
                frame = frames - 1;
        }

    }
    public void reset(){
        frame = 0;
    }
    public void pause(){
        paused = true;
    }
    public void unpause(){
        paused = false;
    }


    public float[][] getTexCoords() {
        return atlas.getTexCoords(frame + offset + row * atlas.getTilesPerRow());
    }
    public int getFrame() {
        return frame;
    }
    public int getFrames() {
        return frames;
    }
    public int getOffset() {
        return offset;
    }
    public int getRow() {
        return row;
    }
    public float getFps() {
        return fps;
    }
    public boolean isLooping() {
        return loop;
    }
    public TextureAtlas getAtlas() {
        return atlas;
    }

    public void setFrame(int frame) {
        this.frame = frame;
    }
    public void setFrames(int frames) {
        this.frames = frames;
    }
    public void setOffset(int offset) {
        this.offset = offset;
    }
    public void setRow(int row) {
        this.row = row;
    }
    public void setFps(float fps) {
        this.fps = fps;
    }
    public void setLoop(boolean loop){
        this.loop = loop;
    }

}
