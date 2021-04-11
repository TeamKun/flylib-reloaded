/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package kotx.minecraft.testserver;

import kotlin.Unit;
import kotx.minecraft.libs.flylib.FlyLib;
import kotx.minecraft.libs.flylib.FlyLibKt;
import kotx.minecraft.libs.flylib.command.Command;
import kotx.minecraft.libs.flylib.command.CommandContext;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.koin.core.Koin;

class TestPl extends JavaPlugin {
    FlyLib flyLib = FlyLibKt.injectFlyLib(this, builder -> {
        builder.commandHandler(handler -> {
            handler.registerCommand(new TestCmd());
            return Unit.INSTANCE;
        });
        return Unit.INSTANCE;
    });
}

class TestCmd extends Command {

    public TestCmd() {
        super("test");
    }

    @NotNull
    @Override
    public String getDescription() {
        return "TEST DESCRIPTION!";
    }

    @Override
    protected void execute(@NotNull CommandContext $this$execute) {
        sendHelp($this$execute);
    }

    @NotNull
    @Override
    public Koin getKoin() {
        return null;
    }
}