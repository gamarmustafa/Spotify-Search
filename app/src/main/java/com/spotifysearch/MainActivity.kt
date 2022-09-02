package com.spotifysearch

import android.app.Dialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import android.widget.Toast
import androidx.core.view.isVisible
import com.spotifysearch.databinding.SearchLayoutBinding
import com.spotifysearch.search.SearchArtist
import retrofit.*


const val BASE_URL = "https://api.spotify.com/v1/artists/"
const val TOKEN_URL = "https://accounts.spotify.com/"
const val SEARCH_URL = "https://api.spotify.com"

class MainActivity : AppCompatActivity() {
    lateinit var binding: SearchLayoutBinding
    private var mProgressDialog:Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = SearchLayoutBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        // to search when enter is clicked on keyboard
        binding.searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                val searchText = binding.searchView.query.toString()
                if(searchText.isEmpty() || searchText.isBlank()){
                    Toast.makeText(this@MainActivity, "Artist's name cannot be empty", Toast.LENGTH_SHORT).show() }

                getToken(searchText)
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                return false }

        })
        // to search on button click
        binding.btnSearch.setOnClickListener {
            val searchText = binding.searchView.query.toString()
            if(searchText.isEmpty() || searchText.isBlank()){
                Toast.makeText(this, "Artist's name cannot be empty", Toast.LENGTH_SHORT).show()
            }
            getToken(searchText)
        }
    }
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (currentFocus != null) {
            val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(this.currentFocus!!.windowToken, 0)
        }
        return super.dispatchTouchEvent(ev)
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
        showProgressDialog()
        val retrofit: Retrofit = Retrofit.Builder().baseUrl(TOKEN_URL)
            .addConverterFactory(GsonConverterFactory.create()).build()
        val service : TopTrackApi = retrofit.create(TopTrackApi::class.java)

        val base64String = "Basic " + get64BaseString("6bcfc9a9fccd45c8b9bb9c980f03e653:96c65e1824084adfb1ae99ef97734621")

        val listCall : Call<Token> = service.getToken(base64String,"application/x-www-form-urlencoded","client_credentials")
        listCall.enqueue(object : Callback<Token> {
            override fun onResponse(response: Response<Token>?, retrofit: Retrofit?) {
                if (response?.body() != null) {
                    hideProgressDialog()
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


    private fun showProgressDialog() {
        mProgressDialog = Dialog(this)
        mProgressDialog!!.setContentView(R.layout.dialog_custom_progress)
        mProgressDialog!!.show()
    }
    private fun hideProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog!!.dismiss()
        }
    }
}