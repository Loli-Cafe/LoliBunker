package uwu.narumi.lolibunker.module

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import uwu.narumi.lolibunker.LoliBunker
import java.io.File
import java.io.RandomAccessFile

abstract class Module(
    val name: String
) {
    protected val logger: Logger = LogManager.getLogger(Module::class.java)

    abstract fun encrypt(
        builder: LoliBunker.Builder,
        loliBunker: LoliBunker,
        file: File,
        randomAccessFile: RandomAccessFile
    )

    abstract fun decrypt(
        builder: LoliBunker.Builder,
        loliBunker: LoliBunker,
        file: File,
        randomAccessFile: RandomAccessFile
    )

    fun complete(loliBunker: LoliBunker) {
        loliBunker.jobsDone.incrementAndGet()

        if ((loliBunker.stopping.get()
                .not() && (loliBunker.jobsDone.get() >= loliBunker.gatheredFiles.size)) || (loliBunker.stopping.get() && (loliBunker.jobsDone.get() >= loliBunker.jobsStarted.get()))
        ) {
            loliBunker.completeJob()
        }
    }
}