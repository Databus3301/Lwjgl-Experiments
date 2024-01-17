#shader vertex
#version 330 core

layout(location = 0) in vec4 position;
layout(location = 1) in vec3 color;
layout(location = 2) in vec2 texCoord;
layout(location = 3) in vec3 normals;
layout(location = 4) in float materialID;

out vec3 v_position;

void main()
{
    gl_Position = position;
    v_position = position.xyz;
};


#shader fragment
#version 430 core

layout (location = 0) out vec4 color;

in vec3 v_position;

void main ()
{
    if(v_position.z > 0.1) {
        color = vec4(v_position.z, v_position.z, v_position.z, 1.0);
    } else {
     color = vec4(v_position.z + 0.5, v_position.z + 0.5, v_position.z + 0.5, 0.0);
    }


};

