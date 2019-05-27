package com.aftabsikander.mercari.network.services

import com.aftabsikander.mercari.network.models.CategoryModel
import com.aftabsikander.mercari.network.models.DisplayItem
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

/**
 * API Endpoint Service Interface which will be used by our network layer [retrofit2.Retrofit]
 */
interface MercariService {

    //region Startup EndPoint
    @GET("master.json")
    fun getStartupData(): Call<ArrayList<CategoryModel>>
    //endregion

    //region Category EndPoints
    @GET
    fun getCategoryData(@Url categoryURL: String): Call<ArrayList<DisplayItem>>
    //endregion
}