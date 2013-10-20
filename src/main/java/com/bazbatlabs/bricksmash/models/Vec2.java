package com.bazbatlabs.bricksmash.models;

public final class Vec2 {

    public static final Vec2 ZERO = new Vec2(0.0f, 0.0f);

    public final float x;
    public final float y;

    public Vec2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vec2 add(Vec2 that) {
        return new Vec2(this.x + that.x, this.y + that.y);
    }

    public Vec2 subtract(Vec2 that) {
        return new Vec2(this.x - that.x, this.y - that.y);
    }

    public Vec2 scale(float factor) {
        return new Vec2(this.x * factor, this.y * factor);
    }

    public Vec2 rotate(float radians) {
        return new Vec2((float)(this.x * Math.cos(radians) - this.y * Math.sin(radians)),
                        (float)(this.x * Math.sin(radians) + this.y * Math.cos(radians)));
    }

    public boolean isWithinRect(Vec2 origin, Vec2 size) {
        return (this.x >= origin.x && this.x <= origin.x + size.x) &&
            (this.y >= origin.y && this.y <= origin.y + size.y);
    }

    public static Vec2 fromAngle(float angle) {
        float x = (float)Math.cos(angle);
        float y = (float)Math.sin(angle);

        return new Vec2(x, y);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Vec2)) {
            return false;
        }
        Vec2 that = (Vec2)o;

        return floatEquals(this.x, that.x) && floatEquals(this.y, that.y);
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + Float.floatToIntBits(x);
        result = 31 * result + Float.floatToIntBits(y);
        return result;
    }

    @Override
    public String toString() {
        return String.format("(x: %.4f, y: %.4f)", x, y);
    }

    private boolean floatEquals(float a, float b) {
        return Math.abs(a - b) < 0.000001;
    }
}
