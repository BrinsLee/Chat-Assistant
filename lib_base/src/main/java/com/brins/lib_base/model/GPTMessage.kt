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

package com.brins.lib_base.model

import android.os.Parcelable
import com.brins.lib_base.config.ROLE_USER
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class GPTMessage(
//  @field:Json(name = "mid") val mid: String,
    @field:Json(name = "role") val role: String = ROLE_USER,
    @field:Json(name = "content") val content: String = "",
    @field:Json(name = "reasoning_content") val reasoningContent: String?

): Parcelable
