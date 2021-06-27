/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package dev.kotx.flylib.menu

import dev.kotx.flylib.*
import dev.kotx.flylib.utils.*
import org.bukkit.*
import org.bukkit.entity.*
import org.bukkit.event.*
import org.bukkit.event.inventory.*
import org.bukkit.inventory.*

interface Menu : Listener, FlyLibComponent {
    fun display(player: Player)

    interface Builder<T : Menu> {
        fun build(): Configuration<T>
    }

    interface Configuration<T: Menu> {
        fun instance(): T
    }

    fun interface Action<T: Menu> {
        fun T.handle(event: InventoryClickEvent)
    }

    data class Item<T: Menu>(
        val index: Int,
        val item: ItemStack,
        val action: Action<T>
    ) : ItemStack(item) {
        class Builder<T: Menu>(private val index: Int, material: Material) : ItemBuilder(material) {
            private var action: Menu.Action<T> = Menu.Action { }

            fun executes(action: Menu.Action<T>): Builder<T> {
                this.action = action
                return this
            }

            fun create(): Item<T> = Item(
                index,
                super.build(),
                action
            )

            fun interface Action<T: Menu> {
                fun Builder<T>.initialize()
            }
        }
    }

    enum class Type(val value: Int) {
        CHEST(27),
        LARGE_CHEST(54),
    }
}