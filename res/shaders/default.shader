#shader vertex
#version 330 core

layout(location = 0) in vec4 position;

uniform mat4 uProj;
uniform mat4 uView;
uniform mat4 uModel;
uniform vec4 uColor;

out vec4 v_color;

void main()
{
        gl_Position =  uProj * uView * uModel * position;
        v_color = uColor;
};

#shader fragment
#version 430 core

layout (location = 0) out vec4 color;

in vec3 v_position;
in vec4 v_color;

void main ()
{
    color = v_color;
};
