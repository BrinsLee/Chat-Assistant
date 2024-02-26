package com.brins.lib_base.base

import android.content.Intent
import android.content.pm.PackageManager
import android.media.AudioManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import com.brins.lib_base.R
import com.brins.lib_base.extensions.hideStatusBar
import com.brins.lib_base.extensions.rootView
import com.brins.lib_base.extensions.setBehindStatusBar
import com.brins.lib_base.utils.VersionUtils
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
abstract class BaseActivity : AppCompatActivity() {

    private var hadPermissions: Boolean = false
    private lateinit var permissions: Array<String>
    private var permissionDeniedMessage: String? = null
    private val snackBarContainer: View
        get() = rootView
    companion object {
        const val TAG = "BaseActivity"
        const val PERMISSION_REQUEST = 100
        const val BLUETOOTH_PERMISSION_REQUEST = 101
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setBehindStatusBar()
        super.onCreate(savedInstanceState)
        volumeControlStream = AudioManager.STREAM_MUSIC
        permissions = getPermissionsToRequest()
        hadPermissions = hasPermissions()
        permissionDeniedMessage = null
    }

    override fun onResume() {
        super.onResume()
        val hasPermissions = hasPermissions()
        if (hasPermissions != hadPermissions) {
            hadPermissions = hasPermissions
            if (VersionUtils.hasMarshmallow()) {
                onHasPermissionsChanged(hasPermissions)
            }
        }
    }

    protected open fun onHasPermissionsChanged(hasPermissions: Boolean) {
        // implemented by sub classes
        Log.d(TAG, "$hasPermissions")
    }

    protected fun hasPermissions(): Boolean {
        for (permission in permissions) {
            if (ActivityCompat.checkSelfPermission(this,
                    permission) != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
        return true
    }

    open fun getPermissionsToRequest(): Array<String> {
        return arrayOf()
    }

    protected fun setPermissionDeniedMessage(message: String) {
        permissionDeniedMessage = message
    }

    fun getPermissionDeniedMessage(): String {
        return if (permissionDeniedMessage == null) getString(R.string.permissions_denied) else permissionDeniedMessage!!
    }

    protected open fun requestPermissions() {
        ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST) {
            for (grantResult in grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    Snackbar.make(
                        snackBarContainer,
                        permissionDeniedMessage!!,
                        Snackbar.LENGTH_INDEFINITE
                    ).setAction(R.string.action_settings) {
                        val intent = Intent()
                        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                        val uri = Uri.fromParts(
                            "package",
                            this@BaseActivity.packageName,
                            null
                        )
                        intent.data = uri
                        startActivity(intent)
                    }
                    return
                }
            }
            hadPermissions = true
            onHasPermissionsChanged(true)
        }
    }
}