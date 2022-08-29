package com.spotifysearch
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.spotifysearch.databinding.ActivityMainBinding
import com.spotifysearch.toptracks.TopTrack
import retrofit.*


const val BASE_URL =  "https://api.spotify.com/v1/artists/"
const val TOKEN_URL = "https://accounts.spotify.com/"

class MainActivity : AppCompatActivity() {
    private var adapter: TopTrackAdapter? = null
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        getToken()
        //setUpRV()
    }

    private fun setUpRV(token:String) {
        val retrofit: Retrofit = Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build()
        val service: TopTrackApi = retrofit.create(TopTrackApi::class.java)

        Log.i("token  value", token)

        binding.progressBar.isVisible = true
        val listCall: Call<TopTrack> = service.getTopTracks(": Bearer "+token)

        listCall.enqueue(object : Callback<TopTrack> {
            override fun onResponse(response: Response<TopTrack>?, retrofit: Retrofit?) {
                if (response?.body() != null) {
                    Log.i("Response! ", "It's working")
                    binding.progressBar.isVisible = false
                    adapter = TopTrackAdapter(response.body().tracks)
                    binding.rv.adapter = adapter
                    binding.rv.layoutManager = LinearLayoutManager(this@MainActivity)
                }
                if(response?.body() == null){
                    Log.i("Code" , response!!.code().toString())
                    Log.i("Response! ", "null response body /setUp function")
                }

            }

            override fun onFailure(t: Throwable?) {
                binding.progressBar.isVisible = false
                Log.e("Error", t!!.message.toString())
            }

        })
    }

    private fun getToken() {
        val retrofit: Retrofit = Retrofit.Builder().baseUrl(TOKEN_URL)
            .addConverterFactory(GsonConverterFactory.create()).build()
        val service :TopTrackApi = retrofit.create(TopTrackApi::class.java)

        val base64String = "Basic " + get64BaseString("6bcfc9a9fccd45c8b9bb9c980f03e653:96c65e1824084adfb1ae99ef97734621")

        Log.i("64Base ", base64String)
        val listCall : Call<Token> = service.getToken(base64String,"application/x-www-form-urlencoded","client_credentials")
        listCall.enqueue(object : Callback<Token>{
            override fun onResponse(response: Response<Token>?, retrofit: Retrofit?) {
                if (response?.body() != null) {
                Log.i("Response!!!!", response.body().access_token)
                setUpRV(response.body().access_token)
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
        return Base64.encodeToString(value.toByteArray(),Base64.NO_WRAP)
    }
}