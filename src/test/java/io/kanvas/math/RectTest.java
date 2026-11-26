/*
 * Copyright (c) 2024-2026 Egor Morozov
 * Arkitectury (formerly KGE, Kotlin Game Engine, Kanvas Game Engine, Kanvas, KgeMultiplatform)
 *
 * SPDX-License-Identifier: MIT
 * Test generated with Arena.ai.
 */

package io.kanvas.math;

import org.joml.Vector2f;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RectTest {

    @Test
    void minMaxAdjustmentsPreserveOppositeEdge() {
        Rect r = Rect.minMax(10f, 20f, 50f, 80f);
        assertEquals(40f, r.width);
        assertEquals(60f, r.height);

        r.setXMin(20f);
        assertEquals(50f, r.getXMax());
        assertEquals(30f, r.width);

        r.setYMax(100f);
        assertEquals(20f, r.getYMin());
        assertEquals(80f, r.height);
    }

    @Test
    void containmentOverlapAndNormalizedCoordinates() {
        Rect outer = new Rect(0f, 0f, 100f, 50f);
        Rect inner = new Rect(25f, 10f, 50f, 20f);
        Rect crossing = new Rect(90f, 40f, 30f, 30f);

        assertTrue(outer.contains(new Vector2f(0f, 0f)));
        assertFalse(outer.contains(new Vector2f(100f, 25f)));
        assertTrue(outer.contains(inner));
        assertTrue(outer.overlaps(crossing));

        Vector2f pt = Rect.normalizedToPoint(outer, new Vector2f(0.5f, 0.5f));
        assertEquals(new Vector2f(50f, 25f), pt);
        assertEquals(new Vector2f(0.5f, 0.5f), Rect.pointToNormalized(outer, pt));
    }

    @Test
    void inverseRectRequiresAllowInverse() {
        Rect inv = new Rect(100f, 100f, -50f, -40f);
        assertFalse(inv.contains(new Vector2f(75f, 80f), false));
        assertTrue(inv.contains(new Vector2f(75f, 80f), true));
        assertEquals(new Vector2f(50f, 80f), inv.closestPoint(new Vector2f(10f, 80f)));
    }
}
