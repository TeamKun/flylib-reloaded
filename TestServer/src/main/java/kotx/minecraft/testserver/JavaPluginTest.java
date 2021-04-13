/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package kotx.minecraft.testserver;

import kotx.minecraft.libs.flylib.FlyLib;
import kotx.minecraft.libs.flylib.command.CommandHandler;
import kotx.minecraft.libs.flylib.command.complete.providers.BasicCompletionContributor;
import kotx.minecraft.libs.flylib.command.complete.providers.ChildrenCompletionContributor;
import kotx.minecraft.libs.flylib.command.complete.providers.OptionCompletionContributor;
import kotx.minecraft.libs.flylib.command.complete.providers.UsageCompletionContributor;
import kotx.minecraft.libs.flylib.command.internal.CommandCompletion;
import org.bukkit.plugin.java.JavaPlugin;

public class JavaPluginTest extends JavaPlugin {
    @Override
    public void onEnable() {
        new FlyLib.Builder(this).commandHandler(
                new CommandHandler.Builder()
                        .registerCommand(new TestCommand())
                        .commandCompletion(new CommandCompletion.Builder().registerContributor(
                                new ChildrenCompletionContributor(),
                                new OptionCompletionContributor(),
                                new UsageCompletionContributor(),
                                new BasicCompletionContributor()
                        ).build())
                        .build()
        ).build();
    }
}