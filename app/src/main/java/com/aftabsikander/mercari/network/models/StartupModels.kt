package com.aftabsikander.mercari.network.models

import com.google.gson.annotations.SerializedName

data class CategoryModel(
    @SerializedName("name")
    val name: String,
    @SerializedName("data")
    val dataURL: String
)

data class DisplayItem(
    @SerializedName("status")
    val status: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("num_likes")
    val likeCount: Long,
    @SerializedName("num_comments")
    val commentCounts: Long,
    @SerializedName("price")
    val amount: Double,
    @SerializedName("photo")
    val imgURL: Double
)


data class StartupResponse(val catTabs: ArrayList<CategoryModel>)
data class CategoryResponse(val catDataCol: ArrayList<DisplayItem>)



