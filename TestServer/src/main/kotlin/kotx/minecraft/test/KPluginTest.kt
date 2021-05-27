/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package kotx.minecraft.test

import kotx.minecraft.libs.flylib.flyLib
import org.bukkit.plugin.java.JavaPlugin

class TestPlugin : JavaPlugin() {
    override fun onEnable() {
        flyLib {
            command {
            }
        }
    }
}