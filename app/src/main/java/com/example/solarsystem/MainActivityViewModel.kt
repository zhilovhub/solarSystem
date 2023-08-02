package com.example.solarsystem

import androidx.lifecycle.ViewModel

class MainActivityViewModel : ViewModel() {

    private var _planets = mutableListOf<Planet>()
    val planets: List<Planet>
        get() = _planets.toList()

}

