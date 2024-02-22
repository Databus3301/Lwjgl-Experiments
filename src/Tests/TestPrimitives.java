package Tests;

import org.joml.Vector2f;
import org.joml.Vector4f;

public class TestPrimitives extends Test {
    public TestPrimitives() {
        super();
    }

    @Override
    public void OnUpdate(float dt) {
        super.OnUpdate(dt);
    }

    @Override
    public void OnRender() {
        super.OnRender();

        renderer.drawPoint(new Vector2f(0, 0), 10, new Vector4f(0, 1, 0, 1));

        renderer.drawLine(new Vector2f(-200, 200), new Vector2f(-200, -200), 2, new Vector4f(1, 0, 0, 1));

        renderer.drawLinesConnected(new Vector2f[] {
                new Vector2f(-20 + 100, +20),
                new Vector2f(120 + 100, -20),
                new Vector2f(120 + 100, +20),
                new Vector2f(-20 + 100, -20)
        }, 2, true, new Vector4f(0, 0, 1, 1));

        renderer.drawRect(new Vector2f(-300, -300), new Vector2f(600, 600));
    }

}
