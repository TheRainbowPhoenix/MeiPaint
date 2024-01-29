package com.medibang.android.paint.tablet.ui.activity

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.medibang.android.paint.tablet.ui.activity.ui.theme.MeiPaintTheme

class PaintActivity : ComponentActivity() {

    companion object {
        init {
            try {
                System.loadLibrary("NativeLib")
            } catch (unused: UnsatisfiedLinkError) {
                // Log the error using Android Log
                Log.e("MeiPaint", "Error loading native library: ${unused.message}")
            }
        }
    }

    // Declare the native method
    private external fun test(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MeiPaintTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting2("Android")
                }
            }
        }
        testNative()
    }

    fun testNative() {
        // Call the native method and get the result
        val result = test()

        // Log the result
        Log.d("MeiPaint", "Native method returned: $result")
    }
}

@Composable
fun Greeting2(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    MeiPaintTheme {
        Greeting2("Android")
    }
}