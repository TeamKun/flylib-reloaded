/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package dev.kotx.flylib.menu.menus

import dev.kotx.flylib.*
import dev.kotx.flylib.menu.*
import org.bukkit.entity.*
import org.bukkit.event.inventory.*

class ChestMenu(
    size: Size,
    items: MutableList<MenuItem>
) : Menu(size, items), FlyLibComponent {

    override fun display() {
        items.forEach {
            inventory.setItem(it.index, it.stack)
        }
    }

    override fun onClick(event: InventoryClickEvent) {
        event.isCancelled = true

        event.whoClicked.closeInventory()

        items.firstOrNull { it.index == event.slot }?.also { it.onClick.handleClick(event) }
    }

    class Builder : Menu.Builder<ChestMenu>() {
        override fun build(): ChestMenu = ChestMenu(size, items)

        fun interface Action {
            fun Builder.initialize()
        }
    }

    companion object {
        fun create(player: Player, block: Builder.Action) {
            Builder().apply { block.apply { initialize() } }.build().display(player)
        }
    }
}

fun menu(block: ChestMenu.Builder.Action) = ChestMenu.Builder().apply { block.apply { initialize() } }.build()