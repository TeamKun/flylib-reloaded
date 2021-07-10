/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package dev.kotx.flylib.menu

import dev.kotx.flylib.*
import org.bukkit.*
import org.bukkit.entity.*
import org.bukkit.event.inventory.*
import org.bukkit.inventory.*
import java.awt.*

class BasicMenu(
    val title: String,
    val size: Int,
    val items: List<MenuItem>
) : FlyLibComponent {
    private val players = mutableMapOf<Player, Inventory>()

    fun display(player: Player) {
        val inventory = Bukkit.createInventory(player, size, title)
        player.openInventory(inventory)!!

        players[player] = inventory
    }

    fun close() {
        players.forEach { (player, inventory) ->
            player.closeInventory()
        }

        players.clear()
    }

    fun close(player: Player) {
        players.remove(player)
        player.closeInventory()
    }

    fun update(builder: BasicMenuAction) {
        val menu = BasicMenuBuilder().apply { builder.apply { initialize() } }.build()
        val playerList = players.keys.toList()
        players.clear()

        playerList.forEach { menu.display(it) }
    }

    companion object {
        fun create(builder: BasicMenuAction) = BasicMenuBuilder().apply { builder.apply { initialize() } }.build()

        fun display(player: Player, builder: BasicMenuAction) = create(builder).display(player)
    }
}

class BasicMenuBuilder {
    private var title = "Chest"
    private var size = 27
    private val items = mutableListOf<MenuItem>()

    fun title(title: String): BasicMenuBuilder {
        this.title = title
        return this
    }

    fun size(size: Int): BasicMenuBuilder {
        this.size = size
        return this
    }

    fun type(type: InventoryType): BasicMenuBuilder {
        this.title = (type.defaultTitle() as TextComponent).text
        this.size = type.defaultSize
        return this
    }

    fun build() = BasicMenu(title, size, items)
}

fun interface BasicMenuAction {
    fun BasicMenuBuilder.initialize()
}

class MenuItem(index: Int, item: ItemStack)