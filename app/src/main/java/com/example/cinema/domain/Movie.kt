package com.example.cinema.domain

import  java.io.Serializable


class Movie : Serializable {
    var noFilm: String? = ""
    var titre: String? = ""
    var duree: String? = ""
    var dateSortie: String? = null
    var budget: String? = ""
    var montantRecette: String? = ""
    var noRea: String? = ""
    var codeCat: String? = null
    val urlImage: String? = null
    val urlTrailer: String? = null

    @Transient
    val realisateur: Realisator? = null
    @Transient
    val categorie: Category? = null
}