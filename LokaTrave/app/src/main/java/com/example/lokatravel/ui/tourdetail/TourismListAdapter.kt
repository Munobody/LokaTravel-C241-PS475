package com.example.lokatravel.ui.tourdetail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.lokatravel.R

class TourismListAdapter(private var items: List<TourismItem>) :
    RecyclerView.Adapter<TourismListAdapter.TourismViewHolder>() {

    // Listener interface for item click
    interface OnItemClickListener {
        fun onItemClick(item: TourismItem)
    }

    private var listener: OnItemClickListener? = null

    // Method to set the click listener externally
    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TourismViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.wisata_card, parent, false)
        return TourismViewHolder(view)
    }

    override fun onBindViewHolder(holder: TourismViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)

        // Set click listener
        holder.itemView.setOnClickListener {
            listener?.onItemClick(item)
        }
    }

    override fun getItemCount(): Int = items.size

    fun updateData(newItems: List<TourismItem>) {
        items = newItems
        notifyDataSetChanged()
    }

    class TourismViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageWisata: ImageView = itemView.findViewById(R.id.imageWisata)
        private val textNamaWisata: TextView = itemView.findViewById(R.id.textNamaWisata)
        private val textKategori: TextView = itemView.findViewById(R.id.textKategori)
        private val textDetailWisata: TextView = itemView.findViewById(R.id.textDetailWisata)

        fun bind(item: TourismItem) {
            imageWisata.setImageResource(item.imageResId)
            textNamaWisata.text = item.nama
            textKategori.text = item.kategori
            textDetailWisata.text = item.detail
        }
    }
}
