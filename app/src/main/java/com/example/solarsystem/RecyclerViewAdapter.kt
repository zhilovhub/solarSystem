package com.example.solarsystem

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.request.CachePolicy
import coil.size.Precision

class RecyclerViewAdapter(
    val clickListener: (Planet) -> Unit
) : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    var planets = listOf<Planet>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount(): Int {
        return planets.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.planet_item, parent, false)
        return ViewHolder(view) {
            clickListener(planets[it])
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(planets[position])
    }

    class ViewHolder(view: View, clickAtPosition: (Int) -> Unit) : RecyclerView.ViewHolder(view) {
        private val planetImageView = view.findViewById<ImageView>(R.id.planet_view)

        init {
            view.setOnClickListener {
                clickAtPosition(adapterPosition)
            }
        }

        fun bind(planet: Planet) {
            planetImageView.load(planet.imgURL) {
                placeholder(R.drawable.empty)
                diskCachePolicy(CachePolicy.DISABLED)
                memoryCachePolicy(CachePolicy.DISABLED)
                precision(Precision.EXACT)
            }
        }

    }
}