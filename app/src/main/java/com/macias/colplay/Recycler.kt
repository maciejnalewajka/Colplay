package com.macias.colplay

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_recycler.*
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException

class Recycler : AppCompatActivity() {
    private var bitmaps: MutableList<Bitmap> = mutableListOf()
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var adapter: ModelAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        recycler_back.setOnClickListener{finish()}
        start()
    }

    override fun onResume() {
        super.onResume()
        start()
    }

    // Funkcja startowa
    private fun start(){
        createBitmaps()
        layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        recycler_lista.layoutManager = layoutManager
        adapter = ModelAdapter(this, bitmaps)
        recycler_lista.adapter = adapter
    }

    // Funkcja tworzy listę bitmap
    private fun createBitmaps(){
        val cw = ContextWrapper(applicationContext)
        val directory = cw.getDir("imageDir", Context.MODE_PRIVATE)
        val bits: MutableList<Bitmap> = mutableListOf()
        for(i in 1 until 11){
            try {
                val name = "image_$i.png"
                val f = File(directory, name)
                val b = BitmapFactory.decodeStream(FileInputStream(f))
                bits.add(b.copy( Bitmap.Config.ARGB_8888 , true))
            } catch (e: FileNotFoundException) {}
        }
        bitmaps = bits
    }
}
