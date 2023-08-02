package com.example.solarsystem

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.solarsystem.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: RecyclerViewAdapter
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewmodel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        val recyclerView = binding.planetsContainer
        adapter = RecyclerViewAdapter {
            navigateToFragmentInfo(it)
        }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(this, 2)

        viewmodel = ViewModelProvider(this)[MainActivityViewModel::class.java]
        viewmodel.planets.observe(this) {
            updateRecyclerViewAdapterData(it)
        }

        viewmodel.parsePlanets()

        setContentView(binding.root)
    }

    private fun updateRecyclerViewAdapterData(planets: List<Planet>) {
        adapter.planets = planets
    }

    private fun navigateToFragmentInfo(planet: Planet) {
        val bundle = Bundle()
        bundle.putString("planetName", planet.planetName)
        bundle.putString("radius", planet.radius)
        bundle.putString("mass", planet.mass)
        bundle.putString("density", planet.density)
        bundle.putString("openDate", planet.openDate)
        bundle.putString("deltaV", planet.deltaV)
        bundle.putString("description", planet.description)
        bundle.putString("imgURL", planet.imgURL)

        supportFragmentManager.beginTransaction()
            .addToBackStack(null).add(R.id.main_container, PlanetInfoFragment.newInstance(bundle))
            .commit()
    }
}