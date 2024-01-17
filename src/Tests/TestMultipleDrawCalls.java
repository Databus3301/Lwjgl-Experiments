package Tests;

import org.joml.Vector2f;
import static org.lwjgl.opengl.GL30.*;

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
        shader.Bind();
        setUniforms();

        // offsetting the <model> matrix
        camera.translate(offset1);
        shader.SetUniformMat4f("uModel", camera.calcModelMatrix());
        renderer.Draw(va, ib, shader);

        // offsetting the <model> matrix
        camera.translate(offset2);
        shader.SetUniformMat4f("uModel", camera.calcModelMatrix());
        renderer.Draw(va, ib, shader);

        //glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);
    }


}
