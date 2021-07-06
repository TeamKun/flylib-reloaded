/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package kotx.minecraft.test;

import dev.kotx.flylib.FlyLib;
import dev.kotx.flylib.command.Command;
import dev.kotx.flylib.command.internal.Permission;
import dev.kotx.flylib.menu.Menu;
import dev.kotx.flylib.menu.menus.BasicMenu;
import dev.kotx.flylib.utils.ChatUtils;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.plugin.java.JavaPlugin;

import java.awt.*;

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