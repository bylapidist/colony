//GL ES specific stuff
#ifdef GL_ES
#define LOWP lowp
precision mediump float;
#else
#define LOWP
#endif

uniform sampler2D u_texture;
uniform vec2 resolution;
varying LOWP vec4 vColor;
varying vec2 vTexCoord;

const float blurSize = 1.0/512.0;

void main() { 
	vec4 sum = vec4(0.0);
 
    // blur in y (vertical)
    // take nine samples, with the distance blurSize between them
    sum += texture2D(u_texture, vec2(vTexCoord.x - 4.0*blurSize, vTexCoord.y)) * 0.05;
    sum += texture2D(u_texture, vec2(vTexCoord.x - 3.0*blurSize, vTexCoord.y)) * 0.09;
    sum += texture2D(u_texture, vec2(vTexCoord.x - 2.0*blurSize, vTexCoord.y)) * 0.12;
    sum += texture2D(u_texture, vec2(vTexCoord.x - blurSize, vTexCoord.y)) * 0.15;
    sum += texture2D(u_texture, vec2(vTexCoord.x, vTexCoord.y)) * 0.16;
    sum += texture2D(u_texture, vec2(vTexCoord.x + blurSize, vTexCoord.y)) * 0.15;
    sum += texture2D(u_texture, vec2(vTexCoord.x + 2.0*blurSize, vTexCoord.y)) * 0.12;
    sum += texture2D(u_texture, vec2(vTexCoord.x + 3.0*blurSize, vTexCoord.y)) * 0.09;
    sum += texture2D(u_texture, vec2(vTexCoord.x + 4.0*blurSize, vTexCoord.y)) * 0.05;
    
 
    gl_FragColor = sum * vColor;		
		
	//gl_FragColor = texColour * vColor;
}