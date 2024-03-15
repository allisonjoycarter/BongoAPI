package com.catscoffeeandkitchen.features.advice

import com.aallam.openai.api.chat.*
import com.aallam.openai.api.core.Parameters
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.*

class AdviceRepository(
    private val openAIClient: OpenAI?
) {
    @Serializable
    private data class AdviceResult(
        val advice: String
    )

    suspend fun getRandomAdvice(fallback: String = "Play more PoE."): String {
        if (openAIClient == null) return fallback

        val completions = openAIClient.chatCompletion(
            request = ChatCompletionRequest(
                model = ModelId("gpt-3.5-turbo"),
                messages = listOf(
                    ChatMessage(
                        role = ChatRole.User,
                        content = "Give me meme advice."
                    )
                ),
                tools = listOf(
                    Tool.function(
                        name = "Advice",
                        description = "Give some semi-serious meme advice.",
                        parameters = Parameters.buildJsonObject {
                            put("type", "object")
                            putJsonObject("properties") {
                                putJsonObject("advice") {
                                    put("type", "string")
                                    put("description", "A short piece of meme advice.")
                                }
                            }
                            putJsonArray("required") {
                                add("advice")
                            }
                        }
                    )
                )
            )
        )

        val toolCall = completions.choices.firstOrNull()?.message?.toolCalls?.firstOrNull()
        val result = toolCall?.let { (it as ToolCall.Function).function.argumentsOrNull }

        return  result?.let { jsonString ->
            Json.decodeFromString<AdviceResult>(jsonString).advice
        } ?: fallback
    }
}