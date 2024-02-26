package com.brins.lib_base.controller

import android.content.ComponentName
import android.content.ContextWrapper
import android.content.ServiceConnection
import android.os.IBinder
import com.brins.lib_base.service.MusicService
import javax.inject.Inject

class MusicPlayerController @Inject constructor() {

    companion object {
        val TAG = MusicPlayerController::class.simpleName
    }
    var musicService: MusicService? = null



    inner class ServiceBinder internal constructor(private val mCallback: ServiceConnection?) :
        ServiceConnection {

        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as MusicService.MusicBinder
            musicService = binder.service
            mCallback?.onServiceConnected(className, service)
        }

        override fun onServiceDisconnected(className: ComponentName) {
            mCallback?.onServiceDisconnected(className)
            musicService = null
        }
    }

    inner class ServiceToken internal constructor(internal var mWrappedContext: ContextWrapper)
}