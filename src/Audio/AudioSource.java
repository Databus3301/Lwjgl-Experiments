package Audio;

import org.joml.Vector3f;
import org.lwjgl.openal.AL11;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

import static java.lang.Thread.sleep;
import static org.lwjgl.openal.AL10.*;

/**
 * AudioPlayer class to <b>play audio</b> files
 * <br><br>
 * Supported file types: <br>
 * AIFF (.aif, .aiff)<br>
 * AU (.au)<br>
 * SND (.snd)<br>
 * MIDI (.mid, .midi, .rmi)<br>
 * RMF (.rmf)<br>
 * WAVE (.wav)<br>
 */

public class AudioSource {
    private final Vector3f position,velocity;

    public final int source, buffer;
    private AudioClip clip;

    public AudioSource() {
        source = alGenSources();
        buffer = alGenBuffers();


        position = new Vector3f(0, 0, 0);
        velocity = new Vector3f(0, 0, 0);

    }

    public void playSound(String filename) {
        AudioClip audioClip = null;
        try {
            audioClip = AudioLoader.loadWavFile(filename);
        } catch (UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
        }
        assert audioClip != null : "[ERROR] Failed to load audio file";
        playSound(audioClip);
    }

    public void playSound(AudioClip clip) {
        assert clip != null : "[ERROR] Provided <AudioClip> is null";
        this.clip = clip;


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
        this.position.set(position);
        alSource3f(source, AL_POSITION, position.x, position.y, position.z);
    }
    public void setVelocity(Vector3f velocity) {
        this.velocity.set(velocity);
        alSource3f(source, AL_VELOCITY, velocity.x, velocity.y, velocity.z);
    }



    public Vector3f getPosition() {
        return position;
    }
    public boolean isPlaying() {
        return alGetSourcei(source, AL_SOURCE_STATE) == AL_PLAYING;
    }
    public float getPlaybackPercentage() {
        int sampleOffset = AL11.alGetSourcei(source, AL11.AL_SAMPLE_OFFSET);
        int totalSamples = clip.getFrameLength();
        return (float) sampleOffset / totalSamples;
    }
}
