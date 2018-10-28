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
 
const float RADIUS = 0.75;
const float SOFTNESS = 0.45;
const vec3 SEPIA = vec3(1.2, 1.0, 0.8);

void main() { 
	vec4 texColor = texture2D(u_texture, vTexCoord);
	vec2 position = (gl_FragCoord.xy / resolution.xy) - vec2(0.5);
	float len = length(position);
	float vignette = smoothstep(RADIUS, RADIUS-SOFTNESS, len);
	texColor.rgb = mix(texColor.rgb, texColor.rgb * vignette, 0.5);
	float gray = dot(texColor.rgb, vec3(0.299, 0.587, 0.114));
	vec3 sepiaColor = vec3(gray) * SEPIA;
	texColor.rgb = mix(texColor.rgb, sepiaColor, 0.75);
	gl_FragColor = texColor * vColor;
}