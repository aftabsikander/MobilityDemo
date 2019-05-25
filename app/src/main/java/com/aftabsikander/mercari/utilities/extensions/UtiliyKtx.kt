package com.aftabsikander.mercari.utilities.extensions

import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.aftabsikander.mercari.MercariApp


fun Fragment.hideToolBar() {
    (activity as AppCompatActivity).supportActionBar?.hide()
}

fun Fragment.showToolBar() {
    (activity as AppCompatActivity).supportActionBar?.show()
}

fun Fragment.showHomeBack() {
    val toolbar = (activity as AppCompatActivity).supportActionBar
    toolbar?.setDisplayHomeAsUpEnabled(true)
    toolbar?.setDisplayShowHomeEnabled(true)
}

fun Fragment.setToolbarTitle(title: String) {
    (activity as AppCompatActivity).supportActionBar?.title = title
}

fun Fragment.setToolbarTitle(resID: Int) {
    (activity as AppCompatActivity).supportActionBar?.title = getString(resID)
}


// region Internet Helper Methods

/***
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


fun Fragment.isWiFiEnabled(): Boolean {
    val wifiManager =
        MercariApp.getInstance().applicationContext?.getSystemService(Context.WIFI_SERVICE) as WifiManager
    return wifiManager.isWifiEnabled
}


// endregion