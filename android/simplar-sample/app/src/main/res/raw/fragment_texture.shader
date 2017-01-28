precision mediump float;

uniform sampler2D sTexture;

varying vec2 vTextureCoords;

void main() {
    gl_FragColor = texture2D( sTexture, vTextureCoords);
}