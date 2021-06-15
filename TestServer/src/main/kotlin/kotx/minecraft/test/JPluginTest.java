/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package kotx.minecraft.test;

import dev.kotx.flylib.FlyLib;
import dev.kotx.flylib.command.Command;
import org.bukkit.plugin.java.JavaPlugin;

public class JPluginTest extends JavaPlugin {
    @Override
    public void onEnable() {
        FlyLib.inject(this, flyLib -> {
            flyLib.command(commandHandler -> {
                commandHandler.register(new TestCommand());
            });
        });
    }
}

class TestCommand extends Command {
    public TestCommand() {
        super("test");
    }
}