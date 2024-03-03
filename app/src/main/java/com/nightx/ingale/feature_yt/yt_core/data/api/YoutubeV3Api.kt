package com.nightx.ingale.feature_yt.yt_core.data.api

import com.nightx.ingale.feature_yt.yt_core.Constants.CATEGORY_ID
import com.nightx.ingale.feature_yt.yt_core.Constants.CATEGORY_MUSIC
import com.nightx.ingale.feature_yt.yt_core.Constants.PARAM_TYPE
import com.nightx.ingale.feature_yt.yt_core.Constants.TYPE_VIDEO
import com.nightx.ingale.feature_yt.yt_core.data.dto.YtVideos
import retrofit2.http.GET
import retrofit2.http.Headers

interface YoutubeV3Api {

    @GET("search")
    @Headers(
        *arrayOf(
            "$PARAM_TYPE: $TYPE_VIDEO",
            "$CATEGORY_ID: $CATEGORY_MUSIC"
        )
    )
    fun getRecommendations(): List<YtVideos>
}