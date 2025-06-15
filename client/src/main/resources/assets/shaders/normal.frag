#ifdef GL_ES
precision mediump float;
#endif

varying vec4 v_color;
varying vec2 v_texCoords;

uniform sampler2D u_texture;
uniform sampler2D u_normal;
uniform sampler2D u_specular;

void main() {
    vec4 diffuse = texture2D(u_texture, v_texCoords);
    vec3 normal = texture2D(u_normal, v_texCoords).xyz * 2.0 - 1.0;
    vec3 lightDir = normalize(vec3(0.0, 0.0, 1.0));
    float diff = max(dot(normal, lightDir), 0.0);
    vec3 spec = texture2D(u_specular, v_texCoords).rgb;
    vec3 color = diffuse.rgb * diff + spec;
    gl_FragColor = vec4(color, diffuse.a) * v_color;
}
