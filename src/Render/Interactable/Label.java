package Render.Interactable;

import Render.Entity.Texturing.Font;

public class Label {
    Font font;
    String text;
    float scale;

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

    public void setFont(Font font) {
        this.font = font;
    }
    public void setText(String text) {
        this.text = text;
    }
    public void setScale(float scale) {
        this.scale = scale;
    }
}
