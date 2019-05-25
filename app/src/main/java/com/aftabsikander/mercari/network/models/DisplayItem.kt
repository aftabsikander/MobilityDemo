package com.aftabsikander.mercari.network.models

import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class DisplayItem(
    @PrimaryKey
    open var uniqueID: String? = null,
    @SerializedName("id")
    open var id: String = "",
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

    fun generateUniqueID() {
        uniqueID = "${this.id.toLowerCase()}-${this.categoryName.toLowerCase()}"
    }
}