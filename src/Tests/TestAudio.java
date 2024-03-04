package Tests;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.lwjgl.openal.AL11.*;

public class TestAudio extends Test {
    int source;
    int buffer;

    public TestAudio() {
        super();
        source = alGenSources();
        buffer = alGenBuffers();

        try {
            ShortBuffer rawAudioBuffer = loadWavFile("res/audio/sample.wav");
            alBufferData(buffer, AL_FORMAT_STEREO16, rawAudioBuffer, 44100);
        } catch (Exception e) {
            e.printStackTrace();
        }

        alSourcei(source, AL_BUFFER, buffer);
    }


    private ShortBuffer loadWavFile(String filename) throws UnsupportedAudioFileException, IOException {
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File(filename));
        AudioFormat audioFormat = audioStream.getFormat();

        byte[] audioBytes = new byte[(int) (audioStream.getFrameLength() * audioFormat.getFrameSize())];
        audioStream.read(audioBytes);

        ByteBuffer byteBuffer = ByteBuffer.wrap(audioBytes);
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);

        return byteBuffer.asShortBuffer();
    }

    @Override
    public void OnUpdate(float dt) {
        super.OnUpdate(dt);
        alSourcePlay(source);
    }

    @Override
    public void OnRender() {
        super.OnRender();
    }
}
