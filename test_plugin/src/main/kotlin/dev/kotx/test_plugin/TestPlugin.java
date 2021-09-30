package dev.kotx.test_plugin;

import dev.kotx.flylib.FlyLibKt;
import dev.kotx.flylib.command.Command;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class TestPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        FlyLibKt.flyLib(this, flyLib -> {
            flyLib.command(new ExplodeCommand());
            flyLib.listen(BlockBreakEvent.class, event -> {
                //block break event logic
            });
        });
    }
}

class ExplodeCommand extends Command {
    public ExplodeCommand() {
        super("explode");

        usage(usage -> {
            usage.entityArgument("targets");
            usage.integerArgument("power", 1, 10);
            usage.selectionArgument("mode", "one", "two");

            usage.executes(context -> {
                context.message("You executed explode command!");
            });
        });
    }
}
