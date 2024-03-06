package Tests;

import Render.Window;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.util.ArrayList;

public class TestInsertionSortVis extends Test {
    private ArrayList<Integer> list;
    private ArrayList<Integer> sortedList;

    private int i = 0;
    private int j = 0;
    private boolean sorted = false;

    public TestInsertionSortVis() {
        super();
        list = new ArrayList<>(100);
        sortedList = new ArrayList<>(100);
        for (int i = 0; i < 100; i++) {
            list.add((int) (Math.random() * 100));
        }
        sortedList.add(100000);
    }

    int f = 0;

    @Override
    public void OnUpdate(float dt) {
        super.OnUpdate(dt);
        // implement insertion sort
        if(f == 300) {
            if (!list.isEmpty()) {
                for (int k = 0; k < sortedList.size(); k++) {
                    if (list.get(i) < sortedList.get(k)) {
                        sortedList.add(k, list.get(i));
                        list.remove(i);
                        break;
                    }
                }
            } else
                sorted = true;
            f = 0;
        }
        f++;
    }

    @Override
    public void OnRender() {
        super.OnRender();
        for (int l = 0; l < sortedList.size()-1; l++) {
            if(sorted)
                renderer.fillRect(new Vector2f( -Window.dim.x/2f + l * Window.dim.x/2f/ sortedList.size()*2, -Window.dim.y/2f), new Vector2f(Window.dim.x/2f/ sortedList.size(), (float) (sortedList.get(l) * Window.dim.y) / sortedList.size()), new Vector4f(0, 1, 0,1));
            else
                renderer.fillRect(new Vector2f( -Window.dim.x/2f + l * Window.dim.x/2f/ sortedList.size()*2, -Window.dim.y/2f), new Vector2f(Window.dim.x/2f/ sortedList.size(), (float) (sortedList.get(l) * Window.dim.y) / sortedList.size()));
        }
    }
}
