/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package kotx.minecraft.test;

import dev.kotx.flylib.FlyLib;
import dev.kotx.flylib.command.Command;
import dev.kotx.flylib.command.internal.Permission;
import net.kyori.adventure.text.Component;
import org.bukkit.plugin.java.JavaPlugin;

class JTestPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        FlyLib.inject(this, flyLib -> flyLib.command(command -> {
            command.defaultConfiguration(defaultConfiguration -> {
                defaultConfiguration.permission(Permission.OP);
            });

            command.register(new JPrintNumberCommand());
        }));
    }
}

class JPrintNumberCommand extends Command {
    public JPrintNumberCommand() {
        super("printnumber");
        usage(usage -> {
            usage.intArgument("number", 0, 10);
            usage.executes(context -> {
                context.send("You sent " + context.getArgs()[0] + "!");
                context.send(builder -> {
                    builder.append(Component.text(""));
                });
            });
        });
    }
}

class JTabCompleteCommand extends Command {
    public JTabCompleteCommand() {
        super("tabcomplete");
        usage(usage -> {
            usage.selectionArgument("mode", "active", "inactive");
            usage.playerArgument("target");
            usage.positionArgument("position");
        });
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