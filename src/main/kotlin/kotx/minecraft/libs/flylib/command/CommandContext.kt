/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package kotx.minecraft.libs.flylib.command

import org.bukkit.Server
import org.bukkit.entity.Player

/**
 * By implementing Command execute and tabComplete as extended functions of CommandContext rather than as arguments,
 * you can avoid writing the same arguments to functions, and furthermore, you can aggregate functions that you want to be executed only from that command,
 * which improves the visibility.
 */
class CommandContext(
    val player: Player,
    val server: Server,
    val message: String,
    val args: Array<String>
)