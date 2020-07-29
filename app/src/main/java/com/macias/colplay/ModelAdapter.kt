package com.macias.colplay

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item.view.*


class ModelAdapter(val context: Context, private val images: MutableList<Bitmap>):
    RecyclerView.Adapter<ModelAdapter.MyViewHolder>(){

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        fun setData(images: MutableList<Bitmap>, poz: Int){
            itemView.image.background = BitmapDrawable(context.resources, images[poz])
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
        val result = MyViewHolder(view)
        view.setOnClickListener {
            val x = result.adapterPosition+1
            val i = Intent(context, EditColor::class.java)
            i.putExtra("count", "$x")
            it.context.startActivity(i)
        }
        return result
    }

    override fun getItemCount(): Int {return images.size}

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.setData(images, position)
    }
}