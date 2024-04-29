package Render.MeshData.Shader;

import Render.Renderer;
import Tests.Test;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;


import static org.lwjgl.opengl.GL43.*;

public class Shader {
    public static final Shader DEFAULT = new Shader("default.shader");
    public static final Shader TEXTURING = new Shader("texturing.shader");

    private final int m_RendererID;
    private final HashMap<String, Integer> m_UniformLocationMap = new HashMap<>();
    public Shader(String filepath) {
        if(!filepath.startsWith("res/shaders"))
            filepath = "res/shaders/" + filepath;

        ShaderProgramSource code = parseShader(filepath);
        m_RendererID = createShader(code);

        m_UniformLocationMap.replaceAll((k, v) -> glGetUniformLocation(m_RendererID, k));
    }

    public void bind() {
        Renderer renderer = Test.getRenderer();
        if(!renderer.getCurrentShader().equals(this)) { // if the current shader is not this shader
            renderer.setCurrentShader(this);
            glUseProgram(m_RendererID);
        }
    }
    public void forceBind() {
        glUseProgram(m_RendererID);
    }
    public void unbind() { glUseProgram(0); }
    public void delete() { glDeleteShader(m_RendererID); }

    public void setUniform1i(String name, int i) {
        int location = getUniformLocation(name);
        glUniform1i(location, i);
    }
    public void setUniform1f(String name, float f) {
        int location = getUniformLocation(name);
        glUniform1f(location, f);
    }
    public void setUniform2f(String name, float v0, float v1) {
        int location = getUniformLocation(name);
        glUniform2f(location, v0, v1);
    }
    public void setUniform2f(String name, Vector2f vec) {
        int location = getUniformLocation(name);
        glUniform2f(location, vec.x, vec.y);
    }
    public void setUniform4f(String name, float v0, float v1, float v2, float v3) {
        int location = getUniformLocation(name);
        glUniform4f(location, v0, v1, v2, v3);
    }
    public void setUniform4f(String name, Vector4f vec4) {
        int location = getUniformLocation(name);
        glUniform4f(location, vec4.x, vec4.y, vec4.z, vec4.w);
    }

    private final float[] fb = new float[16];
    public void setUniformMat4f(String name, Matrix4f mat4) {
        int location = getUniformLocation(name);
        glUniformMatrix4fv(location, false, mat4.get(fb));
    }

    private int getUniformLocation(String name) {
        if(m_UniformLocationMap.containsKey(name))
                return m_UniformLocationMap.get(name);
        int location = glGetUniformLocation(m_RendererID, name);
        if(location == -1)
            System.out.println("[Open GL:] Warning: uniform" + name + "doesn't exist");
        m_UniformLocationMap.put(name, location);
        return location;
    }
     private ShaderProgramSource parseShader(String filepath) {
        String[] shaders = new String[2];
        try {
            File shaderCode = new File(filepath);
            Scanner reader = new Scanner(shaderCode);
            int shaderType = 0; // Vertex = 0 | Fragment = 1;
            while(reader.hasNextLine()) {
                String data = reader.nextLine();

                if(data.contains("uniform")) {
                    String[] tokens = data.split("[ ;]");
                    String name = tokens[2];
                    m_UniformLocationMap.put(name, null);
                }

                // Reading code?
                if(!data.contains("#shader")) {
                    shaders[shaderType] += "\n" + data;
                    continue;
                }
                // Type switch
                if (data.contains("vertex")) {
                    shaderType = 0;
                    shaders[0] = "";
                } else if (data.contains("fragment")) {
                    shaderType = 1;
                    shaders[1] = "";
                }
            }
            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("shaderCode file doesn't exist or is unreadable");
            throw new RuntimeException(e);
        }
        return new ShaderProgramSource(shaders[0], shaders[1]);
    }

     private int compileShader(int type, String source) {

        // Compile shader
        int id = glCreateShader(type);
        glShaderSource(id, source);
        glCompileShader(id);
        // Compilation error handling
        int[] result = new int[1];
        glGetShaderiv(id, GL_COMPILE_STATUS, result);
        if(result[0] == GL_FALSE) {
            int[] length = new int[1];
            glGetShaderiv(id, GL_INFO_LOG_LENGTH, length);
            String message = glGetShaderInfoLog(id, length[0]);
            System.out.println("Failed to compile " + (type == GL_VERTEX_SHADER ? "vertex" : "fragment")  + " shader!");
            System.out.println(message);
            glDeleteShader(id);
            return 0;
        }

        return id;
    }
     private int createShader(ShaderProgramSource code) {
        int program = glCreateProgram();
        int vs = compileShader(GL_VERTEX_SHADER, code.VertexSource);
        int fs = compileShader(GL_FRAGMENT_SHADER, code.FragmentSource);

        glAttachShader(program, vs);
        glAttachShader(program, fs);
        glLinkProgram(program);
        glDetachShader(program, vs);
        glDetachShader(program, fs);
        glValidateProgram(program);

        glDeleteShader(vs);
        glDeleteShader(fs);

        return program;
    }

    public int getID() {
        return m_RendererID;
    }

    public boolean hasUniform(String name) {
        return m_UniformLocationMap.containsKey(name);

    }
}
