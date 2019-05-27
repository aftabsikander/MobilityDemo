package com.aftabsikander.mercari.ui

import android.os.Bundle
import com.aftabsikander.mercari.R
import com.aftabsikander.mercari.databinding.ActivityMainBinding
import com.aftabsikander.mercari.ui.base.BaseActivity
import com.aftabsikander.mercari.ui.fragments.CategoryFragment
import com.aftabsikander.mercari.utilities.FragmentUtils
import com.aftabsikander.mercari.utilities.FragmentUtils.TRANSITION_NONE
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity<ActivityMainBinding>() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)

        if (savedInstanceState == null) {
            FragmentUtils.replaceFragment(
                this, CategoryFragment.newInstance(),
                R.id.fragContainer, false, TRANSITION_NONE
            )
        }
    }

    /**
     * Get Layout resource id for inflating.
     * @return [androidx.annotation.LayoutRes] ID
     */
    override fun getLayoutRes(): Int {
        return R.layout.activity_main
    }

}
