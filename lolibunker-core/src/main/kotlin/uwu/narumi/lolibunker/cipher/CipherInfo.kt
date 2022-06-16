package uwu.narumi.lolibunker.cipher

data class CipherInfo(
    val fastSupport: Boolean,
    val ivSize: Int,
    val isGCM: Boolean
)
