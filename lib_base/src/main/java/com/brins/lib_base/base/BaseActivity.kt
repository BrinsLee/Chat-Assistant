package com.brins.lib_base.base

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.brins.lib_base.extensions.hideStatusBar
import com.brins.lib_base.extensions.setBehindStatusBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
abstract class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setBehindStatusBar()
        super.onCreate(savedInstanceState)
    }
}