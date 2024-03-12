package Render.Entity.Texturing;

import org.joml.Matrix4f;
import org.joml.Vector4f;

public class ColorReplacement {
    public static final Matrix4f NO_SWAP_MATRIX = new Matrix4f().zero();
    public static final ColorReplacement NO_SWAP = new ColorReplacement();
    private final Matrix4f cSwaps;
    private int lastPair = 0;
    public ColorReplacement() {
        cSwaps = new Matrix4f().zero();
    }
    public void swap(Vector4f c1, Vector4f c2) {
        if(lastPair == 1) {
            cSwaps.setRow(0, c1);
            cSwaps.setRow(1, c2);
        } else {
            cSwaps.setRow(2, c1);
            cSwaps.setRow(3, c2);
        }
        lastPair = (lastPair + 1) % 2;
    }
    public void swap(Vector4f c1, Vector4f c2, int pair) {
        lastPair = (pair + 1) % 2;
        swap(c1, c2);
    }
    public void replaceRow(Vector4f c, int row) {
        cSwaps.setRow(row, c);
    }
    public Matrix4f getSwappingMatrix() {
        return cSwaps;
    }
    public void getSwappingMatrix(Matrix4f dest) {
        dest.set(cSwaps);
    }
}
