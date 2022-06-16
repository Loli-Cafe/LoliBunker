import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import com.inet.gradle.setup.msi.Msi
import org.jetbrains.compose.compose

plugins {
    kotlin("jvm")

    id("application")
    id("org.jetbrains.compose") version "1.1.1"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("de.inetsoftware.setupbuilder") version "7.2.13"
}

dependencies {
    implementation("com.formdev:flatlaf:2.2")
    implementation(project(":lolibunker-core"))
    implementation(compose.desktop.currentOs)
}

buildscript {
    repositories {
        maven("https://plugins.gradle.org/m2/")
    }

    dependencies {
        apply("https://raw.githubusercontent.com/i-net-software/SetupBuilder/master/scripts/SetupBuilderVersion.gradle")
        classpath("gradle.plugin.de.inetsoftware:SetupBuilder:7.2.13")
    }
}

apply {
    plugin("de.inetsoftware.setupbuilder")
}

application {
    mainClass.set("uwu.narumi.lolibunker.ui.AppKt")
}

tasks.withType<ShadowJar> {
    dependsOn("jar")
    archiveFileName.set("LoliBunker-Gui.jar")
}

tasks.withType<Msi> {
    dependsOn("shadowJar")
}

setupBuilder {
    vendor = "LoliCafe"
    application = "LoliBunker"
    description = "https://github.com/loli-cafe/LoliBunker"
    version = "2.1"

    icons = projectDir.absolutePath + "/src/main/resources/icon.png"

    bundleJre = org.gradle.internal.jvm.Jvm.current().javaHome.absolutePath
    from(projectDir.absolutePath + "/build/libs/LoliBunker-Gui.jar")

    mainClass = "uwu.narumi.lolibunker.ui.AppKt"
    mainJar = "LoliBunker-Gui.jar"

    desktopStarter {
        displayName = "LoliBunker"
        workDir = "."
    }
}