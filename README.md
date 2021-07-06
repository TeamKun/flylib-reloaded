<h1 align="center">FlyLib Reloaded</h1>

<p align="center"><b>FlyLib Reloaded</b> is a utility library for <a href="https://papermc.io">Minecraft Paper</a> that provides commands, menus, Kotlin extensions, and more.</p>

<div align="center">
    <img src="https://img.shields.io/github/workflow/status/TeamKun/flylib-reloaded/Build?style=flat-square" alt="Build Result">
    <img src="https://img.shields.io/maven-central/v/dev.kotx/flylib-reloaded?color=blueviolet&label=version&style=flat-square" alt="mavencentral release version">
    <a href="https://opensource.org/licenses/mit-license.php"><img src="https://img.shields.io/static/v1?label=license&message=MIT&style=flat-square&color=blue" alt="License"></a>
    <a href="https://twitter.com/kotx__"><img src="https://img.shields.io/static/v1?label=developer&message=kotx__&style=flat-square&color=orange" alt="developer"></a>
    <a href="https://www.codacy.com/gh/TeamKun/flylib-reloaded/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=TeamKun/flylib-reloaded&amp;utm_campaign=Badge_Grade"><img src="https://img.shields.io/codacy/grade/c836938f18e14bd88d9c56f6fd063dca?style=flat-square"/></a>
</div>

⚠️**This library is currently under development (beta version is `0.*.*`), and the API will be changed or removed without notice.**

## ⚡ Quickstart

You can implement tab completion, type checking, help message generation, and subcommands with the following simple code.

❗ **There is no need to add any commands or permissions to plugin.yml. They will be automatically incorporated by FlyLib. permissions, aliases and other command informations are defined as variables in the command.**

[![](https://i.imgur.com/M6Jpyj0.gif)]()

<details>
<summary>Kotlin</summary>
<div>

```kotlin
class KTestPlugin : JavaPlugin() {
    override fun onEnable() {
        flyLib {
            listen<PlayerMoveEvent> { it.player.send("You moved from ${it.from} to ${it.to}") }
            
            command {
                defaultConfiguration {
                    permission(Permission.OP)
                }

                register(KPrintNumberCommand, KTabCompleteCommand, KParentCommand)

                register("menu") {
                    description("Directly registered command")
                    executes {
                        BasicMenu.display(player!!) {
                            item(5, 1, Material.DIAMOND) {
                                displayName("Super Diamond")
                                lore("Very Expensive!")
                                enchant(Enchantment.LUCK)
                                flag(ItemFlag.HIDE_ENCHANTS)

                                executes {
                                    it.whoClicked.send {
                                        bold("DIAMOND", Color.CYAN)
                                        append(" > ", Color.GRAY)
                                        bold("You clicked me!?!?")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

object KPrintNumberCommand : Command("printnumber") {
    init {
        usage {
            intArgument("number", 0, 10)

            executes {
                send("You sent ${args.first()}!")
            }
        }
    }
}

object KTabCompleteCommand : Command("tabcomplete") {
    init {
        usage {
            selectionArgument("mode", "active", "inactive")
            playerArgument("target")
            positionArgument("position")
        }
    }
}

object KParentCommand : Command("parent") {
    init {
        child(ChildrenCommand)
    }

    object ChildrenCommand : Command("children") {
        override fun CommandContext.execute() {
            send("You executed children command!")
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
public class JTestPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        FlyLib.inject(this, flyLib -> {
            flyLib.listen(PlayerMoveEvent.class, event -> event.getPlayer().sendMessage("You moved from " + event.getFrom() + " to " + event.getTo()));

            flyLib.command(command -> {
                command.defaultConfiguration(defaultConfiguration -> defaultConfiguration.permission(Permission.OP));

                command.register(new JPrintNumberCommand(), new JTabCompleteCommand(), new JParentCommand());

                command.register("menu", builder -> builder
                        .description("Direct registered command")
                        .executes(context -> BasicMenu.display(context.getPlayer(), menuBuilder -> menuBuilder
                                .type(Menu.Type.CHEST)
                                .item(Material.DIAMOND, itemBuilder -> itemBuilder
                                        .executes((menu, event) -> context.send(component -> component.append("Hello!", Color.GREEN)))
                                        .displayName(ChatUtils.component("Super Diamond", Color.CYAN))
                                        .lore("Very Expensive!")
                                        .enchant(Enchantment.LUCK)
                                        .flag(ItemFlag.HIDE_ENCHANTS)))));
            });
        });
    }
}

class JPrintNumberCommand extends Command {
    public JPrintNumberCommand() {
        super("printnumber");
        usage(usage -> usage
                .intArgument("number", 0, 10)
                .executes(context -> context.send("You sent " + context.getArgs()[0] + "!")));
    }
}

class JTabCompleteCommand extends Command {
    public JTabCompleteCommand() {
        super("tabcomplete");
        usage(usage -> usage
                .selectionArgument("mode", "active", "inactive")
                .playerArgument("target")
                .positionArgument("position"));
    }
}

class JParentCommand extends Command {
    public JParentCommand() {
        super("parent");
        child(new JChildrenCommand());
    }

    static class JChildrenCommand extends Command {
        public JChildrenCommand() {
            super("children");
        }
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
