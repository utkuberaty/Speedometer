package com.utku.speedsterdash.ui.speedometer

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.utku.speedsterdash.R
import com.utku.speedsterdash.ui.theme.asset
import com.utku.speedsterdash.ui.theme.robotoMono
import org.koin.androidx.compose.koinViewModel

@Composable
fun Speedometer(
    modifier: Modifier = Modifier,
    viewModel: SpeedometerViewModel = koinViewModel()
) {
    var useRealSpeed by remember { mutableStateOf(false) }
    val vehicleSpeed by remember(useRealSpeed) {
        viewModel.vehicleSpeed(useRealSpeed)
    }.collectAsState(initial = 0)
    SpeedometerComponent(
        modifier = modifier,
        vehicleSpeed = { vehicleSpeed },
        useRealSpeed = { useRealSpeed },
        onUseRealSpeed = { useRealSpeed = it }
    )
}

@Composable
fun SpeedometerComponent(
    modifier: Modifier = Modifier,
    vehicleSpeed: () -> Int = { 0 },
    useRealSpeed: () -> Boolean = { false },
    onUseRealSpeed: (Boolean) -> Unit = { },
) {
    var lightMode by remember { mutableStateOf(false) }
    val switchColor by remember(lightMode) {
        mutableStateOf(
            if (lightMode) {
                SwitchColors(
                    activeTrackColor = Color(0xFFDFDFDF),
                    inactiveTrackColor = Color(0xFFDFDFDF),
                    activeThumbColor = Color(0xFF999999),
                )
            } else {
                SwitchColors()
            }
        )
    }
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(if (lightMode) Color.White else Color.Black),
    ) {
        Column(
            modifier = Modifier.align(Alignment.CenterStart).padding(start = 90.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SpeedometerSwitch(
                text = stringResource(R.string.real_speed),
                value = useRealSpeed(),
                switchColors = switchColor,
                onValueChange = onUseRealSpeed,
                textColor = if (lightMode) Color.Black else Color.White
            )
            Spacer(modifier = Modifier.size(20.dp))
            SpeedometerSwitch(
                text = stringResource(R.string.light_mode),
                value = lightMode,
                switchColors = switchColor,
                textColor = if (lightMode) Color.Black else Color.White,
                onValueChange = {
                    lightMode = it
                }
            )
        }
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = vehicleSpeed().toString(),
            fontFamily = asset,
            color = if (lightMode) Color.Black else Color.White,
            fontSize = 300.sp
        )

    }
}

@Composable
fun SpeedometerSwitch(
    value: Boolean,
    text: String,
    modifier: Modifier = Modifier,
    textColor: Color = Color.White,
    switchColors: SwitchColors = SwitchColors(),
    onValueChange: (Boolean) -> Unit = {}
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        CustomSwitch(isChecked = value, onCheckedChange = onValueChange, colors = switchColors)
        Spacer(modifier = Modifier.size(16.dp))
        Text(
            text = text,
            fontFamily = robotoMono,
            fontSize = 32.sp,
            color = textColor,
        )
    }
}

@Composable
fun CustomSwitch(
    isChecked: Boolean,
    modifier: Modifier = Modifier,
    colors: SwitchColors = SwitchColors(),
    switchWidth: Dp = 160.dp,
    switchHeight: Dp = 80.dp,
    thumbSize: Dp = 68.dp,
    thumbPadding: Dp = 8.dp,
    onCheckedChange: (Boolean) -> Unit = {}
) {
    Box(
        modifier = modifier
            .size(width = switchWidth, height = switchHeight)
            .background(
                color = if (isChecked) colors.activeTrackColor else colors.inactiveTrackColor,
                shape = CircleShape
            )
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
            ) { onCheckedChange(!isChecked) },
    ) {
        val thumbPosition = animateFloatAsState(
            targetValue = if (isChecked) {
                switchWidth.value - thumbSize.value - thumbPadding.value
            } else {
                thumbPadding.value
            },
            label = ""
        )
        Box(
            Modifier
                .align(Alignment.CenterStart)
                .offset(x = thumbPosition.value.dp)
                .size(thumbSize)
                .background(
                    color = if (isChecked) colors.activeThumbColor else colors.inactiveThumbColor,
                    shape = CircleShape
                )
        )
    }
}

data class SwitchColors(
    val activeTrackColor: Color = Color(0xFF1F1F1F),
    val inactiveTrackColor: Color = Color(0xFF1F1F1F),
    val activeThumbColor: Color = Color(0xFF999999),
    val inactiveThumbColor: Color = Color(0xFF478ABA),
)
