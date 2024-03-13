package Render.Entity.Texturing;

import org.joml.Vector2f;

public class TextPosParams {
    public static final TextPosParams SHARED = new TextPosParams(new Vector2f(), new Vector2f(), Font.RETRO, "");

    public final Vector2f pos = new Vector2f();
    public final Vector2f size = new Vector2f();
    public Font font;
    public String text;

    public TextPosParams(Vector2f pos, Vector2f size, Font font, String text) {
        this.pos.set(pos);
        this.size.set(size);
        this.font = font;
        this.text = text;
    }

    public Vector2f getPos() {
        return pos;
    }
}
