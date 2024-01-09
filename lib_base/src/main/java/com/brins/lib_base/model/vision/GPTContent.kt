/*
 * Designed and developed by 2022 skydoves (Jaewoong Eum)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.brins.lib_base.model.vision

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

const val CONTENT_TYPE_TEXT = "text"
const val CONTENT_TYPE_IMAGE = "image_url"
const val DETAIL_LEVEL_AUTO = "auto"
const val DETAIL_LEVEL_HIGH = "high"
const val DETAIL_LEVEL_LOW = "low"
//@JsonClass(generateAdapter = true)
data class GPTContent(
    @field:Json(name = "type")
    val contentType: String = CONTENT_TYPE_TEXT,
    @field:Json(name = "text")
    val text: String = "",
    @field:Json(name = "image_url")
    val imageUrl: GPTImageUrl? = null
)

@JsonClass(generateAdapter = true)
data class GPTImageUrl(
    @field:Json(name = "url")
    val url: String,
    @field:Json(name = "detail")
    val detail: String
)
