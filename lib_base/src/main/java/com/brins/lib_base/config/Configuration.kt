package com.brins.lib_base.config

import com.brins.lib_base.BuildConfig
import io.getstream.chat.android.models.User


/**
 * API-KEY
 */
const val API_KEY = BuildConfig.API_KEY

const val MESSAGE_CHANNEL_NAME_PREFIX = "GPT-"

const val IMAGE_CHANNEL_NAME_PREFIX = "DALL•E"

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
const val MODEL_3_5 = "ChatGPT 3.5"
// Up to Apr 2023
const val MODEL_4_1106_PREVIEW = "gpt-4-1106-preview"
const val MODEL_4 = "ChatGPT 4"
// vision-preview
const val MODEL_4_VISION_PREVIEW = "gpt-4-vision-preview"
// dall-e-3 生成图片
const val MODEL_DALL_E_3 = "dall-e-3"
const val MODEL_DALL_E = "DALL•E"
// dall-e-2 生成图片
const val MODEL_DALL_E_2 = "dall-e-2"

/**
 * 语音选项
 *
 */
const val VOICE_ALLOY = "alloy"

const val VOICE_ECHO = "echo"

const val VOICE_FABLE = "fable"

const val VOICE_ONYX = "onyx"

const val VOICE_NOVA = "nova"

const val VOICE_SHIMMER = "shimmer"

/**
 * 语音模型
 */
const val VOICE_MODEL_TTS_1 = "tts-1"

const val VOICE_MODEL_TTS_1_HD = "tts-1-hd"

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

/**
 * setting
 */
const val LANGUAGE_NAME = "language_name"
const val GENERAL_THEME = "general_theme"
const val MANAGE_AUDIO_FOCUS = "manage_audio_focus"
const val PLAYBACK_SPEED = "playback_speed"
const val PLAYBACK_PITCH = "playback_pitch"



