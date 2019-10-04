package com.dmims.dmims.remote
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class ApiClientPhp {
    companion object {
       // var PhpBaseUrl: String = "http://avbrh.gearhostpreview.com/"
      var PhpBaseUrl: String = "http://dmimsdu.in/web/"
        var retrofit: Retrofit? = null

        fun getClient(): Retrofit {
            if (retrofit == null) {

//                val gson = GsonBuilder()
//                    .setLenient()
//                    .create()

                retrofit = Retrofit.Builder()
                    .baseUrl(PhpBaseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return retrofit!!
        }
    }
}