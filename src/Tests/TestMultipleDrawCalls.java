package Tests;

import org.joml.Vector2f;

/*
    There are for now 2 components affecting an objects position at the time of drawing,
    for one the vertexbuffer with the actual position values
    and for the other the shader or more specifically the ModelViewProjection Matrix (MVP)
    the Model matrix is responsible for transforming an objects local coordinates in to world coordinates
    and by apllying a transform like here we can move or rather offset an object.

    This is slower than assiging a new VertexBuffer but it's way more memory efficient.
 */

public class TestMultipleDrawCalls extends TestTextures {

    Vector2f offset1, offset2;
    public TestMultipleDrawCalls() {
        super();
        offset1 = new Vector2f(-50, -50);
        offset2 = new Vector2f(50, 50);
    }

    @Override
    public void OnUpdate(float dt) {
        super.OnUpdate(dt);
    }

    @Override
    public void OnRender() {
        super.OnRender();

        // offsetting the <model> matrix
        e.translate(offset1);
        renderer.drawEntity2D(e);
        // offsetting the <model> matrix
        e.translate(offset2);
        renderer.drawEntity2D(e);
    }


}
