package Tests;

import Audio.AudioSource;
import org.joml.Vector3f;

public class TestAudio extends Test {
    int f;
    AudioSource audioSource = new AudioSource();

    public TestAudio() {
        super();
        audioSource.playSound("res/audio/sample.wav");
    }
    @Override
    public void OnUpdate(float dt) {
        super.OnUpdate(dt);

        f++;

        if (f == 3000)
            audioSource.pauseSound();

        if (f == 6000)
            audioSource.resumeSound();

        if (f == 9000)
            audioSource.stopSound();

        if (f == 12000)
            audioSource.playSound("res/audio/sample.wav");

        if (f == 15000)
            f = 0;

        //audioSource.setPosition(new Vector3f(0, 0, 0));

    }

    @Override
    public void OnRender() {
        super.OnRender();
    }
}
