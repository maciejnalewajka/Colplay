package com.macias.colplay

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.ClipData
import android.content.ClipboardManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_random_color.*
import kotlin.random.Random

class RandomColor : AppCompatActivity() {
    private var myClipboard: ClipboardManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_random_color)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        myClipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager?
        random_color_back.setOnClickListener{finish()}
        random_color_make.setOnClickListener{losuj()}
        random_color_rgb.setOnClickListener{copy(random_color_rgb.text)}
        random_color_int.setOnClickListener{copy(random_color_int.text)}
        losuj()
    }

    @TargetApi(Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n")
    private fun losuj(){
        val ranVal = List(3) { Random.nextInt(0, 256) }
        val color = Color.rgb(ranVal[0], ranVal[1], ranVal[2])
        random_color_color.setColorFilter(color)
        random_color_rgb.text = "RGB(${ranVal[0]}, ${ranVal[1]}, ${ranVal[2]})"
        val hex:String = String.format("#%02X%02X%02X", ranVal[0], ranVal[1], ranVal[2])
        random_color_int.text = hex
    }

    private fun copy(text: CharSequence){
        val myClip = ClipData.newPlainText("text", text)
        myClipboard?.setPrimaryClip(myClip)
        Toast.makeText(this, "Copy successful!", Toast.LENGTH_SHORT).show()
    }
}