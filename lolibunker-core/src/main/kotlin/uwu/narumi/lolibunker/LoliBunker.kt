package uwu.narumi.lolibunker

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.bouncycastle.jce.provider.BouncyCastleProvider
import uwu.narumi.lolibunker.header.FileHeader
import uwu.narumi.lolibunker.header.SIGNATURE
import uwu.narumi.lolibunker.helper.JndiHelper
import uwu.narumi.lolibunker.module.ModuleFactory
import uwu.narumi.lolibunker.type.JobType
import uwu.narumi.lolibunker.type.OperationType
import java.io.File
import java.io.RandomAccessFile
import java.nio.file.Files
import java.nio.file.Path
import java.security.Security
import java.util.concurrent.Executors
import java.util.concurrent.ForkJoinPool
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger
import java.util.stream.Collectors
import kotlin.io.path.isDirectory


enum class LoliBunker {

    INSTANCE;

    private val logger: Logger = LogManager.getLogger(LoliBunker::class.java)

    init {
        JndiHelper.disableJndi()
        Security.addProvider(BouncyCastleProvider())
    }

    val logs = arrayListOf<String>()

    val maxThreads = (Runtime.getRuntime().availableProcessors() * 2) + 2

    var joinPool = ForkJoinPool(1)
    var module: uwu.narumi.lolibunker.module.Module = ModuleFactory.defaultModule

    var currentJob = JobType.NONE
    val jobsDone = AtomicInteger()
    val jobsStarted = AtomicInteger()

    var stopping = AtomicBoolean(false)
    var running = AtomicBoolean(false)

    val gatheredFiles = hashSetOf<File>()

    fun start(builder: Builder) {
        if (running.get() || stopping.get())
            throw UnsupportedOperationException()

        if (builder.files.isEmpty())
            throw Exception("You need to specify files")

        if (builder.password.isEmpty() || builder.password.isBlank())
            throw Exception("You need to specify password")

        Executors.newSingleThreadExecutor().execute {
            running.set(true)
            currentJob = JobType.SEARCHING
            logger.info("Searching for files")

            builder.files.filter {
                it.isNotEmpty() && it.isNotBlank()
            }.map {
                Path.of(it)
            }.forEach { path ->
                if (path.isDirectory()) {
                    gatheredFiles.addAll(
                        Files.walk(path, Int.MAX_VALUE)
                            .filter { it.isDirectory().not() }
                            .map { it.toFile() }
                            .filter { gatheredFiles.contains(it).not() }
                            .collect(Collectors.toSet())
                    )
                } else if (gatheredFiles.contains(path.toFile()).not()) {
                    gatheredFiles.add(path.toFile())
                }
            }

            logger.info("Gathered ${gatheredFiles.size} files")
            if (stopping.get().not()) {
                if (builder.operation == OperationType.DECRYPTION)
                    gatheredFiles.removeIf { FileHeader.hasSignature(it).not() }
                else
                    gatheredFiles.removeIf { FileHeader.hasSignature(it) }

                if (gatheredFiles.isEmpty()) {
                    logger.warn("There are no files suitable for ${if (builder.operation == OperationType.DECRYPTION) "decryption" else "encryption"}")
                    completeJob()
                    return@execute
                }

                module = ModuleFactory.fetch(builder.mode)
                joinPool = ForkJoinPool(builder.threads)
                currentJob =
                    if (builder.operation == OperationType.ENCRYPTION) JobType.ENCRYPTING else JobType.DECRYPTING

                logger.info("Performing ${builder.operation.name} on ${gatheredFiles.size} files, using ${module.name.uppercase()} mode with ${builder.threads} threads")
                gatheredFiles.forEach {
                    doJob(builder, it)
                }
            }
        }
    }

    private fun doJob(builder: Builder, file: File) {
        joinPool.execute {
            if (stopping.get().not()) {
                jobsStarted.incrementAndGet()
                val randomAccessFile = RandomAccessFile(file, "rw")
                if (builder.operation == OperationType.ENCRYPTION) {
                    module.encrypt(builder, this, file, randomAccessFile)
                } else if (builder.operation == OperationType.DECRYPTION) {
                    randomAccessFile.setLength(randomAccessFile.length() - SIGNATURE.length)
                    ModuleFactory
                        .fetch(FileHeader.readType(randomAccessFile), module)
                        .decrypt(builder, this, file, randomAccessFile)
                }
            }
        }
    }

    fun stop() {
        logger.info("Stopping (Finishing started job)")
        stopping.set(true)
        joinPool.shutdownNow()

        if (jobsStarted.get() <= 0) {
            completeJob()
        }
    }

    fun completeJob() {
        gatheredFiles.clear()
        jobsStarted.set(0)
        jobsDone.set(0)
        currentJob = JobType.NONE

        running.set(false)
        stopping.set(false)

        logger.info("Completed")
    }

    data class Builder(
        var files: ArrayList<String> = arrayListOf(),
        var operation: OperationType = OperationType.ENCRYPTION,
        var mode: String = "Safe",
        var cipher: String = "AES/GCM/NoPadding",
        var hwidProtection: Boolean = false,
        var password: String = "",
        var threads: Int = 4
    ) {
        fun files(path: String) = apply { this.files.add(path) }
        fun files(vararg path: String) = apply { this.files.addAll(path) }
        fun files(files: List<String>) = apply { this.files.addAll(files) }
        fun operation(operation: OperationType) = apply { this.operation = operation }
        fun mode(mode: String) = apply { this.mode = mode }
        fun cipher(cipher: String) = apply { this.cipher = cipher }
        fun hwidProtection(hwidProtection: Boolean) = apply { this.hwidProtection = hwidProtection }
        fun hwidProtection() = apply { this.hwidProtection = true }
        fun password(password: String) = apply { this.password = password }
        fun threads(threads: Int) = apply { this.threads = threads }
    }
}