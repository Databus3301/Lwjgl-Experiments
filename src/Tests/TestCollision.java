package Tests;

import Render.Entity.Entity2D;
import Render.Vertices.Model.ObjModel;
import Render.Vertices.Model.ObjModelParser;
import org.joml.Vector2f;

public class TestCollision extends Test{

    Entity2D square1, square2, collision1, square3, square4, collision2;
    // make them switch direction when they collide
    boolean flag1, prevFlag1, flag2, prevFlag2 = false;
    int fc = 10000000;
    int fc2 = 10000000;
    public TestCollision() {
        super();

        ObjModel model = ObjModelParser.parseOBJ("res/models/testModel3.obj");
        square1 = new Entity2D(-300, +150, model);
        square2 = new Entity2D(-300, -150, model);
        square3 = new Entity2D(+200, 0, model);
        square4 = new Entity2D(+200, 0, model);

        square1.scale(50);
        square2.scale(50);
        square3.scale(50);
        square4.scale(50);

        square1.setVelocity(0, -150);
        square2.setVelocity(0, +150);
        square3.setVelocity(-150, 0);
        square4.setVelocity(+150, 0);

        collision1 = new Entity2D(new Vector2f(-150, 0), model);
        collision1.scale(20);

        collision2 = new Entity2D(new Vector2f(+200, 100), model);
        collision2.scale(20);
        }

    @Override
    public void OnUpdate(float dt) {
        super.OnUpdate(dt);
        square1.translate(square1.getVelocity().mul(dt, new Vector2f()));
        square2.translate(square2.getVelocity().mul(dt, new Vector2f()));
        square3.translate(square3.getVelocity().mul(dt, new Vector2f()));
        square4.translate(square4.getVelocity().mul(dt, new Vector2f()));
    }


    @Override
    public void OnRender() {
        super.OnRender();
        renderer.drawEntity2D(square1);
        renderer.drawEntity2D(square2);
        renderer.drawEntity2D(square3);
        renderer.drawEntity2D(square4);

        prevFlag1 = flag1;
        flag1 = false;

        if(square1.collideAABB(square2)) {
            renderer.drawEntity2D(collision1);
            flag1 = true;
        }

        prevFlag2 = flag2;
        flag2 = false;
        if(square3.collideAABB(square4)) {
            renderer.drawEntity2D(collision2);
            flag2 = true;
        }

        if(prevFlag1 && !flag1) {
           fc = 0;
        }
        if(prevFlag2 && !flag2) {
            fc2 = 0;
        }

        if(fc == 700) {
            square1.getVelocity().mul(-1);
            square2.getVelocity().mul(-1);
        }
        if(fc2 == 700) {
            square3.getVelocity().mul(-1);
            square4.getVelocity().mul(-1);
        }

        fc++;
        fc2++;

    }

    @Override
    public void OnClose() {
        super.OnClose();
    }
}
