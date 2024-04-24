package Tests;

import Render.Entity.Entity2D;
import Render.MeshData.Model.ObjModel;
import org.joml.Vector2f;

public class TestSAT extends Test {

    Entity2D square1, square2;


    public TestSAT() {
        super();

        square1 = new Entity2D(new Vector2f(-200, 10), ObjModel.SQUARE);
        square1.rotate(30, 2);
        square1.scale(75);
        square1.setColor(1, 0, 0, 0.3f);
        square2 = new Entity2D(new Vector2f(+150, -10), ObjModel.SQUARE);
        square2.rotate(-1, 2);
        square2.scale(75);
        square2.setColor(1, 0, 0, 0.3f);

    }

    boolean lastCollision = true;
    @Override
    public void OnUpdate(float dt) {
        super.OnUpdate(dt);

        boolean c = square1.collideRectRotated(square2);
        if(c != lastCollision) {
            System.out.println("Collision: " + c);
            lastCollision = c;
        }

        square1.translate(10*dt, 0);
    }

    @Override
    public void OnRender() {
        super.OnRender();

        renderer.draw(square1);
        renderer.draw(square2);

    }

    @Override
    public void OnClose() {

    }
}
