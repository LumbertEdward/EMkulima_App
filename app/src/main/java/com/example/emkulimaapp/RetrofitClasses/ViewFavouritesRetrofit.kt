package com.example.emkulimaapp.RetrofitClasses

import com.example.emkulimaapp.constants.constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ViewFavouritesRetrofit {
    companion object{
        fun getRetrofit(): Retrofit {
            return Retrofit.Builder()
                .baseUrl(constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }
}