package Render.Interactable;

import Render.Entity.Texturing.Texture;
import Render.Shader.Shader;
import Render.Vertices.Model.ObjModel;
import Tests.Test;
import org.joml.Vector2f;

public class Button extends Interactable {
    Label label = new Label();
    Label tooltip = new Label();

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


    public Label getLabel() {
        return label;
    }
    public Label getTooltip() {
        return tooltip;
    }
    public Texture getIcon(){
        return texture;
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

}
