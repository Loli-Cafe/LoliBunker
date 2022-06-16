package uwu.narumi.lolibunker.header

import uwu.narumi.lolibunker.helper.CryptoHelper
import java.io.*
import java.util.*


const val SIGNATURE = "LoliBunker"
const val PASSWORD_LENGTH = 132
val SIGNATURE_BYTES = SIGNATURE.toByteArray()

class FileHeader(
    var cipher: String = "",
    var random: String = "",
    var password: String = "",
    var salt: ByteArray = ByteArray(0),
    var iv: ByteArray = ByteArray(0),
    var hwidProtected: Boolean = false,
    var fileName: String = ""
) {

    val byteStream = ByteArrayOutputStream()

    fun create(): FileHeader {
        if (byteStream.size() <= 0) {
            val stream = DataOutputStream(byteStream)

            stream.writeUTF(cipher)
            stream.writeUTF(random)
            stream.write(Base64.getEncoder().encode(password.toByteArray()))
            stream.writeInt(salt.size)
            stream.write(CryptoHelper.xor(salt, SIGNATURE.plus(random), PASSWORD_LENGTH))
            stream.writeInt(iv.size)
            stream.write(CryptoHelper.xor(iv, SIGNATURE.plus(random), PASSWORD_LENGTH))
            stream.writeBoolean(hwidProtected)
            stream.writeUTF(fileName)
        }

        return this
    }

    fun read(stream: DataInputStream): FileHeader {
        cipher = stream.readUTF()
        random = stream.readUTF()
        password = String(Base64.getDecoder().decode(stream.readNBytes(PASSWORD_LENGTH)))
        salt = CryptoHelper.xor(stream.readNBytes(stream.readInt()), SIGNATURE.plus(random), PASSWORD_LENGTH)
        iv = CryptoHelper.xor(stream.readNBytes(stream.readInt()), SIGNATURE.plus(random), PASSWORD_LENGTH)
        hwidProtected = stream.readBoolean()
        fileName = stream.readUTF()

        return this
    }

    fun read(stream: RandomAccessFile, position: Long): FileHeader {
        val oldPosition = stream.filePointer
        stream.seek(position)

        cipher = stream.readUTF()
        random = stream.readUTF()
        password = String(Base64.getDecoder().decode(readNBytes(stream, PASSWORD_LENGTH)))
        salt = CryptoHelper.xor(readNBytes(stream, stream.readInt()), SIGNATURE.plus(random), PASSWORD_LENGTH)
        iv = CryptoHelper.xor(readNBytes(stream, stream.readInt()), SIGNATURE.plus(random), PASSWORD_LENGTH)
        hwidProtected = stream.readBoolean()
        fileName = stream.readUTF()

        stream.setLength(position)
        stream.seek(oldPosition)

        return this
    }

    fun readSize(stream: RandomAccessFile, position: Long): Int {
        val oldPosition = stream.filePointer
        stream.seek(position)

        val size = stream.readInt()
        stream.setLength(position)
        stream.seek(oldPosition)

        return size
    }

    fun size(): Int {
        if (byteStream.size() <= 0)
            create()

        return byteStream.size()
    }

    companion object {

        fun hasSignature(file: File): Boolean {
            val randomAccessFile = RandomAccessFile(file, "rw")
            val hasSignature = checkSignature(randomAccessFile)

            randomAccessFile.close()
            return hasSignature
        }

        private fun readNBytes(randomAccessFile: RandomAccessFile, size: Int): ByteArray {
            val bytes = ByteArray(size)
            randomAccessFile.read(bytes, 0, size)
            return bytes
        }

        fun checkSignature(randomAccessFile: RandomAccessFile): Boolean {
            if (SIGNATURE.length >= randomAccessFile.length())
                return false

            val position = randomAccessFile.filePointer
            randomAccessFile.seek(randomAccessFile.length() - SIGNATURE.length)

            val present = readNBytes(randomAccessFile, SIGNATURE.length).contentEquals(SIGNATURE_BYTES)
            randomAccessFile.seek(position)

            return present
        }

        fun readType(randomAccessFile: RandomAccessFile): String {
            val position = randomAccessFile.filePointer
            randomAccessFile.seek(randomAccessFile.length() - 4)

            val stringSize = randomAccessFile.readInt()
            randomAccessFile.seek((randomAccessFile.length() - 4) - stringSize)

            val type = String(readNBytes(randomAccessFile, stringSize))
            randomAccessFile.setLength((randomAccessFile.length() - 4) - stringSize)
            randomAccessFile.seek(position)

            return type
        }
    }
}