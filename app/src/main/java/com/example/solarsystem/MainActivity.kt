package com.example.solarsystem

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.*
import okhttp3.*
import org.jsoup.Jsoup
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private val planets = mutableListOf<Planet>()
    private lateinit var adapter: RecyclerViewAdapter
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.planets_container)
        adapter = RecyclerViewAdapter {
            navigateToFragmentInfo(it)
        }

        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(this, 2)

        getHTMLPage()
        println(planets.size)
        println(12345)
    }

    private fun getHTMLPage() {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("https://gvard.github.io/solarsystem/")
            .get()
            .build()

        val call = client.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                throw IOException()
            }
            override fun onResponse(call: Call, response: Response) {
                parseHTML(response.body?.string())
            }
        })
    }

    private fun parseHTML(content: String?) {
        if (content != null) {
            val doc = Jsoup.parse(content)
            val planetsDivs = doc.getElementById("tab")?.getElementsByClass("obj")
            if (planetsDivs != null) {
                var planetName: String
                var radius: String
                var mass: String
                var density: String
                var openDate: String
                var deltaV: String
                var description: String
                var imgURL: String

                for (planetDiv in planetsDivs) {
                    planetName = planetDiv.getElementsByAttributeValue("class", "name").text()
                    radius = planetDiv.getElementsByAttributeValue("class", "size").text()
                    mass = planetDiv.getElementsByAttributeValue("class", "mass").text()
                    openDate = planetDiv.getElementsByAttributeValue("class", "date").text()
                    deltaV = planetDiv.getElementsByAttributeValue("class", "deltav").text()
                    density = planetDiv.getElementsByAttributeValue("class", "dens").text()
                    description = planetDiv.getElementsByAttributeValue("class", "desc").text()
                    imgURL = "https://gvard.github.io/solarsystem/${planetDiv.getElementsByTag("img").first()?.attr("src").toString()}"

                    planets.add(
                        Planet(
                            planetName, radius, mass, density, openDate, deltaV, description, imgURL
                        )
                    )
                }
                CoroutineScope(Dispatchers.Main).launch {
                    println(planets.size)
                    adapter.planets = planets
                }
            }
        }
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