# LoliBunker
### Some file encryption program

<p align="left">
     <a href="https://imgur.com/a/5g5xVkO"><img src="https://i.imgur.com/Ii2SwnK.png"/></a>
</p>

--
### Download
> https://github.com/Loli-Cafe/LoliBunker/releases

---
### TODO
- CLI
- Password matching
- Better admin permission check
- Better kotlin/gradle/jetpack compose

---
### Building
> Depending on what do you want. *(For now only ui has support)*
- `gradlew lolibunker-ui:shadowJar` Creates runnable fatJar with gui: **lolibunker-ui/build/libs/LoliBunker-Gui.jar**
- `gradlew lolibunker-ui:msi` Creates Windows installer: **lolibunker-ui/build/distributions/lolibunker-ui-<version>.msi**

---
> Built on: [Java 11 (Adoptium)](https://adoptium.net/?variant=openjdk11&jvmVariant=hotspot) | [Kotlin 1.6.10](https://kotlinlang.org/) | [JetPack Compose](https://www.jetbrains.com/lp/compose-desktop/)

`My very first Kotlin & JetPack Compose & Gradle project`