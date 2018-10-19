//GL ES specific stuff
#ifdef GL_ES
#define LOWP lowp
precision mediump float;
#else
#define LOWP
#endif

uniform sampler2D u_texture;
varying vec2 vTexCoord;

void main() { 
	vec4 sum = vec4(0);
   	int j;
   	int i;

   	for( i= -4 ;i < 4; i++)	{
   	     for (j = -3; j < 3; j++) {
   	         sum += texture2D(u_texture, vTexCoord + vec2(j, i)*0.004) * 0.25;
   	     }
   	}
   	if (texture2D(u_texture, vTexCoord).r < 0.3) {
   		gl_FragColor = sum*sum*0.012 + texture2D(u_texture, vTexCoord);
   	}
  	else {
   		if (texture2D(u_texture, vTexCoord).r < 0.5) {
   	    	gl_FragColor = sum*sum*0.009 + texture2D(u_texture, vTexCoord);
   	    }
   	    else {
   	    	gl_FragColor = sum*sum*0.0075 + texture2D(u_texture, vTexCoord);
   	    }
   	}
}