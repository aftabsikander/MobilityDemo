package com.aftabsikander.mercari.network.repository

import androidx.annotation.NonNull
import androidx.lifecycle.LiveData
import com.aftabsikander.mercari.network.NetworkBoundResource
import com.aftabsikander.mercari.network.base.Resource
import com.aftabsikander.mercari.network.models.CategoryModel
import com.aftabsikander.mercari.network.models.StartupResponse
import com.aftabsikander.mercari.network.services.MercariService
import com.zhuinden.monarchy.Monarchy
import io.realm.Realm
import retrofit2.Call
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class StartUpRepository @Inject
constructor(private val service: MercariService, private val realm: Realm, var monarchy: Monarchy) {

    fun loadStartUpCategories(): LiveData<Resource<List<CategoryModel>>> {
        return object : NetworkBoundResource<List<CategoryModel>, StartupResponse>() {
            override fun saveCallResult(item: StartupResponse?) {
                realm.executeTransaction {
                    if (item != null) {
                        it.insertOrUpdate(item.catTabs)
                    }
                }
            }

            @NonNull
            override fun loadFromDb(): LiveData<List<CategoryModel>> {
                return monarchy.findAllCopiedWithChanges { realm -> realm.where(CategoryModel::class.java) }
            }

            @NonNull
            override fun createCall(): Call<StartupResponse> {
                return service.getStartupData()
            }
        }.asLiveData
    }

}
