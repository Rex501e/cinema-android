package com.example.cinema.domain

import java.io.Serializable
import java.util.*

class Actor : Serializable {
    val noAct: Int = 0
    val nomAct: String? = null
    val prenAct: String? = null
    val dateNaiss: String? = null
    val dateDeces: String? = null
    var characters: List<Character> = emptyList()
}