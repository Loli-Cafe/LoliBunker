package uwu.narumi.lolibunker.module.impl

import uwu.narumi.lolibunker.LoliBunker
import uwu.narumi.lolibunker.header.FileHeader
import uwu.narumi.lolibunker.header.SIGNATURE
import uwu.narumi.lolibunker.helper.ArgonHelper
import uwu.narumi.lolibunker.helper.CryptoHelper
import java.io.File
import java.io.RandomAccessFile
import java.util.*
import java.util.concurrent.ThreadLocalRandom

class FastModule : uwu.narumi.lolibunker.module.Module(
    "fast"
) {

    /*
        Just... don't fucking say anything XD
        I know
        Stop
     */

    override fun encrypt(
        builder: LoliBunker.Builder,
        loliBunker: LoliBunker,
        file: File,
        randomAccessFile: RandomAccessFile
    ) {
        try {
            logger.info("Encrypting ${file.absolutePath}")

            val salt = CryptoHelper.createSecureByteArray(16)
            val iv = CryptoHelper.createSecureByteArray(16)

            val encryptedName = Base64.getEncoder().encodeToString(
                CryptoHelper.encryptDataFast(file.name.toByteArray(), builder.password.toCharArray(), salt, iv)
            )

            val header = FileHeader(
                builder.cipher,
                String(CryptoHelper.createSecureByteArray(ThreadLocalRandom.current().nextInt(8, 32))),
                /*ArgonHelper.encrypt(builder.password.toCharArray())!!*/
                ArgonHelper.fakePassword!!, //TODO: Idk make usage in future of it
                salt,
                iv,
                builder.hwidProtection,
                encryptedName
            )

            val buffer = ByteArray(4 * 1024)

            val headerSize = header.size()
            val fileSize = randomAccessFile.length()

            var size: Long
            var totalSize: Long = 0
            var oldPosition: Long

            while (totalSize < fileSize) {
                oldPosition = randomAccessFile.filePointer

                randomAccessFile.seek(totalSize)
                totalSize += randomAccessFile.read(buffer, 0, buffer.size).also { size = it.toLong() }

                randomAccessFile.seek(oldPosition)
                randomAccessFile.write(
                    CryptoHelper.encryptDataFast(buffer, builder.password.toCharArray(), salt, iv),
                    0,
                    Math.toIntExact(size)
                )
            }

            randomAccessFile.write(header.byteStream.toByteArray())
            randomAccessFile.writeInt(headerSize)
            randomAccessFile.writeBytes(name)
            randomAccessFile.writeInt(name.length)
            randomAccessFile.writeBytes(SIGNATURE)

            randomAccessFile.close()

            file.renameTo(File(file.parent + File.separator + ThreadLocalRandom.current().nextInt()))
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
            val header = FileHeader()
            val buffer = ByteArray(4 * 1024)

            var size: Long
            var totalSize: Long = 0
            var oldPosition: Long

            val headerSize = header.readSize(randomAccessFile, randomAccessFile.length() - 4) //integer size
            val fileSize = randomAccessFile.length() - headerSize

            header.read(randomAccessFile, fileSize)
            while (totalSize < fileSize) {
                oldPosition = randomAccessFile.filePointer

                randomAccessFile.seek(totalSize)
                totalSize += randomAccessFile.read(buffer, 0, buffer.size).also { size = it.toLong() }

                randomAccessFile.seek(oldPosition)
                randomAccessFile.write(
                    CryptoHelper.decryptDataFast(buffer, builder.password.toCharArray(), header.salt, header.iv),
                    0,
                    Math.toIntExact(size)
                )
            }

            randomAccessFile.close()
            val decryptedName = String(
                CryptoHelper.decryptDataFast(
                    Base64.getDecoder().decode(header.fileName),
                    builder.password.toCharArray(),
                    header.salt,
                    header.iv
                )
            )

            file.renameTo(File(file.parent + File.separator + decryptedName))
            logger.info("Decrypted ${file.absolutePath}")
        } catch (e: Exception) {
            logger.info("Failed to decrypt ${file.absolutePath}")
            logger.debug(e)
        } finally {
            complete(loliBunker)
        }
    }
}