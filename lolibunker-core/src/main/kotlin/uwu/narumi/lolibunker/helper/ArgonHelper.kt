package uwu.narumi.lolibunker.helper

import de.mkammerer.argon2.Argon2
import de.mkammerer.argon2.Argon2Factory

object ArgonHelper {

    private val argon: Argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id)
    val fakePassword = encrypt(charArrayOf('ロ', 'リ'))

    fun encrypt(password: CharArray): String? {
        try {
            return argon.hash(10, 65536, 1, password)
        } finally {
            argon.wipeArray(password)
        }
    }

    fun match(encryptedPassword: String, password: CharArray): Boolean {
        try {
            return argon.verify(encryptedPassword, password)
        } finally {
            argon.wipeArray(password)
        }
    }
}