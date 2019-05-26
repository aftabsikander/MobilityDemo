package com.aftabsikander.mercari.ui.adapters

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.aftabsikander.mercari.network.models.CategoryModel
import com.aftabsikander.mercari.ui.fragments.CategoryDetailFragment

class CategoryPagerAdapter(
    private var catColl: ArrayList<CategoryModel>,
    fragmentManager: FragmentManager) :
    FragmentPagerAdapter(fragmentManager) {

    override fun getItem(position: Int): Fragment {
        return pushFragmentWithData(position)
    }

    override fun getCount(): Int {
        return catColl.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return getPageTitleFromCollection(position)
    }

    private fun getPageTitleFromCollection(position: Int): String {
        return catColl[position].name
    }

    private fun pushFragmentWithData(position: Int): Fragment {
        return CategoryDetailFragment.newInstance(catColl[position].name)
    }

    fun setData(updatedCollection: ArrayList<CategoryModel>) {
        this.catColl = updatedCollection
        notifyDataSetChanged()
    }

}