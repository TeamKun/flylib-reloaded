<h1 align="center">FlyLib Reloaded</h1>

<p align="center"><b>FlyLib Reloaded</b> is a utility library for <a href="https://papermc.io">Minecraft Paper</a> that provides commands, menus, Kotlin extensions, and more.</p>

<div align="center">
    <img src="https://img.shields.io/github/workflow/status/TeamKun/flylib-reloaded/Build?style=flat-square" alt="Build Result">
    <img src="https://img.shields.io/maven-central/v/dev.kotx/flylib-reloaded?color=blueviolet&label=version&style=flat-square" alt="mavencentral release version">
    <a href="https://opensource.org/licenses/mit-license.php"><img src="https://img.shields.io/static/v1?label=license&message=MIT&style=flat-square&color=blue" alt="License"></a>
    <a href="https://twitter.com/kotx__"><img src="https://img.shields.io/static/v1?label=developer&message=kotx__&style=flat-square&color=orange" alt="developer"></a>
    <a href="https://www.codacy.com/gh/TeamKun/flylib-reloaded/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=TeamKun/flylib-reloaded&amp;utm_campaign=Badge_Grade"><img alt="codacy quality" src="https://img.shields.io/codacy/grade/c836938f18e14bd88d9c56f6fd063dca?style=flat-square"/></a>
</div>

⚠️**This library is currently under development (beta version is `0.*.*`), and the API will be changed or removed without notice.**

## ⚡ Quickstart

You can implement tab completion, type checking, help message generation, and subcommands with the following simple code.

❗ **There is no need to add any commands or permissions to plugin.yml. They will be automatically incorporated by FlyLib. permissions, aliases and other command informations are defined as variables in the command.**

WAIT...

<details>
<summary>Kotlin</summary>
<div>

```kotlin
```
</div>
</details>

<details>
<summary>Java</summary>
<div>

```java
```
</div>
</details>

## ⚙️ Installation

Replace `<version>` with the version you want to use.

<details>
<summary>Gradle Kotlin DSL</summary>
<div>

Please add the following configs to your `build.gradle.kts`.  
Use the `shadowJar` task when building plugins (generating jars to put in plugins/).

```kotlin
plugins {
    id("com.github.johnrengelman.shadow") version "6.0.0"
}
```
```kotlin
dependencies {
    implementation("dev.kotx:flylib-reloaded:<version>")
}
```

The following code is a configuration of shadowJar that combines all dependencies into one jar.  
It relocates all classes under the project's groupId to avoid conflicts that can occur when multiple plugins using different versions of flylib are deployed to the server.  

By setting the following, the contents of the jar file will look like this  

```kotlin
import com.github.jengelman.gradle.plugins.shadow.tasks.ConfigureShadowRelocation

//some gradle configurations

val relocateShadow by tasks.registering(ConfigureShadowRelocation::class) {
    target = tasks.shadowJar.get()
    prefix = project.group.toString()
}

tasks.shadowJar {
    dependsOn(relocateShadow)
}
```

</div>
</details>

<details>
<summary>Gradle</summary>
<div>

```groovy
plugins {
    id 'com.github.johnrengelman.shadow' version '6.0.0'
}
```

```groovy
dependencies {
    implementation 'dev.kotx:flylib-reloaded:<version>'
}
```

The following code is a configuration of shadowJar that combines all dependencies into one jar.  
It relocates all classes under the project's groupId to avoid conflicts that can occur when multiple plugins using different versions of flylib are deployed to the server.

By setting the following, the contents of the jar file will look like this  

```groovy
import com.github.jengelman.gradle.plugins.shadow.tasks.ConfigureShadowRelocation

//some gradle configurations

task relocateShadow(type: ConfigureShadowRelocation) {
    target = tasks.shadowJar
    prefix = project.group
}

tasks.shadowJar.dependsOn tasks.relocateShadow
```

</div>
</details>

<details>
<summary>Maven</summary>
<div>

wait...!

</div>
</details>
