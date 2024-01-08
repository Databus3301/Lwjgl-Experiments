package Render.Shader;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Scanner;


import static org.lwjgl.opengl.GL43.*;

public class Shader {
    private int m_RendererID;
    private String m_FilePath;

    private HashMap<String, Integer> m_UniformLocationMap = new HashMap<>();
    public Shader(String filepath) {
        m_FilePath = filepath;

        ShaderProgramSource code = ParseShader(filepath);
        m_RendererID = CreateShader(code);
    }

    public void Bind() { glUseProgram(m_RendererID); }
    public void Unbind() { glUseProgram(0); }
    public void Delete() { glDeleteShader(m_RendererID); }

    public void SetUniform1i(String name, int i) {
        int location = GetUniformLocation(name);
        glUniform1i(location, i);
    }
    public void SetUniform4f(String name, float v0, float v1, float v2, float v3) {
        int location = GetUniformLocation(name);
        glUniform4f(location, v0, v1, v2, v3);
    }

    public void SetUniformMat4f(String name, Matrix4f mat4) {
        int location = GetUniformLocation(name);
        FloatBuffer fb = BufferUtils.createFloatBuffer(16);
        mat4.get(fb);
        glUniformMatrix4fv(location, false, fb);
    }


    private int GetUniformLocation(String name) {
        if(m_UniformLocationMap.containsKey(name))
                m_UniformLocationMap.get(name);
        int location = glGetUniformLocation(m_RendererID, name);
        if(location == -1)
            System.out.println("[Open GL:] Warning: uniform" + name + "doesn't exist");
        m_UniformLocationMap.put(name, location);
        return location;
    }
     private ShaderProgramSource ParseShader(String filepath) {
        String[] shaders = new String[2];
        try {
            File shaderCode = new File(filepath);
            Scanner reader = new Scanner(shaderCode);
            int shaderType = 0; // Vertex = 0 | Fragment = 1;
            while(reader.hasNextLine()) {
                String data = reader.nextLine();
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
     private int CompileShader(int type, String source) {

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
     private int CreateShader(ShaderProgramSource code) {
        int program = glCreateProgram();
        int vs = CompileShader(GL_VERTEX_SHADER, code.VertexSource);
        int fs = CompileShader(GL_FRAGMENT_SHADER, code.FragmentSource);

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
}
