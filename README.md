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

❗ **There is no need to add any commands or permissions to plugin.yml. They will be automatically incorporated by FlyLib. permissions, aliases and other command informations are defined as variables in the command.**

[![Codacy Badge](https://api.codacy.com/project/badge/Grade/eedeef2019374b658746245da1a69fbf)](https://app.codacy.com/gh/TeamKun/flylib-reloaded?utm_source=github.com&utm_medium=referral&utm_content=TeamKun/flylib-reloaded&utm_campaign=Badge_Grade_Settings)
[![](https://i.imgur.com/M6Jpyj0.gif)]()

<details>
<summary>Kotlin</summary>
<div>

```kotlin
class TestPlugin : JavaPlugin() {
    override fun onEnable() {
        flyLib {
            listen<PlayerMoveEvent> {
                it.player.send("You moved from ${it.from} to ${it.to}")
            }

            command {
                defaultConfiguration {
                    permission(Permission.OP)
                }

                register(PrintNumberCommand)
                register(TabCompleteCommand)
                register(ParentCommand)
                register(MenuCommand)
                register("direct") {
                    description("Directly registered command")
                    executes {
                        send("Hello direct command!")
                    }
                }
            }
        }
    }
}

object PrintNumberCommand : Command("printnumber") {
    init {
        usage {
            intArgument("number", 0, 10)

            executes {
                send("You sent ${args.first()}!")
            }
        }
    }
}

object TabCompleteCommand : Command("tabcomplete") {
    init {
        usage {
            selectionArgument("mode", "active", "inactive")
            playerArgument("target")
            positionArgument("position")
        }
    }
}

object ParentCommand : Command("parent") {
    init {
        child(ChildrenCommand)
    }

    object ChildrenCommand : Command("children") {
        override fun CommandContext.execute() {
            send("You executed children command!")
        }
    }
}

object MenuCommand : Command("menu") {
    override fun CommandContext.execute() {
        ChestMenu.display(player!!) {
            item(5, 1, item(Material.DIAMOND) {
                displayName("Super Diamond")
                lore("Very Expensive!")
                enchant(LUCK)
                flag(ItemFlag.HIDE_ENCHANTS)
            }) {
                send {
                    append("You clicked me!?", TextDecoration.BOLD)
                }
            }
        }
    }
}
```
</div>
</details>

<details>
<summary>Java</summary>
<div>

```java
public class TestPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        FlyLib.inject(this, flyLib -> {
            flyLib.listen(PlayerMoveEvent.class, event -> event.getPlayer().sendMessage("You moved from " + event.getFrom() + " to " + event.getTo()));

            flyLib.command(command -> {
                command.defaultConfiguration(defaultConfiguration -> defaultConfiguration.permission(Permission.OP));

                command.register(new PrintNumberCommand());
                command.register(new TabCompleteCommand());
                command.register(new ParentCommand());
                command.register(new MenuCommand());
                command.register("direct", builder -> builder
                        .description("Directly registered command")
                        .executes(context -> context.send("Hello direct command!")));
            });
        });
    }
}

class PrintNumberCommand extends Command {
    public PrintNumberCommand() {
        super("printnumber");
        usage(usage -> usage
                .intArgument("number", 0, 10)
                .executes(context -> context.send("You sent " + context.getArgs()[0] + "!")));
    }
}

class TabCompleteCommand extends Command {
    public TabCompleteCommand() {
        super("tabcomplete");
        usage(usage -> usage
                .selectionArgument("mode", "active", "inactive")
                .playerArgument("target")
                .positionArgument("position"));


    }
}

class ParentCommand extends Command {
    public ParentCommand() {
        super("parent");
        child(new JChildrenCommand());
    }

    static class ChildrenCommand extends Command {
        public JChildrenCommand() {
            super("children");
        }
    }
}

class MenuCommand extends Command {
    public MenuCommand() {
        super("menu");
    }

    @Override
    public void execute(@NotNull CommandContext context) {
        ChestMenu.display(context.getPlayer(), menu -> menu
                .size(Menu.Size.LARGE_CHEST)
                .item(5, 1, Utils.item(Material.DIAMOND, item -> item
                                .displayName("Super Diamond")
                                .lore("Very Expensive")
                                .enchant(Enchantment.LUCK)
                                .flag(ItemFlag.HIDE_ENCHANTS)),
                        event -> context.send(component -> ChatUtils.append(component, "You clicked me!?", TextDecoration.BOLD))));
    }
}
```
</div>
</details>

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
