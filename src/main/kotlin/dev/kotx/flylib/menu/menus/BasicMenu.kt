/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package dev.kotx.flylib.menu.menus

import dev.kotx.flylib.menu.*
import dev.kotx.flylib.utils.*
import net.kyori.adventure.text.*
import org.bukkit.*
import org.bukkit.entity.*
import org.bukkit.event.*
import org.bukkit.event.inventory.*
import org.bukkit.inventory.*
import org.bukkit.plugin.java.*
import org.koin.core.component.*

class BasicMenu(
    private val items: List<Menu.Item<BasicMenu>>,
    private val type: Menu.Type,
    private val title: Component?,
    private val unregisterAutomatically: Boolean
) : Menu {
    private val plugin: JavaPlugin by inject()
    private val inventories: MutableMap<Player, Inventory> = mutableMapOf()

    init {
        plugin.server.pluginManager.registerEvents(this, plugin)
    }

    override fun display(player: Player) {
        val inventory = if (title != null)
            player.server.createInventory(player, type.value, title)
        else
            player.server.createInventory(player, type.value)


        items.forEach {
            inventory.setItem(it.index, it.item)
        }

        player.openInventory(inventory)

        inventories[player] = inventory
    }

    fun update(builder: Builder.Action) {
        val inv = create(builder)

        inventories.toMap().keys.forEach {
            inv.display(it)
        }
    }

    fun close(player: Player) {
        player.closeInventory()
        inventories.remove(player)
    }

    fun closeAll() {
        inventories.keys.forEach {
            it.closeInventory()
        }

        inventories.clear()
    }

    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {
        if (inventories[event.whoClicked] != event.inventory) return
        event.isCancelled = true
        items.find { it.index == event.rawSlot }?.action?.apply {
            handle(event)
        }
    }

    @EventHandler
    fun onInventoryClose(event: InventoryCloseEvent) {
        if (unregisterAutomatically)
            inventories.remove(event.player)
    }

    class Builder : Menu.Builder<BasicMenu> {
        private val items = mutableListOf<Menu.Item<BasicMenu>>()
        private var title: Component? = null
        private var type: Menu.Type = Menu.Type.CHEST
        private var unregisterAutomatically: Boolean = true

        fun title(text: String): Builder {
            title = text.component()
            return this
        }

        fun title(component: Component): Builder {
            title = component
            return this
        }

        fun title(builder: TextComponentAction): Builder {
            title = text(builder)
            return this
        }

        fun disableUnregisterAutomatically(): Builder {
            unregisterAutomatically = false
            return this
        }

        fun type(type: Menu.Type): Builder {
            this.type = type
            return this
        }

        @JvmOverloads
        fun item(index: Int, item: ItemStack, action: Menu.Action<BasicMenu> = Menu.Action { }): Builder {
            items.add(Menu.Item(index, item, action))
            return this
        }

        @JvmOverloads
        fun item(x: Int, y: Int, item: ItemStack, action: Menu.Action<BasicMenu> = Menu.Action { }): Builder {
            val index = (y - 1) * 9 + (x - 1)
            item(index, item, action)
            return this
        }

        @JvmOverloads
        fun item(item: ItemStack, action: Menu.Action<BasicMenu> = Menu.Action { }): Builder {
            val indexRange = 0 until type.value
            val firstIndex = indexRange.firstOrNull { i -> items.none { it.index == i } } ?: throw IllegalStateException("There is no place where I can place an item!")

            items.add(Menu.Item(firstIndex, item, action))

            return this
        }

        @JvmOverloads
        fun item(index: Int, material: Material, itemBuilder: Menu.Item.Builder.Action<BasicMenu> = Menu.Item.Builder.Action { }): Builder {
            items.add(Menu.Item.Builder<BasicMenu>(index, material).apply { itemBuilder.apply { initialize() } }.create())
            return this
        }


        @JvmOverloads
        fun item(x: Int, y: Int, material: Material, itemBuilder: Menu.Item.Builder.Action<BasicMenu> = Menu.Item.Builder.Action { }): Builder {
            val index = (y - 1) * 9 + (x - 1)
            item(index, material, itemBuilder)
            return this
        }

        @JvmOverloads
        fun item(material: Material, itemBuilder: Menu.Item.Builder.Action<BasicMenu> = Menu.Item.Builder.Action { }): Builder {
            val indexRange = 0 until type.value
            val firstIndex = indexRange.firstOrNull { i -> items.none { it.index == i } } ?: throw IllegalStateException("There is no place where I can place an item!")

            items.add(Menu.Item.Builder<BasicMenu>(firstIndex, material).apply { itemBuilder.apply { initialize() } }.create())

            return this
        }

        override fun build(): BasicMenu = BasicMenu(
            items,
            type,
            title,
            unregisterAutomatically
        )

        fun interface Action {
            fun Builder.initialize()
        }
    }

    companion object {
        @JvmStatic
        fun create(builder: Builder.Action): BasicMenu = Builder().apply { builder.apply { initialize() } }.build()

        @JvmStatic
        fun display(player: Player, builder: Builder.Action): BasicMenu = create(builder).also { it.display(player) }
    }
}