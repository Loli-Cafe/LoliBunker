package uwu.narumi.lolibunker.helper

import java.io.File

//TODO: Better system to check administrator perm
object FileHelper {

    private val testFile = File("test.lolibunker")

    init { //safety
        try {
            testFile.delete()
            testFile.deleteOnExit()
        } catch (_: Exception) {
        }
    }

    fun testPermission() = try {
        if (testFile.exists()) {
            testFile.delete() && testFile.createNewFile()
        } else {
            testFile.createNewFile() && testFile.delete()
        }
    } catch (_: Exception) {
        false
    }
}