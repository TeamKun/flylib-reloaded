/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package kotx.minecraft.testserver;

import kotx.minecraft.libs.flylib.command.Command;
import kotx.minecraft.libs.flylib.command.CommandContext;
import kotx.minecraft.libs.flylib.command.internal.Usage;

import java.util.ArrayList;
import java.util.List;

public class TestCommand extends Command {
    public TestCommand() {
        super("test");
    }

    public List<Usage> getUsages() {
        ArrayList<Usage> usages = new ArrayList<>();
        usages.add(new Usage("test <a/b/c>", "description", new ArrayList()));
        return usages;
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
