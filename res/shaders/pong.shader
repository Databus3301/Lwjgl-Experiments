#shader vertex
#version 330 core

layout(location = 0) in vec4 position;
layout(location = 1) in vec4 color;

uniform mat4 uProj;
uniform mat4 uView;
uniform mat4 uModel;

out vec4 v_Color;

void main()
{
    gl_Position = uProj * uView * uModel * position;
    v_Color = color;
};


#shader fragment
#version 430 core

layout (location = 0) out vec4 color;

in vec4 v_Color;

void main ()
{
    color = v_Color;
};
