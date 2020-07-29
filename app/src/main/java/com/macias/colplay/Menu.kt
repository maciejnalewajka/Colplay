package com.macias.colplay

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_menu.*

class Menu : AppCompatActivity() {
    private var ob = 2880F

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        menu_info.setOnClickListener{Toast.makeText(this,
            "Author: Maciej Nalewajka", Toast.LENGTH_SHORT).show()}
        menu_anim.setOnClickListener {obrot()}
        button_colouring.setOnClickListener{
            val i = Intent(this, Recycler::class.java)
            startActivity(i)
        }
        menu_random_color.setOnClickListener {
            val i = Intent(this, RandomColor::class.java)
            startActivity(i)
        }
        menu_exit.setOnClickListener{finishAffinity()}
    }

    override fun onBackPressed(){}

    private fun obrot() {
        menu_anim.animate().setDuration(2500).rotation(ob)
        ob *=-1
    }
}