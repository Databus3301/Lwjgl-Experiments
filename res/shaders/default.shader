#shader vertex
#version 330 core

layout(location = 0) in vec4 position;

uniform mat4 uProj;
uniform mat4 uView;
uniform mat4 uModel;

void main()
{
        gl_Position =  uProj * uView * uModel * position;
};

#shader fragment
#version 430 core

layout (location = 0) out vec4 color;

in vec3 v_position;

void main ()
{
    color = vec4(1.0, 1.0, 1.0, 1.0);
};
