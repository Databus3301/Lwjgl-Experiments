package Tests;

import Render.Entity.Entity2D;
import Render.MeshData.Model.ObjModel;
import Render.MeshData.Model.ObjModelParser;
import org.joml.Vector2f;

public class TestCollision extends Test{

    private static final int TESTS = 3;
    private final Entity2D[] entities = new Entity2D[TESTS*3];

    // make them switch direction when they collide
    private final boolean[][] flags = new boolean[TESTS][1];
    private final int[][] fcs = new int[TESTS][1];  // frame counters
    public TestCollision() {
        super();


        ObjModel model = ObjModelParser.parseOBJ("testModel3.obj");
        entities[0] = new Entity2D(-300, +350,  model);
        entities[1] = new Entity2D(-300, +50,   model);
        entities[2] = new Entity2D(-150, 200,   model);

        entities[3] = new Entity2D(+200, 0,     model);
        entities[4] = new Entity2D(+200, 0,     model);
        entities[5] = new Entity2D(+200, 100,   model);

        entities[6] = new Entity2D(-300, -100,  model);
        entities[7] = new Entity2D(-100, -300,  model);
        entities[8] = new Entity2D(-100, -100,  model);

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
        entities[6].setVelocity(+150, -150);
        entities[7].setVelocity(-150, +150);

    }

    @Override
    public void OnUpdate(float dt) {
        super.OnUpdate(dt);
        for (Entity2D entity : entities) {
           entity.translate(entity.getVelocity().mul(dt, new Vector2f()));
        }
        entities[4].rotate(100*dt, 2);
    }


    @Override
    public void OnRender() {
        super.OnRender();

        handleEntities(entities[0], entities[1], entities[2], flags[0], fcs[0]);
        handleEntities(entities[3], entities[4], entities[5], flags[1], fcs[1]);
        handleEntities(entities[6], entities[7], entities[8], flags[2], fcs[2]);


    }

    private void handleEntities(Entity2D e1, Entity2D e2, Entity2D c, boolean[] flag, int[] fc) {
        renderer.drawEntity2D(e1);
        renderer.drawEntity2D(e2);
        renderer.drawCollisionRect(e1);
        renderer.drawCollisionRect(e2);

        boolean[] prevFlag = flag.clone();
        flag[0] = false;

        if(e1.collideRect(e2)) {
            renderer.drawEntity2D(c);
            flag[0] = true;
        }
        if(prevFlag[0] && !flag[0]) {
            fc[0] = 1;
        }
        if(fc[0] == 700) {
            e1.getVelocity().mul(-1);
            e2.getVelocity().mul(-1);
        }
        if(fc[0] == 0) fc[0] = 100000;
        fc[0]++;
    }

    @Override
    public void OnClose() {
        super.OnClose();
    }
}
