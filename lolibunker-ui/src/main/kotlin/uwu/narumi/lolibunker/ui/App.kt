package uwu.narumi.lolibunker.ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toPainter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.formdev.flatlaf.FlatDarkLaf
import com.formdev.flatlaf.FlatLaf
import uwu.narumi.lolibunker.LoliBunker
import uwu.narumi.lolibunker.cipher.CipherFactory
import uwu.narumi.lolibunker.helper.FileHelper
import uwu.narumi.lolibunker.module.ModuleFactory
import uwu.narumi.lolibunker.type.JobType
import uwu.narumi.lolibunker.type.OperationType
import uwu.narumi.lolibunker.ui.alert.composeErrorAlert
import uwu.narumi.lolibunker.ui.component.*
import uwu.narumi.lolibunker.ui.component.helper.pink
import uwu.narumi.lolibunker.ui.component.helper.whitePink
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import javax.imageio.ImageIO
import javax.swing.JFileChooser
import kotlin.system.exitProcess

fun main() {
    setupUi()
}

fun setupUi() = application {
    FlatLaf.setGlobalExtraDefaults(mapOf(Pair("@accentColor", "#ff3399")))
    FlatDarkLaf.setup()

    Window(
        icon = ImageIO.read(this.javaClass.getResourceAsStream("/icon.png")).toPainter(),
        title = "LoliBunker",
        resizable = false,
        onCloseRequest = ::exitApplication
    ) {
        createApp()
    }
}

val trashViewModel = TrashViewModel()

