package com.example.lokatravel.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.lokatravel.R

class HomeAdapter(private val dataList: List<HomeViewModel>) :
    RecyclerView.Adapter<HomeAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = dataList[position]
        holder.bind(data)
    }

    override fun getItemCount(): Int = dataList.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageViewCard: ImageView = itemView.findViewById(R.id.imageViewCard)
        private val textViewCard: TextView = itemView.findViewById(R.id.textViewCard)

        fun bind(data: HomeViewModel) {
            imageViewCard.setImageResource(data.imageResourceId)
            textViewCard.text = data.name
        }
    }
}
