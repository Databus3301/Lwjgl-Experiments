package Tests;

import Render.Renderer;

public class Test {

    Renderer renderer;

    public Test() {
        renderer = new Renderer();
    }
    public void OnUpdate(float dt) {
    }
    public void OnRender() {
        renderer.Clear();
    }
    public void OnClose() {}
    public void OnKeyInput(long window, int key, int scancode, int action, int mods) {}
}
