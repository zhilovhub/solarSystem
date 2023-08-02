package com.example.solarsystem

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import coil.load
import coil.request.CachePolicy
import coil.size.Precision
import com.example.solarsystem.databinding.PlanetInfoBinding


class PlanetInfoFragment : Fragment() {

    private lateinit var binding: PlanetInfoBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = PlanetInfoBinding.inflate(inflater, container, false)

        binding.planetName.text = arguments?.getString("planetName")

        binding.planetImage.load(arguments?.getString("imgURL")) {
            diskCachePolicy(CachePolicy.DISABLED)
            memoryCachePolicy(CachePolicy.DISABLED)
            precision(Precision.EXACT)
        }

        binding.apply {val informationKeysValues =
            mapOf(
                Pair("radius", Pair(radius, radiusInfo)),
                Pair("mass", Pair(mass, massInfo)),
                Pair("density", Pair(density, densityInfo)),
                Pair("openDate", Pair(openDate, openDateInfo)),
                Pair("deltaV", Pair(dv, dvInfo)),
                Pair("description", Pair(description, descriptionInfo))
            )
            setInformation(informationKeysValues)
        }

        return binding.root
    }

    private fun setInformation(information: Map<String,Pair<TextView, LinearLayout>>) {
        var info: String?
        for (entry in information) {
            info = arguments?.getString(entry.key)
            if (info.isNullOrEmpty()) {
                entry.value.second.visibility = View.GONE
            } else {
                entry.value.first.text = info
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