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
 
uniform float MAX_OPACITY = 0.75;
uniform float OPACITY = 1.;
uniform float RADIUS = 0.6;
uniform float SOFTNESS = 0.95;
uniform vec3 TINT = vec3(1.2, 1.0, 0.8);

void main() { 
	vec4 texColor = texture2D(u_texture, vTexCoord);
	vec2 position = (gl_FragCoord.xy / resolution.xy) - vec2(0.5);
	float len = length(position);
	float vignette = smoothstep(RADIUS, RADIUS-SOFTNESS, len);
	texColor.rgb = mix(texColor.rgb, texColor.rgb * vignette, 0.5);
	float gray = dot(texColor.rgb, vec3(0.299, 0.587, 0.114));
	vec3 sepiaColor = vec3(gray) * TINT;
	texColor.rgb = mix(texColor.rgb, sepiaColor, OPACITY*MAX_OPACITY);
	gl_FragColor = texColor * vColor;
}