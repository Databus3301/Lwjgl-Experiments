package Audio;

import org.joml.Vector3f;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

import static org.lwjgl.openal.AL10.*;

/**
 * AudioPlayer class to <b>play audio</b> files
 * <br><br>
 * Supported file types: .wav (PCM format)???
 * yet to test other formats
 */

public class AudioSource {
    Vector3f position;
    Vector3f velocity;

    int source;
    int buffer;

    public AudioSource() {
        source = alGenSources();
        buffer = alGenBuffers();
    }

    public void playSound(String filename) {
        AudioClip audioClip = null;
        try {
            audioClip = AudioLoader.loadWavFile(filename);
        } catch (UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
        }

        assert audioClip != null : "Audio file not found!";
        playSound(audioClip);
    }

    public void playSound(AudioClip clip) {
        assert clip != null;
        int formatAL = AL_FORMAT_MONO8;
        if (clip.getChannels() == 1)
            formatAL += clip.getSampleSizeInBits() / 8 - 1;
        else if (clip.getChannels() == 2)
            formatAL += clip.getSampleSizeInBits() / 8 + 1;

        alBufferData(buffer, formatAL, clip.getRawAudioBuffer(), clip.getSampleRate());
        alSourcei(source, AL_BUFFER, buffer);
        alSourcePlay(source);
    }

    public void stopSound() {
        alSourceStop(source);
    }

    public void pauseSound() {
        alSourcePause(source);
    }

    /**
     * Resumes the audio. <br>
     * Might restart the audio from the beginning if not paused.
     */
    public void resumeSound() {
        alSourcePlay(source);
    }

    public void cleanup() {
        stopSound();
        alDeleteSources(source);
        alDeleteBuffers(buffer);
    }

    public void setVolume(float volume) {
        alSourcef(source, AL_GAIN, volume);
    }

    public void setPitch(float pitch) {
        alSourcef(source, AL_PITCH, pitch);
    }

    public void setPosition(Vector3f position) {
        this.position = position;
        alSource3f(source, AL_POSITION, position.x, position.y, position.z);
    }

    public void setVelocity(Vector3f velocity) {
        this.velocity = velocity;
        alSource3f(source, AL_VELOCITY, velocity.x, velocity.y, velocity.z);
    }

}
