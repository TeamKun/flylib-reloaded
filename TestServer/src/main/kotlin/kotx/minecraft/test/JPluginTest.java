/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package kotx.minecraft.test;

import dev.kotx.flylib.FlyLib;
import dev.kotx.flylib.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class JPluginTest extends JavaPlugin {
    @Override
    public void onEnable() {
        FlyLib.inject(this, flylib -> flylib.command(command -> command.register(new ExplodeCommand())));
    }
}

class ExplodeCommand extends Command {
    public ExplodeCommand() {
        super("explode");
        addUsage(builder -> {
            builder.floatArgument("power", 10, 20, ctx -> {
                ctx.suggest("15");
            });
            builder.playerArgument("targets");

            builder.executes(context -> {
                float power = (float) context.getTypedArgs()[0];
                List<Player> targets = (List<Player>) context.getTypedArgs()[1];
                targets.forEach(player -> {
                    context.getWorld().createExplosion(player, power);
                });
            });
        });
        addExample("explode 15 @a");
    }

    @Override
    public String getDescription() {
        return "Explode someone.";
    }
}