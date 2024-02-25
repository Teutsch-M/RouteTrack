package com.example.routetrack.adapters

import android.annotation.SuppressLint
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
import com.example.routetrack.database.Route
import com.example.routetrack.utility.TrackingUtility
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class RouteAdapter(
    private val context: Context,
    private val routeList: List<Route>
) :
    RecyclerView.Adapter<RouteAdapter.RouteViewHolder>() {

    inner class RouteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val vehicleImage: ImageView = itemView.findViewById(R.id.item_vehicleImage)
        val image: ImageView = itemView.findViewById(R.id.item_routeImage)
        val distance: TextView = itemView.findViewById(R.id.item_routeDistance)
        val time: TextView = itemView.findViewById(R.id.item_routeTime)
        val avgSpeed: TextView = itemView.findViewById(R.id.item_routeSpeed)
        val date: TextView = itemView.findViewById(R.id.item_routeDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RouteViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_route, parent, false)
        return RouteViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RouteViewHolder, position: Int) {
        val currentRoute = routeList[position]

        when (currentRoute.vehicle) {
            0 -> Glide
                .with(context)
                .load(R.drawable.baseline_directions_car_24)
                .into(holder.vehicleImage)

            1 -> Glide
                .with(context)
                .load(R.drawable.baseline_directions_bike_24)
                .into(holder.vehicleImage)

            2 -> Glide
                .with(context)
                .load(R.drawable.baseline_directions_walk_24)
                .into(holder.vehicleImage)
        }

        val img = currentRoute.img?.let { Converter.toBitmap(it) }

        if (img != null){
            Glide
                .with(context)
                .load(img)
                .into(holder.image)
        }

        holder.distance.text = String.format("%.2f km", currentRoute.distance)
        holder.time.text = TrackingUtility.formatTime(currentRoute.duration, false)
        holder.avgSpeed.text = "${currentRoute.avgSpeed} km/h"

        val sdf = SimpleDateFormat("dd.MM.yy", Locale.getDefault())
        val date = Date(currentRoute.timestamp)

        holder.date.text = sdf.format(date)

    }

    override fun getItemCount(): Int {
        return routeList.size
    }
}