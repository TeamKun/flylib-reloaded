/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package kotx.minecraft.test

import dev.kotx.flylib.*
import dev.kotx.flylib.command.*
import org.bukkit.entity.*
import org.bukkit.plugin.java.*

class TestPlugin : JavaPlugin() {
    override fun onEnable() {
        flyLib {
            command {
            }
        }
    }
}

class ExplodeeCommand : Command("explode") {
    init {
        addUsage {
            floatArgument("power", 10f, 20f) {
                addSuggestion("15")
            }
            playerArgument("targets")

            executes {
                val power = typedArgs[0] as Float
                val targets = typedArgs[1] as List<Player>
                targets.forEach { world!!.createExplosion(it, power) }
            }
        }

        addExample("explode 15 @a")
    }
}