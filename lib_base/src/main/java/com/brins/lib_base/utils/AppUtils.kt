package com.brins.lib_base.utils

import android.app.Application

object AppUtils {

    var sApplication: Application? = null
    fun init(application: Application) {
        this.sApplication = application
    }

}