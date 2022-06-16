package uwu.narumi.lolibunker.type

enum class JobType(val description: String) {

    NONE(""),
    SEARCHING("Searching for files"),
    ENCRYPTING("Encrypting files. %.2f%s"),
    DECRYPTING("Decrypting files. %.2f%s"),
    STOPPING("Stopping")
}