package com.brins.lib_base.config

enum class ChatModel(val modelName: String, val simpleName: String) {
    ChatGPT_4_1106_PREVIEW(MODEL_4_1106_PREVIEW, MODEL_4),
    ChatGPT_4_VISION_PREVIEW(
        MODEL_4_VISION_PREVIEW, MODEL_4
    ),
    ChatGPT_3_5_TURBO_1106(MODEL_3_5_TURBO_1106, MODEL_3_5),
    ChatGPT_3_5_TURBO(
        MODEL_3_5_TURBO,
        MODEL_3_5
    ),
    DALL_E_3(MODEL_DALL_E_3, MODEL_DALL_E),
    DALL_E_2(MODEL_DALL_E_2, MODEL_DALL_E)
}