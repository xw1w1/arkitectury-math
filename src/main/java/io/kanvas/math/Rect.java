/*
 * Copyright (c) 2024-2026 Egor Morozov
 * Arkitectury (formerly KGE / Kotlin Game Engine / Kanvas Game Engine / Kanvas)
 *
 * SPDX-License-Identifier: MIT
 */

package io.kanvas.math;

import org.joml.Vector2f;
import org.joml.Vector3f;

/**
 * Float-driven rectangle implementation.
 * <p>A 2D rectangle defined by x, y, width and height.
 *
 * <p>Width and height may be negative (an "inverted" rectangle).
 */
public class Rect {
    /**
     * The X coordinate of the rectangle (same value as xMin).
     */
    public float x;

    /**
     * The Y coordinate of the rectangle (same value as yMin).
     */
    public float y;

    /**
     * The width of the rectangle.
     */
    public float width;

    /**
     * The height of the rectangle.
     */
    public float height;

    /**
     * Creates a new rectangle with all values set to zero.
     */
    public Rect() {
        this(0f, 0f, 0f, 0f);
    }

    /**
     * Creates a new rectangle.
     */
    public Rect(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
     * Creates a new rectangle from position and size vectors.
     */
    public Rect(Vector2f position, Vector2f size) {
        this.x = position.x;
        this.y = position.y;
        this.width = size.x;
        this.height = size.y;
    }

    /**
     * Copy constructor.
     */
    public Rect(Rect other) {
        this.x = other.x;
        this.y = other.y;
        this.width = other.width;
        this.height = other.height;
    }

    /**
     * Creates a rectangle from min/max coordinate values.
     */
    public static Rect minMax(float xmin, float ymin, float xmax, float ymax) {
        return new Rect(xmin, ymin, xmax - xmin, ymax - ymin);
    }

    public float getXMin() {
        return x;
    }

    /**
     * Sets the minimum X coordinate, keeping xMax fixed.
     */
    public void setXMin(float value) {
        float old = x;
        x = value;
        width += old - value;
    }

    public float getYMin() {
        return y;
    }

    /**
     * Sets the minimum Y coordinate, keeping yMax fixed.
     */
    public void setYMin(float value) {
        float old = y;
        y = value;
        height += old - value;
    }

    public float getXMax() {
        return x + width;
    }

    /**
     * Sets the maximum X coordinate, keeping xMin fixed.
     */
    public void setXMax(float value) {
        width = value - x;
    }

    public float getYMax() {
        return y + height;
    }

    /**
     * Sets the maximum Y coordinate, keeping yMin fixed.
     */
    public void setYMax(float value) {
        height = value - y;
    }

    /**
     * The position of the minimum corner of the rectangle.
     */
    public Vector2f getPosition() {
        return new Vector2f(x, y);
    }

    public void setPosition(Vector2f value) {
        x = value.x;
        y = value.y;
    }

    /**
     * The size of the rectangle.
     */
    public Vector2f getSize() {
        return new Vector2f(width, height);
    }

    public void setSize(Vector2f value) {
        width = value.x;
        height = value.y;
    }

    /**
     * The center of the rectangle.
     */
    public Vector2f getCenter() {
        return new Vector2f(x + width / 2f, y + height / 2f);
    }

    /**
     * Setting the center of the rectangle, keeping its size.
     * @param value new center of the rectangle
     */
    public void setCenter(Vector2f value) {
        x = value.x - width / 2f;
        y = value.y - height / 2f;
    }

    public Vector2f getMin() {
        return new Vector2f(getXMin(), getYMin());
    }

    public void setMin(Vector2f value) {
        setXMin(value.x);
        setYMin(value.y);
    }

    /**
     * The maximum corner of the rectangle.
     */
    public Vector2f getMax() {
        return new Vector2f(getXMax(), getYMax());
    }

    public void setMax(Vector2f value) {
        setXMax(value.x);
        setYMax(value.y);
    }

    /**
     * Returns true if the point is inside the rectangle.
     *
     * <p>Inverted rectangles never contain anything.
     */
    public boolean contains(Vector2f point) {
        return contains(point, false);
    }

    /**
     * Returns true if the point is inside the rectangle.
     *
     * @param allowInverse when true, a rectangle with negative width/height is normalized
     *                     before the test.
     */
    public boolean contains(Vector2f point, boolean allowInverse) {
        float xMin = getXMinNormalized(allowInverse);
        float xMax = getXMaxNormalized(allowInverse);
        float yMin = getYMinNormalized(allowInverse);
        float yMax = getYMaxNormalized(allowInverse);
        return point.x >= xMin && point.x < xMax && point.y >= yMin && point.y < yMax;
    }

    /**
     * Returns true if the point is inside the rectangle (the Z component is ignored).
     */
    public boolean contains(Vector3f point) {
        return contains(point, false);
    }

    /**
     * Returns true if the point is inside the rectangle (the Z component is ignored).
     */
    public boolean contains(Vector3f point, boolean allowInverse) {
        return contains(new Vector2f(point.x, point.y), allowInverse);
    }

    /**
     * Returns true if the other rectangle is completely inside this rectangle.
     */
    public boolean contains(Rect rect) {
        return contains(rect, false);
    }

    /**
     * Returns true if the other rectangle is completely inside this rectangle.
     */
    public boolean contains(Rect rect, boolean allowInverse) {
        float xMin = getXMinNormalized(allowInverse);
        float xMax = getXMaxNormalized(allowInverse);
        float yMin = getYMinNormalized(allowInverse);
        float yMax = getYMaxNormalized(allowInverse);
        float rxMin = rect.getXMinNormalized(allowInverse);
        float rxMax = rect.getXMaxNormalized(allowInverse);
        float ryMin = rect.getYMinNormalized(allowInverse);
        float ryMax = rect.getYMaxNormalized(allowInverse);
        return rxMin >= xMin && rxMax <= xMax && ryMin >= yMin && ryMax <= yMax;
    }

    /**
     * Returns true if the other rectangle overlaps this rectangle.
     */
    public boolean overlaps(Rect rect) {
        return overlaps(rect, false);
    }

    /**
     * Returns true if the other rectangle overlaps this rectangle.
     *
     * @param allowInverse when true, inverted rectangles are normalized before the test.
     */
    public boolean overlaps(Rect rect, boolean allowInverse) {
        float xMax = getXMaxNormalized(allowInverse);
        float xMin = getXMinNormalized(allowInverse);
        float yMax = getYMaxNormalized(allowInverse);
        float yMin = getYMinNormalized(allowInverse);
        float rxMin = rect.getXMinNormalized(allowInverse);
        float rxMax = rect.getXMaxNormalized(allowInverse);
        float ryMin = rect.getYMinNormalized(allowInverse);
        float ryMax = rect.getYMaxNormalized(allowInverse);
        return rxMax > xMin && rxMin < xMax && ryMax > yMin && ryMin < yMax;
    }

    /**
     * Returns the closest point on (or inside) the rectangle to the given point.
     */
    public Vector2f closestPoint(Vector2f point) {
        float xMin = getXMinNormalized(true);
        float xMax = getXMaxNormalized(true);
        float yMin = getYMinNormalized(true);
        float yMax = getYMaxNormalized(true);
        return new Vector2f(
                Math.clamp(point.x, xMin, xMax),
                Math.clamp(point.y, yMin, yMax)
        );
    }

    /**
     * Returns a point inside the rectangle defined by a normalized pair of coordinates.
     */
    public static Vector2f normalizedToPoint(Rect rectangle, Vector2f normalizedRectPoint) {
        return new Vector2f(
                rectangle.x + normalizedRectPoint.x * rectangle.width,
                rectangle.y + normalizedRectPoint.y * rectangle.height
        );
    }

    /**
     * Returns the normalized coordinates of a point inside the rectangle. Components are 0
     * when the corresponding rectangle dimension is zero.
     */
    public static Vector2f pointToNormalized(Rect rectangle, Vector2f point) {
        float nx = rectangle.width != 0f ? (point.x - rectangle.x) / rectangle.width : 0f;
        float ny = rectangle.height != 0f ? (point.y - rectangle.y) / rectangle.height : 0f;
        return new Vector2f(nx, ny);
    }

    private float getXMinNormalized(boolean allowInverse) {
        return allowInverse ? Math.min(x, x + width) : x;
    }

    private float getXMaxNormalized(boolean allowInverse) {
        return allowInverse ? Math.max(x, x + width) : x + width;
    }

    private float getYMinNormalized(boolean allowInverse) {
        return allowInverse ? Math.min(y, y + height) : y;
    }

    private float getYMaxNormalized(boolean allowInverse) {
        return allowInverse ? Math.max(y, y + height) : y + height;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Rect other)) return false;
        return x == other.x && y == other.y && width == other.width && height == other.height;
    }

    @Override
    public int hashCode() {
        int result = Float.hashCode(x);
        result = 31 * result + Float.hashCode(y);
        result = 31 * result + Float.hashCode(width);
        result = 31 * result + Float.hashCode(height);
        return result;
    }

    @Override
    public String toString() {
        return String.format(java.util.Locale.ROOT, "(x=%.2f, y=%.2f, width=%.2f, height=%.2f)", x, y, width, height);
    }
}
