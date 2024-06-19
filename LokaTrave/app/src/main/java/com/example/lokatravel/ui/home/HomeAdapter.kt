package com.example.lokatravel.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.lokatravel.R

class HomeAdapter(
    private val items: List<HomeViewModel>,
    private val leftClickListener: OnLeftItemClickListener?,
    private val rightClickListener: OnRightItemClickListener?
) : RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {

    interface OnLeftItemClickListener {
        fun onLeftItemClick(data: HomeViewModel)
    }

    interface OnRightItemClickListener {
        fun onRightItemClick(data: HomeViewModel)
    }

    inner class HomeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageViewCard: ImageView = itemView.findViewById(R.id.imageViewCard)
        private val textViewCard: TextView = itemView.findViewById(R.id.textViewCard)

        fun bind(data: HomeViewModel) {
            imageViewCard.setImageResource(data.imageResId)
            textViewCard.text = data.name

            itemView.setOnClickListener {
                leftClickListener?.onLeftItemClick(data)
                rightClickListener?.onRightItemClick(data)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_card, parent, false)
        return HomeViewHolder(view)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}
