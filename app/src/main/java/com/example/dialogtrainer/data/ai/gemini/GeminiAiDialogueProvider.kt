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

    // ---------------------------------------------------------
    // 1) FIRST LINE
    // ---------------------------------------------------------
    override suspend fun generateFirstLine(
        sceneId: String,
        nativeLanguageCode: String,
        learningLanguageCode: String
    ): DialogueLine = withContext(Dispatchers.IO) {

        val prompt = """
You are a dialogue partner in a language‑learning app.

TARGET LANGUAGE: $learningLanguageCode
NATIVE LANGUAGE: $nativeLanguageCode

Your task:
1. Speak ONLY in the target language.
2. ALWAYS provide a translation into the native language.
3. Format EXACTLY like this:

<target_language_sentence>
<native_language_translation>

Scene: $sceneId

Start the conversation naturally.
Respond ONLY with the two lines.
""".trimIndent()

        val text = callGemini(prompt).trim()

        DialogueLine(
            speaker = Speaker.AGENT,
            text = text
        )
    }

    // ---------------------------------------------------------
    // 2) EVALUATE
    // ---------------------------------------------------------
    override suspend fun evaluateUserAnswer(
        sceneId: String,
        previousLine: DialogueLine,
        userAnswer: String,
        learningLanguageCode: String
    ): Feedback = withContext(Dispatchers.IO) {

        val prompt = """
You are a strict JSON generator.

RULES:
- Return ONLY valid JSON.
- No markdown.
- No code fences.
- No explanations.
- No comments.
- No translation.
- No text before or after JSON.
- JSON MUST start with '{' and end with '}'.

TASK:
Evaluate the user's answer in the target language ($learningLanguageCode).

Return EXACTLY this JSON:

{
  "score": number,
  "corrected": "string",
  "comment": "string"
}

DATA:
Scene: $sceneId

Previous agent line:
"${previousLine.text}"

User answer:
"$userAnswer"
""".trimIndent()

        val raw = callGemini(prompt).trim()
        val json = JSONObject(raw)

        Feedback(
            score = json.optInt("score", 0),
            corrected = json.optString("corrected", ""),
            comment = json.optString("comment", "")
        )
    }

    // ---------------------------------------------------------
    // 3) NEXT LINE
    // ---------------------------------------------------------
    override suspend fun generateNextLine(
        sceneId: String,
        previousLine: DialogueLine,
        userAnswer: String,
        learningLanguageCode: String,
        nativeLanguageCode: String
    ): DialogueLine? = withContext(Dispatchers.IO) {

        val prompt = """
You are a dialogue partner in a language‑learning app.

TARGET LANGUAGE: $learningLanguageCode
NATIVE LANGUAGE: $nativeLanguageCode

Your task:
1. Continue the conversation in the target language.
2. ALWAYS provide a translation into the native language.
3. Format EXACTLY like this:

<target_language_sentence>
<native_language_translation>

If the conversation is over, respond EXACTLY with:
[END]

Scene: $sceneId

Previous agent line:
"${previousLine.text}"

User answer:
"$userAnswer"

Respond ONLY with the two lines or [END].
No markdown. No explanations. No extra text.
""".trimIndent()

        val text = callGemini(prompt).trim()

        if (text == "[END]") null
        else DialogueLine(Speaker.AGENT, text)
    }

    // ---------------------------------------------------------
    // LOW-LEVEL HTTP CALL
    // ---------------------------------------------------------
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

    // ---------------------------------------------------------
    // LIST MODELS
    // ---------------------------------------------------------
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
