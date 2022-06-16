plugins {
    kotlin("jvm")
}

dependencies {
    implementation("org.bouncycastle:bcpkix-jdk18on:1.71")
    implementation("de.mkammerer:argon2-jvm:2.11")

    api("org.apache.logging.log4j:log4j-core:2.17.2")
}