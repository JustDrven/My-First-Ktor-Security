package dev.justdrven.managers

import java.util.Base64

class HashManager {
    companion object {
        fun decode(source: String): String {
            return String(Base64.getDecoder().decode(source.toByteArray()))
        }

        fun encode(source: String): String {
            return Base64.getEncoder().encodeToString(source.toByteArray())
        }
    }
}