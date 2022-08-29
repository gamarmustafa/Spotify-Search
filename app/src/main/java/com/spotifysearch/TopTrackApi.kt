package com.spotifysearch

import com.spotifysearch.search.SearchArtist
import com.spotifysearch.toptracks.TopTrack
import retrofit.*
import retrofit.http.*

interface TopTrackApi {
    @Headers(
        "Accept: application/json",
        "Content-Type: application/json"
    )
    @GET
    fun getTopTracks(@Url url: String, @Header("Authorization") token1: String): Call<TopTrack>


    @GET("/v1/search")
    fun getArtist(
        @Header("Authorization") token2: String,
        @Query("q") q: String,
        @Query("type") type: String
    ): Call<SearchArtist>


    @POST("api/token")
    @FormUrlEncoded
    fun getToken(
        @Header("Authorization") auth: String,
        @Header("Content-Type") content: String,
        @Field(("grant_type")) grantType: String
    ): Call<Token>

}