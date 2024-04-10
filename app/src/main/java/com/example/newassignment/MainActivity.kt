package com.example.newassignment

import android.os.Bundle
import android.util.Log
import android.widget.HorizontalScrollView
import android.widget.ImageButton
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.newassignment.ui.theme.NewAssignmentTheme
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NewAssignmentTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TextEditingScreen()
                }
            }
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextEditingScreen() {
    var textList by remember { mutableStateOf(emptyList<String>()) }
    var inputText by remember { mutableStateOf("") }
    var selectedTextIndex by remember { mutableStateOf(-1) }
    var showColorPicker by remember { mutableStateOf(false) }
    var fontSizeSliderPosition by remember { mutableStateOf(20f) }
    var textSizes by remember { mutableStateOf(List(textList.size) { 20.sp }) }
    var showBackgroundColorPicker by remember { mutableStateOf(false) }
    var backgroundColor by remember { mutableStateOf(Color.White) }
    val fontStyles = listOf(
        TextStyle.Default,
        TextStyle(fontWeight = FontWeight.Bold),
        TextStyle(fontStyle = FontStyle.Italic),
        TextStyle(fontWeight = FontWeight.Bold, fontStyle = FontStyle.Italic)
    )
    var showFontStylePicker by remember { mutableStateOf(false) }


    var textFontStyles by remember { mutableStateOf(List(textList.size) { TextStyle.Default }) }

    var textColors by remember { mutableStateOf(List(textList.size) { Color.Black }) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .background(color = Color.LightGray)
        ) {
            TextField(
                value = inputText,
                onValueChange = { inputText = it },
                label = { Text("Enter Text") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
            Text(
                text = "Add Text",
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 32.dp)
                    .clickable {
                        if (inputText.isNotEmpty()) {
                            textList = textList
                                .toMutableList()
                                .apply {
                                    add(inputText)
                                }
                            textColors =
                                textColors + Color.Black
                            textSizes =
                                textSizes + 20.sp
                            textFontStyles =
                                textFontStyles + TextStyle.Default
                            inputText = ""
                        }
                    },
                fontSize = 20.sp
            )
        }


        Box(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .fillMaxHeight(0.6f)
                .padding(top = 30.dp)
                .background(color = backgroundColor, shape = RoundedCornerShape(13.dp))
                .border(3.dp, Color.Black, shape = RoundedCornerShape(13.dp))
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(10.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                itemsIndexed(textList) { index, text ->
                    var offsetX by remember { mutableStateOf(0f) }
                    var offsetY by remember { mutableStateOf(0f) }
                    val isSelected = index == selectedTextIndex
                    val lineHeight = 50.dp
                    var rotationAngle by remember { mutableStateOf(0f) }

                    Text(
                        text = "$text",
                        color = textColors[index],
                        modifier = Modifier
                            .padding(2.dp)
                            .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
                            .pointerInput(Unit) {
                                detectDragGestures { change, dragAmount ->
                                    change.consumeAllChanges()
                                    offsetX += dragAmount.x
                                    offsetY += dragAmount.y
                                }
                            }
                            .rotate(rotationAngle)
                            .height(lineHeight)
                            .padding(end = 16.dp)
                            .clickable {
                                selectedTextIndex = index
                                fontSizeSliderPosition = textSizes[index].value

                            },
                        style = TextStyle(
                            fontWeight = textFontStyles[index].fontWeight,
                            fontStyle = textFontStyles[index].fontStyle,
                            textDecoration =
                            if (isSelected)
                                TextDecoration.Underline
                            else TextDecoration.None,
                            fontSize = textSizes[index]

                        ),
                    )
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.End
        ){
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete",
                tint = Color.Red,
                modifier = Modifier
                    .size(32.dp)
                    .clickable {
                        if (selectedTextIndex != -1) {
                            textList = textList
                                .toMutableList()
                                .apply { removeAt(selectedTextIndex) }
                            textColors = textColors.toMutableList().apply { removeAt(selectedTextIndex) }
                            textSizes = textSizes.toMutableList().apply { removeAt(selectedTextIndex) }
                            textFontStyles = textFontStyles.toMutableList().apply { removeAt(selectedTextIndex) }
                            selectedTextIndex = -1
                        }
                    }
                    .weight(2f)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Image(
                painter = painterResource(id = R.drawable.reset),
                contentDescription = "Reset",
                modifier = Modifier
                    .size(32.dp)
                    .clickable {
                        // Clear all lists
                        textList = emptyList()
                        textColors = emptyList()
                        textSizes = emptyList()
                        textFontStyles = emptyList()
                        // Reset selected text index
                        selectedTextIndex = -1
                        backgroundColor = Color.White

                    }
                    .weight(2f)
            )
        }



        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.End
        ) {
            Button(
                onClick = {
                    if (selectedTextIndex != -1) {
                        showColorPicker = true
                    }
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("    Text Color   ")
            }
            Spacer(modifier = Modifier.width(16.dp))

            Button(
                onClick = {
                    showBackgroundColorPicker = true
                }
            ) {
                Text("Background Color")
            }
        }


        if (showColorPicker) {
            ColorPickerDialog(
                onColorSelected = {
                    if (selectedTextIndex != -1) {
                        textColors = textColors.toMutableList().apply { set(selectedTextIndex, it) }
                        showColorPicker = false
                    }
                },
                onDismiss = { showColorPicker = false }
            )
        }
        if (showBackgroundColorPicker) {
            ColorPickerDialog(
                onColorSelected = {
                    backgroundColor = it
                    showBackgroundColorPicker = false
                },
                onDismiss = { showBackgroundColorPicker = false }
            )
        }
        Slider(
            value = fontSizeSliderPosition,
            onValueChange = { newSize ->
                fontSizeSliderPosition = newSize
                if (selectedTextIndex != -1) {
                    textSizes =
                        textSizes.toMutableList().apply { set(selectedTextIndex, newSize.sp) }
                }
            },
            valueRange = 0f..45f,
            steps = 51,
            modifier = Modifier.fillMaxWidth()
        )
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "0dp")
            Text(text = "${fontSizeSliderPosition.toInt()}dp")
            Text(text = "45dp")
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = {
                    if (selectedTextIndex != -1) {

                        showFontStylePicker = true
                    }
                }
            ) {
                Text("Font Style")
            }
        }

        if (showFontStylePicker) {
            FontStylePickerDialog(
                fontStyles = fontStyles,
                onFontStyleSelected = { index ->
                    if (selectedTextIndex != -1) {
                        textFontStyles = textFontStyles.toMutableList().apply { set(selectedTextIndex, fontStyles[index]) }
                        showFontStylePicker = false
                    }
                },
                onDismiss = { showFontStylePicker = false }
            )
        }
    }



}


