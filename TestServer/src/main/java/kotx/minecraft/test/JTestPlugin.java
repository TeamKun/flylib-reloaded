/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package kotx.minecraft.test;

import dev.kotx.flylib.FlyLib;
import dev.kotx.flylib.command.Command;
import dev.kotx.flylib.command.CommandContext;
import dev.kotx.flylib.command.internal.Permission;
import dev.kotx.flylib.menu.Menu;
import dev.kotx.flylib.menu.menus.ChestMenu;
import dev.kotx.flylib.utils.ExtensionsKt;
import dev.kotx.flylib.utils.TextExtensionsKt;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class JTestPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        FlyLib.inject(this, flyLib -> {
            flyLib.listen(PlayerMoveEvent.class, event -> event.getPlayer().sendMessage("You moved from " + event.getFrom() + " to " + event.getTo()));

            flyLib.command(command -> {
                command.defaultConfiguration(defaultConfiguration -> defaultConfiguration.permission(Permission.OP));

                command.register(new JPrintNumberCommand());
                command.register(new JTabCompleteCommand());
                command.register(new JParentCommand());
                command.register(new JMenuCommand());
                command.register("direct", builder -> builder
                        .description("Directly registered command")
                        .executes(context -> context.send("Hello direct command!")));
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

class JMenuCommand extends Command {
    public JMenuCommand() {
        super("menu");
    }

    @Override
    public void execute(@NotNull CommandContext context) {
        ChestMenu.display(context.getPlayer(), menu -> menu
                .size(Menu.Size.LARGE_CHEST)
                .item(5, 1, ExtensionsKt.item(Material.DIAMOND, item -> item
                                .displayName("Super Diamond")
                                .lore("Very Expensive")
                                .enchant(Enchantment.LUCK)
                                .flag(ItemFlag.HIDE_ENCHANTS)),
                        event -> context.send(component -> TextExtensionsKt.append(component, "You clicked me!?", TextDecoration.BOLD))));
    }
}