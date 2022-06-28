package com.ehubstar.marketplace.models.local


import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class District(
    var id: String = "",
    var name: String = "",
    var projects: List<Project> = listOf(),
    var streets: List<Street> = listOf(),
    var wards: List<Ward> = listOf()/*,
    var isSelected: Boolean = false*/
) : Parcelable