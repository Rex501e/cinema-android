package com.example.cinema.domain

import  java.io.Serializable


class Movie : Serializable {
    var noFilm: String? = null
    var titre: String = ""
    var duree: String = ""
    var dateSortie: String = ""
    var budget: String = ""
    var montantRecette: String = ""
    var noRea: String = ""
    var codeCat: String = ""
    var urlImage: String = ""
    var videotrailer: String = ""
    var titleimage: String = ""

    val realisateur: Realisator? = null
    val categorie: Category? = null
}