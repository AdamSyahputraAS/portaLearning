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
    var imageUri:String? = null,
    var rating:String? = null,
    var uid:String? = null

): Parcelable

@Parcelize
data class Rating(
    var id:String,
    var userId:String,
    var videoId:String,
    var rateVal:Float
):Parcelable