package Render.Entity.Texturing;

import Render.Shader.Shader;
import org.joml.Vector2f;

public class TextPosParams {
    public static final TextPosParams SHARED = new TextPosParams(new Vector2f(), new Vector2f(), Font.RETRO, "", null);

    public Vector2f pos;
    public Vector2f size;
    public Font font;
    public String text;

    public Vector2f offset = new Vector2f();
    public int maxWidth = Integer.MAX_VALUE;


    public TextPosParams(Vector2f pos, Vector2f size, Font font, String text, Vector2f offset, int maxWidth) {
        this(pos, size, font, text, offset);
        this.maxWidth = maxWidth;
    }

    public TextPosParams(Vector2f pos, Vector2f size, Font font, String text, Vector2f offset) {
        if(text == null || text.isEmpty()) text = " ";
        if(font == null) font = Font.RETRO;
        if(size == null) size = new Vector2f(1, 1);
        if(size.x <= 0) size.x = 1;
        if(size.y <= 0) size.y = 1;
        if(pos == null) pos = new Vector2f(0, 0);

        this.pos = pos;
        this.size = size;
        this.font = font;
        this.text = text;
        if(offset != null) this.offset.set(offset);
    }

    public Vector2f getPos() {
        return pos;
    }

}
