package com.aftabsikander.mercari.network.models

import com.aftabsikander.mercari.utilities.constants.DisplayItemStatus
import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * Display item class which acts both as Data and Realm Object class
 *
 * @param uniqueID Acts as a [PrimaryKey] field which hold unique value for avoiding duplicate values.
 * @param id ID from sever
 * @param categoryName Category name for which this item belongs to.
 * @param status Status values i.e [DisplayItemStatus.SOLD_OUT] or [DisplayItemStatus.ON_SALE]
 * @param name Name which is received from server.
 * @param likeCount Number of like received on this item
 * @param commentCounts Number of comments received on this item
 * @param amount Price of this item
 * @param imgURL Display image URL
 *
 * @see [RealmObject]
 */
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

    /**
     * Display amount with currency sign
     *
     * @return Amount with Dollar sign
     */
    fun displayAmount(): String {
        return "$ ${this.amount}"
    }

    /**
     * Generate Unique primary key which combination of [id] and [categoryName]
     *
     * @return Unique primary key
     */
    fun generateUniqueID() {
        uniqueID = "${this.id.toLowerCase()}-${this.categoryName.toLowerCase()}"
    }
}