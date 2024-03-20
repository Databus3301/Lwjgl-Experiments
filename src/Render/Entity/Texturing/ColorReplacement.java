package Render.Entity.Texturing;

import org.joml.Matrix4f;
import org.joml.Vector4f;

public class ColorReplacement {
    public static final Matrix4f NO_SWAP_MATRIX = new Matrix4f().zero();
    public static final ColorReplacement NO_SWAP = new ColorReplacement();
    public static final ColorReplacement SWAP_BLACK_WHITE = new ColorReplacement().swap(new Vector4f(0, 0, 0, 1), new Vector4f(1, 1, 1, 1)).swap(new Vector4f(1, 1, 1, 1), new Vector4f(0, 0, 0, 1));
    private final Matrix4f cSwaps;
    private int lastPair = 1;
    public ColorReplacement() {
        cSwaps = new Matrix4f().zero();
    }

    /**
     * Swaps the colors of the two given colors in the next draw call, if the shader supports it <br><br>
     * with colors in <b>rgba</b>
     * @param c1 color one (to be replaced)
     * @param c2 color two (to insert in place of c1)
     */
    public ColorReplacement swap(Vector4f c1, Vector4f c2) {
        if(lastPair == 1) {
            cSwaps.setColumn(0, c1);
            cSwaps.setColumn(1, c2);
        } else {
            cSwaps.setColumn(2, c1);
            cSwaps.setColumn(3, c2);
        }
        lastPair = (lastPair + 1) % 2;
        return this;
    }
    public ColorReplacement swap(Vector4f c1, Vector4f c2, int pair) {
        lastPair = (pair + 1) % 2;
        return swap(c1, c2);
    }
    public void replaceRow(Vector4f c, int row) {
        cSwaps.setColumn(row, c);
    }
    public Matrix4f getSwappingMatrix() {
        return cSwaps;
    }
    public void getSwappingMatrix(Matrix4f dest) {
        dest.set(cSwaps);
    }
}
