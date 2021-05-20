/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package kotx.minecraft.libs.flylib.menu.menus

import kotx.minecraft.libs.flylib.FlyLibComponent
import kotx.minecraft.libs.flylib.menu.Menu
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent

class ChestMenu(
    player: Player,
    size: Int,
    items: MutableList<MenuItem>
) : Menu(player, size, items), FlyLibComponent {
    override fun display() {
        items.forEach {
            inventory.setItem(it.index, it.stack)
        }
        player.openInventory(inventory)
    }

    override fun onClick(event: InventoryClickEvent) {
        event.isCancelled = true

        items.firstOrNull { it.index == event.slot }?.also { it.onClick(event) }
    }

    class Builder(player: Player) : Menu.Builder<ChestMenu>(player) {
        override fun build(): ChestMenu = ChestMenu(player, size, items)
    }

    companion object {
        fun create(player: Player, block: Builder.() -> Unit) {
            Builder(player).apply(block).build().display()
        }
    }
}