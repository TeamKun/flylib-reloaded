<h1 align="center">FlyLib Reloaded</h1>

<p align="center"><b>FlyLib Reloaded</b> is a utility library for <a href="https://papermc.io">Minecraft Paper</a> that provides commands, menus, Kotlin extensions, and more.</p>

<div align="center">
    <a href="https://github.com/TeamKun/flylib-reloaded"><img src="https://img.shields.io/github/workflow/status/TeamKun/flylib-reloaded/Build?style=flat-square" alt="Build Result"></a>
    <a href="https://jitpack.io/#TeamKun/flylib-reloaded"><img src="https://img.shields.io/jitpack/v/github/TeamKun/flylib-reloaded?label=Version&logo=Github&style=flat-square&color=blueviolet" alt="jitpack release version"></a>
    <a href="https://opensource.org/licenses/mit-license.php"><img src="https://img.shields.io/static/v1?label=License&message=Mit&style=flat-square&color=blue" alt="License"></a>
    <a href="https://twitter.com/kotx__"><img src="https://img.shields.io/static/v1?label=Developer&message=Kotx__&style=flat-square&color=orange" alt="developer"></a>
</div>
  
⚠️**This library is currently under development (beta version is `0.*.*`), and the API will be changed or removed without notice.**

## ⚡ Quickstart

<details>
<summary>Command</summary>
<div>
    
You can implement tab completion, type checking, help message generation, and subcommands with the following simple code.  
`There is no need to add any commands or permissions to plugin.yml. They will be automatically incorporated by FlyLib. All permissions and aliases can be defined as variables in the command.`

![](https://i.imgur.com/jNh77XS.gif)

```kotlin
class PluginTest : JavaPlugin() {
    override fun onEnable() {
        flyLib {
            command {
                defaultConfiguration {
                    playerOnly(true)
                }

                register(PrintNumberCommand())
                register(ExplodeCommand())
                register(OuterCommand())
            }
        }
    }
}

class PrintNumberCommand : Command("printnumber") {
    override val description = "Prints your number. (10 or less)"
    override val usages: List<Usage> = listOf(
        Usage(Argument.Integer("number", max = 10)) {
            sendMessage("Your Number -> ${args.first().toInt()}")
        }
    )
}

class ExplodeCommand : Command("explode") {
    override val permission = Permission.OP
    override val usages: List<Usage> = listOf(
        Usage(
            Argument.Selection("type", "here", "there")
        ),
        Usage(
            Argument.Position("location")
        ),
        Usage(
            Argument.Player("player")
        ),
    )
}

class OuterCommand : Command("outer") {
    override val children: List<Command> = listOf(
        InnerCommand()
    )
    override val examples = listOf(
        "outer inner hoge"
    )

    class InnerCommand : Command("inner") {
        override val usages: List<Usage> = listOf(
            Usage(
                Argument.Selection("hoge/fuga", "hoge", "fuga")
            )
        )
    }
}
```
</div>
</details>

## ⚙️ Installation

[![](https://img.shields.io/jitpack/v/github/TeamKun/flylib-reloaded?label=Version&logo=Github&style=flat-square&color=blueviolet)](https://jitpack.io/#TeamKun/flylib-reloaded)

Replace `<version>` with the version you want to use of jitpack.

<details>
<summary>Gradle Kotlin DSL</summary>
<div>

```kotlin
repositories {
    maven("https://jitpack.io")
}
```

```kotlin
dependencies {
    implementation("com.github.TeamKun:flylib-reloaded:<version>")
}
```

</div>
</details>

<details>
<summary>Gradle</summary>
<div>

```groovy
repositories {
    maven { url "https://jitpack.io" }
}
```

```groovy
dependencies {
    implementation "com.github.TeamKun:flylib-reloaded:<version>"
}
```

</div>
</details>

<details>
<summary>Maven</summary>
<div>

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

```xml
<dependency>
    <groupId>com.github.TeamKun</groupId>
    <artifactId>flylib-reloaded</artifactId>
    <version>version</version>
</dependency>
```

</div>
</details>

## 📝 Dependencies

- [kotlin](https://github.com/JetBrains/kotlin)
- [spigot-api](https://github.com/SpigotMC/Spigot-API)
- [paper-api](https://github.com/PaperMC/Paper)
- [logback-classic](http://logback.qos.ch)
- [koin](https://github.com/InsertKoinIO/koin)
