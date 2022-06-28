package com.ehubstar.marketplace.models.local


import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Province(
    var code: String = "",
    var districts: ArrayList<District> = ArrayList(),
    var id: String = "",
    var name: String = "",
    var isExpanded: Boolean = false,
    var type:Int = LocalConstants.PARENT
) : Parcelable{
    fun clone():Province{
        return Province(code,districts, id, name, isExpanded)
    }
}
