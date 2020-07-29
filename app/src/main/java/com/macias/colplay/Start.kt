package com.macias.colplay

import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.res.AssetManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_start.*
import java.io.*
import java.util.*

class Start : AppCompatActivity() {
    private val filename: String = "colplay_sp.txt"
    private var str: String = ""
    private lateinit var assetMan: AssetManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        assetMan = assets
        val file = File(filesDir.path + File.separator + filename)
        odczyt(file)
        obrot()
        start()
        start_start.setOnClickListener {startActivity(Intent(this, Menu::class.java))}
    }

    private fun obrot() {
        start_anim.animate().setDuration(2500).rotation(2880F).startDelay = 200
        start_anim.animate().scaleX(1.5F)
        start_anim.animate().scaleY(1.5F)
    }

    private fun start() {
        Handler().postDelayed({
            start_start.visibility = View.VISIBLE
        }, 4500)
    }

    // Zapisuje asstey w pamięci
    private fun zapiszWPamieci() {
        val cw = ContextWrapper(applicationContext)
        val directory = cw.getDir("imageDir", Context.MODE_PRIVATE)
        for(i in 1 until 11){
            try {
                val name = "image_$i.png"
                val f = File(directory, name)
                val it: InputStream = assetMan.open(name)
                val b = BitmapFactory.decodeStream(it)
                var fos: FileOutputStream? = null
                try {
                    fos = FileOutputStream(f)
                    b.compress(Bitmap.CompressFormat.PNG, 100, fos)
                }
                catch (e: Exception) {}
                finally {
                    try {
                        fos!!.close()
                    } catch (e: IOException) {}
                }
            } catch (e: FileNotFoundException) {}
        }
    }

    // Zapis txt do pliku
    private fun zapis(file: File){
        val fos = FileOutputStream(file)
        fos.write("0".toByteArray())
        fos.close()
    }

    // Odczyt txt z pliku
    private fun odczyt(file: File): String{
        try {
            val sc = Scanner(file)
            while (sc.hasNext()){
                str +=sc.nextLine()
            }
            sc.close()
        }catch (e: FileNotFoundException){
            zapiszWPamieci()
            zapis(file)
        }
        return str
    }
}