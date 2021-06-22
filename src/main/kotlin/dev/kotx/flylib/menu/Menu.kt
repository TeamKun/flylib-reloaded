/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package dev.kotx.flylib.menu

import dev.kotx.flylib.*
import dev.kotx.flylib.menu.Menu.*
import org.bukkit.*
import org.bukkit.entity.*
import org.bukkit.event.*
import org.bukkit.event.inventory.*
import org.bukkit.inventory.*
import org.bukkit.plugin.java.*
import org.koin.core.component.*

abstract class Menu(
    size: Size,
    val items: MutableList<MenuItem>
) : Listener, FlyLibComponent {
    internal var player: Player? = null

    private val plugin by inject<JavaPlugin>()
    protected val inventory = Bukkit.createInventory(null, size.size)

    init {
        plugin.server.pluginManager.registerEvents(this, plugin)
    }

    fun display(player: Player) {
        this.player = player

        setInventory()

        player.openInventory(inventory)
    }

    fun update(updater: InventoryUpdateAction) {
        player!!.openInventory(inventory.apply { updater.apply { initialize() } })
    }

    protected abstract fun setInventory()

    @EventHandler
    fun handleClick(event: InventoryClickEvent) {
        if (event.whoClicked.uniqueId != player?.uniqueId) return
        if (event.inventory != inventory) return

        onClick(event)
    }

    abstract fun onClick(event: InventoryClickEvent)

    abstract class Builder<T : Menu> {
        protected val items = mutableListOf<MenuItem>()
        protected var size: Size = Size.CHEST

        fun size(size: Size): Builder<T> {
            this.size = size
            return this
        }

        @JvmOverloads
        fun item(index: Int, itemStack: ItemStack, onClick: Action = Action { }): Builder<T> {
            if (index >= size.size)
                throw IllegalArgumentException("index provided $index exceeds size: ${size.size}")

            items.removeIf { it.index == index }
            items.add(MenuItem(index, itemStack, onClick))

            return this
        }

        @JvmOverloads
        fun item(x: Int, y: Int, itemStack: ItemStack, onClick: Action = Action { }): Builder<T> {
            val index = (y - 1) * 9 + (x - 1)
            item(index, itemStack, onClick)

            return this
        }

        @JvmOverloads
        fun item(index: Int, material: Material, onClick: Action = Action { }): Builder<T> {
            item(index, ItemStack(material), onClick)

            return this
        }

        @JvmOverloads
        fun item(x: Int, y: Int, material: Material, onClick: Action = Action { }): Builder<T> {
            item(x, y, ItemStack(material), onClick)

            return this
        }

        abstract fun build(): T
    }

    class MenuItem(
        val index: Int,
        val stack: ItemStack,
        val onClick: Action = Action { }
    )

    fun interface Action {
        fun Menu.handleClick(event: InventoryClickEvent)
    }

    fun interface InventoryUpdateAction {
        fun Inventory.initialize()
    }

    enum class Size(val size: Int) {
        CHEST(27),
        LARGE_CHEST(56)
    }
}