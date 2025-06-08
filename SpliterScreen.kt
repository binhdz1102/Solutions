package com.unknown.pockizzy.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.MeasurePolicy
import androidx.compose.ui.layout.Measured
import androidx.compose.ui.layout.ParentDataModifier
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.layout.VerticalAlignmentLine
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay


@Composable
fun DividedColumn(
    modifier: Modifier = Modifier,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    spliter: @Composable () -> Unit = { Divider() },
    content: @Composable DividedColumnScope.() -> Unit
) {
    Layout(
        modifier = modifier,
        measurePolicy = dividedColumnMeasurePolicy(
            verticalArrangement,
            horizontalAlignment
        ),
        content = {
            val scope = object : DividedColumnScope {
                @Composable
                override fun spliter() {
                    Box(Modifier.dividerParentData()) { spliter() }
                }
            }
            scope.content()
        }
    )
}

/** Đánh dấu divider bằng
 * ParentData */
private fun Modifier.dividerParentData() = this.then(object : ParentDataModifier {
    override fun Density.modifyParentData(parentData: Any?) = DividerTag
})


interface DividedColumnScope {
    @Composable fun spliter()
}

internal object DividerTag

internal fun dividedColumnMeasurePolicy(
    verticalArrangement: Arrangement.Vertical,
    horizontalAlignment: Alignment.Horizontal
) = MeasurePolicy { measurables, constraints ->

    /*  Xác định loại node */
    val n = measurables.size
    val isDivider = BooleanArray(n) { measurables[it].parentData is DividerTag }
    val hasContentAfter = BooleanArray(n).also { arr ->
        var contentSeen = false
        for (i in n - 1 downTo 0) {
            arr[i] = contentSeen
            if (!isDivider[i]) contentSeen = true      // content ⇒ bật cờ
        }
    }

    /* Đo và quyết định giữ hay bỏ */
    var seenContentBefore = false
    var gapHasDivider = false
    val placeables = ArrayList<Placeable>(n)

    measurables.forEachIndexed { i, measurable ->
        if (isDivider[i]) {
            val keep = seenContentBefore &&
                    hasContentAfter[i] &&
                    !gapHasDivider                   // chỉ 1 divider mỗi khoảng trống
            val plc = if (keep) {
                measurable.measure(constraints)
            } else {
                measurable.measure(
                    constraints.copy(minWidth = 0, maxWidth = 0,
                        minHeight = 0, maxHeight = 0)
                )
            }
            placeables += plc
            if (keep) gapHasDivider = true             // đã có divider
        } else {                                       // content
            placeables += measurable.measure(constraints)
            seenContentBefore = true
            gapHasDivider = false                      // reset sau content
        }
    }

    val width  = placeables.maxOfOrNull { it.width } ?: 0
    val height = placeables.sumOf { it.height }

    layout(width, height) {
        var y = 0
        placeables.forEach { p ->
            val x = horizontalAlignment.align(
                size = p.width,
                space = width - p.width,
                layoutDirection = layoutDirection
            )
            p.placeRelative(x, y)
            y += p.height
        }
    }
}

//////////////////////////////////////////////////////////////////////////////////

@Composable
fun SpliterScreen() {
    var isVisible1 by remember { mutableStateOf(true) }
    var isVisible2 by remember { mutableStateOf(false) }
    var isVisible3 by remember { mutableStateOf(false) }
    var isVisible4 by remember { mutableStateOf(false) }
    var isVisible5 by remember { mutableStateOf(false) }
    var isVisible6 by remember { mutableStateOf(false) }
    var isVisible7 by remember { mutableStateOf(false) }
    var isVisible8 by remember { mutableStateOf(false) }
    var isVisible9 by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        val rnd = kotlin.random.Random
        while (true) {
            delay(10_000L)
            isVisible1 = rnd.nextBoolean()
            isVisible2 = rnd.nextBoolean()
            isVisible3 = rnd.nextBoolean()
            isVisible4 = rnd.nextBoolean()
            isVisible5 = rnd.nextBoolean()
            isVisible6 = rnd.nextBoolean()
            isVisible7 = rnd.nextBoolean()
            isVisible8 = rnd.nextBoolean()
            isVisible9 = rnd.nextBoolean()
        }
    }

    DividedColumn(
        spliter = { Divider(thickness = 2.dp) }
    ) {
        if (isVisible1) {
            Text("Switch A")
            Switch(checked = true, onCheckedChange = {})
        }
        spliter()

        if (isVisible2) {
            Text("Button 1")
            Button(onClick = {}) { Text("Do Action") }
        }
        spliter()

        if (isVisible3) {
            Text("Volume")
            Slider(
                value = 0.5f,
                onValueChange = {},
                modifier = Modifier.fillMaxWidth()
            )
        }
        spliter()

        // Nhóm lẻ
        if (isVisible4) {
            Text("Button 2")
            Button(onClick = {}) { Text("Do Action") }
        }
        if (isVisible5) {
            Text("Switch B")
            Switch(checked = true, onCheckedChange = {})
        }
        spliter()

        // Section ví dụ
        if (isVisible6) {
            Text("Section Header", style = MaterialTheme.typography.titleMedium)
        }
        if (isVisible7) {
            Text("Section line 1")
        }
        if (isVisible8) {
            Text("Section line 2")
        }
        spliter()

        if (isVisible9) {
            Text("Switch Last")
            Switch(checked = false, onCheckedChange = {})
        }
    }
}







