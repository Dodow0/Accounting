package com.dodo.accounting.ui.screen

import android.util.Base64
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

internal class WebDavBackupClient(
    private val config: WebDavConfig
) {
    suspend fun uploadJson(content: String) = withContext(Dispatchers.IO) {
        val bytes = content.toByteArray(Charsets.UTF_8)
        val connection = openConnection("PUT").apply {
            doOutput = true
            setRequestProperty("Content-Type", "application/json; charset=utf-8")
            setRequestProperty("Content-Length", bytes.size.toString())
        }
        connection.outputStream.use { it.write(bytes) }
        connection.requireSuccess("WebDAV 上传失败")
    }

    suspend fun downloadJson(): String = withContext(Dispatchers.IO) {
        val connection = openConnection("GET")
        connection.requireSuccess("WebDAV 下载失败")
        connection.inputStream.use { input ->
            input.bufferedReader(Charsets.UTF_8).readText()
        }
    }

    private fun openConnection(method: String): HttpURLConnection {
        require(config.url.trim().isNotBlank()) { "请先填写 WebDAV 地址" }
        val connection = URL(config.backupUrl()).openConnection() as HttpURLConnection
        connection.requestMethod = method
        connection.connectTimeout = 12_000
        connection.readTimeout = 20_000
        connection.setRequestProperty("Accept", "application/json")
        config.basicAuthHeader()?.let { connection.setRequestProperty("Authorization", it) }
        return connection
    }

    private fun HttpURLConnection.requireSuccess(prefix: String) {
        val code = responseCode
        if (code !in 200..299) {
            val detail = runCatching {
                errorStream?.use { input ->
                    input.bufferedReader(Charsets.UTF_8).readText().take(240)
                }
            }.getOrNull().orEmpty()
            disconnect()
            throw IOException(if (detail.isBlank()) "$prefix：HTTP $code" else "$prefix：HTTP $code $detail")
        }
    }
}

private fun WebDavConfig.backupUrl(): String {
    val trimmed = url.trim()
    return if (trimmed.endsWith(".json", ignoreCase = true)) {
        trimmed
    } else {
        trimmed.trimEnd('/') + "/accounting-backup.json"
    }
}

private fun WebDavConfig.basicAuthHeader(): String? {
    if (username.isBlank()) return null
    val token = "$username:$password"
    return "Basic " + Base64.encodeToString(token.toByteArray(Charsets.UTF_8), Base64.NO_WRAP)
}
