package com.brins.lib_base.config

/*enum class ChatModel(val modelName: String, val simpleName: String, isReasoner: Boolean = false) {
    ChatGPT_4_1106_PREVIEW(MODEL_4_1106_PREVIEW, MODEL_NAME_4),
    ChatGPT_4_VISION_PREVIEW(MODEL_4_VISION_PREVIEW, MODEL_NAME_4),
    ChatGPT_3_5_TURBO_1106(MODEL_3_5_TURBO_1106, MODEL_NAME_3_5),
    ChatGPT_3_5_TURBO(MODEL_3_5_TURBO, MODEL_NAME_3_5),
    DALL_E_3(MODEL_DALL_E_3, MODEL_DALL_E),
    DALL_E_2(MODEL_DALL_E_2, MODEL_DALL_E),

    DEEP_SEEK_R1(MODEL_DEEP_SEEK_R1, MODEL_DEEP_SEEK_R1_SIMPLE),
    DEEP_SEEK_V3(MODEL_DEEP_SEEK_V3, MODEL_DEEP_SEEK_V3_SIMPLE);
}*/
sealed class ChatModel(
    open val modelName: String,
    open val simpleName: String,
    open val isReasoner: Boolean = false
) {
    // ChatGPT 系列模型
    data object ChatGPT_O_3_MINI : ChatModel(
        modelName = MODEL_O3_MINI,
        simpleName = MODEL_NAME_O3_MINI,
        isReasoner = true
    )

    data object ChatGPT_O_1 : ChatModel(
        modelName = MODEL_O1,
        simpleName = MODEL_NAME_O1,
        isReasoner = true
    )

    data object ChatGPT_4_O_MIN : ChatModel(
        modelName = MODEL_4O_MINI,
        simpleName = MODEL_NAME_4O_MINI
    )

    data object ChatGPT_4_O : ChatModel(
        modelName = MODEL_4O,
        simpleName = MODEL_NAME_4O
    )

    data object ChatGPT_4_1106_PREVIEW : ChatModel(
        modelName = MODEL_4_1106_PREVIEW,
        simpleName = MODEL_NAME_4
    )

    data object ChatGPT_4_VISION_PREVIEW : ChatModel(
        modelName = MODEL_4_VISION_PREVIEW,
        simpleName = MODEL_NAME_4
    )

    data object ChatGPT_3_5_TURBO_1106 : ChatModel(
        modelName = MODEL_3_5_TURBO_1106,
        simpleName = MODEL_NAME_3_5
    )

    data object ChatGPT_3_5_TURBO : ChatModel(
        modelName = MODEL_3_5_TURBO,
        simpleName = MODEL_NAME_3_5
    )

    // DALL-E 系列模型
    data object DALL_E_3 : ChatModel(
        modelName = MODEL_DALL_E_3,
        simpleName = MODEL_DALL_E
    )

    data object DALL_E_2 : ChatModel(
        modelName = MODEL_DALL_E_2,
        simpleName = MODEL_DALL_E
    )

    // DeepSeek 系列模型
    data object DEEP_SEEK_R1 : ChatModel(
        modelName = MODEL_DEEP_SEEK_R1,
        simpleName = MODEL_DEEP_SEEK_R1_SIMPLE,
        isReasoner = true
    )

    data object DEEP_SEEK_V3 : ChatModel(
        modelName = MODEL_DEEP_SEEK_V3,
        simpleName = MODEL_DEEP_SEEK_V3_SIMPLE,
    )

    companion object {
        /**
         * 模型相关
         */
        // Up to Sep 2021
        const val MODEL_3_5_TURBO = "gpt-3.5-turbo"
        const val MODEL_3_5_TURBO_1106 = "gpt-3.5-turbo-1106"
        const val MODEL_NAME_3_5 = "ChatGPT 3.5"

        // Up to Apr 2023
        // vision-preview
        const val MODEL_4_VISION_PREVIEW = "gpt-4-vision-preview"
        const val MODEL_4_1106_PREVIEW = "gpt-4-1106-preview"
        const val MODEL_NAME_4 = "ChatGPT 4"

        // Up to Oct 2023
        const val MODEL_4O = "gpt-4o"
        const val MODEL_NAME_4O = "ChatGPT 4o"
        const val MODEL_4O_MINI = "gpt-4o-mini"
        const val MODEL_NAME_4O_MINI = "ChatGPT 4o mini"

        // 推理模型
        const val MODEL_O1 = "o1"
        const val MODEL_NAME_O1 = "o1"

        const val MODEL_O3_MINI = "o3-mini"
        const val MODEL_NAME_O3_MINI = "o3 mini"


        // dall-e-3 生成图片
        const val MODEL_DALL_E_3 = "dall-e-3"
        // dall-e-2 生成图片
        const val MODEL_DALL_E_2 = "dall-e-2"
        const val MODEL_DALL_E = "DALL•E"

        /**
         * DeepSeek 模型
         */
        const val MODEL_DEEP_SEEK_V3 = "deepseek-chat"
        const val MODEL_DEEP_SEEK_V3_SIMPLE = "DeepSeek-V3"

        const val MODEL_DEEP_SEEK_R1 = "deepseek-reasoner"
        const val MODEL_DEEP_SEEK_R1_SIMPLE = "DeepSeek-R1"
    }
}