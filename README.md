# FlyLib Reloaded

__**FlyLib Reloaded is currently under development and is buggy and will undergo specification changes soon. That is, it is not stable.**__

![Java CI with Gradle](https://github.com/TeamKun/flylib-reloaded/workflows/Java%20CI%20with%20Gradle/badge.svg)  
[![](https://jitpack.io/v/TeamKun/flylib-reloaded.svg)](https://jitpack.io/#TeamKun/flylib-reloaded)  
A utility librarry for Minecraft developed in Kotlin. We are developing it with the aim of making it as easy to write as possible and easy to
maintain.  
This library is mainly developed by [Kotx\_\_](https://twitter.com/kotx__)

**[Preview](https://imgur.com/Wy5yUvI)**  
**[Examples](https://github.com/TeamKun/flylib-reloaded/tree/master/TestServer)**

Check the **[Wiki](https://github.com/TeamKun/flylib-reloaded/wiki/FlyLib-Reloaded-Docs:-Welcome)** for more information.

Just write as follows, tab completion according to usages, execution of subcommands, and generation of help commands will all be done. There is no
need for the user to register the command in plugin.yml. Fly Lib will automatically register everything in bukkit.

### Kotlin
```kotlin
class PluginTest : JavaPlugin() {
    override fun onEnable() {
        flyLib {
            command {
                register(TestCommand())
                completion {
                    register(
                        ChildrenCompletionContributor(),
                        OptionCompletionContributor(),
                        UsageCompletionContributor(),
                        BasicCompletionContributor()
                    )
                }
            }
        }
    }
}

class TestCommand: Command("test") {
    override val description: String = "A test command for describe usage"
    override val usages = listOf(
        Usage("test <name>", "Say hello to you"),
    )
    
    override fun CommandContext.execute() {
        if (args.isEmprty()) {
            sendHelp()
            return
        }
        
        send("Hello ${args.first()}!")
    }
}
```

### Java
```java
public class JavaPluginTest extends JavaPlugin {
    @Override
    public void onEnable() {
        new FlyLib.Builder(this).commandHandler(
                new CommandHandler.Builder()
                        .register(new TestCommand())
                        .completion(new CommandCompletion.Builder().register(
                                new ChildrenCompletionContributor(),
                                new OptionCompletionContributor(),
                                new UsageCompletionContributor(),
                                new BasicCompletionContributor()
                        ).build())
                        .build()
        ).build();
    }
}
```

## Requirements

Minecraft: 1.16.5  
Paper: 1.16.5-R0.1-SNAPSHOT

## Installation

[![](https://jitpack.io/v/TeamKun/flylib-reloaded.svg)](https://jitpack.io/#TeamKun/flylib-reloaded)  

<details>
<summary>Gradle Kotlin DSL</summary>
<div>

```gradle
repositories {
    maven("https://jitpack.io")
}
```
```gradle
dependencies {
    implementation("com.github.TeamKun:flylib-reloaded:<VERSION>")
}

```
</div>
</details>

<details>
<summary>Gradle</summary>
<div>

```gradle
repositories {
    maven { url "https://jitpack.io" }
}
```
```gradle
dependencies {
    implementation "com.github.TeamKun:flylib-reloaded:<VERSION>"
}

```
</div>
</details>

<details>
<summary>Maven</summary>
<div>

```maven
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```
```maven
<dependency>
    <groupId>com.github.TeamKun</groupId>
    <artifactId>flylib-reloaded</artifactId>
    <version>VERSION</version>
</dependency>

```
</div>
</details>

## License

[MIT](https://github.com/TeamKun/flylib-reloaded/blob/master/LICENSE)
