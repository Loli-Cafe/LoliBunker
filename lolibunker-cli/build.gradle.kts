import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    kotlin("jvm")

    id("application")
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

dependencies {
    implementation(project(":lolibunker-core"))
}

application {
    mainClass.set("uwu.narumi.lolibunker.cli.Launcher")
}

tasks.withType<ShadowJar> {
    archiveFileName.set("LoliBunker-CLI.jar")
}
