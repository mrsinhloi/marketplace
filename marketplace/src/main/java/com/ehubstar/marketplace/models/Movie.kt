package com.ehubstar.marketplace.models


import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class Movie(
    var category: String? = "",
    var desc: String? = "",
    var imageUrl: String? = "",
    var name: String? = ""
) : Parcelable