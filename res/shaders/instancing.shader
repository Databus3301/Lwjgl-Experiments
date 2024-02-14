#shader vertex
#version 330 core

layout (location = 0) in vec3 position;
layout (location = 1) in vec3 textureCoords;
layout (location = 2) in mat4 instanceMatrix;

out vec3 v_position;

uniform mat4 uProj;
uniform mat4 uView;
uniform mat4 uModel;

void main()
{
        gl_Position = uProj * uView * instanceMatrix *  vec4(position, 1.0);
        v_position = position.xyz + uModel[0].xyz + uModel[3].xyz;
}


#shader fragment
#version 430 core

layout (location = 0) out vec4 color;

in vec3 v_position;

void main ()
{
        color = vec4(v_position.xy+0.2, 1.0, 0.2);
};
