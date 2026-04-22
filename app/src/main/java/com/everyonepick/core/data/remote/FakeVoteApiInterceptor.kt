package com.everyonepick.core.data.remote

import java.io.IOException
import javax.inject.Inject
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import okio.Buffer

class FakeVoteApiInterceptor @Inject constructor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val path = request.url.encodedPath

        val responseBody = when {
            request.method == "GET" && path == "/polls/current" -> CURRENT_POLLS_RESPONSE
            request.method == "POST" && path.startsWith("/polls/") && path.endsWith("/vote") -> {
                val pollId = path.substringAfter("/polls/").substringBefore("/vote").trim('/')
                val body = request.body?.readUtf8().orEmpty()
                val optionId = OPTION_ID_REGEX.find(body)?.groupValues?.get(1)
                    ?: throw IOException("Missing optionId in fake vote request")
                """
                {
                  "pollId": $pollId,
                  "optionId": $optionId,
                  "accepted": true
                }
                """.trimIndent()
            }

            else -> null
        }

        return if (responseBody != null) {
            Response.Builder()
                .code(200)
                .message("OK")
                .protocol(Protocol.HTTP_1_1)
                .request(request)
                .body(responseBody.toResponseBody(JSON_MEDIA_TYPE))
                .build()
        } else {
            chain.proceed(request)
        }
    }

    private fun okhttp3.RequestBody.readUtf8(): String {
        val buffer = Buffer()
        writeTo(buffer)
        return buffer.readUtf8()
    }

    private companion object {
        val JSON_MEDIA_TYPE = "application/json".toMediaType()
        val OPTION_ID_REGEX = "\"optionId\"\\s*:\\s*(\\d+)".toRegex()

        val CURRENT_POLLS_RESPONSE = """
            [
              {
                "id": 1,
                "title": "What should we pick for tonight?",
                "description": "The app opens directly into the vote domain for now, but the entry feature can evolve later.",
                "closesAtEpochMillis": 1767222000000,
                "options": [
                  { "id": 101, "title": "Korean BBQ", "voteCount": 14, "displayOrder": 0 },
                  { "id": 102, "title": "Sushi", "voteCount": 18, "displayOrder": 1 },
                  { "id": 103, "title": "Mala Hotpot", "voteCount": 11, "displayOrder": 2 },
                  { "id": 104, "title": "Pasta", "voteCount": 9, "displayOrder": 3 }
                ]
              }
            ]
        """.trimIndent()
    }
}

