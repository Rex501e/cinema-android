package com.example.cinema.domain

import java.io.Serializable

/**
 * Created by christian on 14/11/2020
 */


class Client (  nomCli: String,
                adrRueCli: String,
               villeCli: String,
                cpCli: String,
                numPieceCli : String,
                pieceCli  : String): Serializable {

    var numCli = 0f
    var nomCli: String? = nomCli
    var adrRueCli: String? = adrRueCli
    var villeCli: String? = villeCli
    var cpCli: String? = cpCli
    var numPieceCli: String? = numPieceCli
    var pieceCli: String? = pieceCli

}