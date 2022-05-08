package com.adl.portalearning.model

class ModelVideo {

    //variables, use same names as in firebase
    var id:String? = null
    var title:String? = null
    var description:String? = null
    var timestamp:String? = null
    var videoUri:String? = null

    //constructor

    constructor(){

    }

    constructor(
        id: String?,
        title: String?,
        description: String?,
        timestamp: String?,
        videoUri: String?
    ) {
        this.id = id
        this.title = title
        this.description = description
        this.timestamp = timestamp
        this.videoUri = videoUri
    }


}