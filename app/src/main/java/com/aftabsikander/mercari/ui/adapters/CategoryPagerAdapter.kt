package com.aftabsikander.mercari.ui.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.aftabsikander.mercari.network.models.CategoryModel
import com.aftabsikander.mercari.ui.fragments.CategoryDetailFragment

/**
 * Category Tab Pager Adapter which holds all the setup and configure values for it's population
 *
 * @param catColl [CategoryModel] collection which holds how many tabs needs to be displayed on screen.
 */
class CategoryPagerAdapter(
    private var catColl: ArrayList<CategoryModel>,
    fragmentManager: FragmentManager
) :
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


    //region General Helper methods
    /**
     * Retrieve title for current page title
     * @param position Current position
     *
     * @return Title for current page
     */
    private fun getPageTitleFromCollection(position: Int): String {
        return catColl[position].name
    }

    /**
     * Create new instance for [CategoryDetailFragment] for given position of pager.
     * @param position Current position
     *
     * @return Instance of [CategoryDetailFragment]
     */
    private fun pushFragmentWithData(position: Int): Fragment {
        return CategoryDetailFragment.newInstance(catColl[position].name)
    }

    /**
     * Add/Update data source for pager and updates pager.
     * @param updatedCollection Latest collection for pager which holds [CategoryModel] instances.
     */
    fun setData(updatedCollection: ArrayList<CategoryModel>) {
        this.catColl = updatedCollection
        notifyDataSetChanged()
    }
    //endregion

}