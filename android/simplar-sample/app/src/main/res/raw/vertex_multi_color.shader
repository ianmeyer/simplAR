uniform mat4 uMVPMatrix;

attribute vec4 aPosition;
attribute vec4 aColors;

varying vec4 vColors;

void main() {
    vColors = aColors;
    gl_Position = uMVPMatrix * aPosition;
}