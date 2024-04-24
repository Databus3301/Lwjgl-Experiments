#shader vertex
#version 300 es

precision mediump float;

layout(location = 0) in vec4 position;
layout(location = 1) in vec2 texCoord;

out vec2 v_TexCoord;
out vec2 v_ModelPos;
out vec2 v_ScreenPos;

uniform mat4 uProj;
uniform mat4 uView;
uniform mat4 uModel;

void main()
{
gl_Position = uProj * uView * uModel * position;

v_TexCoord = texCoord;
v_ModelPos = position.xy;
v_ScreenPos = gl_Position.xy;
};


#shader fragment
#version 300 es

precision lowp float;

layout (location = 0) out vec4 color;

in vec2 v_TexCoord;
in vec2 v_ModelPos;
in vec2 v_ScreenPos;

uniform sampler2D u_Texture;
uniform mat4 uColors; // (uColors[0] -> uColors[1], uColors[2] -> uColors[3])
uniform vec4 uColor;

uniform float uTime;
uniform vec2 uResolution;

void main () {
    // SAMPLE TEXTURE
    vec4 texColor = texture(u_Texture, v_TexCoord);
    color = texColor;
};


