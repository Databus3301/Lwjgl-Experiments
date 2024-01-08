package Tests;

import static org.lwjgl.opengl.GL30.*;


public class TestClearColour extends Test {
    private float[] m_ClearColour;
    public TestClearColour() {
        m_ClearColour = new float[]{1f, 1f, 1f, 1.0f};
    }
    public TestClearColour(float[] clearColour) {
        m_ClearColour = clearColour;
    }

    @Override
    public void OnUpdate(float dt) {
        super.OnUpdate(dt);
    }

    @Override
    public void OnRender() {
        glClearColor(m_ClearColour[0], m_ClearColour[1], m_ClearColour[2], m_ClearColour[3]);
        glClear(GL_COLOR_BUFFER_BIT);
    }

    public void setM_ClearColour(float[] m_ClearColour) {
        this.m_ClearColour = m_ClearColour;
    }
}
