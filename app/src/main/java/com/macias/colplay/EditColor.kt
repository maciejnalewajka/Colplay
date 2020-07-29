package com.macias.colplay

import android.content.Context
import android.content.ContextWrapper
import android.content.res.AssetManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.MotionEvent
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.set
import androidx.core.view.drawToBitmap
import kotlinx.android.synthetic.main.activity_edit_color.*
import java.io.*

@Suppress("DEPRECATION")
class EditColor : AppCompatActivity(), SensorEventListener {
    private lateinit var bitmapRainbow: Bitmap
    private var x:Int = 0
    private var y:Int = 0
    private var rgb = Rgb(0,0,0)    // Do
    private var kol = Rgb(0,0,0)    // Is
    private lateinit var assetMan: AssetManager
    private var asname: String = "image_1.png"
    private lateinit var bitmap: Bitmap
    private lateinit var bit: Bitmap
    private var tablica: MutableList<BitRgb> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_color)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        val sm = getSystemService(SENSOR_SERVICE) as SensorManager
        val mySensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        sm.registerListener(this, mySensor, SensorManager.SENSOR_DELAY_GAME)
        assetMan = assets
        val myIntent = intent
        val count = myIntent.getStringExtra("count")
        asname = "image_$count.png"
        val it: InputStream = assetMan.open("rainbow.png")
        bitmapRainbow = BitmapFactory.decodeStream(it)
        x = bitmapRainbow.width/2
        y = bitmapRainbow.height/2
        edit_color_image.isDrawingCacheEnabled = true
        edit_color_image.buildDrawingCache(true)
        ustaw()
        edit_color_back.setOnClickListener{
            zapiszWPamieci(bit)
            finish()}
        edit_color_do.setOnClickListener{assety()}
        edit_color_image.setOnTouchListener { _, event ->
            when(event.action){
                MotionEvent.ACTION_DOWN -> {moveDown(event.x.toInt(), event.y.toInt())}
                MotionEvent.ACTION_MOVE -> {moveDown(event.x.toInt(), event.y.toInt())}
            }
            true
        }
        edit_color_back_color.setOnClickListener{
            backColor()
        }
    }

    override fun onResume() {
        super.onResume()
        val sm = getSystemService(SENSOR_SERVICE) as SensorManager
        val mySensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        sm.registerListener(this, mySensor, SensorManager.SENSOR_DELAY_GAME)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int){}

    override fun onSensorChanged(event: SensorEvent) {
        x -= event.values[0].toInt()
        y += event.values[1].toInt()
        if(x<0)x = bitmapRainbow.width-1
        if(x>bitmapRainbow.width-1) x = 0
        if(y<1) y = 1
        if(y>bitmapRainbow.height-1) y = bitmapRainbow.height-1
        val color = bitmapRainbow.getPixel(x,y)
        rgb = Rgb(Color.red(color), Color.green(color), Color.blue(color))
        edit_color_color.setColorFilter(Color.rgb(rgb.r, rgb.g, rgb.b))
    }

    // Pobiera pozycje obrazu oraz obraz i zwraca rgb z danego pixela
    private fun rainbow2(x:Int, y:Int, bitmap: Bitmap): Rgb {
        if(x<0){return kol}
        if(y<0){return kol}
        if(x>bitmap.width-1){return kol}
        if(y>bitmap.height-1){return kol}
        val color = bitmap.getPixel(x,y)
        return Rgb(Color.red(color),Color.green(color),Color.blue(color))
    }

    // Zmienia zaznaczone kolory na bitmapie
    private fun assety(){
        tablica.add(BitRgb(bit.copy( Bitmap.Config.ARGB_8888 , true), kol))
        for (i in 0 until bit.width){
            for (j in 0 until bit.height){
                if(rainbow2(i,j,bit) == kol) bit[i, j] = Color.rgb(rgb.r,rgb.g,rgb.b)
            }
        }
        bit(rgb, bit)
    }

    // Pierwsze ustawienie bitmapy
    private fun ustaw() {
        val cw = ContextWrapper(applicationContext)
        val directory = cw.getDir("imageDir", Context.MODE_PRIVATE)
        try {
            val f = File(directory, asname)
            bitmap = BitmapFactory.decodeStream(FileInputStream(f))
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        bit = bitmap.copy( Bitmap.Config.ARGB_8888 , true)
        edit_color_image.background = BitmapDrawable(resources, bit)
        background_view_do.setColorFilter(Color.rgb(0,0,0))
    }

    // Ustawia bitmapę i kolor
    private fun bit(rgb:Rgb, bit: Bitmap){
        kol = rgb
        edit_color_image.background = BitmapDrawable(resources, bit)
        background_view_do.setColorFilter(Color.rgb(rgb.r,rgb.g,rgb.b))
    }

    // Funkcja zczytuje kolor z dotknięcia bitmapy
    private fun moveDown(x: Int, y:Int) {
        val bit3 = edit_color_image.drawToBitmap()
        kol = rainbow2(x, y, bit3)
        background_view_do.setColorFilter(Color.rgb(kol.r,kol.g,kol.b))
    }

    // Funkcja służy co cofnięcia bitmapy i zaznaczonego koloru
    private fun backColor(){
        if(tablica.size > 0){
            val x = tablica[tablica.size-1]
            bit = x.bit.copy( Bitmap.Config.ARGB_8888 , true)
            bit(x.rgb, x.bit)
            tablica.remove(tablica[tablica.size-1])
        }
    }

    // Funkcja zapisuje obraz w pamięci urządzenia
    private fun zapiszWPamieci(bitmapImage: Bitmap): String? {
        val cw = ContextWrapper(applicationContext)
        val directory = cw.getDir("imageDir", Context.MODE_PRIVATE)
        val mypath = File(directory, asname)
        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(mypath)
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                fos!!.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return directory.absolutePath
    }
}