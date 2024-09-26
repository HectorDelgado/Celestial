package com.hectordelgado.celestial
import java.io.File
import java.util.Properties

/**
 * A handy class to load secrets from a properties file.
 */
class SecretsExtractor {
    fun loadSecrets(filePath: String): Properties {
        val file = File(filePath)

        return if (!file.exists()) {
            throw Exception("File not found at path: $filePath")
        } else {
            Properties().apply {
                load(file.inputStream())
            }
        }
    }
}