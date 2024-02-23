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

void main ()
{
    vec4 texColor = texture(u_Texture, v_TexCoord);
    color = vec4(texColor.xyz, 0.5f);
};
