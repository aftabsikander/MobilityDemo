package com.aftabsikander.mercari.ui.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

/**
 * Abstract Base activity class which holds common implementation for all derived activities.
 *
 * @param T Instance of [ViewDataBinding] which we will use to bind our activity to the provided layout.
 *
 * @see [ViewDataBinding]
 * @see [HasSupportFragmentInjector]
 */
abstract class BaseActivity<T : ViewDataBinding> : AppCompatActivity(), HasSupportFragmentInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    lateinit var dataBinding: T

    /**
     * Get Layout resource id for inflating.
     * @return [androidx.annotation.LayoutRes] ID
     */
    @LayoutRes
    protected abstract fun getLayoutRes(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = DataBindingUtil.setContentView(this, getLayoutRes())
    }

    override fun supportFragmentInjector(): DispatchingAndroidInjector<Fragment>? {
        return dispatchingAndroidInjector
    }
}