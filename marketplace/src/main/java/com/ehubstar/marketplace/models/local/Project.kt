package com.ehubstar.marketplace.models.local


import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class Project(
    var id: String = "",
    var lat: String = "",
    var lng: String = "",
    var name: String = ""
) : Parcelable