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
    vec2 uv = v_ModelPos.xy * (uResolution.x / uResolution.y);

    vec2 uv2 = v_TexCoord;
    // wabern
    uv2.x = uv2.x + sin(uv2.y * 10.0 + uTime * 2.0) * 0.01;

    // offset the uv2.x so that there is a horizontal shift in a range of 10 pixels traveling vertically down the screen only do it in one line and make it hard
    float sinValue = sin(uTime*0.5);
    if(v_ScreenPos.y > sinValue && v_ScreenPos.y < 0.05 + sinValue){ // 0.05 determines line thickness
        uv2.x += uv2.x * 0.01;
    }

    // SAMPLE TEXTURE
    vec4 texColor = texture(u_Texture, uv2);
    color = texColor;

    float isBlack = step(0.4, color.r + color.g + color.b);

    // invert colors
    //color = vec4(1.0-color.xyz, color.a);
    // swizzle color channels
    //color = color.gbra;
    // component based dynamic (de)saturation
    //color.rb *= 0.5 / color.g; // desature red and blue on pixels with green > 0.5 and saturate them otherwise



    // SCANLINE/PIXELATION EFFECT
    vec2 c = v_ScreenPos.xy * uResolution.xy;
    float xs = step(1., mod(c.x*.3 + sinValue*8., 2.0f)); /**/ xs = 0.0f; //*/ toggle horizontals
    float ys = step(1., mod(c.y*.3 + sinValue*8., 2.0f)); // sinValue - alternatively uTime*.4
    color = vec4((color.rgb - xs/15. + ys/15.) * isBlack, color.a);

};


