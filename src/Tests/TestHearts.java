package Tests;

import Render.Entity.Entity2D;
import Render.MeshData.Model.ObjModel;
import Render.MeshData.Shader.Shader;
import Render.Window;
import org.joml.Vector2f;
import org.joml.Vector2i;

public class TestHearts extends Test{
    Entity2D bg;

    public TestHearts() {
        super();
        bg = new Entity2D(new Vector2f(Window.dim.div(-2f, new Vector2i())), ObjModel.SQUARE, Shader.TEXTURING) {{
            scale(1000);
            setColor(0.2f, 0.2f, 0.2f, 0.1f);
        }};
    }


    @Override
    public void OnRender() {
        super.OnRender();
        renderer.drawUI(bg);
    }
}
