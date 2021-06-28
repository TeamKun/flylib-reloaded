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
import org.bukkit.*
import org.bukkit.enchantments.*
import org.bukkit.inventory.*
import org.bukkit.plugin.java.*
import java.awt.Color

class KTestPlugin : JavaPlugin() {
    override fun onEnable() {
        flyLib {
            val menu = lazy { BasicMenu.create {
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
            }.instance() }

            command {
                defaultConfiguration {
                    permission(Permission.OP)
                }

                register(KPrintNumberCommand, KTabCompleteCommand, KParentCommand)
                register("menu") {
                    description("Directly registered command")
                    executes {
                        menu.value.display(player!!)
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