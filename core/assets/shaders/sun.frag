#ifdef GL_ES
    #define LOWP lowp
    precision mediump float;
#else
    #define LOWP
#endif

uniform vec4 u_color;

void main() {
    gl_FragColor.rgb = u_color.rgb;
}
