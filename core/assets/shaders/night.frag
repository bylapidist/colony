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
 
uniform float MAXOPACITY = 0.75;
uniform float OPACITY = 1.0;
uniform vec3 SKYLIGHT = vec3(0.0, 0.0, 0.3);
uniform vec3 BLOCKLIGHT = vec3(0.0, 0.0, 0.0);

void main() { 
	vec4 texColour = texture2D(u_texture, vTexCoord);
			
	// Colour shifting
	float gray = dot(texColour.rgb, vec3(0.299, 0.587, 0.114));			   // Mix in some gray to desaturate it a bit
	vec3 blockColour = vec3(gray) * SKYLIGHT;
	texColour.rgb = mix(texColour.rgb, BLOCKLIGHT, 0.25);				   // Overlay blocklight
	texColour.rgb = mix(texColour.rgb, blockColour, OPACITY * MAXOPACITY); // Overlay skylight
	
	// Brightness shifting
	float brightness = 1.0 - 0.8 * OPACITY;
	texColour *= vec4(brightness, brightness, brightness, texColour.a);
		
	gl_FragColor = texColour * vColor;
}