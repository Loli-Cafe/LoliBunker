package uwu.narumi.lolibunker.cipher

import org.bouncycastle.jce.provider.BouncyCastleProvider
import uwu.narumi.lolibunker.helper.CryptoHelper
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.IvParameterSpec

class LoliCipher(cipher: String, info: CipherInfo?) {

    val javaCipher: Cipher
    val iv: ByteArray = ByteArray(info?.ivSize ?: 16)
    val isGCM: Boolean

    init {
        javaCipher = Cipher.getInstance(cipher, BouncyCastleProvider())
        isGCM = info?.isGCM ?: false

        val randomSecureRandom: SecureRandom = SecureRandom.getInstance("SHA1PRNG")
        randomSecureRandom.nextBytes(iv)
    }

    fun init(mode: Int, password: CharArray, salt: ByteArray, iv: ByteArray): LoliCipher {
        javaCipher.init(
            mode,
            CryptoHelper.createSecretKey(password, salt),
            if (isGCM) GCMParameterSpec(96, iv) else IvParameterSpec(iv)
        )

        return this
    }

    fun init(mode: Int, password: CharArray, salt: ByteArray): LoliCipher {
        javaCipher.init(
            mode,
            CryptoHelper.createSecretKey(password, salt),
            if (isGCM) GCMParameterSpec(96, iv) else IvParameterSpec(iv)
        )
        return this
    }

    fun encryptData(bytes: ByteArray): ByteArray {
        return javaCipher.doFinal(bytes)
    }

    fun decryptData(bytes: ByteArray): ByteArray {
        return javaCipher.doFinal(bytes)
    }

    fun update(buffer: ByteArray, offset: Int, size: Int): ByteArray {
        return javaCipher.update(buffer, offset, size)
    }

    fun doFinal(): ByteArray {
        return javaCipher.doFinal()
    }
}