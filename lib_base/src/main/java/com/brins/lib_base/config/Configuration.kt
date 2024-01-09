package com.brins.lib_base.config

import android.text.SpannableString
import android.text.SpannableStringBuilder
import com.brins.lib_base.BuildConfig
import com.brins.lib_base.BuildConfig.CHAT_API_KEY
import com.brins.lib_base.extensions.bold
import com.brins.lib_base.extensions.getSenderDisplayNames
import com.brins.lib_base.extensions.isFromChatGPT
import com.brins.lib_base.model.user.ChatUser
import io.getstream.chat.android.client.utils.message.isSystem
import io.getstream.chat.android.models.Channel
import io.getstream.chat.android.models.Message
import io.getstream.chat.android.models.User
import io.getstream.chat.android.ui.ChatUI
import io.getstream.chat.android.ui.common.utils.extensions.isDirectMessaging
import io.getstream.chat.android.ui.helper.MessagePreviewFormatter



/**
 * API-KEY
 */
const val API_KEY = BuildConfig.API_KEY

const val CHANNEL_NAME_PREFIX = "GPT-"

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




