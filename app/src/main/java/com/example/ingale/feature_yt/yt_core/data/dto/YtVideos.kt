package com.example.ingale.feature_yt.yt_core.data.dto

import com.google.gson.annotations.SerializedName

data class YtVideos(
    @SerializedName("items") val videos: List<YtVideo>
)

data class YtVideo(
    @SerializedName("id") val ytVideoId: YtVideoId
)

data class YtVideoId(
    @SerializedName("videoId") val id: String
)