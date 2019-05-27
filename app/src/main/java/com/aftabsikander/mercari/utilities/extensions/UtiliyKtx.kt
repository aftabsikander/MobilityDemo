package com.aftabsikander.mercari.utilities.extensions

import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.aftabsikander.mercari.MercariApp


/**
 * Extension function for hiding Toolbar from fragment.
 */
fun Fragment.hideToolBar() {
    (activity as AppCompatActivity).supportActionBar?.hide()
}

/**
 * Extension function for showing Toolbar from fragment.
 */
fun Fragment.showToolBar() {
    (activity as AppCompatActivity).supportActionBar?.show()
}

/**
 * Extension function for showing Toolbar with Home back button enabled.
 */
fun Fragment.showHomeBack() {
    val toolbar = (activity as AppCompatActivity).supportActionBar
    toolbar?.setDisplayHomeAsUpEnabled(true)
    toolbar?.setDisplayShowHomeEnabled(true)
}

/**
 * Extension function for setting title on Toolbar.
 * @param title Title which needs to be set
 */
fun Fragment.setToolbarTitle(title: String) {
    (activity as AppCompatActivity).supportActionBar?.title = title
}

/**
 * Extension function for setting title on Toolbar.
 * @param resID Title which needs to be set
 */

fun Fragment.setToolbarTitle(resID: Int) {
    (activity as AppCompatActivity).supportActionBar?.title = getString(resID)
}


// region Internet Helper Methods

/**
 * This method is used to check for network connection if the user is connected to WI-FI or network mobile we can
 * access Network related method
 *
 * @return if WI-FI or Mobile net is on return true Else false;
 */
fun Fragment.checkNetworkStatus(): Boolean {
    val connMgr =
        MercariApp.getInstance().applicationContext?.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork = connMgr.activeNetworkInfo
    return activeNetwork != null && activeNetwork.isConnected
}

/**
 * This method is used to check for network connection if the user is connected to WI-FI
 * @return if WI-FI is on return true else false
 */
fun Fragment.isWiFiEnabled(): Boolean {
    val wifiManager =
        MercariApp.getInstance().applicationContext?.getSystemService(Context.WIFI_SERVICE) as WifiManager
    return wifiManager.isWifiEnabled
}


// endregion