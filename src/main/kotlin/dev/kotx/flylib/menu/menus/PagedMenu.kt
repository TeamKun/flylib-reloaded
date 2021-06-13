/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package dev.kotx.flylib.menu.menus

import dev.kotx.flylib.FlyLibComponent
import dev.kotx.flylib.menu.Menu
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent

class PagedMenu(
    player: Player,
    items: MutableList<MenuItem>
) : Menu(player, 45, items), FlyLibComponent {
    init {
        items.size / 9 > 4
    }

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