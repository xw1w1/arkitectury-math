/*
 * Copyright (c) 2024-2026 Egor Morozov
 * Arkitectury (formerly KGE / Kotlin Game Engine / Kanvas Game Engine / Kanvas)
 *
 * SPDX-License-Identifier: MIT
 */

package io.kanvas.math;

/**
 * Representation of RGBA color with one byte per channel.
 */
public class Color32 {

    /**
     * Red component of the color.
     */
    public byte r;

    /**
     * Green component of the color.
     */
    public byte g;

    /**
     * Blue component of the color.
     */
    public byte b;

    /**
     * Alpha component of the color.
     */
    public byte a;

    public Color32() {
        this((byte) 0, (byte) 0, (byte) 0, (byte) 0);
    }

    public Color32(byte r, byte g, byte b, byte a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    /**
     * Builds a color from a packed {@code ARGB} value.
     *
     * @param argb the packed value
     */
    public Color32(int argb) {
        this((byte) ((argb >> 16) & 0xFF), (byte) ((argb >> 8) & 0xFF), (byte) (argb & 0xFF), (byte) ((argb >> 24) & 0xFF));
    }

    /**
     * Returns this color as a packed {@code ARGB} value, the inverse of {@link #Color32(int)}.
     *
     * @return the packed value
     */
    public int toARGB() {
        return (a & 0xFF) << 24 | (r & 0xFF) << 16 | (g & 0xFF) << 8 | (b & 0xFF);
    }

    /**
     * Constructs a {@link Color32} from float {@link Color} components in 0..1, rounding each
     * channel to the nearest byte.
     *
     * @throws IllegalArgumentException if color is null
     */
    public Color32(Color color) {
        if (color == null) {
            throw new IllegalArgumentException("color must not be null");
        }

        this.r = toByte(color.r);
        this.g = toByte(color.g);
        this.b = toByte(color.b);
        this.a = toByte(color.a);
    }

    /**
     * Returns this color as a {@link Color} with float components in 0..1.
     */
    public Color toColor() {
        return new Color((r & 0xFF) / 255f, (g & 0xFF) / 255f, (b & 0xFF) / 255f, (a & 0xFF) / 255f);
    }

    private static byte toByte(float component) {
        float clamped = Math.clamp(component, 0f, 1f);
        return (byte) Math.round(clamped * 255f);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Color32 other)) return false;
        return r == other.r && g == other.g && b == other.b && a == other.a;
    }

    @Override
    public int hashCode() {
        int result = Byte.hashCode(r);
        result = 31 * result + Byte.hashCode(g);
        result = 31 * result + Byte.hashCode(b);
        result = 31 * result + Byte.hashCode(a);
        return result;
    }

    @Override
    public String toString() {
        return "RGBA32(" + (r & 0xFF) + ", " + (g & 0xFF) + ", " + (b & 0xFF) + ", " + (a & 0xFF) + ")";
    }
}
