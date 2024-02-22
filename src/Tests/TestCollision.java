package Tests;

import Render.Entity.Entity2D;
import Render.Vertices.Model.ObjModel;
import Render.Vertices.Model.ObjModelParser;
import org.joml.Vector2f;

public class TestCollision extends Test{

    private static final int TESTS = 2;
    Entity2D[] entities = new Entity2D[TESTS*3];
    // make them switch direction when they collide
    boolean[][] flags = new boolean[TESTS*2][1];
    int[][] fcs = new int[TESTS][1];
    public TestCollision() {
        super();

        ObjModel model = ObjModelParser.parseOBJ("res/models/sphere.obj");
        entities[0] = new Entity2D(-300, +150, model);
        entities[1] = new Entity2D(-300, -150, model);
        entities[2] = new Entity2D(new Vector2f(-150, 0), model);

        entities[3] = new Entity2D(+200, 0, model);
        entities[4] = new Entity2D(+200, 0, model);
        entities[5] = new Entity2D(new Vector2f(+200, 100), model);

        int indicator = 0;
        for (int i = 0; i < TESTS*3; i++) {
            indicator++;
            if(indicator == 3) {
                entities[i].scale(20);
                indicator = 0;
            }
            else
                entities[i].scale(50);
        }

        entities[0].setVelocity(0, -150);
        entities[1].setVelocity(0, +150);
        entities[3].setVelocity(-150, 0);
        entities[4].setVelocity(+150, 0);

        for (boolean[] flag : flags) {
            flag = new boolean[]{false};
        }
        for (int[] fc : fcs) {
            fc = new int[]{100000};
        }
    }

    @Override
    public void OnUpdate(float dt) {
        super.OnUpdate(dt);
        for (Entity2D entity : entities) {
           entity.translate(entity.getVelocity().mul(dt, new Vector2f()));
        }
    }


    @Override
    public void OnRender() {
        super.OnRender();
        handleEntities(entities[0], entities[1], entities[2], flags[0], fcs[0]);
        handleEntities(entities[3], entities[4], entities[5], flags[2], fcs[1]);
    }

    private void handleEntities(Entity2D e1, Entity2D e2, Entity2D c, boolean[] flag, int[] fc) {
        renderer.drawEntity2D(e1);
        renderer.drawEntity2D(e2);

        boolean[] prevFlag = flag.clone();
        flag[0] = false;

        if(e1.collideRect(e2)) {
            renderer.drawEntity2D(c);
            flag[0] = true;
        }
        if(prevFlag[0] && !flag[0]) {
            fc[0] = 0;
        }
        if(fc[0] == 700) {
            e1.getVelocity().mul(-1);
            e2.getVelocity().mul(-1);
        }
        fc[0]++;
    }

    @Override
    public void OnClose() {
        super.OnClose();
    }
}
