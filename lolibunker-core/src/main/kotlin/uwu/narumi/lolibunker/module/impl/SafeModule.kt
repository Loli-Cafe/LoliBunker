package uwu.narumi.lolibunker.module.impl

import uwu.narumi.lolibunker.LoliBunker
import uwu.narumi.lolibunker.cipher.CipherFactory
import uwu.narumi.lolibunker.header.FileHeader
import uwu.narumi.lolibunker.header.SIGNATURE
import uwu.narumi.lolibunker.helper.ArgonHelper
import uwu.narumi.lolibunker.helper.CryptoHelper
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.File
import java.io.RandomAccessFile
import java.util.*
import java.util.concurrent.ThreadLocalRandom
import javax.crypto.Cipher

class SafeModule : uwu.narumi.lolibunker.module.Module(
    "safe"
) {

    override fun encrypt(
        builder: LoliBunker.Builder,
        loliBunker: LoliBunker,
        file: File,
        randomAccessFile: RandomAccessFile
    ) {
        try {
            logger.info("Encrypting ${file.absolutePath}")
            randomAccessFile.close()

            val salt = CryptoHelper.createSecureByteArray(16)
            var cipher = CipherFactory.create(builder.cipher)
                .init(
                    Cipher.ENCRYPT_MODE,
                    builder.password.toCharArray(),
                    salt
                )

            val encryptedName = Base64.getEncoder().encodeToString(cipher.encryptData(file.name.toByteArray()))
            val outputFile = File(file.parent + File.separator + ThreadLocalRandom.current().nextInt())
            val removalFile = File(ThreadLocalRandom.current().nextInt().toString())
            val buffer = ByteArray(4 * 1024)
            var size: Int

            val header = FileHeader(
                builder.cipher,
                String(CryptoHelper.createSecureByteArray(ThreadLocalRandom.current().nextInt(8, 32))),
                /*ArgonHelper.encrypt(builder.password.toCharArray())!!*/
                ArgonHelper.fakePassword!!, //TODO: Idk make usage in future of it
                salt,
                cipher.iv,
                builder.hwidProtection,
                encryptedName
            ).create()

            cipher = CipherFactory.create(builder.cipher)
                .init(
                    Cipher.ENCRYPT_MODE,
                    builder.password.toCharArray(),
                    salt,
                    header.iv
                )

            DataInputStream(file.inputStream()).use { input ->
                DataOutputStream(outputFile.outputStream()).use { output ->
                    output.write(header.byteStream.toByteArray())

                    while (input.read(buffer, 0, buffer.size).let { size = it; it != -1 }) {
                        cipher.update(buffer, 0, size).let { output.write(it) }
                    }

                    cipher.doFinal().let {
                        output.write(it)
                        output.writeBytes(name)
                        output.writeInt(name.length)
                        output.writeBytes(SIGNATURE)
                    }
                }
            }

            file.outputStream().use { it.write(69) }
            file.renameTo(removalFile)
            removalFile.delete()

            logger.info("Encrypted ${file.absolutePath}")
        } catch (e: Exception) {
            logger.info("Failed to encrypt ${file.absolutePath}")
            logger.debug(e)
        } finally {
            complete(loliBunker)
        }
    }

    override fun decrypt(
        builder: LoliBunker.Builder,
        loliBunker: LoliBunker,
        file: File,
        randomAccessFile: RandomAccessFile
    ) {
        try {
            logger.info("Decrypting ${file.absolutePath}")
            randomAccessFile.close()

            val removalFile = File(ThreadLocalRandom.current().nextInt().toString())
            val buffer = ByteArray(4 * 1024)
            var size: Int

            DataInputStream(file.inputStream()).use { input ->
                val header = FileHeader().read(input)
                val cipher = CipherFactory.create(header.cipher)
                    .init(
                        Cipher.DECRYPT_MODE,
                        builder.password.toCharArray(),
                        header.salt,
                        header.iv
                    )
                val decryptedName = String(cipher.decryptData(Base64.getDecoder().decode(header.fileName)))
                DataOutputStream(File(file.parent + File.separator + decryptedName).outputStream()).use { output ->
                    while (input.read(buffer, 0, buffer.size).let { size = it; it != -1 }) {
                        cipher.update(buffer, 0, size).let { output.write(it) }
                    }

                    cipher.doFinal().let { output.write(it) }
                }
            }

            file.outputStream().use { it.write(69) }
            file.renameTo(removalFile)
            removalFile.delete()

            logger.info("Decrypted ${file.absolutePath}")
        } catch (e: Exception) {
            logger.info("Failed to decrypt ${file.absolutePath}")
            logger.debug(e)
        } finally {
            complete(loliBunker)
        }
    }
}