package com.example.solarsystem

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import okhttp3.*
import org.jsoup.Jsoup
import java.io.IOException

class MainActivityViewModel : ViewModel() {

    private val coroutineScope = CoroutineScope(Dispatchers.Main + Job())
    private var _planets =  MutableLiveData<MutableList<Planet>>(mutableListOf())
    val planets: LiveData<MutableList<Planet>>
        get() = _planets

    fun parsePlanets() {
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

                    _planets.value?.add(
                        Planet(
                            planetName, radius, mass, density, openDate, deltaV, description, imgURL
                        )
                    )
                    coroutineScope.launch { _planets.value = _planets.value }
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        coroutineScope.cancel()
    }

}

