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
//gl_Position = uMVP * position;

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
    vec2 uv = v_ModelPos.xy * (uResolution.x / uResolution.y);

    // SAMPLE TEXTURE
    vec4 texColor = texture(u_Texture, v_TexCoord.xy);
    color = texColor;

    // COLOR REPLACEMENT
    // match also colors close to the replaced ones
    float tolerance = 0.05;
    // change color if it is close to uColors[0]
    vec3 diff = color.xyz - uColors[0].xyz;
    float condition = step(length(diff), tolerance);
    color = mix(color, uColors[1].rgba, condition);
    // change color if it is close to uColors[2]
    diff = color.xyz - uColors[2].xyz;
    condition = step(length(diff), tolerance) * (1.0 - condition); // and wasn't swapped before
    color = mix(color, uColors[3].rgba, condition);

    // DEBUG COLOR || CUSTOM COLOR
    vec3 debugColor = vec3(0.976f, 0.164f, 0.976f);
    condition = step(length(uColor.xyz - debugColor), 0.1);
    color = mix(uColor, color, condition);



    // SCANLINE/PIXELATION EFFECT
    vec2 c = v_ScreenPos.xy * uResolution.xy;
    float xs = step(1., mod(c.x*.3 + sin(uTime*.4)*8., 2.0f)); /**/ xs = 0.0f; //*/ toggle horizontals
    float ys = step(1., mod(c.y*.3 + sin(uTime*.4)*8., 2.0f));
    color = vec4(color.rgb - xs/15. + ys/15., color.a);


};


