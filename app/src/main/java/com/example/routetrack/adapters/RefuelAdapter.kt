package com.example.routetrack.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.routetrack.R
import com.example.routetrack.database.Converter
import com.example.routetrack.database.Refuel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class RefuelAdapter(
    private val context: Context,
    private val refuelList: List<Refuel>
) :
    RecyclerView.Adapter<RefuelAdapter.RefuelViewHolder>() {

    inner class RefuelViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.refuelImage)
        val price: TextView = itemView.findViewById(R.id.refuelPrice)
        val liter: TextView = itemView.findViewById(R.id.refuelLiter)
        val date: TextView = itemView.findViewById(R.id.refuelDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RefuelViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_refuel, parent, false)
        return RefuelViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RefuelViewHolder, position: Int) {
        val currentRefuel = refuelList[position]

        val img = currentRefuel.img?.let {
            Converter.toBitmap(it)
        }

        if (img != null){
            Glide
                .with(context)
                .load(img)
                .into(holder.image)
        }

        holder.price.text = String.format("%.2f RON", currentRefuel.price)
        holder.liter.text = String.format("%.2f L", currentRefuel.liter)

        val date = Date(currentRefuel.timestamp)
        val sdf = SimpleDateFormat("dd.MM.yy", Locale.getDefault())

        holder.date.text = sdf.format(date)

    }

    override fun getItemCount(): Int {
        return refuelList.size
    }
}