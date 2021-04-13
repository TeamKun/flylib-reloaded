# FlyLib Reloaded

__**FlyLib Reloaded is currently under development and is buggy and will undergo specification changes soon. That is, it is not stable.**__

![Java CI with Gradle](https://github.com/TeamKun/flylib-reloaded/workflows/Java%20CI%20with%20Gradle/badge.svg)  
[![](https://jitpack.io/v/TeamKun/flylib-reloaded.svg)](https://jitpack.io/#TeamKun/flylib-reloaded)  
A utility librarry for Minecraft developed in Kotlin. We are developing it with the aim of making it as easy to write as possible and easy to
maintain.  
This mod is developed by [Kotx\_\_](https://twitter.com/kotx__)

**[Preview](https://imgur.com/Wy5yUvI)**  
**[Examples](https://github.com/TeamKun/flylib-reloaded/tree/master/TestServer)**

Check the **[Wiki](https://github.com/TeamKun/flylib-reloaded/wiki/FlyLib-Reloaded-Docs:-Welcome)** for more information.

Just write as follows, tab completion according to usages, execution of subcommands, and generation of help commands will all be done. There is no
need for the user to register the command in plugin.yml. Fly Lib will automatically register everything in bukkit.

```kotlin
class TestPlugin : JavaPlugin() {
    val flyLib = injectFlyLib {
        commandHandler {
            registerCommand(TestCommand())
            addUsageReplacement("user") {
                server.onlinePlayers.mapNotNull { it.playerProfile.name }
            }
        }
    }
}

class TestCommand : Command("test") {
    override val permission: Permission = Permission.EVERYONE
    override val usages: List<Usage> = listOf(
        Usage(
            "test <aaa/bbb/ccc> <user> <arg> [..", options = listOf(
                Option("opt", "Option!!", aliases = listOf("o")),
                Option("tst", "test", aliases = listOf("t")),
                Option("hoge", "hogeeeeee", aliases = listOf("h", "hg")),
            )
        )
    )

    override fun CommandContext.execute() {
        sendHelp()
    }
}
```

## Requirements

Minecraft: 1.16.5  
Paper: 1.16.5-R0.1-SNAPSHOT

## Installation

Add jitpack repository

```gradle
repositories {
    maven { url 'https://jitpack.io' }
}
```

Add dependency

[![](https://jitpack.io/v/TeamKun/flylib-reloaded.svg)](https://jitpack.io/#TeamKun/flylib-reloaded)

```gradle
dependencies {
    implementation 'com.github.TeamKun:flylib-reloaded:<RELEASE_VERSION>'
}
```

## License

[MIT](https://github.com/TeamKun/flylib-reloaded/blob/master/LICENSE)
