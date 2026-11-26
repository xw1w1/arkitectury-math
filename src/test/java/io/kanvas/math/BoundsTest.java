/*
* Copyright (c) 2024-2026 Egor Morozov
* Arkitectury (formerly KGE, Kotlin Game Engine, Kanvas Game Engine, Kanvas, KgeMultiplatform)
*
* SPDX-License-Identifier: MIT
* Test generated with Arena.ai.
*/

package io.kanvas.math;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoundsTest {

    @Test
    void constructsFromSizeAndMinMax() {
        Bounds b = new Bounds(new Vector3f(1f, 2f, 3f), new Vector3f(4f, 6f, 8f));
        assertEquals(new Vector3f(1f, 2f, 3f), b.center);
        assertEquals(new Vector3f(2f, 3f, 4f), b.extents);
        assertEquals(new Vector3f(-1f, -1f, -1f), b.min());
        assertEquals(new Vector3f(3f, 5f, 7f), b.max());

        Bounds mm = Bounds.fromMinMax(new Vector3f(5f, 4f, 3f), new Vector3f(1f, 0f, -1f));
        assertEquals(new Vector3f(3f, 2f, 1f), mm.center);
        assertEquals(new Vector3f(2f, 2f, 2f), mm.extents);
    }

    @Test
    void encapsulateAndExpandGrowBounds() {
        Bounds b = new Bounds(new Vector3f(0f, 0f, 0f), new Vector3f(2f, 2f, 2f));
        b.encapsulate(new Vector3f(3f, 1f, -1f));
        assertEquals(new Vector3f(-1f, -1f, -1f), b.min());
        assertEquals(new Vector3f(3f, 1f, 1f), b.max());

        b.expand(2f);
        assertEquals(new Vector3f(3f, 2f, 2f), b.extents);
    }

    @Test
    void containsIntersectsAndClosestPoint() {
        Bounds a = new Bounds(new Vector3f(0f, 0f, 0f), new Vector3f(2f, 2f, 2f));
        Bounds b = new Bounds(new Vector3f(1.5f, 0f, 0f), new Vector3f(1f, 1f, 1f));
        Bounds c = new Bounds(new Vector3f(5f, 0f, 0f), new Vector3f(1f, 1f, 1f));

        assertTrue(a.contains(new Vector3f(1f, -1f, 0.5f)));
        assertFalse(a.contains(new Vector3f(1.1f, 0f, 0f)));
        assertTrue(a.intersects(b));
        assertFalse(a.intersects(c));

        assertEquals(new Vector3f(1f, 1f, 0f), a.closestPoint(new Vector3f(4f, 3f, 0f)));
        assertEquals(0f, a.sqrDistance(new Vector3f(0.5f, 0f, 0f)), 1e-5f);
        assertEquals(9f, a.sqrDistance(new Vector3f(4f, 0f, 0f)), 1e-5f);
    }

    @Test
    void transformEnclosesRotatedBox() {
        Bounds unit = Bounds.fromCenterExtents(new Vector3f(1f, 0f, 0f), new Vector3f(1f, 1f, 1f));
        Matrix4f m = new Matrix4f().translate(2f, 3f, 0f).rotateZ((float) (Math.PI * 0.25));
        Bounds transformed = unit.transform(m);

        float diag = (float) Math.sqrt(2.0);
        assertEquals(diag, transformed.extents.x, 1e-4f);
        assertEquals(diag, transformed.extents.y, 1e-4f);
        assertEquals(1f, transformed.extents.z, 1e-4f);
    }
}
