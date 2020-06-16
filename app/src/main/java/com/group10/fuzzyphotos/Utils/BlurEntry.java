package com.group10.fuzzyphotos.Utils;

public class BlurEntry {

    private float scale; // scale of this blur mode
    private int radius; // radius of this blur mode

    /**
     * A blur entry contains a certain scale and radius
     */

    public BlurEntry(float scale, int radius) {
        this.scale = scale;
        this.radius = radius;
    }

    public float getBlurScale() {
        return scale;
    }
    public int getBlurRadius() { return radius; }
}
