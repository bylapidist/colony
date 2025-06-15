#ifdef GL_ES
precision mediump float;
#endif

varying vec4 v_color;
varying vec2 v_texCoords;

uniform sampler2D u_texture;
uniform sampler2D u_normal;
uniform sampler2D u_specular;
uniform vec3 u_lightDir;
uniform vec3 u_viewDir;

void main() {
    vec4 diffuse = texture2D(u_texture, v_texCoords);
    vec3 normal = texture2D(u_normal, v_texCoords).xyz * 2.0 - 1.0;
    vec3 lightDir = normalize(u_lightDir);
    vec3 viewDir = normalize(u_viewDir);
    float diff = max(dot(normal, lightDir), 0.0);
    vec3 halfDir = normalize(lightDir + viewDir);
    float specIntensity = pow(max(dot(normal, halfDir), 0.0), 16.0);
    float specMap = texture2D(u_specular, v_texCoords).r;
    vec3 color = diffuse.rgb * diff + vec3(specIntensity * specMap);
    gl_FragColor = vec4(color, diffuse.a) * v_color;
}
