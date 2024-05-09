package com.utku.speedsterdash.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.TextUnit

// sp(TextUnit) → px(Float)
@Composable
internal fun TextUnit.spToPx(): Float {
    return this.value * LocalDensity.current.fontScale
}