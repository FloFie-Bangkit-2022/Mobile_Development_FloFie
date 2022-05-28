package com.capstone.flofie.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Shop(
    var id : Int,
    var name : String? = null,
    var rating : String? = null,
    var address : String? = null,
    var picture : String? = null,
    var lat : Double? = null,
    var long : Double? = null,
) : Parcelable