package Render.Interactable;

import Render.Entity.Texturing.Texture;
import Render.Renderer;
import Render.Shader.Shader;
import Render.Vertices.Model.ObjModel;
import Tests.Test;
import org.joml.Vector2f;

public class Button extends Interactable {
    private Label label = new Label();
    private Label tooltip = new Label();
    private int framesHovered = 0; // TODO: change this to system time / use delta time to make it system independent
    private int hitTime = 300;

    public <T extends Test>Button(T test, Vector2f position, ObjModel model, Texture icon, Shader shader) {
        super(test, position, model, icon, shader);
    }
    //    public <T extends Test>Button(T test, Vector2f position, ObjModel model, Texture texture) {
//        super(test, position, model, texture);
//    }
//    public <T extends Test>Button(T test, Vector2f position, ObjModel model, Shader shader) {
//        super(test, position, model, shader);
//    }
    public <T extends Test>Button(T test, Vector2f position, ObjModel model) {
        super(test, position, model);
    }
    public <T extends Test>Button(T test, Vector2f position) {
        super(test, position, ObjModel.SQUARE);
    }
    public <T extends Test>Button(T test) {
        super(test);
    }


    @Override
    public void onUpdate(float dt, Vector2f mousePos) {
        super.onUpdate(dt, mousePos);

        // if hovered long enough move tooltip to mousepos
        if(state == States.HOVER){
            framesHovered++;
            if(framesHovered > hitTime)
                tooltip.setScreenPosition(mousePos);
        } else {
            framesHovered = 0;
        }
    }

    public Label getLabel() {
        return label;
    }
    public Label getTooltip() {
        return tooltip;
    }
    public Texture getIcon(){
        return texture;
    }
    public int getHitTime() {
        return hitTime;
    }
    public boolean shouldDisplayTooltip(){
        return framesHovered > hitTime;
    }

    public void setLabel(Label label) {
        this.label = label;
    }
    public void setLabel(String text) {
        label.setText(text);
    }
    public void setTooltip(Label tooltip) {
        this.tooltip = tooltip;
    }
    public void setTooltip(String tooltip) {
        this.tooltip.setText(tooltip);
    }
    public void setIcon(Texture texture) {
        this.texture = texture;
    }
    public void setHitTime(int hitTime) {
        this.hitTime = hitTime;
    }
}
