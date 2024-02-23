#shader vertex
#version 300 es

precision highp float;

layout (location = 0) in vec3 position;
layout (location = 1) in vec3 textureCoords;
layout (location = 2) in mat4 instanceMatrix;

out vec3 v_position;

uniform mat4 uProj;
uniform mat4 uView;
uniform mat4 uModel;

void main()
{
        mat4 model = uModel * instanceMatrix;
        gl_Position = uProj * uView * model * vec4(position, 1.0f);
        v_position = position.xyz + model[0].xyz + model[3].xyz;
}

#shader fragment
#version 300 es

precision highp float;

layout (location = 0) out vec4 color;

in vec3 v_position;

void main ()
{
        color = vec4(v_position.xy+0.2f, 1.0f, 0.2f);
};
