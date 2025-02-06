package com.brins.lib_base.base

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
open class AbsMusicServiceFragment(@LayoutRes layout: Int): BaseFragment(layout) {

    var serviceActivity: AbsMusicServiceActivity? = null
        private set

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            serviceActivity = context as AbsMusicServiceActivity?
        } catch (e: ClassCastException) {
            throw RuntimeException(context.javaClass.simpleName + " must be an instance of " + AbsMusicServiceActivity::class.java.simpleName)
        }
        Log.d("AbsMusicServiceFragment","onAttach $activity this $this")
    }

    override fun onDetach() {
        super.onDetach()
        serviceActivity = null
        Log.d("AbsMusicServiceFragment","onDetach $activity this $this")
    }

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    @CallSuper
    override fun onDestroyView() {
        super.onDestroyView()
    }

}