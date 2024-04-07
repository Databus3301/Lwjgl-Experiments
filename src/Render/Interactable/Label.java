package Render.Interactable;

import Render.Entity.Texturing.Font;
import org.joml.Vector2f;

public class Label {
    Font font;
    String text;
    float scale;
    final Vector2f screenPosition = new Vector2f();

    public Label(Font font, String text, int scale) {
        this.font = font;
        this.text = text;
        this.scale = scale;
    }
    public Label(Font font, String text) {
        this.font = font;
        this.text = text;
    }
    public Label() {
        this.font = Font.RETRO;
        this.text = "";
        this.scale = 10;
    }


    public Font getFont() {
        return font;
    }
    public String getText() {
        return text;
    }
    public float getScale() {
        return scale;
    }
    public Vector2f getScreenPosition() {
        return screenPosition;
    }

    public void setFont(Font font) {
        this.font = font;
    }
    public void setText(String text) {
        this.text = text;
    }
    public void setScale(float scale) {
        this.scale = scale;
    }
    public void setScreenPosition(Vector2f position) {
        this.screenPosition.set(position);
    }
}
