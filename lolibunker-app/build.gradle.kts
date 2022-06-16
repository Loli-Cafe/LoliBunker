import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    kotlin("jvm")

    id("application")
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

dependencies {
    implementation(project(":lolibunker-core"))
    implementation(project(":lolibunker-cli"))
    implementation(project(":lolibunker-ui"))
}

application {
    mainClass.set("BootstrapKt")
}

tasks.withType<ShadowJar> {
    archiveFileName.set("LoliBunker.jar")
}