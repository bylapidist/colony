#ifdef GL_ES
precision mediump float;
#endif

uniform vec3 u_color;
uniform sampler2D u_texture0;
uniform sampler2D u_texture1;
uniform float u_time;
uniform vec3 u_resolution;

varying vec2 v_texCoord;
varying vec2 v_uv;

void main() {
    vec2 uv = v_uv;
    // Zooms out by a factor of 2.0
    uv *= 2.0;
    // Shifts every axis by -1.0
    uv -= 1.0;

    // Base color for the effect
    vec3 finalColor = vec3 ( .2, 1., 0. );

    finalColor *= abs(0.05 / (sin( uv.x + sin(uv.y+u_time)* 0.3 ) * 20.0) );

    gl_FragColor = vec4( finalColor, 1.0 );
}