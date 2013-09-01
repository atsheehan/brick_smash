attribute vec4 a_Position;
attribute vec4 a_Color;

varying vec4 v_Color;

void main() {
  v_Color = a_Color;

  gl_Position = vec4(a_Position.x * 2.0 / 320.0 - 1.0,
                     a_Position.y * 2.0 / 480.0 - 1.0,
                     a_Position.z, 1.0);
  gl_PointSize = 10.0;
}
