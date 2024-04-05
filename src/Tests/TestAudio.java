package Tests;

import Audio.AudioClip;
import Audio.AudioSource;

public class TestAudio extends Test {
    private int f;
    private AudioSource audioSource = new AudioSource();
    private AudioClip audioClip;

    public TestAudio() {
        super();
        audioSource.playSound("res/audio/sample.wav");
        audioClip = audioSource.getAudioClip();
    }
    @Override
    public void OnUpdate(float dt) {
        super.OnUpdate(dt);

        f++;

        if (f == 3000)
            audioSource.pauseSound();

        if (f == 5000)
            audioSource.resumeSound();

        if (f == 6000)
            audioSource.stopSound();

        if (f == 7000)
            audioSource.playSound(audioClip);

        if (f == 15000)
            f = 0;

    }

    @Override
    public void OnRender() {
        super.OnRender();
    }

    @Override
    public void OnClose() {
        super.OnClose();
        audioSource.cleanup();
    }
}
