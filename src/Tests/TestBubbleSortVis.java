package Tests;

import Render.Window.Window;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class TestBubbleSortVis extends Test {
    private int[] arr;
    private int i = 0;
    private int j = 0;
    private boolean sorted = false;

    public TestBubbleSortVis() {
        super();
        arr = new int[100];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (int) (Math.random() * 100);
        }
    }

    @Override
    public void OnUpdate(float dt) {
        super.OnUpdate(dt);
        // swapping once every frame --> no loop
        if (!sorted) {
            if (arr[j] > arr[j + 1]) {
                int temp = arr[j];
                arr[j] = arr[j + 1];
                arr[j + 1] = temp;
            }
            j++;
            if (j >= arr.length - i - 1) { // don't check already sorted elements
                j = 0;
                i++;
            }
            if (i >= arr.length) {
                sorted = true;
            }
        }
    }

    @Override
    public void OnRender() {
        super.OnRender();
        for (int i = 0; i < arr.length; i++) {
            if(sorted)
                renderer.fillRect(new Vector2f( -Window.dim.x/2f + i * Window.dim.x/2f/ arr.length*2, -Window.dim.y/2f), new Vector2f(Window.dim.x/2f/ arr.length, arr[i] * Window.dim.y/ arr.length), new Vector4f(0, 1, 0,1));
            else
                renderer.fillRect(new Vector2f( -Window.dim.x/2f + i * Window.dim.x/2f/ arr.length*2, -Window.dim.y/2f), new Vector2f(Window.dim.x/2f/ arr.length, arr[i] * Window.dim.y/ arr.length));
        }
    }
}
