package com.example.cinema.domain

import java.io.Serializable

class Character : Serializable {
    val noFilm = 0
    val noAct = 0
    val film: Movie? = null
    val acteur: Actor? = null
    var nomPers: String = ""
}