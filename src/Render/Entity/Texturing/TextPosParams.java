package Render.Entity.Texturing;

import org.joml.Vector2f;

public class TextPosParams {
    public static final TextPosParams SHARED = new TextPosParams(new Vector2f(), new Vector2f(), Font.RETRO, "");

    private final Vector2f pos;
    private final Vector2f size;
    private Font font;
    private String text;

    public TextPosParams(Vector2f pos, Vector2f size, Font font, String text) {
        this.pos = pos;
        this.size = size;
        this.font = font;
        this.text = text;
    }

    public Vector2f getPos() {
        return pos;
    }
    public Font getFont() {
        return font;
    }
    public String getText() {
        return text;
    }
    public Vector2f getSize() {
        return size;
    }

    public void setFont(Font font) {
        this.font = font;
    }
    public void setText(String text) {
        this.text = text;
    }
    public void setPos(Vector2f pos) {
        this.pos.set(pos);
    }
    public void setSize(Vector2f size) {
        this.size.set(size);
    }
}
