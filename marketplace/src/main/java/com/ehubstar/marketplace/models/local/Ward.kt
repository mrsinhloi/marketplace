package com.ehubstar.marketplace.models.local


import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class Ward(
    var id: String = "",
    var name: String = "",
    var prefix: String = ""
) : Parcelable