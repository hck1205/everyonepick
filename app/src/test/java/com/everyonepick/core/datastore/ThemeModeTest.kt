package com.everyonepick.core.datastore

import org.junit.Assert.assertEquals
import org.junit.Test

class ThemeModeTest {
    @Test
    fun `falls back to system when storage value is unknown`() {
        assertEquals(ThemeMode.SYSTEM, ThemeMode.fromStorageValue("unexpected"))
    }

    @Test
    fun `parses known storage value`() {
        assertEquals(ThemeMode.DARK, ThemeMode.fromStorageValue("dark"))
    }
}
