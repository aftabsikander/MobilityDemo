package com.aftabsikander.mercari.network.models

import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * Category item class which acts both as Data and Realm Object class
 *
 * @param name Acts as a [PrimaryKey] field which hold unique value for avoiding duplicate values
 * @param dataURL URL for Displaying its [DisplayItem]
 *
 * @see [RealmObject]
 */
open class CategoryModel(
    @PrimaryKey
    @SerializedName("name")
    open var name: String = "",
    @SerializedName("data")
    open var dataURL: String = ""
) : RealmObject()