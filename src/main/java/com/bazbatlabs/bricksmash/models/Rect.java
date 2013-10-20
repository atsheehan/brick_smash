package com.bazbatlabs.bricksmash.models;

public final class Rect {

    public final Vec2 origin;
    public final Vec2 size;

    public Rect(Vec2 origin, Vec2 size) {
        this.origin = origin;
        this.size = size;
    }

    public float left() { return origin.x; }
    public float right() { return origin.x + size.x; }
    public float bottom() { return origin.y; }
    public float top() { return origin.y + size.y; }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Rect)) {
            return false;
        }
        Rect that = (Rect)o;

        return this.origin == that.origin && this.size == that.size;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + origin.hashCode();
        result = 31 * result + size.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return String.format("(x: %.4f, y: %.4f, w: %.4f, h: %.4f)",
                             origin.x, origin.y, size.x, size.y);
    }
}
