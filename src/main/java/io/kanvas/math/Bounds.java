/*
 * Copyright (c) 2024-2026 Egor Morozov
 * Arkitectury (formerly KGE / Kotlin Game Engine / Kanvas Game Engine / Kanvas)
 *
 * SPDX-License-Identifier: MIT
 */

package io.kanvas.math;

import java.util.Objects;
import org.joml.Matrix4f;
import org.joml.Vector3f;

/**
 * AABB (Axis Aligned Bounding Box), represented by its center and extends.
 */
public final class Bounds {

    /**
     * Center of the bounding box.
     */
    public Vector3f center;

    /**
     * Half-sizes of the bounding box along each axis.
     */
    public Vector3f extents;

    /**
     * Creates an empty bounding box.
     */
    public Bounds() {
        this.center = new Vector3f();
        this.extents = new Vector3f();
    }

    /**
     * Creates a bounding box with the given {@code center} and {@code size}.
     *
     * <p>Sizes should be full (for example, if you want to have exactly 1m X 1m X 1m cube, its {@code sizes} should be 1).
     *
     * @param center box center
     * @param size   width, height, and depth
     */
    public Bounds(Vector3f center, Vector3f size) {
        Objects.requireNonNull(center, "center");
        Objects.requireNonNull(size, "size");
        this.center = new Vector3f(center);
        this.extents = new Vector3f(
                Math.max(0f, size.x * 0.5f),
                Math.max(0f, size.y * 0.5f),
                Math.max(0f, size.z * 0.5f)
        );
    }

    public Bounds(Bounds other) {
        Objects.requireNonNull(other, "other");
        this.center = new Vector3f(other.center);
        this.extents = new Vector3f(other.extents);
    }

    /**
     * Creates a bounding box directly from center and half-extents.
     *
     * @param center  box center
     * @param extents half-size along each axis
     * @return the constructed bounds
     */
    public static Bounds fromCenterExtents(Vector3f center, Vector3f extents) {
        Objects.requireNonNull(center, "center");
        Objects.requireNonNull(extents, "extents");
        Bounds bounds = new Bounds();
        bounds.center.set(center);
        bounds.extents.set(
                Math.max(0f, extents.x),
                Math.max(0f, extents.y),
                Math.max(0f, extents.z)
        );
        return bounds;
    }

    /**
     * Creates a bounding box with {@code min} and {@code max} values.
     *
     * @param min minimum corner
     * @param max maximum corner
     * @return the constructed bounds
     */
    public static Bounds fromMinMax(Vector3f min, Vector3f max) {
        Objects.requireNonNull(min, "min");
        Objects.requireNonNull(max, "max");
        Bounds bounds = new Bounds();
        bounds.setMinMax(min, max);
        return bounds;
    }

    /**
     * Full size of this {@link Bounds}.
     */
    public Vector3f size() {
        return size(new Vector3f());
    }

    /**
     * Writes the full size into {@code destination}.
     *
     * @param destination target vector
     * @return {@code destination}
     */
    public Vector3f size(Vector3f destination) {
        Objects.requireNonNull(destination, "destination");
        return destination.set(extents.x * 2f, extents.y * 2f, extents.z * 2f);
    }

    public void setSize(Vector3f size) {
        Objects.requireNonNull(size, "size");
        extents.set(
                Math.max(0f, size.x * 0.5f),
                Math.max(0f, size.y * 0.5f),
                Math.max(0f, size.z * 0.5f)
        );
    }

    /**
     * Minimum corner of this {@link Bounds}.
     *
     * @return a new vector holding the minimum point
     */
    public Vector3f min() {
        return min(new Vector3f());
    }

    /**
     * Writes the minimum corner into {@code destination}.
     *
     * @param destination target vector
     * @return {@code destination}
     */
    public Vector3f min(Vector3f destination) {
        Objects.requireNonNull(destination, "destination");
        return destination.set(center).sub(extents);
    }

    /**
     * Maximum corner of this {@link Bounds}.
     *
     * @return a new vector holding the maximum point
     */
    public Vector3f max() {
        return max(new Vector3f());
    }

    /**
     * Writes the maximum corner into {@code destination}.
     *
     * @param destination target vector
     * @return {@code destination}
     */
    public Vector3f max(Vector3f destination) {
        Objects.requireNonNull(destination, "destination");
        return destination.set(center).add(extents);
    }

    public void setMinMax(Vector3f min, Vector3f max) {
        Objects.requireNonNull(min, "min");
        Objects.requireNonNull(max, "max");
        float minX = Math.min(min.x, max.x);
        float minY = Math.min(min.y, max.y);
        float minZ = Math.min(min.z, max.z);
        float maxX = Math.max(min.x, max.x);
        float maxY = Math.max(min.y, max.y);
        float maxZ = Math.max(min.z, max.z);
        center.set((minX + maxX) * 0.5f, (minY + maxY) * 0.5f, (minZ + maxZ) * 0.5f);
        extents.set((maxX - minX) * 0.5f, (maxY - minY) * 0.5f, (maxZ - minZ) * 0.5f);
    }

    /**
     * Grow this {@link Bounds} to include some point.
     *
     * @param point point to enclose
     */
    public void encapsulate(Vector3f point) {
        Objects.requireNonNull(point, "point");
        float minX = Math.min(center.x - extents.x, point.x);
        float minY = Math.min(center.y - extents.y, point.y);
        float minZ = Math.min(center.z - extents.z, point.z);
        float maxX = Math.max(center.x + extents.x, point.x);
        float maxY = Math.max(center.y + extents.y, point.y);
        float maxZ = Math.max(center.z + extents.z, point.z);
        center.set((minX + maxX) * 0.5f, (minY + maxY) * 0.5f, (minZ + maxZ) * 0.5f);
        extents.set((maxX - minX) * 0.5f, (maxY - minY) * 0.5f, (maxZ - minZ) * 0.5f);
    }

