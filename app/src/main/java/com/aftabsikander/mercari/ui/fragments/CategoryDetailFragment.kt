package com.aftabsikander.mercari.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import com.aftabsikander.mercari.MercariApp
import com.aftabsikander.mercari.R
import com.aftabsikander.mercari.callbacks.DisplayItemClickListener
import com.aftabsikander.mercari.databinding.CategoryDetailFragmentBinding
import com.aftabsikander.mercari.network.base.Status
import com.aftabsikander.mercari.network.models.DisplayItem
import com.aftabsikander.mercari.ui.adapters.DisplayListAdapter
import com.aftabsikander.mercari.ui.base.BaseFragment
import com.aftabsikander.mercari.ui.widget.ListSpacingDecoration
import com.aftabsikander.mercari.utilities.constants.BundleConstants
import com.aftabsikander.mercari.viewmodel.CategoryDetailViewModel
import timber.log.Timber


/**
 * Category Display Item fragment which displays all the items present in specific category
 *
 * @see [com.aftabsikander.mercari.callbacks.DisplayItemClickListener]
 * @see [CategoryDetailViewModel]
 * @see [BaseFragment]
 */
class CategoryDetailFragment :
    BaseFragment<CategoryDetailViewModel, CategoryDetailFragmentBinding>(), DisplayItemClickListener {

    private lateinit var adapter: DisplayListAdapter
    private lateinit var categoryID: String
    private var rootView: View? = null

    override fun getViewModel(): Class<CategoryDetailViewModel> {
        return CategoryDetailViewModel::class.java
    }

    override fun getLayoutRes(): Int {
        return R.layout.category_detail_fragment
    }

    companion object {
        fun newInstance(categoryID: String) =
            CategoryDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(BundleConstants.CATEGORY_ID_TAG, categoryID)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            categoryID = it.getString(BundleConstants.CATEGORY_ID_TAG, "")
        }
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
        loadDisplayItemsForCategory()
        observePaginationNetworkChanges()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecycleView()
        setupDisplayListAdapter()
    }

    //region Helper method for RecycleView Setup & Adapter
    /**
     * Setup [androidx.recyclerview.widget.RecyclerView] configuration
     */
    private fun setupRecycleView() {
        dataBinding.rvDetail.isVerticalScrollBarEnabled = true
        dataBinding.rvDetail.itemAnimator = DefaultItemAnimator()
        dataBinding.rvDetail.setHasFixedSize(true)
        dataBinding.rvDetail.addItemDecoration(ListSpacingDecoration(MercariApp.getInstance(), R.dimen.spacing_xsmall))
        dataBinding.rvDetail.layoutManager = GridLayoutManager(activity, 2)
    }

    /**
     * Setup adapter which holds category display items
     */
    private fun setupDisplayListAdapter() {
        adapter = DisplayListAdapter(itemClick = this)
        dataBinding.rvDetail.adapter = adapter

    }
    //endregion

    //region Display Item Click events

    /**
     * Handle display item click event
     * @param item [DisplayItem] instance
     */
    override fun displayItemPressed(item: DisplayItem) {
        Timber.d("Item clicked")
    }

    /**
     * Handle photo like click event
     * @param item [DisplayItem] instance
     */
    override fun photoLiked(item: DisplayItem) {
        Timber.d("Photo liked")
    }

    /**
     * Handle Comment click event
     * @param item [DisplayItem] instance
     */
    override fun commentPressed(item: DisplayItem) {
        Timber.d("Comment Pressed")
    }

    //endregion

    //region Helper methods for View Model

    /**
     * Observe display item collection for specific category and display it on UI.
     *
     * @see [CategoryDetailViewModel]
     * @see [com.aftabsikander.mercari.network.repository.CategoryRepository]
     */
    private fun loadDisplayItemsForCategory() {
        viewModel.performDisplayItemLoading(categoryID).observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })
    }

    /**
     * Observe Pagination network changes when loading data from repository
     *
     * @see [CategoryDetailViewModel]
     * @see [com.aftabsikander.mercari.network.repository.CategoryRepository]
     */
    private fun observePaginationNetworkChanges() {
        viewModel.getLiveDataForPaginationCallBack().observe(viewLifecycleOwner, Observer { listResource ->
            if (null != listResource && (listResource.status === Status.ERROR || listResource.status === Status.SUCCESS)) {
                dataBinding.Progress.visibility = View.GONE
            }
            dataBinding.resource = listResource
            if (listResource.data.isNullOrEmpty()) {
                dataBinding.rvDetail.visibility = View.VISIBLE
            }
        })
    }

    //endregion

}
