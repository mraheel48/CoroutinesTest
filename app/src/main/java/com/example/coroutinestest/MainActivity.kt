package com.example.coroutinestest

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.core.view.drawToBitmap
import kotlinx.coroutines.*
import java.net.URL

class MainActivity : AppCompatActivity() {

    private val IMAGE_URL =
        "https://androidwave.com/wp-content/uploads/2018/12/useful-tools-for-logging-debugging-in-android.png.webp"
    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    private lateinit var progressBar: ProgressBar
    private lateinit var imageView: ImageView

    lateinit var imageBitmap: Bitmap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        progressBar = findViewById(R.id.progressBar)
        imageView = findViewById(R.id.imageView)

        Handler(Looper.getMainLooper()).postDelayed({

            imageBitmap = imageView.drawToBitmap(Bitmap.Config.ARGB_8888)
        }, 500)

        Handler(Looper.getMainLooper()).postDelayed({

            coroutineScope.launch {

                /*  val remoteImageDeferred = coroutineScope.async(Dispatchers.IO) {
                      getImageFromUrl()
                  }
                  val imageBitmap = remoteImageDeferred.await()*/
                //loadImage(imageBitmap)
                launch(Dispatchers.Default) {
                    val filterBitmap = Filter.apply(imageBitmap)
                    withContext(Dispatchers.Main) {
                        loadImage(filterBitmap)
                    }

                    Log.i("myTag", "Default. Thread: ${Thread.currentThread().name}")
                }
            }
        }, 500)




    }

    private fun getImageFromUrl(): Bitmap =
        URL(IMAGE_URL).openStream().use {
            BitmapFactory.decodeStream(it)
        }

    private fun loadImage(bmp: Bitmap) {
        progressBar.visibility = View.GONE
        imageView.setImageBitmap(bmp)
        imageView.visibility = View.VISIBLE
    }
}