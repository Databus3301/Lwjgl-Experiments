package Tests;

import Render.Entity.Entity2D;
import Render.MeshData.Model.ObjModel;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;

import static org.lwjgl.glfw.GLFW.*;

public class TestSAT extends Test {

    Entity2D square1, square2;


    public TestSAT() {
        super();

        square1 = new Entity2D(new Vector2f(-200, 10), ObjModel.SQUARE);
        square1.rotate(0, 2);
        square1.scale(75);
        square1.setColor(1, 0, 0, 0.3f);
        square2 = new Entity2D(new Vector2f(+150, -10), ObjModel.SQUARE);
        square2.rotate(180, 2);
        square2.scale(75);
        square2.setColor(1, 0, 0, 0.3f);

    }
    boolean lastCollision = true;
    @Override
    public void OnUpdate(float dt) {
        super.OnUpdate(dt);

        boolean c = collideRectRotatedVis(square1, square2);
        if(c != lastCollision) {
            System.out.println("Collision: " + c);
            lastCollision = c;
        }
    }
    @Override
    public void OnRender() {
        super.OnRender();

        renderer.draw(square1);
        renderer.draw(square2);
    }
    @Override
    public void OnKeyInput(long window, int key, int scancode, int action, int mods) {
        super.OnKeyInput(window, key, scancode, action, mods);
        if(key == GLFW_KEY_SPACE && action == GLFW_PRESS) {
            square1.rotate(1, 2);
        }
        // move square1 based on WASD

        if(key == GLFW_KEY_W && action == GLFW_PRESS) {
            square1.translate(0, 10);
        }
        if(key == GLFW_KEY_S && action == GLFW_PRESS) {
            square1.translate(0, -10);
        }
        if(key == GLFW_KEY_A && action == GLFW_PRESS) {
            square1.translate(-10, 0);
        }
        if(key == GLFW_KEY_D && action == GLFW_PRESS) {
            square1.translate(10, 0);
        }

    }


    private boolean collideRectRotatedVis(Entity2D e1, Entity2D e2) {
        assert e1 != null && e1.getModel() != null && e2.getModel() != null : "[ERROR] (Render.Entity.Entity2D.collideRectRotated) Entity2D has no model";

        final int TOP = 0, RIGHT = 1, BOTTOM = 2, LEFT = 3;

        Vector4f[] corners1, corners2;
        corners1 = calcCorners(e1);
        corners2 = calcCorners(e2);
        corners1 = sortCorners(corners1);
        corners2 = sortCorners(corners2);

        Vector4f edge = corners1[TOP].sub(corners1[RIGHT], new Vector4f());
        Vector4f axisX = new Vector4f(edge.y, -edge.x, 0 , 0).normalize();
        Vector4f axisY = new Vector4f(edge.x, edge.y , 0 , 0).normalize();

        float thisMaxX = maxDot(corners1, axisX);
        float thisMinX = minDot(corners1, axisX);
        float thisMaxY = maxDot(corners1, axisY);
        float thisMinY = minDot(corners1, axisY);

        float otherMaxX = maxDot(corners2, axisX);
        float otherMinX = minDot(corners2, axisX);
        float otherMaxY = maxDot(corners2, axisY);
        float otherMinY = minDot(corners2, axisY);

        boolean intersect1 = (Math.min(thisMaxX, otherMaxX) - Math.max(thisMinX, otherMinX) < 0);
        boolean intersect2 = (Math.min(thisMaxY, otherMaxY) - Math.max(thisMinY, otherMinY) < 0);

        renderer.drawLine(new Vector2f(axisX.x * -1000, axisX.y * -1000), new Vector2f(axisX.x * 1000, axisX.y * 1000), 2, new Vector4f(0, 0, 1, 1));
        renderer.drawLine(new Vector2f(axisY.x * -1000, axisY.y * -1000), new Vector2f(axisY.x * 1000, axisY.y * 1000), 2, new Vector4f(0, 1, 1, 1));

        renderer.drawPoint(new Vector2f(thisMaxX * axisX.x,  thisMaxX * axisX.y), 10, new Vector4f(1, 0, 0, 0.7f));
        renderer.drawPoint(new Vector2f(thisMinX * axisX.x,  thisMinX * axisX.y), 10, new Vector4f(1, 0, 0, 0.7f));
        renderer.drawPoint(new Vector2f(thisMaxY * axisY.x,  thisMaxY * axisY.y), 10, new Vector4f(1, 0, 0, 0.7f));
        renderer.drawPoint(new Vector2f(thisMinY * axisY.x,  thisMinY * axisY.y), 10, new Vector4f(1, 0, 0, 0.7f));

        renderer.drawPoint(new Vector2f(otherMaxX * axisX.x,  otherMaxX * axisX.y), 10, new Vector4f(0, 1, 0, 0.7f));
        renderer.drawPoint(new Vector2f(otherMinX * axisX.x,  otherMinX * axisX.y), 10, new Vector4f(0, 1, 0, 0.7f));
        renderer.drawPoint(new Vector2f(otherMaxY * axisY.x,  otherMaxY * axisY.y), 10, new Vector4f(0, 1, 0, 0.7f));
        renderer.drawPoint(new Vector2f(otherMinY * axisY.x,  otherMinY * axisY.y), 10, new Vector4f(0, 1, 0, 0.7f));

        edge = corners2[TOP].sub(corners2[RIGHT], new Vector4f());
        axisX = new Vector4f(edge.y, -edge.x, 0 , 0).normalize();
        axisY = new Vector4f(edge.x, edge.y , 0 , 0).normalize();

        thisMaxX = maxDot(corners1, axisX);
        thisMinX = minDot(corners1, axisX);
        thisMaxY = maxDot(corners1, axisY);
        thisMinY = minDot(corners1, axisY);

        otherMaxX = maxDot(corners2, axisX);
        otherMinX = minDot(corners2, axisX);
        otherMaxY = maxDot(corners2, axisY);
        otherMinY = minDot(corners2, axisY);

        boolean intersect3 = (Math.min(thisMaxX, otherMaxX) - Math.max(thisMinX, otherMinX) < 0);
        boolean intersect4 = (Math.min(thisMaxY, otherMaxY) - Math.max(thisMinY, otherMinY) < 0);

        renderer.drawLine(new Vector2f(axisX.x * -1000, axisX.y * -1000), new Vector2f(axisX.x * 1000, axisX.y * 1000), 2, new Vector4f(1, 1,  0, 1));
        renderer.drawLine(new Vector2f(axisY.x * -1000, axisY.y * -1000), new Vector2f(axisY.x * 1000, axisY.y * 1000), 2, new Vector4f(0, 1, 0, 1));

        renderer.drawPoint(new Vector2f(thisMaxX * axisX.x,  thisMaxX * axisX.y), 10, new Vector4f(0, 0, 1, 0.7f));
        renderer.drawPoint(new Vector2f(thisMinX * axisX.x,  thisMinX * axisX.y), 10, new Vector4f(0, 0, 1, 0.7f));
        renderer.drawPoint(new Vector2f(thisMaxY * axisY.x,  thisMaxY * axisY.y), 10, new Vector4f(0, 0, 1, 0.7f));
        renderer.drawPoint(new Vector2f(thisMinY * axisY.x,  thisMinY * axisY.y), 10, new Vector4f(0, 0, 1, 0.7f));

        renderer.drawPoint(new Vector2f(otherMaxX * axisX.x,  otherMaxX * axisX.y), 10, new Vector4f(1, 0, 1, 0.7f));
        renderer.drawPoint(new Vector2f(otherMinX * axisX.x,  otherMinX * axisX.y), 10, new Vector4f(1, 0, 1, 0.7f));
        renderer.drawPoint(new Vector2f(otherMaxY * axisY.x,  otherMaxY * axisY.y), 10, new Vector4f(1, 0, 1, 0.7f));
        renderer.drawPoint(new Vector2f(otherMinY * axisY.x,  otherMinY * axisY.y), 10, new Vector4f(1, 0, 1, 0.7f));

        for (int i = 0; i < 4; i++) {
            renderer.drawPoint(new Vector2f(corners1[i].x, corners1[i].y), 10, new Vector4f(1, 1, 1, 1));
            renderer.drawText(String.valueOf(i), new Vector2f(corners1[i].x, corners1[i].y), 10);
        }
        for (int i = 0; i < 4; i++) {
            renderer.drawPoint(new Vector2f(corners2[i].x, corners2[i].y), 10, new Vector4f(1, 1, 1, 1));
            renderer.drawText(String.valueOf(i), new Vector2f(corners2[i].x, corners2[i].y), 10);
        }

        return !(intersect1 || intersect2 || intersect3 || intersect4);
    }


