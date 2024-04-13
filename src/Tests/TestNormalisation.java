package Tests;

import Render.Entity.Entity2D;
import Render.MeshData.Model.ObjModel;
import Render.MeshData.Model.ObjModelParser;

/**
 * TestNormalisation
 * successful normalisation to -1, 1 would mean all models have the same width and height (relative to their proportions)
 *
 */
public class TestNormalisation extends Test{
    Entity2D[] entities;
    public TestNormalisation() {
        super();
        ObjModel[] models = new ObjModel[]{
            ObjModelParser.parseOBJ("res/models/square.obj"),
            ObjModelParser.parseOBJ("res/models/circle.obj"),
            ObjModelParser.parseOBJ("res/models/testModel3.obj"),
            ObjModelParser.parseOBJ("res/models/sphere.obj"),
            ObjModelParser.parseOBJ("res/models/cam.obj"),
        };

        int scale = 25;
        entities = new Entity2D[models.length * models.length];
        int entityIndex = 0;
        for (int i = 0; i < models.length; i++) {
            for (int j = 0; j < models.length; j++) {
                entities[entityIndex] = new Entity2D(models[(j+i)% models.length]);
                entities[entityIndex].scale(scale);
                int offset = scale*(models.length-1); // auto center everything
                entities[entityIndex++].translate(j * scale*2 - offset , i * scale*2 - offset);
            }
        }
    }

    @Override
    public void OnUpdate(float dt) {
        super.OnUpdate(dt);
    }

    @Override
    public void OnRender() {
        super.OnRender();
        renderer.draw(entities);
    }

    @Override
    public void OnClose() {
        super.OnClose();
    }
}
