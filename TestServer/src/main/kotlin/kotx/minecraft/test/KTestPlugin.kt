/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package kotx.minecraft.test

import dev.kotx.flylib.*
import dev.kotx.flylib.command.*
import dev.kotx.flylib.command.internal.*
import dev.kotx.flylib.menu.menus.*
import dev.kotx.flylib.utils.*
import net.kyori.adventure.text.format.*
import org.bukkit.*
import org.bukkit.enchantments.Enchantment.*
import org.bukkit.inventory.*
import org.bukkit.plugin.java.*

class KTestPlugin : JavaPlugin() {
    override fun onEnable() {
        flyLib {
//            listen<PlayerMoveEvent> {
//                it.player.send("You moved from ${it.from} to ${it.to}")
//            }

            command {
                defaultConfiguration {
                    permission(Permission.OP)
                }

                register(KPrintNumberCommand)
                register(KTabCompleteCommand)
                register(KParentCommand)
                register(KMenuCommand)
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

object KMenuCommand : Command("menu") {
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