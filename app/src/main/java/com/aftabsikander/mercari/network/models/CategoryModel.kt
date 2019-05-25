package com.aftabsikander.mercari.network.models

import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class CategoryModel(
    @PrimaryKey
    @SerializedName("name")
    open var name: String = "",
    @SerializedName("data")
    open var dataURL: String = ""
) : RealmObject()