package com.brins.lib_base.model.adapter

import com.brins.lib_base.model.vision.CONTENT_TYPE_TEXT
import com.brins.lib_base.model.vision.DETAIL_LEVEL_AUTO
import com.brins.lib_base.model.vision.GPTContent
import com.brins.lib_base.model.vision.GPTImageUrl
import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.Moshi
import com.squareup.moshi.ToJson
import javax.inject.Inject

class GPTContentJsonAdapter @Inject constructor(private val moshi: Moshi): JsonAdapter<GPTContent>() {

    private val options: JsonReader.Options = JsonReader.Options.of("type", "text", "image_url")
    private val imageUrlAdapter = moshi.adapter(GPTImageUrl::class.java)
    @ToJson
    override fun toJson(writer: JsonWriter, value: GPTContent?) {
        if (value == null) {
            throw NullPointerException("GPTContent was null! Wrap in .nullSafe() to write nullable values.")
        }

        writer.beginObject()
        writer.name("type").value(value.contentType)
        if (value.text.isNotEmpty()) {
            writer.name("text").value(value.text)
        }

        if (value.imageUrl != null && value.imageUrl.url.isNotEmpty()) {
            writer.name("image_url")
            imageUrlAdapter.toJson(writer, value.imageUrl)
        }

        writer.endObject()
    }

    @FromJson
    override fun fromJson(reader: JsonReader): GPTContent {
        var contentType = CONTENT_TYPE_TEXT
        var text = ""
        var imageUrl: GPTImageUrl? = null

        reader.beginObject()
        while (reader.hasNext()) {
            when (reader.selectName(options)) {
                0 -> contentType = reader.nextString()
                1 -> text = reader.nextString()
                2 -> imageUrl = imageUrlAdapter.fromJson(reader)
                else -> {
                    reader.skipName()
                    reader.skipValue()
                }
            }
        }
        reader.endObject()

        return GPTContent(contentType, text, imageUrl)
    }


    /*    @ToJson
        fun toJson(gptContent: GPTContent): Map<String, Any?> {
            val result = mutableMapOf<String, Any?>()
            result["type"] = gptContent.contentType
            if (gptContent.text.isNotEmpty()) {
                result["text"] = gptContent.text
            }
            if (gptContent.imageUrl != null) {
                result["image_url"] = gptContent.imageUrl
            }
            if (gptContent.detail.isNotEmpty()) {
                result["detail"] = gptContent.detail
            }
            return result
        }

        @FromJson
        fun fromJson(json: Map<String, Any?>): GPTContent {
            val contentType: String = json["type"] as String? ?: CONTENT_TYPE_TEXT
            val text: String = json["text"] as String? ?: ""
            val imageUrl: String = json["image_url"] as String? ?: ""
            val detail: String = json["detail"] as String? ?: DETAIL_LEVEL_AUTO
            return GPTContent(contentType, text, imageUrl, detail)
        }*/
}