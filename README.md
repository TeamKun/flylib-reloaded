<h1 align="center">FlyLib Reloaded</h1>

<p align="center"><b>FlyLib Reloaded</b> is a utility library for <a href="https://papermc.io">Minecraft Paper</a> that provides commands, menus, Kotlin extensions, and more.</p>

<div align="center">
    <a href="https://github.com/TeamKun/flylib-reloaded"><img src="https://img.shields.io/github/workflow/status/TeamKun/flylib-reloaded/Build?style=flat-square" alt="Build Result"></a>
    <a href="https://github.com/TeamKun/flylib-reloaded"><img src="https://img.shields.io/maven-central/v/dev.kotx/flylib-reloaded?color=blueviolet&label=version&style=flat-square" alt="mavencentral release version"></a>
    <a href="https://opensource.org/licenses/mit-license.php"><img src="https://img.shields.io/static/v1?label=license&message=MIT&style=flat-square&color=blue" alt="License"></a>
    <a href="https://twitter.com/kotx__"><img src="https://img.shields.io/static/v1?label=developer&message=kotx__&style=flat-square&color=orange" alt="developer"></a>
</div>

⚠️**This library is currently under development (beta version is `0.*.*`), and the API will be changed or removed without notice.**

## ⚡ Quickstart

You can implement tab completion, type checking, help message generation, and subcommands with the following simple code.

❗ **There is no need to add any commands or permissions to plugin.yml. They will be automatically incorporated by FlyLib. All permissions and aliases can be defined as variables in
the command.**

[![](https://cdn.kotx.dev/2021-06-14%2023-07-02.gif)]()

```kotlin
class TestPlugin : JavaPlugin() {
    override fun onEnable() {
        flyLib {
            command {
                defaultConfiguration {
                    permission(Permission.OP)
                }

                register(PrintNumberCommand)
                register(TabCompleteCommand)
                register(ParentCommand)
            }
        }
    }
}

object PrintNumberCommand : Command("printnumber") {
    override val usages: List<Usage> = listOf(
        Usage(
            Argument.Integer("number", min = 0, max = 10)
        ) {
            sendMessage("You sent ${args.first()}!")
        }
    )
}

object TabCompleteCommand : Command("tabcomplete") {
    override val usages: List<Usage> = listOf(
        Usage(
            Argument.Selection("mode", "active", "inactive"),
            Argument.Player("target"),
            Argument.Position("position")
        )
    )
}

object ParentCommand : Command("parent") {
    override val children: List<Command> = listOf(ChildrenCommand)

    object ChildrenCommand: Command("children") {
        override fun CommandContext.execute() {
            sendMessage("You executed children command!")
        }
    }
}
```

## ⚙️ Installation

[![](https://img.shields.io/maven-central/v/dev.kotx/flylib-reloaded?color=blueviolet&label=version&style=flat-square)](https://github.com/TeamKun/flylib-reloaded)

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
[![](https://cdn.kotx.dev/idea64_2021-06-14%2022-38-27.png)]()

```kotlin
import com.github.jengelman.gradle.plugins.shadow.tasks.ConfigureShadowRelocation

<..some gradle configurations..>

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
    id "com.github.johnrengelman.shadow" version "6.0.0"
}
```
```groovy
dependencies {
    implementation "dev.kotx:flylib-reloaded:<version>"
}
```

The following code is a configuration of shadowJar that combines all dependencies into one jar.  
It relocates all classes under the project's groupId to avoid conflicts that can occur when multiple plugins using different versions of flylib are deployed to the server.

By setting the following, the contents of the jar file will look like this  
[![](https://cdn.kotx.dev/idea64_2021-06-14%2022-38-27.png)]()

```groovy
import com.github.jengelman.gradle.plugins.shadow.tasks.ConfigureShadowRelocation

<..some gradle configurations..>

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
