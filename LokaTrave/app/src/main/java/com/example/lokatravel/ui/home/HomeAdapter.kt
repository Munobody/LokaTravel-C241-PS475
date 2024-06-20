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
) : RecyclerView.Adapter<HomeAdapter.ViewHolder>() {

    interface OnLeftItemClickListener {
        fun onLeftItemClick(data: HomeViewModel)
    }

    interface OnRightItemClickListener {
        fun onRightItemClick(data: HomeViewModel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.imageViewCard.setImageResource(item.imageResId)
        holder.textViewCardTitle.text = item.name

        holder.itemView.setOnClickListener {
            leftClickListener?.onLeftItemClick(item)
            rightClickListener?.onRightItemClick(item)
        }
    }

    override fun getItemCount(): Int = items.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageViewCard: ImageView = view.findViewById(R.id.imageViewCard)
        val textViewCardTitle: TextView = view.findViewById(R.id.textViewCard)
    }
}
