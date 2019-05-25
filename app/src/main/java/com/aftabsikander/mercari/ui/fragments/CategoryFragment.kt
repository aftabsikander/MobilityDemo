package com.aftabsikander.mercari.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.aftabsikander.mercari.R
import com.aftabsikander.mercari.databinding.CategoryFragmentBinding
import com.aftabsikander.mercari.network.base.Status
import com.aftabsikander.mercari.network.models.CategoryModel
import com.aftabsikander.mercari.ui.adapters.CategoryPagerAdapter
import com.aftabsikander.mercari.ui.base.BaseFragment
import com.aftabsikander.mercari.utilities.constants.AppConstants
import com.aftabsikander.mercari.viewmodel.CategoryListViewModel
import com.google.android.material.tabs.TabLayout
import timber.log.Timber

class CategoryFragment : BaseFragment<CategoryListViewModel, CategoryFragmentBinding>() {

    private var categoryList = arrayListOf<CategoryModel>()
    private var rootView: View? = null
    private var adapter: CategoryPagerAdapter? = null
    private var selectedTab = 0


    companion object {
        fun newInstance() = CategoryFragment()
    }

    override fun getViewModel(): Class<CategoryListViewModel> {
        return CategoryListViewModel::class.java
    }

    override fun getLayoutRes(): Int {
        return R.layout.category_fragment
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        if (rootView == null) {
            rootView = dataBinding.root
        }
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.performStartLoad().observe(viewLifecycleOwner, Observer { listResource ->
            if (null != listResource && (listResource.status === Status.ERROR || listResource.status === Status.SUCCESS)) {
                dataBinding.Progress.visibility = View.GONE
            }
            dataBinding.resource = listResource
            if (listResource.data != null) {
                showMainLayout()
                listResource.data as ArrayList<CategoryModel>
                adapter?.setData(listResource.data)
                updateTabStyleAccordingToCategoryCount(listResource.data.size)
                dataBinding.viewPagerForListing.currentItem = selectedTab
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setToolbarText(this.getString(R.string.app_name))
        setupViewPager()
        if (savedInstanceState != null) {
            restoreInstance(savedInstanceState)
        }

        dataBinding.fabSell.setOnClickListener {
            Timber.d("Fab Clicked")
        }
    }

    //region General Helper methods
    private fun showMainLayout() {
        dataBinding.viewPagerForListing.visibility = View.VISIBLE
        dataBinding.fabSell.visibility = View.VISIBLE
    }

    private fun updateTabStyleAccordingToCategoryCount(tabCount: Int) {
        if (tabCount >= AppConstants.MINIMUM_TAB_SCROLL_COUNT) {
            dataBinding.slidingTabs.tabMode = TabLayout.MODE_SCROLLABLE
        } else {
            dataBinding.slidingTabs.tabMode = TabLayout.MODE_FIXED
        }
    }
    //endregion

    //region Helper methods for Setting up View pager and Tab layout
    private fun setupViewPager() {
        if (adapter == null) {
            adapter =
                CategoryPagerAdapter(categoryList, fragmentManager = childFragmentManager)
            dataBinding.viewPagerForListing.adapter = adapter
            dataBinding.viewPagerForListing.offscreenPageLimit = AppConstants.MINIMUM_TAB_SCROLL_COUNT
            dataBinding.slidingTabs.setupWithViewPager(dataBinding.viewPagerForListing, true)
            setupPageChangeListener()
        }
    }

    private fun setupPageChangeListener() {
        dataBinding.slidingTabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                // called when tab selected
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                // called when tab unselected
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
                // called when a tab is reselected
            }
        })
    }
    //endregion

    //region Save State and Restore States
    override fun onSaveInstanceState(outState: Bundle) {
        saveState(outState)
        super.onSaveInstanceState(outState)
    }

    private fun saveState(outState: Bundle) {
        outState.putInt("selectedTab", dataBinding.slidingTabs.selectedTabPosition)
    }

    private fun restoreInstance(outState: Bundle) {
        selectedTab = outState.getInt("selectedTab", 0)
    }
    //endregion

}