@Composable
@Preview
fun createApp() {

    //TODO: Better way to check administrator perms
    var errorAlert by remember { mutableStateOf(true) }
    var exitAfterDismiss by remember { mutableStateOf(FileHelper.testPermission().not()) }
    val errorText: MutableState<String?> =
        remember { mutableStateOf("This program needs administrator privileges to work properly.\nPlease run it as an administrator.") }

    val list = listOf("Files", "Cipher", "Options", "Logs")
    var selectedTab by remember { mutableStateOf(0) }

    var password by remember { mutableStateOf("") }
    var passwordVisibility by remember { mutableStateOf(false) }

    var passwordRetype by remember { mutableStateOf("") }
    var passwordVisibilityRetype by remember { mutableStateOf(false) }

    var path by remember { mutableStateOf("") }
    var files by remember { mutableStateOf("") }

    var selectedMode by remember { mutableStateOf(ModuleFactory.defaultModule.name) }
    var expandedMode by remember { mutableStateOf(false) }

    var selectedCipher by remember { mutableStateOf(CipherFactory.defaultCipher) }
    var expandedCipher by remember { mutableStateOf(false) }

    val threads = remember { mutableStateOf(LoliBunker.INSTANCE.maxThreads / 3) }

    MaterialTheme {
        composeErrorAlert(
            errorText.value,
            errorAlert,
            onDismiss = {
                if (exitAfterDismiss) {
                    exitProcess(69)
                } else {
                    errorAlert = false
                    errorText.value = null
                }
            }
        )

        if (selectedMode == "fast" && selectedCipher != CipherFactory.defaultCipherFast) //xd
            selectedCipher = CipherFactory.defaultCipherFast
        else if (selectedMode == "safe" && selectedCipher == CipherFactory.defaultCipherFast)
            selectedCipher = CipherFactory.defaultCipher

        Scaffold(
            topBar = {
                TabRow(selectedTabIndex = selectedTab,
                    backgroundColor = pink,
                    indicator = { tabPositions: List<TabPosition> ->
                        Box(
                            modifier = Modifier
                                .tabIndicatorOffset(tabPositions[selectedTab])
                                .height(5.dp)
                                .padding(horizontal = 28.dp, vertical = 1.dp)
                                .offset(y = (-1).dp)
                                .clip(CircleShape)
                                .background(color = whitePink)
                        )
                    }
                ) {
                    list.forEachIndexed { index, text ->
                        val isSelected = selectedTab == index
                        Tab(
                            modifier = Modifier.background(pink),
                            selected = isSelected,
                            onClick = { selectedTab = index },
                            text = {
                                Text(
                                    text = text,
                                    color = whitePink,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        )
                    }
                }
            },
            content = {
                Column {
                    Box(modifier = Modifier.background(Color.DarkGray).fillMaxWidth().height(420.dp)) {
                        when (selectedTab) {
                            0 -> {
                                Column {
                                    composePath(
                                        path = path,
                                        onSearchClick = {
                                            val chooser = JFileChooser()
                                            chooser.fileSelectionMode = JFileChooser.FILES_AND_DIRECTORIES
                                            chooser.showSaveDialog(null)
                                            path = chooser.selectedFile?.toString() ?: path
                                        },
                                        onAddClick = {
                                            if (path.isNotBlank() && path.isNotEmpty()) {
                                                files += path.trim().plus("\n")
                                                path = ""
                                            }
                                        },
                                        onValueChange = { path = it }
                                    )

                                    composeFiles(
                                        files = files,
                                        onValueChange = {
                                            files = it
                                        }
                                    )
                                }
                            }
                            1 -> {
                                Column {
                                    composePasswordWithRetype(
                                        password = password,
                                        passwordVisibility = passwordVisibility,
                                        onClick = { passwordVisibility = !passwordVisibility },
                                        onValueChange = { password = it },

                                        passwordRetype = passwordRetype,
                                        passwordVisibilityRetype = passwordVisibilityRetype,
                                        onClickRetype = { passwordVisibilityRetype = !passwordVisibilityRetype },
                                        onValueChangeRetype = { passwordRetype = it }
                                    )

                                    composeMode(
                                        selected = selectedMode.replaceFirstChar { it.uppercase() },
                                        expanded = expandedMode,
                                        onFocus = {
                                            expandedMode = true
                                        },
                                        onDismissRequest = {
                                            expandedMode = false
                                        },
                                        onIconClick = {
                                            expandedMode = !expandedMode
                                        }
                                    ) {
                                        ModuleFactory.modules.keys.forEach { label ->
                                            DropdownMenuItem(onClick = {
                                                selectedMode = label
                                                expandedMode = false
                                            }) {
                                                Text(text = label, color = Color.LightGray)
                                            }
                                        }
                                    }

                                    composeCipher(
                                        selected = selectedCipher.replaceFirstChar { it.uppercase() },
                                        expanded = expandedCipher,
                                        onFocus = {
                                            expandedCipher = true
                                        },
                                        onDismissRequest = {
                                            expandedCipher = false
                                        },
                                        onIconClick = {
                                            expandedCipher = !expandedCipher
                                        }
                                    ) {
                                        if (selectedMode == "fast") { //ugh
                                            DropdownMenuItem(onClick = {
                                                selectedCipher = CipherFactory.defaultCipherFast
                                                expandedCipher = false
                                            }) {
                                                Text(text = CipherFactory.defaultCipherFast, color = Color.LightGray)
                                            }
                                        } else {
                                            CipherFactory.defaultCiphers.map { it.key }.forEach { label ->
                                                DropdownMenuItem(onClick = {
                                                    selectedCipher = label
                                                    expandedCipher = false
                                                }) {
                                                    Text(text = label, color = Color.LightGray)
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            2 -> {
                                composeThreads(threads)
                            }
                            3 -> {
                                composeLogs(trashViewModel.logs)
                            }
                        }
                    }

                    Box(modifier = Modifier.background(Color.DarkGray).fillMaxSize()) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            composeActionButtons(
                                running = trashViewModel.running,
                                stopping = trashViewModel.stopping,
                                onEncryptClick = {
                                    LoliBunker.INSTANCE.logs.clear()
                                    trashViewModel.logs.clear()
                                    try {
                                        if (password != passwordRetype)
                                            throw Exception("Passwords does not match")

                                        LoliBunker.INSTANCE.start(
                                            LoliBunker.Builder()
                                                .files(files.lines())
                                                .operation(OperationType.ENCRYPTION)
                                                .mode(selectedMode)
                                                .cipher(selectedCipher)
                                                .password(password)
                                                .threads(threads.value)
                                        )
                                        trashViewModel.running = true //safety
                                    } catch (e: Exception) {
                                        errorText.value = e.message
                                        errorAlert = true
                                    }
                                },
                                onDecryptClick = {
                                    LoliBunker.INSTANCE.logs.clear()
                                    trashViewModel.logs.clear()
                                    try {
                                        if (password != passwordRetype)
                                            throw Exception("Passwords does not match")

                                        LoliBunker.INSTANCE.start(
                                            LoliBunker.Builder()
                                                .files(files.lines())
                                                .operation(OperationType.DECRYPTION)
                                                .mode(selectedMode)
                                                .cipher(selectedCipher)
                                                .password(password)
                                                .threads(threads.value)
                                        )
                                        trashViewModel.running = true //safety
                                    } catch (e: Exception) {
                                        errorText.value = e.message
                                        errorAlert = true
                                    }
                                },
                                onStopClick = {
                                    trashViewModel.stopping = true //safety
                                    LoliBunker.INSTANCE.stop()
                                }
                            )

                            if (trashViewModel.running || trashViewModel.stopping) {
                                when (trashViewModel.currentJob) {
                                    JobType.SEARCHING -> {
                                        LinearProgressIndicator(
                                            modifier = Modifier.size(height = 15.dp, width = 780.dp)
                                                .padding(top = 10.dp, start = 20.dp, end = 20.dp),
                                            color = pink
                                        )

                                        Text(
                                            JobType.SEARCHING.description,
                                            color = Color.LightGray,
                                            modifier = Modifier.padding(5.dp)
                                        )
                                    }
                                    JobType.STOPPING -> {
                                        LinearProgressIndicator(
                                            modifier = Modifier.size(height = 15.dp, width = 780.dp)
                                                .padding(top = 10.dp, start = 20.dp, end = 20.dp),
                                            color = pink
                                        )

                                        Text(
                                            JobType.STOPPING.description,
                                            color = Color.LightGray,
                                            modifier = Modifier.padding(5.dp)
                                        )
                                    }
                                    JobType.DECRYPTING -> {
                                        LinearProgressIndicator(
                                            progress = trashViewModel.progress,
                                            modifier = Modifier.size(height = 15.dp, width = 780.dp)
                                                .padding(top = 10.dp, start = 20.dp, end = 20.dp),
                                            color = pink
                                        )

                                        Text(
                                            JobType.DECRYPTING.description.format(trashViewModel.progress * 100, "%"),
                                            color = Color.LightGray,
                                            modifier = Modifier.padding(5.dp)
                                        )
                                    }
                                    JobType.ENCRYPTING -> {
                                        LinearProgressIndicator(
                                            progress = trashViewModel.progress,
                                            modifier = Modifier.size(height = 15.dp, width = 780.dp)
                                                .padding(top = 10.dp, start = 20.dp, end = 20.dp),
                                            color = pink
                                        )

                                        Text(
                                            JobType.ENCRYPTING.description.format(trashViewModel.progress * 100, "%"),
                                            color = Color.LightGray,
                                            modifier = Modifier.padding(5.dp)
                                        )
                                    }
                                    else -> {
                                        LinearProgressIndicator(
                                            modifier = Modifier.size(height = 15.dp, width = 780.dp)
                                                .padding(top = 10.dp, start = 20.dp, end = 20.dp),
                                            color = pink
                                        )

                                        Text(
                                            "Waiting",
                                            color = Color.LightGray,
                                            modifier = Modifier.padding(5.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        )
    }
}

class TrashViewModel {
    var running by mutableStateOf(LoliBunker.INSTANCE.running.get())
    var stopping by mutableStateOf(LoliBunker.INSTANCE.stopping.get())
    var currentJob by mutableStateOf(LoliBunker.INSTANCE.currentJob)

    var progress by mutableStateOf(0f)
    var logs = mutableStateListOf("")

    init {
        Executors.newSingleThreadScheduledExecutor().scheduleWithFixedDelay({
            running = LoliBunker.INSTANCE.running.get()
            stopping = LoliBunker.INSTANCE.stopping.get()
            currentJob = LoliBunker.INSTANCE.currentJob
            progress =
                LoliBunker.INSTANCE.jobsDone.get().toFloat() / LoliBunker.INSTANCE.gatheredFiles.size.toFloat()

            logs.addAll(LoliBunker.INSTANCE.logs)
            LoliBunker.INSTANCE.logs.clear()
        }, 0, 200, TimeUnit.MILLISECONDS)
    }
}