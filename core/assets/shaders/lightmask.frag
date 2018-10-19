//GL ES specific stuff
#ifdef GL_ES
#define LOWP lowp
precision mediump float;
#else
#define LOWP
#endif

uniform sampler2D u_texture;
uniform sampler2D u_lightmask;
uniform vec2 resolution;
varying LOWP vec4 vColor;
varying vec2 vTexCoord;

const float ratio = 1 / 255.0;

uniform float OPACITY = 1.0;
uniform vec3 TINT = vec3(248 * ratio, 228 * ratio, 155 * ratio);

void main() {
	vec4 diffuseColour = texture2D(u_texture, vTexCoord);
	vec4 light = texture2D(u_lightmask, vTexCoord);
	
	diffuseColour.rgb = mix(diffuseColour.rgb, TINT, 0.05);

	vec4 intensity = diffuseColour + light;
	vec4 finalColour = diffuseColour * intensity;
	vec4 outputColour = mix(diffuseColour, finalColour, OPACITY);
	
	gl_FragColor = outputColour * vColor;
}

