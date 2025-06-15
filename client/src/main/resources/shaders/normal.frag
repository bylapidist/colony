#ifdef GL_ES
precision mediump float;
#endif

varying vec4 v_color;
varying vec2 v_texCoord;

uniform sampler2D u_texture;
uniform sampler2D u_normal;
uniform sampler2D u_specular;
uniform vec3 u_lightDir;

void main() {
    vec4 diff = texture2D(u_texture, v_texCoord);
    vec3 n = texture2D(u_normal, v_texCoord).xyz * 2.0 - 1.0;
    float d = max(0.0, dot(n, normalize(u_lightDir)));
    float s = texture2D(u_specular, v_texCoord).r;
    vec3 color = diff.rgb * d + vec3(s);
    gl_FragColor = vec4(color, diff.a) * v_color;
}
