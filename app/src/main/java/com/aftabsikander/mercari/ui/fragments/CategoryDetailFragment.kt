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
import com.aftabsikander.mercari.network.models.DisplayItem
import com.aftabsikander.mercari.ui.adapters.DisplayListAdapter
import com.aftabsikander.mercari.ui.base.BaseFragment
import com.aftabsikander.mercari.ui.widget.ListSpacingDecoration
import com.aftabsikander.mercari.utilities.constants.BundleConstants
import com.aftabsikander.mercari.utilities.extensions.checkNetworkStatus
import com.aftabsikander.mercari.viewmodel.CategoryDetailViewModel
import timber.log.Timber

class CategoryDetailFragment :
    BaseFragment<CategoryDetailViewModel, CategoryDetailFragmentBinding>(), DisplayItemClickListener {
    private var detailList = arrayListOf<DisplayItem>()
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
        if (checkNetworkStatus()) {
            viewModel.performCategoryLoad(categoryID).observe(viewLifecycleOwner, Observer {
                adapter.submitList(it)
            })

            //viewModel.getResourceCallBack().observe(viewLifecycleOwner, Observer {})
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecycleView()
        setupDisplayListAdapter()
    }

    //region Helper method for RecycleView Setup & Adapter
    private fun setupRecycleView() {
        dataBinding.rvDetail.isVerticalScrollBarEnabled = true
        dataBinding.rvDetail.itemAnimator = DefaultItemAnimator()
        dataBinding.rvDetail.setHasFixedSize(true)
        dataBinding.rvDetail.addItemDecoration(
            ListSpacingDecoration(MercariApp.getInstance(), R.dimen.spacing_medium)
        )
        dataBinding.rvDetail.layoutManager = GridLayoutManager(activity, 2)
    }

    private fun setupDisplayListAdapter() {
        adapter = DisplayListAdapter(itemClick = this)
        dataBinding.rvDetail.adapter = adapter

    }
    //endregion


    //region Display Item Click events
    override fun displayItemPressed(item: DisplayItem) {
        Timber.d("Item clicked")
    }

    override fun photoLiked(item: DisplayItem) {
        Timber.d("Photo liked")
    }

    override fun commentPressed(item: DisplayItem) {
        Timber.d("Comment Pressed")
    }
    //endregion

}