    private Vector4f[] calcCorners(Entity2D entity) {
        Vector4f bb = entity.getModel().getBoundingBox();
        entity.rotate(0.0001f, 2);
        Matrix4f modelMatrix = entity.calcModelMatrix();

        Vector4f[] corners = new Vector4f[]{
                new Vector4f(bb.x, bb.y, 0, 1),
                new Vector4f(bb.x + bb.z, bb.y, 0, 1),
                new Vector4f(bb.x + bb.z, bb.y + bb.w, 0, 1),
                new Vector4f(bb.x, bb.y + bb.w, 0, 1)
        };

        for (Vector4f corner : corners) {
            modelMatrix.transform(corner);
        }
        entity.rotate(-0.0001f, 2);

        return corners;
    }
    private Vector4f[] sortCorners(Vector4f[] corners) {
        Vector4f top    = new Vector4f(Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY, 0, 0);
        Vector4f right  = new Vector4f(Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY, 0, 0);
        Vector4f bottom = new Vector4f(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, 0, 0);
        Vector4f left   = new Vector4f(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, 0, 0);

        for(int i = 0; i < 4; i++) {
            Vector4f corner = new Vector4f(corners[i]);
            if(corner.y > top.y) {
                top = corner;
            }
            if(corner.y < bottom.y) {
                bottom = corner;
            }
            if(corner.x > right.x) {
                right = corner;
            }
            if(corner.x < left.x) {
                left = corner;
            }
        }

        return new Vector4f[] {new Vector4f(top), new Vector4f(right), new Vector4f(bottom), new Vector4f(left)};
    }

    private float maxDot(Vector4f[] corners, Vector4f axis) {
        float max = corners[0].dot(axis);
        for (int i = 1; i < 4; i++) {
            float dot = corners[i].dot(axis);
            if(dot > max) max = dot;
        }
        return max;
    }
    private float minDot(Vector4f[] corners, Vector4f axis) {
        float min = corners[0].dot(axis);
        for (int i = 1; i < 4; i++) {
            float dot = corners[i].dot(axis);
            if(dot < min) min = dot;
        }
        return min;
    }
}
