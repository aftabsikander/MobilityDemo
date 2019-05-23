package com.aftabsikander.mercari.network.services

import com.aftabsikander.mercari.network.models.CategoryResponse
import com.aftabsikander.mercari.network.models.StartupResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface MercariService {

    //region Startup EndPoint
    @GET("master.json")
    fun getStartupData(): Call<StartupResponse>
    //endregion

    //region Category EndPoints
    @GET
    fun getCategoryData(@Url categoryURL: String): Call<CategoryResponse>
    //endregion
}