package uwu.narumi.lolibunker.helper

import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.security.SecureRandom
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec
import kotlin.experimental.and
import kotlin.experimental.inv
import kotlin.experimental.xor

object CryptoHelper {

    //cringe
    fun encryptDataFast(bytes: ByteArray, password: CharArray, salt: ByteArray, iv: ByteArray): ByteArray {
        for (i in bytes.indices) {
            bytes[i] = (bytes[i] + salt.size).toByte()
            bytes[i] xor (i or bytes.size and password.size).toByte()
            bytes[i] xor iv.hashCode().toByte() and salt.hashCode().toByte()
            bytes[i] xor password.hashCode().toByte()
            bytes[i] = (bytes[i] - iv.size).toByte()
            bytes[i] = bytes[i].inv()
        }

        return bytes
    }

    //cringe
    fun decryptDataFast(bytes: ByteArray, password: CharArray, salt: ByteArray, iv: ByteArray): ByteArray {
        for (i in bytes.indices) {
            bytes[i] = bytes[i].inv()
            bytes[i] = (bytes[i] + iv.size).toByte()
            bytes[i] xor password.hashCode().toByte()
            bytes[i] xor iv.hashCode().toByte() and salt.hashCode().toByte()
            bytes[i] xor (i or bytes.size and password.size).toByte()
            bytes[i] = (bytes[i] - salt.size).toByte()
        }

        return bytes
    }

    //cringe
    fun xor(bytes: ByteArray, key: String, xor: Int): ByteArray {
        for (i in bytes.indices) {
            bytes[i] xor if (i % 2 == 0) key.hashCode().toByte() else xor.toByte()
        }

        return bytes
    }

    fun createSecureByteArray(size: Int): ByteArray {
        val bytes = ByteArray(size)
        SecureRandom().nextBytes(bytes)
        return bytes
    }

    fun createSecretKey(password: CharArray, salt: ByteArray): SecretKey {
        val secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256", BouncyCastleProvider())
        val keySpec = PBEKeySpec(password, salt, 65536, 256)
        return SecretKeySpec(secretKeyFactory.generateSecret(keySpec).encoded, "AES")
    }
}