package com.example.cinema.domain

import java.io.Serializable

class Character : Serializable {
    var noFilm: String? = null
    var noAct: String? = null
    val film: Movie? = null
    val acteur: Actor? = null
    var nomPers: String = ""
}