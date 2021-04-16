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

### Examples

**Kotlin**

```kotlin
class PluginTest : JavaPlugin() {
    private val flyLib = flyLib {
        command {
            register(KTestCommand())

            defaultConfiguration {
                description("this is a description of the default command.")
                permission(Permission.EVERYONE)
                invalidMessage { "Hey! Looks like you don't have the necessary permissions to run the command!" }
            }

            completion {
                register(
                    ChildrenCompletionContributor(),
                    OptionCompletionContributor(),
                    UsageCompletionContributor(),
                    LikelyCompletionContributor(),
                    BasicCompletionContributor(),
                )
            }
        }
    }

    override fun onEnable() {
        flyLib.initialize()
    }
}

class KTestCommand : Command("test") {
    override fun CommandContext.execute() {
        if (args.isEmpty()) {
            sendHelp()
            return
        }

        sendMessage("Hello ${args.first()}!")
    }
}
```

**Java**

```java
public class JPluginTest extends JavaPlugin {
    private final FlyLib flyLib = new FlyLib.Builder(this)
            .command(new CommandHandler.Builder()
                    .register(new JTestCommand())
                    .completion(new CommandCompletion.Builder()
                            .register(new ChildrenCompletionContributor(),
                                    new OptionCompletionContributor(),
                                    new UsageCompletionContributor(),
                                    new LikelyCompletionContributor(),
                                    new BasicCompletionContributor())
                            .build())
                    .defaultConfiguration(new CommandDefault.Builder()
                            .description("this is a description of the default command.")
                            .permission(Permission.EVERYONE)
                            .invalidMessage(command -> "Hey! Looks like you don't have the necessary permissions to run the command!")
                            .build()
                    ).build()
            ).build();

    @Override
    public void onEnable() {
        flyLib.initialize();
    }
}

class JTestCommand extends Command {
    public JTestCommand() {
        super("test");
    }

    @Override
    protected void execute(CommandContext context) {
        if (context.getArgs().length == 0) {
            sendHelp(context);
            return;
        }

        context.sendMessage("Hello " + context.getArgs()[0] + "!");
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