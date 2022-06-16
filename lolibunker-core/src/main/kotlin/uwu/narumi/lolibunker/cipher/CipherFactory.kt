package uwu.narumi.lolibunker.cipher

object CipherFactory {

    const val defaultCipher = "AES/GCM/NoPadding"
    const val defaultCipherFast = "Shit (You should not use it)"

    val defaultCiphers = hashMapOf(
        Pair("RC6", CipherInfo(false, 16, false)),
        Pair("RC6/CTR/NoPadding", CipherInfo(false, 16, false)), //true
        Pair("RC6/GCM/NoPadding", CipherInfo(false, 12, true)),
        Pair("Shacal2", CipherInfo(false, 16, false)),
        Pair("Camellia", CipherInfo(false, 16, false)),
        Pair("Camellia/CTR/NoPadding", CipherInfo(false, 16, false)), //true
        Pair("Camellia/GCM/NoPadding", CipherInfo(false, 12, true)),
        Pair("Aria", CipherInfo(false, 16, false)),
        Pair("AriaGCM", CipherInfo(false, 16, false)),
        Pair("SkipJack", CipherInfo(false, 24, false)),
        Pair("Serpent", CipherInfo(false, 16, false)),
        Pair("Serpent/CTR/NoPadding", CipherInfo(false, 16, false)), //true
        Pair("Serpent/GCM/NoPadding", CipherInfo(false, 12, true)),
        Pair("AES", CipherInfo(false, 16, false)),
        Pair("AES/CTR/NoPadding", CipherInfo(false, 16, false)), //true
        Pair("AES/GCM/NoPadding", CipherInfo(false, 12, true)),
        Pair("ZUC-256", CipherInfo(false, 16, false)), //true
        Pair("Grain128", CipherInfo(false, 16, false)), //true
        Pair("HC256", CipherInfo(false, 16, false)), //true
        Pair("CAST6", CipherInfo(false, 16, false)),
        Pair("CAST6/CTR/NoPadding", CipherInfo(false, 16, false)), //true
        Pair("CAST6/GCM/NoPadding", CipherInfo(false, 12, true))
    )

    fun create(cipher: String): LoliCipher {
        val matchedCipher = match(cipher)
        return LoliCipher(matchedCipher, defaultCiphers[matchedCipher])
    }

    private fun match(cipher: String): String {
        return defaultCiphers.map { it.key }.firstOrNull { it.equals(cipher, true) } ?: defaultCipher
    }

    fun has(cipher: String): Boolean {
        return defaultCiphers.map { it.key }.any { it.equals(cipher, true) }
    }

    fun fastSupport(cipher: String): Boolean {
        return defaultCiphers.filter { it.key.equals(cipher, true) }.map { it.value.fastSupport }.firstOrNull() ?: false
    }
}