    /**
     * Grow this {@link Bounds} to include the other one.
     *
     * @param other bounds to enclose
     */
    public void encapsulate(Bounds other) {
        Objects.requireNonNull(other, "other");
        float minX = Math.min(center.x - extents.x, other.center.x - other.extents.x);
        float minY = Math.min(center.y - extents.y, other.center.y - other.extents.y);
        float minZ = Math.min(center.z - extents.z, other.center.z - other.extents.z);
        float maxX = Math.max(center.x + extents.x, other.center.x + other.extents.x);
        float maxY = Math.max(center.y + extents.y, other.center.y + other.extents.y);
        float maxZ = Math.max(center.z + extents.z, other.center.z + other.extents.z);
        center.set((minX + maxX) * 0.5f, (minY + maxY) * 0.5f, (minZ + maxZ) * 0.5f);
        extents.set((maxX - minX) * 0.5f, (maxY - minY) * 0.5f, (maxZ - minZ) * 0.5f);
    }

    /**
     * Expand this {@link Bounds} by {@code amount}.
     *
     * @param amount amount
     */
    public void expand(float amount) {
        float half = amount * 0.5f;
        extents.set(
                Math.max(0f, extents.x + half),
                Math.max(0f, extents.y + half),
                Math.max(0f, extents.z + half)
        );
    }

    /**
     * Expand this {@link Bounds} by {@code amount} per axis.
     *
     * @param amount per-axis full size delta
     */
    public void expand(Vector3f amount) {
        Objects.requireNonNull(amount, "amount");
        extents.set(
                Math.max(0f, extents.x + amount.x * 0.5f),
                Math.max(0f, extents.y + amount.y * 0.5f),
                Math.max(0f, extents.z + amount.z * 0.5f)
        );
    }

    public boolean contains(Vector3f point) {
        Objects.requireNonNull(point, "point");
        return Math.abs(point.x - center.x) <= extents.x
                && Math.abs(point.y - center.y) <= extents.y
                && Math.abs(point.z - center.z) <= extents.z;
    }

    public boolean intersects(Bounds other) {
        Objects.requireNonNull(other, "other");
        return Math.abs(center.x - other.center.x) <= (extents.x + other.extents.x)
                && Math.abs(center.y - other.center.y) <= (extents.y + other.extents.y)
                && Math.abs(center.z - other.center.z) <= (extents.z + other.extents.z
        );
    }

    public Vector3f closestPoint(Vector3f point) {
        Objects.requireNonNull(point, "point");
        return new Vector3f(
                Math.clamp(point.x, center.x - extents.x, center.x + extents.x),
                Math.clamp(point.y, center.y - extents.y, center.y + extents.y),
                Math.clamp(point.z, center.z - extents.z, center.z + extents.z)
        );
    }

    /**
     * Returns the squared distance between {@code point} and this box.
     *
     * @param point point
     * @return squared distance
     */
    public float sqrDistance(Vector3f point) {
        Objects.requireNonNull(point, "point");
        float dx = Math.max(0f, Math.abs(point.x - center.x) - extents.x);
        float dy = Math.max(0f, Math.abs(point.y - center.y) - extents.y);
        float dz = Math.max(0f, Math.abs(point.z - center.z) - extents.z);
        return dx * dx + dy * dy + dz * dz;
    }

    /**
     * Transforms this {@link Bounds} by an affine {@code matrix} and returns the tightest
     * enclosing axis-aligned box in the target space.
     *
     * @param matrix affine transformation matrix
     * @return transformed AABB
     */
    public Bounds transform(Matrix4f matrix) {
        Objects.requireNonNull(matrix, "matrix");
        return transform(matrix, new Bounds());
    }

    /**
     * Transforms this {@link Bounds} by an affine {@code matrix} into {@code destination}
     * without heap allocation.
     *
     * @param matrix      affine transformation matrix
     * @param destination target bounds
     * @return {@code destination}
     */
    public Bounds transform(Matrix4f matrix, Bounds destination) {
        Objects.requireNonNull(matrix, "matrix");
        Objects.requireNonNull(destination, "destination");
        float cx = center.x;
        float cy = center.y;
        float cz = center.z;
        float ex = extents.x;
        float ey = extents.y;
        float ez = extents.z;

        float newCenterX = matrix.m00() * cx + matrix.m10() * cy + matrix.m20() * cz + matrix.m30();
        float newCenterY = matrix.m01() * cx + matrix.m11() * cy + matrix.m21() * cz + matrix.m31();
        float newCenterZ = matrix.m02() * cx + matrix.m12() * cy + matrix.m22() * cz + matrix.m32();

        float newExtX = Math.abs(matrix.m00()) * ex + Math.abs(matrix.m10()) * ey + Math.abs(matrix.m20()) * ez;
        float newExtY = Math.abs(matrix.m01()) * ex + Math.abs(matrix.m11()) * ey + Math.abs(matrix.m21()) * ez;
        float newExtZ = Math.abs(matrix.m02()) * ex + Math.abs(matrix.m12()) * ey + Math.abs(matrix.m22()) * ez;

        destination.center.set(newCenterX, newCenterY, newCenterZ);
        destination.extents.set(newExtX, newExtY, newExtZ);
        return destination;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Bounds other)) {
            return false;
        }
        return Objects.equals(center, other.center) && Objects.equals(extents, other.extents);
    }

    @Override
    public int hashCode() {
        return Objects.hash(center, extents);
    }

    @Override
    public String toString() {
        return "Bounds[center=" + center + ", extents=" + extents + "]";
    }
}
