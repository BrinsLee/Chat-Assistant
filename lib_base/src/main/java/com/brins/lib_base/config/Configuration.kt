package com.brins.lib_base.config

import com.brins.lib_base.BuildConfig
import io.getstream.chat.android.models.User


/**
 * API-KEY
 */
const val API_KEY = BuildConfig.API_KEY

const val MESSAGE_CHANNEL_NAME_PREFIX = "GPT-"

const val IMAGE_CHANNEL_NAME_PREFIX = "Dall-e"

const val GPT_MESSAGE_KEY = "ChatGpt"

/**
 * GPT Role
 */
const val ROLE_SYSTEM = "system"

const val ROLE_USER = "user"

const val ROLE_ASSISTANT = "assistant"

/**
 * 模型相关
 */
// Up to Sep 2021
const val MODEL_3_5_TURBO = "gpt-3.5-turbo"

const val MODEL_3_5_TURBO_1106 = "gpt-3.5-turbo-1106"

// Up to Apr 2023
const val MODEL_4_1106_PREVIEW = "gpt-4-1106-preview"
// vision-preview
const val MODEL_4_VISION_PREVIEW = "gpt-4-vision-preview"
// dall-e-3 生成图片
const val MODEL_DALL_E_3 = "dall-e-3"
// dall-e-2 生成图片
const val MODEL_DALL_E_2 = "dall-e-2"

/**
 * 请求头参数
 */
const val Authorization = "Authorization"


/**
 * 默认头像
 */
const val DEFAULT_AVATAR = "https://getstream.io/random_png?id=none&name=none&size=200"

/**
 * 测试用户 40b5202b-4a05-4351-93db-5dabc7487205 40b5202b-4a05-4351-93db-5dabc7487205
 */

val chatGPTUser = User(
    id = "40b5202b-4a05-4351-93db-5dabc7487205",
    role = "user",
    name = "User 9014",
    image = DEFAULT_AVATAR
)

/**
 * ==================mmkv===================
 *
 */
const val EXTRA_KEY_USER_DATA: String = "extra_key_user_data" //用户数据




