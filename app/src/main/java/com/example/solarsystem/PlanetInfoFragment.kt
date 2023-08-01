package com.example.solarsystem

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import coil.load
import coil.request.CachePolicy
import coil.size.Precision

class PlanetInfoFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.planet_info, container, false)

        val informationKeysValues = mapOf(
            Pair("radius", intArrayOf(R.id.radius, R.id.radius_info)),
            Pair("mass", intArrayOf(R.id.mass, R.id.mass_info)),
            Pair("density", intArrayOf(R.id.density, R.id.density_info)),
            Pair("openDate", intArrayOf(R.id.open_date, R.id.open_date_info)),
            Pair("deltaV", intArrayOf(R.id.dv, R.id.dv_info)),
            Pair("description", intArrayOf(R.id.description, R.id.description_info))
        )

        view.findViewById<TextView>(R.id.planet_name).text = arguments?.getString("planetName")

        view.findViewById<ImageView>(R.id.planet_image).load(arguments?.getString("imgURL")) {
            diskCachePolicy(CachePolicy.DISABLED)
            memoryCachePolicy(CachePolicy.DISABLED)
            precision(Precision.EXACT)
        }

        setInformation(view, informationKeysValues)

        return view
    }

    private fun setInformation(view: View, information: Map<String,IntArray>) {
        var info: String?
        for (entry in information) {
            info = arguments?.getString(entry.key)
            if (info.isNullOrEmpty()) {
                view.findViewById<LinearLayout>(entry.value[1]).visibility = View.GONE
            } else {
                view.findViewById<TextView>(entry.value[0]).text = info
            }
        }
    }

    companion object {
        fun newInstance(bundle: Bundle): PlanetInfoFragment {
            val fragment = PlanetInfoFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}