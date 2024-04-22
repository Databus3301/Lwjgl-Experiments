package Render.MeshData.Texturing;

import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL43.*;

import org.joml.Vector2f;
import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBImage;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;

public class Texture {

    private int m_RendererID;
    private String m_FilePath;
    private IntBuffer m_Width;
    private IntBuffer m_Height;
    private IntBuffer channels;
    private int textureSlot;
    private String name;

    public static HashMap<String, Texture> textures = new HashMap<>();

    public Texture(String path, int slot) {
        this(path);
        bind(slot);
    }

    public Texture(String path) {
        if(!path.startsWith("res/textures"))
            path = "res/textures/" + path;
        m_FilePath = path;



        name = path.substring(path.lastIndexOf('/')+1, path.lastIndexOf('.'));

        m_Width = BufferUtils.createIntBuffer(1);
        m_Height = BufferUtils.createIntBuffer(1);
        channels = BufferUtils.createIntBuffer(1);

        m_RendererID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, m_RendererID);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT); // GL_CLAMP_TO_EDGE
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT); // GL_CLAMP_TO_EDGE

        STBImage.stbi_set_flip_vertically_on_load(true);
        ByteBuffer image = STBImage.stbi_load(path, m_Width, m_Height, channels, 4);


        if(image != null) {
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, getWidth(), getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, image);
            glBindTexture(GL_TEXTURE_2D, 0);

            STBImage.stbi_image_free(image);
        } else {
            assert false : "[STBI Error:] (Render.Entity.Camera.Camera.Texturing.Texture) Could not load image '" + path + "'";
        }

        textures.put(path, this);
    }

    public int getHeight(){
        return m_Height.get(0);
    }
    public int getWidth() {
        return m_Width.get(0);
    }
    public Vector2f getDim() {
        return new Vector2f(getWidth(), getHeight());
    }
    public float getAspect() {
        return (float)getWidth()/(float)getHeight();
    }

    public String getName() {
        return name;
    }

    public void bind(int slot){
        textureSlot = slot;
        bind();
    }

    public void bind(){
        glActiveTexture(GL_TEXTURE0 + textureSlot);
        glBindTexture(GL_TEXTURE_2D, m_RendererID);
    }

    public void unbind(){
        glActiveTexture(GL_TEXTURE0 + textureSlot);
        glBindTexture(GL_TEXTURE_2D, 0);
    }
}
