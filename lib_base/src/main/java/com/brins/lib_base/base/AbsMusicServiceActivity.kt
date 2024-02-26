package com.brins.lib_base.base

import com.brins.lib_base.controller.MusicPlayerController
import com.brins.lib_base.interfaces.IMusicServiceEventListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
abstract class AbsMusicServiceActivity: BaseActivity(), IMusicServiceEventListener {

    private val mMusicServiceEventListeners = ArrayList<IMusicServiceEventListener>()
    private var serviceToken: MusicPlayerController.ServiceToken? = null



}