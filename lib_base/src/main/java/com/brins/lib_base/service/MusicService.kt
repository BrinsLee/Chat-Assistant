package com.brins.lib_base.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder

class MusicService : Service() {

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    inner class MusicBinder : Binder() {
        val service: MusicService
            get() = this@MusicService
    }
}