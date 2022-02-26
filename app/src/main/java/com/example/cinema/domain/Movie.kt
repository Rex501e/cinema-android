package com.example.cinema.domain

import  java.io.Serializable


class Movie : Serializable {
    var noFilm: String = "-1"
    var titre: String = ""
    var duree: String = ""
    var dateSortie: String = ""
    var budget: String = ""
    var montantRecette: String = ""
    var noRea: String = ""
    var codeCat: String = ""
    var urlImage: String = ""
    var urlTrailer: String = ""

    @Transient
    val realisateur: Realisator? = null
    @Transient
    val categorie: Category? = null
}