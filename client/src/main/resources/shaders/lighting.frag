#ifdef GL_ES
precision mediump float;
#endif

varying vec4 v_color;
varying vec2 v_texCoords;

uniform sampler2D u_texture;
uniform sampler2D u_lightmap;

void main() {
    vec4 color = texture2D(u_texture, v_texCoords) * v_color;
    vec4 light = texture2D(u_lightmap, v_texCoords);
    gl_FragColor = color * light;
}
