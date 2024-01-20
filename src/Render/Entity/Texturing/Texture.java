package Render.Entity.Texturing;

import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL43.*;

import Render.Shader.Shader;
import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBImage;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class Texture {

    private int m_RendererID = 0;
    private String m_FilePath;
    private IntBuffer m_Width;
    private IntBuffer m_Height;
    private IntBuffer channels;
    private int textureSlot;

    public Texture(String path, int slot) {
        this(path);
        Bind(slot);
    }

    public Texture(String path) {
        m_FilePath = path;

        m_Width = BufferUtils.createIntBuffer(1);
        m_Height = BufferUtils.createIntBuffer(1);
        channels = BufferUtils.createIntBuffer(1);


        m_RendererID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, m_RendererID);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);

        STBImage.stbi_set_flip_vertically_on_load(true);
        ByteBuffer image = STBImage.stbi_load(path, m_Width, m_Height, channels, 0);


        if(image != null) {
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, GetWidth(), GetHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, image);
            glBindTexture(GL_TEXTURE_2D, 0);

            STBImage.stbi_image_free(image);
        } else {
            assert false : "[STBI Error:] (Render.Entity.Camera.Camera.Texturing.Texture) Could not load image '" + path + "'";
        }
    }

    public int  GetHeight(){
        return m_Height.get(0);
    }
    public int GetWidth() {
        return m_Width.get(0);
    }


    public void Bind(int slot){
        textureSlot = slot;
        Bind();
    }

    public void Bind(){
        glActiveTexture(GL_TEXTURE0 + textureSlot);
        glBindTexture(GL_TEXTURE_2D, m_RendererID);
    }

    public void Unbind(){
        glActiveTexture(GL_TEXTURE0 + textureSlot);
        glBindTexture(GL_TEXTURE_2D, 0);
    }
}
