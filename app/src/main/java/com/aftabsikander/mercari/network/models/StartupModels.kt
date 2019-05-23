package com.aftabsikander.mercari.network.models

import com.google.gson.annotations.SerializedName
import io.realm.RealmModel
import io.realm.annotations.PrimaryKey

open class CategoryModel(
    @PrimaryKey
    @SerializedName("name")
    open val name: String = "",
    @SerializedName("data")
    open val dataURL: String = ""
) : RealmModel

open class DisplayItem(
    @PrimaryKey
    @SerializedName("id")
    open val id: String = "",
    open var categoryID: String = "",
    @SerializedName("status")
    open val status: String,
    @SerializedName("name")
    open val name: String,
    @SerializedName("num_likes")
    open val likeCount: Long,
    @SerializedName("num_comments")
    open val commentCounts: Long,
    @SerializedName("price")
    open val amount: Double,
    @SerializedName("photo")
    open val imgURL: Double
) : RealmModel

class StartupResponse(val catTabs: ArrayList<CategoryModel>)
data class CategoryResponse(val catDataCol: ArrayList<DisplayItem>)