@Composable
fun FontStylePickerDialog(
    fontStyles: List<TextStyle>,
    onFontStyleSelected: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier.padding(16.dp),
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Select Font Style", style = MaterialTheme.typography.bodyMedium)

                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 8.dp)
                ) {
                    itemsIndexed(fontStyles) { index, fontStyle ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                                .clickable {
                                    onFontStyleSelected(index)
                                    onDismiss()
                                }
                        ) {
                            Text(
                                text = getFontStyleName(fontStyle),
                                style = TextStyle(fontWeight = FontWeight.Bold)
                            )
                        }
                    }
                }
            }
        }
    }
}

fun getFontStyleName(textStyle: TextStyle): String {
    if (textStyle.fontWeight == FontWeight.Bold && textStyle.fontStyle == FontStyle.Italic) {
        return "Bold Italic"
    } else if (textStyle.fontWeight == FontWeight.Bold) {
        return "Bold"
    } else if (textStyle.fontStyle == FontStyle.Italic) {
        return "Italic"
    } else {
        return "Normal"
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ColorPickerDialog(
    onColorSelected: (Color) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier.padding(16.dp),
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Select Color", style = MaterialTheme.typography.bodyMedium)

                Spacer(modifier = Modifier.height(16.dp))


                Row(    modifier = Modifier.horizontalScroll(state = rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    for (color in listOf(
                        Color.White,Color.Red, Color.Green, Color.Blue, Color.Yellow,
                        Color.Cyan, Color.Magenta, Color.Gray, Color.DarkGray
                    )) {
                        ColorOption(color = color) {
                            onColorSelected(color)
                        }
                    }
                }

            }
        }

    }
}

@Composable
fun ColorOption(color: Color, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(50.dp)
            .background(color)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
    }
}