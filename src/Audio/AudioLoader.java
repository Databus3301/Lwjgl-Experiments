package Audio;

import org.lwjgl.BufferUtils;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class AudioLoader {

    public static AudioClip loadWavFile(String filename) throws UnsupportedAudioFileException, IOException {
        if(!filename.startsWith("res/audio"))
            filename = "res/audio/" + filename;

        AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File(filename));
        AudioFormat audioFormat = audioStream.getFormat();

        byte[] audioBytes = new byte[(int) (audioStream.getFrameLength() * audioFormat.getFrameSize())];
        audioStream.read(audioBytes);

        ByteBuffer byteBuffer = BufferUtils.createByteBuffer(audioBytes.length).put(audioBytes);
        byteBuffer.order(audioFormat.isBigEndian() ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN);
        byteBuffer.flip();
        return new AudioClip(audioStream, byteBuffer);
    }

    public static AudioClip loadWavFileSafe(String filename){
        try {
            return loadWavFile(filename);
        } catch (UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
