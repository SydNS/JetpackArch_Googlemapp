package com.example.danmech.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.danmech.R

class ViewPagerAdapter(
    private val imagesList: List<Int>,
    private val titlesList: List<String>,
    private val slogansList: List<String>
) : RecyclerView.Adapter<ViewPagerAdapter.ViewPagerHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_viewpager, parent, false)
        return ViewPagerHolder(view)
    }

    override fun onBindViewHolder(holder: ViewPagerHolder, position: Int) {
        val image = imagesList[position]
        val title = titlesList[position]
        val slogan = slogansList[position]
        holder.bind(image,title,slogan)
    }

    override fun getItemCount(): Int {
        return imagesList.size
    }


    class ViewPagerHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val ivSliderImage = itemView.findViewById<ImageView>(R.id.ivSliderImage)
        private val ivSliderTitle = itemView.findViewById<TextView>(R.id.ivSliderTitle)
        private val ivSliderSlogan = itemView.findViewById<TextView>(R.id.ivSliderSlogan)

        fun bind(image: Int,title:String,slogan:String) {
            ivSliderImage.setImageResource(image)
            ivSliderSlogan.text=slogan
            ivSliderTitle.text=title
        }
    }
}
