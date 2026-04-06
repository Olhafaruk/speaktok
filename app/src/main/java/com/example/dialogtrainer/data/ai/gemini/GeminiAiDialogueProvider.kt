//GeminiAiDialogueProvider.kt

package com.example.dialogtrainer.data.ai.gemini

import com.example.dialogtrainer.data.ai.AiDialogueProvider
import com.example.dialogtrainer.data.model.dialogue.DialogueLine
import com.example.dialogtrainer.data.model.dialogue.Feedback
import com.example.dialogtrainer.data.model.dialogue.Speaker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class GeminiAiDialogueProvider(
    private val apiKey: String
) : AiDialogueProvider {

    private val endpoint =
        "https://generativelanguage.googleapis.com/v1/models/gemini-2.5-flash:generateContent"


    override suspend fun generateFirstLine(
        sceneId: String,
        nativeLanguageCode: String,
        learningLanguageCode: String
    ): DialogueLine = withContext(Dispatchers.IO) {

        val prompt = """
            You are an English dialogue partner for a language learning app.
            Scene: $sceneId
            User's native language: $nativeLanguageCode
            Target language: $learningLanguageCode

            Start the conversation naturally.
            Respond ONLY as the agent.
        """.trimIndent()

        val text = callGemini(prompt)

        DialogueLine(
            speaker = Speaker.AGENT,
            text = text
        )
    }

    override suspend fun evaluateUserAnswer(
        sceneId: String,
        previousLine: DialogueLine,
        userAnswer: String,
        learningLanguageCode: String
    ): Feedback = withContext(Dispatchers.IO) {

        val prompt = """
            You are a language tutor.
            Scene: $sceneId
            Target language: $learningLanguageCode

            Previous agent line:
            "${previousLine.text}"

            User answer:
            "$userAnswer"

            Evaluate the answer.
            Return JSON:
            {
              "score": number,
              "corrected": "string",
              "comment": "string"
            }
        """.trimIndent()

        val raw = callGemini(prompt)
        val json = JSONObject(raw)

        Feedback(
            score = json.optInt("score", 0),
            corrected = json.optString("corrected", ""),
            comment = json.optString("comment", "")
        )
    }

    override suspend fun generateNextLine(
        sceneId: String,
        previousLine: DialogueLine,
        userAnswer: String,
        learningLanguageCode: String
    ): DialogueLine? = withContext(Dispatchers.IO) {

        val prompt = """
            Continue the conversation.
            Scene: $sceneId
            Target language: $learningLanguageCode

            Previous agent line:
            "${previousLine.text}"

            User answer:
            "$userAnswer"

            If conversation is over, respond exactly: [END]
            Otherwise respond with the next agent line only.
        """.trimIndent()

        val text = callGemini(prompt).trim()

        if (text == "[END]") null
        else DialogueLine(Speaker.AGENT, text)
    }

    private fun callGemini(prompt: String): String {
        val url = URL("$endpoint?key=$apiKey")
        val connection = (url.openConnection() as HttpURLConnection).apply {
            requestMethod = "POST"
            doOutput = true
            setRequestProperty("Content-Type", "application/json; charset=UTF-8")
        }

        val body = JSONObject().apply {
            put("contents", JSONArray().apply {
                put(
                    JSONObject().apply {
                        put("parts", JSONArray().apply {
                            put(JSONObject().apply { put("text", prompt) })
                        })
                    }
                )
            })
        }

        connection.outputStream.use { it.write(body.toString().toByteArray()) }

        val code = connection.responseCode
        val response =
            if (code == 200)
                connection.inputStream.bufferedReader().use { it.readText() }
            else
                connection.errorStream?.bufferedReader()?.use { it.readText() }
                    ?: "Unknown error"

        if (code != 200) throw Exception("Gemini API error ($code): $response")

        val root = JSONObject(response)
        val candidates = root.getJSONArray("candidates")
        val first = candidates.getJSONObject(0)
        val content = first.getJSONObject("content")
        val parts = content.getJSONArray("parts")
        return parts.getJSONObject(0).getString("text")
    }


    override suspend fun listModels(): String = withContext(Dispatchers.IO) {
        val url = URL("https://generativelanguage.googleapis.com/v1/models?key=$apiKey")
        val connection = (url.openConnection() as HttpURLConnection).apply {
            requestMethod = "GET"
            setRequestProperty("Content-Type", "application/json")
        }

        val code = connection.responseCode
        val response =
            if (code == 200)
                connection.inputStream.bufferedReader().use { it.readText() }
            else
                connection.errorStream?.bufferedReader()?.use { it.readText() }
                    ?: "Unknown error"

        response
    }

}
