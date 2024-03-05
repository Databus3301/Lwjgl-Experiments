package Audio;

import org.joml.Vector2f;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import java.nio.ByteBuffer;

public class AudioClip {
    ByteBuffer rawAudioBuffer;
    int frameRate;
    int frameSize;
    int frameLength;
    int sampleRate;
    int sampleSizeInBits;
    int channels;
    AudioFormat.Encoding encoding;
    boolean bigEndian;

    Vector2f position;

    public AudioClip(AudioInputStream audioStream, ByteBuffer rawAudioBuffer) {
        AudioFormat audioFormat = audioStream.getFormat();

        this.rawAudioBuffer = rawAudioBuffer;
        this.frameRate = (int) audioFormat.getFrameRate();
        this.frameSize = audioFormat.getFrameSize();
        this.sampleRate = (int) audioFormat.getSampleRate();
        this.sampleSizeInBits = audioFormat.getSampleSizeInBits();
        this.channels = audioFormat.getChannels();
        this.encoding = audioFormat.getEncoding();
        this.frameLength = (int) audioStream.getFrameLength();
        this.bigEndian = audioFormat.isBigEndian();
        this.frameSize = audioFormat.getFrameSize();
    }

    public ByteBuffer getRawAudioBuffer() {
        return rawAudioBuffer;
    }

    public void setRawAudioBuffer(ByteBuffer rawAudioBuffer) {
        this.rawAudioBuffer = rawAudioBuffer;
    }

    public int getFrameRate() {
        return frameRate;
    }

    public void setFrameRate(int frameRate) {
        this.frameRate = frameRate;
    }

    public int getFrameSize() {
        return frameSize;
    }

    public void setFrameSize(int frameSize) {
        this.frameSize = frameSize;
    }

    public int getFrameLength() {
        return frameLength;
    }

    public void setFrameLength(int frameLength) {
        this.frameLength = frameLength;
    }

    public int getSampleRate() {
        return sampleRate;
    }

    public void setSampleRate(int sampleRate) {
        this.sampleRate = sampleRate;
    }

    public int getSampleSizeInBits() {
        return sampleSizeInBits;
    }

    public void setSampleSizeInBits(int sampleSizeInBits) {
        this.sampleSizeInBits = sampleSizeInBits;
    }

    public int getChannels() {
        return channels;
    }

    public void setChannels(int channels) {
        this.channels = channels;
    }

    public AudioFormat.Encoding getEncoding() {
        return encoding;
    }

    public void setEncoding(AudioFormat.Encoding encoding) {
        this.encoding = encoding;
    }

    public boolean isBigEndian() {
        return bigEndian;
    }

    public void setBigEndian(boolean bigEndian) {
        this.bigEndian = bigEndian;
    }

    public AudioClip(ByteBuffer rawAudioBuffer, int format, int sampleRate) {

    }

}
