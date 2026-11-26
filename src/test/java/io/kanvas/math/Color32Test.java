/*
 * Copyright (c) 2024-2026 Egor Morozov
 * Arkitectury (formerly KGE, Kotlin Game Engine, Kanvas Game Engine, Kanvas, KgeMultiplatform)
 *
 * SPDX-License-Identifier: MIT
 * Test generated with Arena.ai.
 */

package io.kanvas.math;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Color32Test {

    @Test
    void packedArgbRoundTrip() {
        int packed = 0xCC112233;
        Color32 c = new Color32(packed);
        assertEquals(packed, c.toARGB());
        assertEquals((byte) 0x11, c.r);
        assertEquals((byte) 0x22, c.g);
        assertEquals((byte) 0x33, c.b);
        assertEquals((byte) 0xCC, c.a);
    }

    @Test
    void convertsFromAndToFloatColor() {
        Color32 c = new Color32(new Color(1f, 0f, 0.5f, 1f));
        assertEquals((byte) 255, c.r);
        assertEquals((byte) 0, c.g);
        assertEquals((byte) 128, c.b);
        assertEquals((byte) 255, c.a);

        Color back = c.toColor();
        assertEquals(1f, back.r, 1e-5f);
        assertEquals(0f, back.g, 1e-5f);
        assertEquals(128f / 255f, back.b, 1e-5f);
    }
}
