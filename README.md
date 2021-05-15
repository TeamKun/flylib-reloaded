# FlyLib Reloaded

A utility library for Minecraft developed in Kotlin. This is a companion version for the `Paper 1.16.5`   
This library is developed by [Kotx\_\_](https://twitter.com/kotx__)

[![](https://jitpack.io/v/TeamKun/flylib-reloaded.svg)](https://jitpack.io/#TeamKun/flylib-reloaded)
[![GitHub license](https://img.shields.io/badge/license-Mit%20License%202.0-blue.svg?style=flat)](https://opensource.org/licenses/mit-license.php)

**FlyLib Reloaded is currently under development and is buggy and will undergo specification changes soon. That is, it is not stable.**

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
    override val usages: List<Usage> = listOf(
        Usage(Argument.Integer("number", max = 10)) {
            sendMessage("Your Number -> ${args.first().toInt()}")
        }
    )
}

class ExplodeCommand : Command("explode") {
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

    class InnerCommand : Command("inner") {
        override val usages: List<Usage> = listOf(
            Usage(
                Argument.Selection("hoge/fuga", "hoge", "fuga")
            )
        )
    }
}
```

## Using in your projects

[![](https://jitpack.io/v/TeamKun/flylib-reloaded.svg)](https://jitpack.io/#TeamKun/flylib-reloaded)

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
    implementation("com.github.TeamKun:flylib-reloaded:<VERSION>")
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
    implementation "com.github.TeamKun:flylib-reloaded:<VERSION>"
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
    <version>VERSION</version>
</dependency>
```

</div>
</details>