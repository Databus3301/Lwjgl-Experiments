#shader vertex
#version 300 es

precision highp float;

layout(location = 0) in vec4 position;
layout(location = 1) in vec2 texCoord;

out vec2 v_TexCoord;

uniform mat4 uProj;
uniform mat4 uView;
uniform mat4 uModel;

void main()   
{   
    gl_Position = uProj * uView * uModel * position;
    v_TexCoord = texCoord;
};


#shader fragment
#version 300 es

precision highp float;

layout (location = 0) out vec4 color;

in vec2 v_TexCoord;

uniform sampler2D u_Texture;
uniform mat4 uColors;
uniform vec4 uColor;

void main ()
{

    if(uColor.xyz != vec3(0.976f, 0.164f, 0.976f)) {
        color = uColor;
        return;
    }

    vec4 texColor = texture(u_Texture, v_TexCoord);
    color = texColor;

    float tolerance = 0.05;
    vec3 diff = color.xyz - uColors[0].xyz;
    if(length(diff) < tolerance) {
        color = vec4(uColors[1].xyz, color.a);
    } else {
        vec3 diff = color.xyz - uColors[2].xyz;
        if (length(diff) < tolerance)
        color = vec4(uColors[3].xyz, color.a);
    }
};
