package com.bazbatlabs.smashballs.models;

public final class Color {

    public static final Color RED = new Color(1.0f, 0.0f, 0.0f, 1.0f);
    public static final Color GREEN = new Color(0.0f, 1.0f, 0.0f, 1.0f);
    public static final Color BLUE = new Color(0.0f, 0.0f, 1.0f, 1.0f);
    public static final Color YELLOW = new Color(1.0f, 1.0f, 0.0f, 1.0f);
    public static final Color WHITE = new Color(1.0f, 1.0f, 1.0f, 1.0f);
    public static final Color LIGHT_GRAY = new Color(0.66f, 0.66f, 0.66f, 1.0f);
    public static final Color DARK_GRAY = new Color(0.33f, 0.33f, 0.33f, 1.0f);
    public static final Color BLACK = new Color(0.0f, 0.0f, 0.0f, 1.0f);
    public static final Color CLEAR = new Color(0.0f, 0.0f, 0.0f, 0.0f);

    public final float r;
    public final float g;
    public final float b;
    public final float a;

    public Color(float r, float g, float b, float a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Color)) {
            return false;
        }
        Color that = (Color)o;

        return (this.r == that.r) && (this.g == that.g) &&
            (this.b == that.b) && (this.a == that.a);
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + Float.floatToIntBits(r);
        result = 31 * result + Float.floatToIntBits(g);
        result = 31 * result + Float.floatToIntBits(b);
        result = 31 * result + Float.floatToIntBits(a);
        return result;
    }

    @Override
    public String toString() {
        return String.format("(r: %.3f, g: %.3f, b: %.3f, a: %.3f)", r, g, b, a);
    }
}
