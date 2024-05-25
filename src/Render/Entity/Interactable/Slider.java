package Render.Entity.Interactable;

import Render.Entity.Entity2D;
import Render.MeshData.Model.ObjModel;
import Render.MeshData.Texturing.Font;
import Tests.Test;
import org.joml.Vector2f;
import org.joml.Vector4f;

import static Tests.Test.mousePos;
import static Tests.Test.renderer;

public class Slider extends Interactable {

    Entity2D sliderBar;
    float value = 0.5f;

    Label l = new Label(Font.RETRO_TRANSPARENT_WHITE, "", 10);

    public Slider(Test scene, Vector2f position) {
        super(scene, position);

        sliderBar = new Entity2D(position, ObjModel.SQUARE.clone());
        sliderBar.scale(100, 10);
        sliderBar.setColor(0.5f, 0.5f, 0.5f, 1);
        color = new Vector4f(0.7f, 0.7f, 0.7f, 1);
        model = ObjModel.SQUARE.clone();

        initSlider();
    }

    public void initSlider() {

        setScale(sliderBar.getScale().y + sliderBar.getScale().y/5f);
        position.set(sliderBar.getPosition());

        this.setDraggedCallback((s) -> {
            Vector2f pos = renderer.screenToWorldCoords(mousePos); // get cursor pos
            pos.sub(s.getScale().div(4, new Vector2f())); // center on cursor
            pos.y = s.getPosition().y; // isolate x pos
            pos.x = Math.max(sliderBar.getPosition().x - sliderBar.getScale().x, Math.min(sliderBar.getPosition().x + sliderBar.getScale().x, pos.x)); // clamp x pos
            s.setPosition(pos.x, pos.y); // set new pos

            value = (s.getPosition().x - sliderBar.getPosition().x + sliderBar.getScale().x) / (sliderBar.getScale().x * 2);
        });
    }


    public void setBarScale(float x, float y)  {
        sliderBar.scale(x, y);
        initSlider();
    }
    public Entity2D getBar() {
        return sliderBar;
    }
    public float getValue() {
        return value;
    }
    public Label getLabel() {
        return l;
    }

    @Override
    public void setPosition(Vector2f position) {
        super.setPosition(position);
        initSlider();
    }

    public void setLabel(Label l) {
        this.l = l;
    }
    public void setLabel(String text) {
        l.setText(text);
    }
}
