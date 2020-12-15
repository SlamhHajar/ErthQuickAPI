package com.hajarslamah.erthquickapi

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hajarslamah.erthquickapi.api.ErthQuckApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val TAG = "ErthFetchr"

class ErthQuickFetcher {
      private val erthApi: ErthQuckApi

    init {
        val retrofit: Retrofit = Retrofit.
        Builder()
            .baseUrl("https://earthquake.usgs.gov/fdsnws/event/1/")
             .addConverterFactory(
                GsonConverterFactory.create()
            ).build()
        erthApi = retrofit.create(ErthQuckApi::class.java)
    }
    fun fetchContents(): LiveData<List<ErthData>> {
        val responseLiveData: MutableLiveData<List<ErthData>> = MutableLiveData()
        val erthRequest: Call<ErthResponse> = erthApi.fetchContents()
        erthRequest.enqueue(object : Callback <ErthResponse> {
 override fun onFailure(call: Call<ErthResponse>,t: Throwable) {
             Log.e(TAG, "Failed to fetch erthItems", t)            }
  override fun onResponse(
                call: Call<ErthResponse>,
                response: Response<ErthResponse>
            ) {
                Log.d(TAG, "Response received ${response.code().toString()} ")
               // response.body().toString()
                //responseLiveData.value = response.body()
                val erthResponse: ErthResponse? = response.body()
           //  val erthToResponse :ErthResponse?  = E?.erth
                var erthItems: List<ErthData> = erthResponse?.erthItems
                    ?: mutableListOf()
//                erthItems = erthItems.filterNot {
//                    it.url.isBlank()
              //  }
                responseLiveData.value = erthItems
            }
        })
        return responseLiveData}

}
