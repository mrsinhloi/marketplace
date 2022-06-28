package com.ehubstar.marketplace.models.local


import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class Street(
    var id: String = "",
    var name: String = "",
    var prefix: String = ""
) : Parcelable