package com.aftabsikander.mercari.utilities.constants

object AppConstants {
    const val WEB_CONNECTION_TIMEOUT = 5L // in 5 sec
    const val WEB_READ_TIMEOUT = 60L // in minutes
    const val WEB_FILE_READ_TIMEOUT = 120L // in minutes
    const val INITIAL_PAGE = 1
    const val RECORD_LIMIT = 20
    const val PRE_FETCH_DISTANCE = 3
    const val INITIAL_LOAD_SIZE_HINT = RECORD_LIMIT * 2
    const val MINIMUM_TAB_SCROLL_COUNT = 4
}