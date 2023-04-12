package com.dev.james.booktracker.compose_ui.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

val BookAppShapes = Shapes(
    small = RoundedCornerShape(5.dp),
    medium = RoundedCornerShape(10.dp),
    large = RoundedCornerShape(
        bottomStart = 50.dp ,
        bottomEnd = 50.dp
    )
)