package com.catscoffeeandkitchen.features.namegenerator

import com.aallam.openai.api.chat.ChatCompletionRequest
import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.chat.ChatRole
import com.aallam.openai.api.chat.Tool
import com.aallam.openai.api.chat.ToolCall
import com.aallam.openai.api.core.Parameters
import com.aallam.openai.api.exception.OpenAIException
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.add
import kotlinx.serialization.json.put
import kotlinx.serialization.json.putJsonArray
import kotlinx.serialization.json.putJsonObject
import java.io.File

class NameGeneratorRepository(
    private val openAIClient: OpenAI?
) {
    private suspend fun getAIGeneratedName(type: NameCategory, context: String?, amount: Int): List<String>? {
        if (openAIClient == null) return null

        val typeDescription = when (type) {
            NameCategory.Fantasy -> "for a fantasy setting"
            NameCategory.Pun -> "using a pun"
            NameCategory.Realistic -> "for a typical american"
            NameCategory.PopCulture -> "that is a play on words using references from pop culture"
        }

        val completions = openAIClient.chatCompletion(
            request = ChatCompletionRequest(
                model = ModelId("gpt-3.5-turbo"),
                messages = listOf(
                    ChatMessage(
                        role = ChatRole.User,
                        content = "Generate a random username for a ${context.orEmpty()} character " +
                                "in a game $typeDescription."
                    )
                ),
                tools = listOf(
                    Tool.function(
                        name = "RandomName",
                        description = "Generate a random name.",
                        parameters = Parameters.buildJsonObject {
                            put("type", "object")
                            putJsonObject("properties") {
                                putJsonObject("names") {
                                    put("type", "array")
                                    put("description", "Random usernames for a game character.")
                                    put("minItems", amount)
                                    put("maxItems", amount + EXTRA_GENERATION)
                                    putJsonObject("items") {
                                        put("type", "string")
                                    }
                                }
                            }
                            putJsonArray("required") {
                                add("name")
                            }
                        }
                    )
                )
            )
        )

        val toolCall = completions.choices.firstOrNull()?.message?.toolCalls?.firstOrNull()
        val result = toolCall?.let { (it as ToolCall.Function).function.argumentsOrNull }

        return result?.let { jsonString ->
            Json.decodeFromString<RandomNameResult>(jsonString).names.take(amount)
        }
    }

    private fun getRandomNameFromAssets(
        type: NameCategory,
        context: String?,
        amount: Int
    ): List<String> {
        return when (type) {
            NameCategory.Fantasy -> {
                val prefixInputStream = File("assets/fantasy_names_1.txt").inputStream()
                val suffixInputStream = File("assets/fantasy_names_2.txt").inputStream()

                val prefixes = prefixInputStream.bufferedReader().readLines()
                val suffixes = suffixInputStream.bufferedReader().readLines()

                (0..amount).toList().map { _ ->
                    prefixes.random().replaceFirstChar { it.uppercase() } + suffixes.random()
                }
            }
            NameCategory.Realistic -> {
                val inputStream = if (context?.lowercase() in listOf("woman", "female", "girl")) {
                    File("assets/realistic_names_female.txt").inputStream()
                } else {
                    File("assets/realistic_names_male.txt").inputStream()
                }

                inputStream.bufferedReader().readLines().shuffled().take(amount)
            }
            else -> {
                emptyList()
            }
        }
    }

    @Suppress("SwallowedException", "TooGenericExceptionThrown")
    suspend fun generateName(category: NameCategory, context: String?, amount: Int): List<String> {
        return try {
            getAIGeneratedName(category, context, amount) ?: throw Exception("Names were null.")
        } catch (error: OpenAIException) {
            getRandomNameFromAssets(category, context, amount)
        }
    }

    companion object {
        const val EXTRA_GENERATION = 5
    }
}
