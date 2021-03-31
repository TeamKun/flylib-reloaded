/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package kotx.minecraft.libs.flylib.menu

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

abstract class Menu(
    val player: Player,
    size: Int,
    val items: MutableList<MenuItem>
) : Listener, KoinComponent {
    private val plugin by inject<JavaPlugin>()
    protected val inventory = Bukkit.createInventory(player, size)

    init {
        plugin.server.pluginManager.registerEvents(this, plugin)
    }

    abstract fun display()

    @EventHandler
    fun handleClick(event: InventoryClickEvent) {
        if (event.whoClicked.uniqueId != player.uniqueId) return
        if (event.inventory != inventory) return

        onClick(event)
    }

    abstract fun onClick(event: InventoryClickEvent)

    abstract class Builder<T : Menu>(val player: Player) {
        protected val items = mutableListOf<MenuItem>()
        var size = 27

        fun addItem(index: Int, itemStack: ItemStack, onClick: (InventoryClickEvent) -> Unit = {}) {
            if (index >= size)
                throw IllegalArgumentException("index provided $index exceeds size: $size")

            items.removeIf { it.index == index }
            items.add(MenuItem(index, itemStack, onClick))
        }

        fun addItem(x: Int, y: Int, itemStack: ItemStack, onClick: (InventoryClickEvent) -> Unit = {}) {
            val index = (y - 1) * 9 + (x - 1)
            addItem(index, itemStack, onClick)
        }

        abstract fun build(): T
    }

    class MenuItem(
        val index: Int,
        val stack: ItemStack,
        val onClick: (InventoryClickEvent) -> Unit
    )
}