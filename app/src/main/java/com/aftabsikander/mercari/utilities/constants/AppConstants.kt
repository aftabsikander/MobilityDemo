package com.aftabsikander.mercari.utilities.constants

/**
 * Constant helper class which holds all the application common constants fields for further usages in our application.
 */
object AppConstants {

    /**
     * Web connection timeout which will be used for our network layer
     */
    const val WEB_CONNECTION_TIMEOUT = 5L // in 5 sec
    /**
     * Web Connection read timeout which will be used for our network layer
     */
    const val WEB_READ_TIMEOUT = 60L // in minutes

    /**
     * Web Connection file read timeout which will be used for our network layer
     */
    const val WEB_WRITE_FILE_TIMEOUT = 3L // in 3 minutes

    /**
     * Initial Page index key for pagination API
     */
    const val INITIAL_PAGE = 1

    /**
     * Fetch limit for pagination limit per page. i.e number of item which would be displayed per page.
     */
    const val RECORD_LIMIT = 20

    /**
     * Pre Fetch distance for calling pagination API for fetching more details
     */
    const val PRE_FETCH_DISTANCE = 3

    /**
     * Initial Load size hint value for [androidx.paging] library.
     */
    const val INITIAL_LOAD_SIZE_HINT = RECORD_LIMIT * 2

    /**
     * Minimum number of tab count, after which [com.google.android.material.tabs.TabLayout] would be scrollable
     */
    const val MINIMUM_TAB_SCROLL_COUNT = 4
}