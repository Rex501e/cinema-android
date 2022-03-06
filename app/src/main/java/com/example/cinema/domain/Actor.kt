package com.example.cinema.domain

import java.io.Serializable
import java.util.*

class Actor : Serializable {
    val noAct: String? = null
    var nomAct: String = ""
    var prenAct: String = ""
    var dateNaiss: String? = null
    var dateDeces: String? = null

    @Transient
    var characters: List<Character> = emptyList()
}