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

class ColorTest {

    @Test
    void argbAndRgbaRoundTrip() {
        int argb = 0x8040A0FF;
        Color fromArgb = new Color(argb);
        assertEquals(argb, fromArgb.toARGB());

        int rgba = 0x40A0FF80;
        Color fromRgba = Color.fromRgba32(rgba);
        assertEquals(rgba, fromRgba.toRgba32());
        assertEquals(fromArgb, fromRgba);
    }

    @Test
    void toArgbClampsOutOfRangeChannels() {
        Color overbright = new Color(1.5f, -0.2f, 0.5f, 2.0f);
        assertEquals(0xFFFF0080, overbright.toARGB());
    }

    @Test
    void presetsAndCopyConstructor() {
        Color white = Color.white();
        Color copy = new Color(white);
        assertEquals(white, copy);
        assertNotSame(white, copy);
        assertEquals(0f, Color.clear().a);
    }
}
