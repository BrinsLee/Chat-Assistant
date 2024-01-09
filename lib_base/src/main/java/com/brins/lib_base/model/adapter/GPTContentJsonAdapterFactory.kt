package com.brins.lib_base.model.adapter

import com.brins.lib_base.model.vision.GPTContent
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import java.lang.reflect.Type

class GPTContentJsonAdapterFactory: JsonAdapter.Factory {
    override fun create(type: Type, annotation: MutableSet<out Annotation>, moshi: Moshi): JsonAdapter<*>? {
        if (Types.getRawType(type) == GPTContent::class.java) {
            return GPTContentJsonAdapter(moshi)
        }
        return null
    }

}