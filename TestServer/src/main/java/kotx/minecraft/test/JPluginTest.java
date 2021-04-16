/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package kotx.minecraft.test;

import kotx.minecraft.libs.flylib.FlyLib;
import kotx.minecraft.libs.flylib.command.Command;
import kotx.minecraft.libs.flylib.command.CommandContext;
import kotx.minecraft.libs.flylib.command.CommandHandler;
import kotx.minecraft.libs.flylib.command.complete.providers.*;
import kotx.minecraft.libs.flylib.command.internal.CommandCompletion;
import kotx.minecraft.libs.flylib.command.internal.CommandDefault;
import kotx.minecraft.libs.flylib.command.internal.Permission;
import org.bukkit.plugin.java.JavaPlugin;

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