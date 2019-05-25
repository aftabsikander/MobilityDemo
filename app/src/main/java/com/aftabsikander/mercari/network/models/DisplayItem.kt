package com.aftabsikander.mercari.network.models

import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import java.util.*

open class DisplayItem(
    open var catID: String = UUID.randomUUID().toString(),
    @SerializedName("id")
    open var idFromServer: String = "",
    open var categoryName: String = "",
    @SerializedName("status")
    open var status: String = "",
    @SerializedName("name")
    open var name: String = "",
    @SerializedName("num_likes")
    open var likeCount: Long = 0L,
    @SerializedName("num_comments")
    open var commentCounts: Long = 0L,
    @SerializedName("price")
    open var amount: Double = 0.0,
    @SerializedName("photo")
    open var imgURL: String = ""
) : RealmObject() {

    fun displayAmount(): String {
        return "$ ${this.amount}"
    }
}