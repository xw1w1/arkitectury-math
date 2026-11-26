/*
 * Copyright (c) 2024-2026 Egor Morozov
 * Arkitectury (formerly KGE / Kotlin Game Engine / Kanvas Game Engine / Kanvas)
 *
 * SPDX-License-Identifier: MIT
 */

package io.kanvas.math;

/**
 * Representation of RGBA color, with every component as a {@code float} in {@code 0...1} range.
 */
public class Color {

    /**
     * Red component of the color.
     */
    public float r;

    /**
     * Green component of the color.
     */
    public float g;

    /**
     * Blue component of the color.
     */
    public float b;

    /**
     * Alpha component of the color (0 = transparent, 1 = opaque).
     */
    public float a;

    public Color() {
        this(0f, 0f, 0f, 1f);
    }

    public Color(float r, float g, float b) {
        this(r, g, b, 1f);
    }

    public Color(float r, float g, float b, float a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    /**
     * Builds a color from a packed {@code ARGB} value.
     *
     * <p>Alpha in the top byte, then red, green and blue.
     *
     * @param argb the packed value
     */
    public Color(int argb) {
        this(((argb >> 16) & 0xFF) / 255f, ((argb >> 8) & 0xFF) / 255f, (argb & 0xFF) / 255f, ((argb >> 24) & 0xFF) / 255f);
    }

    /**
     * Returns this color as a packed {@code ARGB} value, the inverse of {@link #Color(int)}.
     *
     * @return the packed value
     */
    public int toARGB() {
        int a = Math.round(clamp01f(this.a) * 255f);
        int r = Math.round(clamp01f(this.r) * 255f);
        int g = Math.round(clamp01f(this.g) * 255f);
        int b = Math.round(clamp01f(this.b) * 255f);
        return a << 24 | r << 16 | g << 8 | b;
    }

    public Color(Color other) {
        this(other.r, other.g, other.b, other.a);
    }

    public static Color clear() {
        return new Color(0f, 0f, 0f, 0f);
    }

    public static Color black() {
        return new Color(0f, 0f, 0f, 1f);
    }

    public static Color white() {
        return new Color(1f, 1f, 1f, 1f);
    }

    public static Color red() {
        return new Color(1f, 0f, 0f, 1f);
    }

    public static Color green() {
        return new Color(0f, 1f, 0f, 1f);
    }

    public static Color blue() {
        return new Color(0f, 0f, 1f, 1f);
    }

    public static Color yellow() {
        return new Color(1f, 0.92156863f, 0.015686275f, 1f);
    }

    public static Color cyan() {
        return new Color(0f, 1f, 1f, 1f);
    }

    public static Color magenta() {
        return new Color(1f, 0f, 1f, 1f);
    }

    public static Color gray() {
        return new Color(0.5f, 0.5f, 0.5f, 1f);
    }

    public static Color grey() {
        return gray();
    }

    /**
     * Constructs a {@link Color} from a 32-bit RGBA hex value ({@code 0xRRGGBBAA}).
     */
    public static Color fromRgba32(int rgba) {
        return new Color(
                ((rgba >> 24) & 0xFF) / 255f,
                ((rgba >> 16) & 0xFF) / 255f,
                ((rgba >> 8) & 0xFF) / 255f,
                (rgba & 0xFF) / 255f
        );
    }

    /**
     * Returns the color as a 32-bit RGBA hex value.
     */
    public int toRgba32() {
        int ri = Math.round(clamp01f(r) * 255f);
        int gi = Math.round(clamp01f(g) * 255f);
        int bi = Math.round(clamp01f(b) * 255f);
        int ai = Math.round(clamp01f(a) * 255f);
        return (ri << 24) | (gi << 16) | (bi << 8) | ai;
    }

    private static float clamp01f(float v) {
        return Math.clamp(v, 0f, 1f);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Color other)) return false;
        return r == other.r && g == other.g && b == other.b && a == other.a;
    }

    @Override
    public int hashCode() {
        int result = Float.hashCode(r);
        result = 31 * result + Float.hashCode(g);
        result = 31 * result + Float.hashCode(b);
        result = 31 * result + Float.hashCode(a);
        return result;
    }

    @Override
    public String toString() {
        return String.format(java.util.Locale.ROOT, "RGBA(%.3f, %.3f, %.3f, %.3f)", r, g, b, a);
    }
}
