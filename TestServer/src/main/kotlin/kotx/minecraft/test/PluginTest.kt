/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package kotx.minecraft.test

import kotx.minecraft.libs.flylib.command.complete.providers.BasicCompletionContributor
import kotx.minecraft.libs.flylib.command.complete.providers.ChildrenCompletionContributor
import kotx.minecraft.libs.flylib.command.complete.providers.OptionCompletionContributor
import kotx.minecraft.libs.flylib.command.complete.providers.UsageCompletionContributor
import kotx.minecraft.libs.flylib.injectFlyLib
import org.bukkit.plugin.java.JavaPlugin

class PluginTest : JavaPlugin() {
    override fun onEnable() {
        injectFlyLib {
            commandHandler {
                registerCommand(TestCommand())
                commandCompletion {
                    registerContributor(
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