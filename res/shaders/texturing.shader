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

float dot2( in vec2 v ) { return dot(v,v); }

float sdHeart( in vec2 p ) { // https://www.shadertoy.com/view/3tyBzV
    p.x = abs(p.x);

    if( p.y+p.x>1.0 )
        return sqrt(dot2(p-vec2(0.25,0.75))) - sqrt(2.0)/4.0;
    return sqrt(min(dot2(p-vec2(0.00,1.00)), dot2(p-0.5*max(p.x+p.y,0.0)))) * sign(p.x-p.y);
}

void main () {
    vec2 uv = v_ModelPos.xy * (uResolution.x / uResolution.y);

    // SAMPLE TEXTURE
    vec4 texColor = texture(u_Texture, v_TexCoord);
    color = texColor;

    // COLOR REPLACEMENT
    // match also colors close to the replaced ones
    float tolerance = 0.05;
    // change color if it is close to uColors[0]
    vec3 diff = color.xyz - uColors[0].xyz;
    float condition = step(length(diff), tolerance);
    color = mix(color, vec4(uColors[1].xyz, color.a), condition);
    // change color if it is close to uColors[2]
    diff = color.xyz - uColors[2].xyz;
    condition = step(length(diff), tolerance) * (1.0 - condition); // and wasn't swapped before
    color = mix(color, vec4(uColors[3].xyz, color.a), condition);

    // DEBUG COLOR || CUSTOM COLOR
    vec3 debugColor = vec3(0.976f, 0.164f, 0.976f);
    condition = step(length(uColor.xyz - debugColor), 0.1);
    color = mix(uColor, color, condition);

    // invert colors
    //color = vec4(1.0-color.xyz, color.a);
    // swizzle color channels
    //color = color.gbra;
    // component based dynamic (de)saturation
    //color.rb *= 0.5 / color.g; // desature red and blue on pixels with green > 0.5 and saturate them otherwise

    // HEARTS
    vec2 p = vec2(v_ScreenPos.x * (uResolution.x / uResolution.y) + uResolution.x / uResolution.y - 0.185f, v_ScreenPos.y+1. - 1.85f)*10.;
    //vec2 p = vec2(v_ScreenPos.x * (uResolution.x / uResolution.y), v_ScreenPos.y+(1.*sin(uTime*1.5)*2.+2.))*(1./((sin(uTime*1.5)*2.+2.)*2.));

    float d = sdHeart(p); // heart distance
    d = abs(d) - 0.05; // hollow heart

    float h = 0.1; // tolerance for "inside"
    //float alpha = step(-0.15, d) * (1.-step(-0.1, d)); // hard cut  between -0.15 and -0.10
    float alpha = smoothstep(-0.2, -0.10, d) * (1.0 - smoothstep(-0.10, -0.05, d)); // smooth cut between -0.10 and -0.05
    alpha = smoothstep(h, -h, d) * (1.-alpha); // only color pixels close/inside the heart
    float falloff = 1. / length(p/10.) * 0.2; // falloff effect
    color = mix(color, vec4(1.0, 0.0, 0.0, 1.- smoothstep(1.0, 0.0, falloff)), alpha);


    // SCANLINE/PIXELATION EFFECT
    vec2 c = v_ScreenPos.xy * uResolution.xy;
    float xs = step(1., mod(c.x*.3 + sin(uTime*.4)*8., 2.0f)); /**/ xs = 0.0f; //*/ toggle horizontals
    float ys = step(1., mod(c.y*.3 + sin(uTime*.4)*8., 2.0f));
    color = vec4(color.rgb - xs/15. + ys/15., color.a);


};


