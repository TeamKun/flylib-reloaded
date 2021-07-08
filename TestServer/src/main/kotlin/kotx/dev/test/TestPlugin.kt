/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package kotx.dev.test

import dev.kotx.flylib.*
import dev.kotx.flylib.command.*
import dev.kotx.flylib.command.arguments.*
import org.bukkit.plugin.java.*

class TestPlugin: JavaPlugin() {
    val flyLib = FlyLib.create(this, listOf(TestCommand()))
}

class TestCommand : Command("test") {
    init {
        permission = Permission.EVERYONE

        aliases.add("hoge")

        usages.add(
            Usage(
                listOf(LiteralArgument("hello")),
            ),
        )

        usages.add(
            Usage(
                listOf(TextArgument("text"), IntegerArgument("number")),
            )
        )
    }
}