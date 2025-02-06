package com.brins.lib_base.model.audio

import android.os.Parcelable
import io.getstream.chat.android.models.Message
import kotlinx.parcelize.Parcelize
import java.io.File


open class Audio(
    val id: String,
    val message: Message?,
    val filePath: String,
    val file: File?,
    val duration: Long
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Audio
        if (id != other.id) return false
        if (message != other.message) return false
        if (duration != other.duration) return false
        return true
    }

    companion object {

        @JvmStatic
        val empty = Audio(
            id = "",
            message = null,
            filePath = "",
            file = null,
            duration = -1L,
        )
    }
}
