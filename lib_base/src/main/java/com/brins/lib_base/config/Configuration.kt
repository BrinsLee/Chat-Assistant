package com.brins.lib_base.config

import com.brins.lib_base.BuildConfig
import com.brins.lib_base.BuildConfig.CHAT_API_KEY
import com.brins.lib_base.model.user.ChatUser


/**
 * API-KEY
 */
const val API_KEY = BuildConfig.API_KEY

/**
 * 模型相关
 */
const val MODEL_3_5 = "gpt-3.5-turbo"

const val MODEL_DAVINCI = "text-davinci-002-render"


/**
 * 请求头参数
 */
const val Authorization = "Authorization"


/**
 * 默认头像
 */
const val DEFAULT_AVATAR = "https://getstream.io/random_png?id=none&name=none&size=200"

/**
 * 测试用户
 */
val USER9014: ChatUser = ChatUser(CHAT_API_KEY,
    "40b5202b-4a05-4351-93db-5dabc7487205",
    "User 9014",
        token = "",
        DEFAULT_AVATAR
    )


/**
 * ==================mmkv===================
 *
 */
const val EXTRA_KEY_USER_DATA: String = "extra_key_user_data" //用户数据

