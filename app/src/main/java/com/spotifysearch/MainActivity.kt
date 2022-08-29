package com.spotifysearch

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import com.spotifysearch.databinding.SearchLayoutBinding
import com.spotifysearch.search.SearchArtist
import com.spotifysearch.toptracks.TopTrack
import retrofit.*


const val BASE_URL = "https://api.spotify.com/v1/artists/"
const val TOKEN_URL = "https://accounts.spotify.com/"
const val SEARCH_URL = "https://api.spotify.com"

class MainActivity : AppCompatActivity() {
    lateinit var binding: SearchLayoutBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        binding = SearchLayoutBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnSearch.setOnClickListener {
            val searchText = binding.searchView.query.toString()
            getToken(searchText)
        }


    }

    fun searchArtist(token:String, artist:String) {
        val retrofit: Retrofit = Retrofit.Builder().baseUrl(SEARCH_URL)
            .addConverterFactory(GsonConverterFactory.create()).build()
        val service: TopTrackApi = retrofit.create(TopTrackApi::class.java)
        val listCall: Call<SearchArtist> = service.getArtist(": Bearer "+token,artist, "artist")
        listCall.enqueue(object : Callback<SearchArtist> {
            override fun onResponse(response: Response<SearchArtist>?, retrofit: Retrofit?) {
                if (response?.body() != null){
                    Log.i("result!", response.body().artists.items[0].id)
                    val intent = Intent(this@MainActivity, ResultActivity::class.java)
                    intent.putExtra("id", response.body().artists.items[0].id)
                    startActivity(intent)

                }
                if(response?.body() == null){
                    Log.i("Code" , response!!.code().toString())
                    Log.i("Response! ", "null response body /searchArtist function")
                }
            }

            override fun onFailure(t: Throwable?) {
                Log.e("Error", t!!.message.toString())
            }


        }
        )


    }
    private fun getToken(searchedArtist:String) {
        val retrofit: Retrofit = Retrofit.Builder().baseUrl(TOKEN_URL)
            .addConverterFactory(GsonConverterFactory.create()).build()
        val service : TopTrackApi = retrofit.create(TopTrackApi::class.java)

        val base64String = "Basic " + get64BaseString("6bcfc9a9fccd45c8b9bb9c980f03e653:96c65e1824084adfb1ae99ef97734621")

        val listCall : Call<Token> = service.getToken(base64String,"application/x-www-form-urlencoded","client_credentials")
        listCall.enqueue(object : Callback<Token> {
            override fun onResponse(response: Response<Token>?, retrofit: Retrofit?) {
                if (response?.body() != null) {
                    searchArtist(response.body().access_token,searchedArtist)
                }
                if(response?.body() == null){
                    Log.i("Response!", "null response body /getToken")
                }
            }
            override fun onFailure(t: Throwable?) {
                Log.e("Error", t!!.message.toString())
            }
        })

    }

    fun get64BaseString(value:String):String{
        return Base64.encodeToString(value.toByteArray(), Base64.NO_WRAP)
    }

}
