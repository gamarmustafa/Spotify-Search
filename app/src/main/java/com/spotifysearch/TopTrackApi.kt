package com.spotifysearch

import com.spotifysearch.toptracks.TopTrack
import retrofit.*
import retrofit.http.*

interface TopTrackApi {
    @Headers(
        "Accept: application/json",
        "Content-Type: application/json")
       // "Authorization: Bearer BQC7bsTTGHOeh-IrXJwP4HD5RR3JKThFwH1iYXbBRZnklPez2CyCfai_o9q2Hk_piCEZqFCeoYXfFFdxil5Le0ln1W-7cJOMB4RljDq_JjIv3-bqeZU"

    @GET("7bu3H8JO7d0UbMoVzbo70s/top-tracks?market=ES")
    fun getTopTracks(@Header("Authorization") token1 :String): Call<TopTrack>

    @POST("api/token")
    @FormUrlEncoded
    fun getToken(
        @Header("Authorization") auth: String,
        @Header("Content-Type") content: String,
        @Field(("grant_type")) grantType: String
    ): Call<Token>

}