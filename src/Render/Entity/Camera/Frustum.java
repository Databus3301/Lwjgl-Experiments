package Render.Entity.Camera;

import Render.Entity.Entity2D;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class Frustum {
    private Vector4f[] planes = new Vector4f[6];

    public Frustum() {
        for (int i = 0; i < 6; i++) {
            planes[i] = new Vector4f();
        }
    }

    // Math from: https://www.gamedevs.org/uploads/fast-extraction-viewing-frustum-planes-from-world-view-projection-matrix.pdf
    public void update(Matrix4f viewProjectionMatrix) {
        planes[0].set(viewProjectionMatrix.m03() + viewProjectionMatrix.m00(), viewProjectionMatrix.m13() + viewProjectionMatrix.m10(), viewProjectionMatrix.m23() + viewProjectionMatrix.m20(), viewProjectionMatrix.m33() + viewProjectionMatrix.m30()); // Left
        planes[1].set(viewProjectionMatrix.m03() - viewProjectionMatrix.m00(), viewProjectionMatrix.m13() - viewProjectionMatrix.m10(), viewProjectionMatrix.m23() - viewProjectionMatrix.m20(), viewProjectionMatrix.m33() - viewProjectionMatrix.m30()); // Right
        planes[2].set(viewProjectionMatrix.m03() + viewProjectionMatrix.m01(), viewProjectionMatrix.m13() + viewProjectionMatrix.m11(), viewProjectionMatrix.m23() + viewProjectionMatrix.m21(), viewProjectionMatrix.m33() + viewProjectionMatrix.m31()); // Bottom
        planes[3].set(viewProjectionMatrix.m03() - viewProjectionMatrix.m01(), viewProjectionMatrix.m13() - viewProjectionMatrix.m11(), viewProjectionMatrix.m23() - viewProjectionMatrix.m21(), viewProjectionMatrix.m33() - viewProjectionMatrix.m31()); // Top
        planes[4].set(viewProjectionMatrix.m03() + viewProjectionMatrix.m02(), viewProjectionMatrix.m13() + viewProjectionMatrix.m12(), viewProjectionMatrix.m23() + viewProjectionMatrix.m22(), viewProjectionMatrix.m33() + viewProjectionMatrix.m32()); // Near
        planes[5].set(viewProjectionMatrix.m03() - viewProjectionMatrix.m02(), viewProjectionMatrix.m13() - viewProjectionMatrix.m12(), viewProjectionMatrix.m23() - viewProjectionMatrix.m22(), viewProjectionMatrix.m33() - viewProjectionMatrix.m32()); // Far

        for (Vector4f plane : planes) {
            float length = (float) Math.sqrt(plane.x * plane.x + plane.y * plane.y + plane.z * plane.z);
            plane.div(length);
        }
    }

    public boolean isInside(Entity2D entity) {
        Vector3f position = new Vector3f(entity.getPosition().x, entity.getPosition().y, 0);
        float radius = entity.getScale().length() * 0.5f; // Assuming the entity is a sphere for simplicity

        for (Vector4f plane : planes) {
            if (plane.x * position.x + plane.y * position.y + plane.z * position.z + plane.w < -radius) {
                return false;
            }
        }

        return true;
    }
}