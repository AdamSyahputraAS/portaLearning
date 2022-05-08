package com.adl.portalearning.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ModelVideo (

    //variables, use same names as in firebase
    var id:String? = null,
    var title:String? = null,
    var description:String? = null,
    var timestamp:String? = null,
    var videoUri:String? = null,
    var imageUri:String? = null

): Parcelable