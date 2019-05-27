package com.aftabsikander.mercari.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.aftabsikander.mercari.di.Injectable
import com.aftabsikander.mercari.utilities.extensions.setToolbarTitle
import com.aftabsikander.mercari.utilities.extensions.showToolBar
import javax.inject.Inject

/**
 * Abstract Base fragment class which holds common implementation for all derived fragments.
 *
 * @param V Instance of [ViewModel] which will be used for UI interactions
 * @param D Instance of [ViewDataBinding] which we will use to bind our fragment to the provided layout.
 */
abstract class BaseFragment<V : ViewModel, D : ViewDataBinding> : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    protected lateinit var viewModel: V

    protected lateinit var dataBinding: D

    /**
     * Get [ViewModel] instance for the fragment.
     * @return [ViewModel] instance
     */
    protected abstract fun getViewModel(): Class<V>

    /**
     * Get Layout resource id for inflating.
     * @return [LayoutRes] ID
     */
    @LayoutRes
    protected abstract fun getLayoutRes(): Int

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dataBinding = DataBindingUtil.inflate(inflater, getLayoutRes(), container, false)
        return dataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(getViewModel())
    }

    /**
     * Set Toolbar title and display it on UI
     * @param title Title which needs to be displayed.
     */
    protected fun setToolbarText(title: String) {
        showToolBar()
        setToolbarTitle(title)
    }
}