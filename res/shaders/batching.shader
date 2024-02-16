#shader vertex
#version 330 core

layout(location = 0) in vec4 position;

uniform mat4 uProj;
uniform mat4 uView;
uniform mat4 uModel;

out vec3 v_position;

void main()
{
    gl_Position =  uProj * uView * uModel * position;
    v_position = gl_Position.xyz +1;
};

#shader fragment
#version 430 core

layout (location = 0) out vec4 color;

in vec3 v_position;

void main ()
{
    color = vec4(v_position, 1.0);
};
