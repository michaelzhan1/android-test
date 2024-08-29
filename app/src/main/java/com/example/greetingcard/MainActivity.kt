package com.example.greetingcard

import android.graphics.Bitmap
import android.graphics.Matrix
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore.Images.Media
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.greetingcard.ui.theme.GreetingCardTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GreetingCardTheme {
                Scaffold( modifier = Modifier.fillMaxSize() ) { innerPadding ->
                    Column () {

                        Greeting(
                            name = "Actual Android",
                            modifier = Modifier.padding(innerPadding)
                        )

                        TestButton(
                            onClick = {
                                Log.i("Test", "Test")
                            },
                            modifier = Modifier.padding(innerPadding)
                        )

                        ImageChooseAndDisplay()
                    }
                }
            }

        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Surface(color = Color.Cyan) {
        Text(
            text = "This is a sdf $name!",
            modifier = modifier.padding(24.dp)
        )
    }
}

@Composable
fun TestButton(onClick: () -> Unit, modifier: Modifier) {
    Button(
        onClick = onClick,
        modifier = modifier.padding(0.dp, 100.dp)
    ) {
        Text("Like")
    }
}

@Composable
fun ImageChooseAndDisplay() {
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var imageData by remember { mutableStateOf<Bitmap?>(null) }
    val context = LocalContext.current
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            imageUri = uri
            imageData = flipHorizontally(Media.getBitmap(context.contentResolver, uri))
        }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        Button(onClick = { imagePickerLauncher.launch("image/*") }) {
            Text("Choose image")
        }

        Spacer(modifier = Modifier.height(16.dp))

        imageUri?.let {
            Image(
                painter = rememberAsyncImagePainter(it),
                contentDescription = null,
                modifier = Modifier.size(100.dp)
            )
        } ?: run {
            Text(
                text = "Choose an image"
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        imageData?.let {
            AsyncImage(
                model = imageData,
                contentDescription = null,
                modifier = Modifier.size(100.dp)
            )
        } ?: run {
            Text(
                text = "Choose an image"
            )
        }
    }
}

fun flipHorizontally(bitmap: Bitmap): Bitmap {
    val matrix = Matrix().apply {
        preScale(-1f, 1f)
    }
    return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, false)
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    GreetingCardTheme {
        Greeting("Test Android")
    }
}