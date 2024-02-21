package Tests;

import Render.Entity.Entity2D;
import Render.Vertices.Model.ObjModel;
import Render.Vertices.Model.ObjModelParser;
import org.joml.Vector2f;

/**
 * TestNormalisation
 * successful normalisation to -1, 1 would mean all models have the same width and height (relative to their proportions)
 *
 */
public class TestNormalisation extends Test{
    Entity2D[] entities;
    ObjModel[] models;
    public TestNormalisation() {
        super();
        models = new ObjModel[4];
        models[0] = ObjModelParser.parseOBJ("res/models/square.obj");
        models[1] = ObjModelParser.parseOBJ("res/models/circle.obj");
        models[2] = ObjModelParser.parseOBJ("res/models/testModel3.obj");
        models[3] = ObjModelParser.parseOBJ("res/models/sphere.obj");

        entities = new Entity2D[models.length * models.length];
        int entityIndex = 0;
        for (int i = 0; i < models.length; i++) {
            for (int j = 0; j < models.length; j++) {
                entities[entityIndex] = new Entity2D(models[(j+i)% models.length]);
                entities[entityIndex].scale(50);
                entities[entityIndex++].translate(-150 + 100 * j, i*100-150);
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
        renderer.drawEntities2D(entities);
    }

    @Override
    public void OnClose() {
        super.OnClose();
    }
}
