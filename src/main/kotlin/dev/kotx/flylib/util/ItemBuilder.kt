/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package dev.kotx.flylib.util

import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

/**
 * Item builder to create custom itemStack
 */
class ItemBuilder(
        private val material: Material
) {
    private var amount = 1
    private var name: Component? = null
    private var lores = mutableListOf<Component>()
    private var meta: MetaBuilder? = null

    private val flags = mutableListOf<ItemFlag>()
    private val enchants = mutableMapOf<Enchantment, Int>()

    /**
     * Sets item amount.
     */
    fun amount(amount: Int): ItemBuilder {
        this.amount = amount
        return this
    }

    /**
     * Sets custom item name.
     */
    fun name(name: String): ItemBuilder {
        this.name = name.component()
        return this
    }

    /**
     * Sets custom item name with component builder.
     */
    fun name(builder: ComponentBuilder.() -> Unit): ItemBuilder {
        this.name = ComponentBuilder().apply(builder).build()
        return this
    }

    /**
     * Adds item lore.
     */
    fun lore(vararg lore: String): ItemBuilder {
        this.lores.addAll(lore.map { it.component() })
        return this
    }

    /**
     * Adds item lore with component.
     */
    fun lore(vararg lore: Component): ItemBuilder {
        this.lores.addAll(lore)
        return this
    }

    /**
     * Sets item meta.
     */
    fun meta(metaBuilder: MetaBuilder): ItemBuilder {
        this.meta = metaBuilder
        return this
    }

    /**
     * Adds item flags.
     */
    fun flag(vararg flag: ItemFlag): ItemBuilder {
        this.flags.addAll(flag)
        return this
    }

    /**
     * Enchant items.
     */
    fun enchant(enchantment: Enchantment, level: Int): ItemBuilder {
        this.enchants[enchantment] = level
        return this
    }

    /**
     * Enchant items.
     */
    fun enchant(vararg enchantment: Enchantment): ItemBuilder {
        this.enchants.putAll(enchantment.map { it to 1 })
        return this
    }

    fun build() = ItemStack(material).apply {
        this.amount = this@ItemBuilder.amount
        this.itemFlags.addAll(this@ItemBuilder.flags)
        this.addEnchantments(enchants)

        itemMeta = itemMeta.apply {
            displayName(name)
            lore(lores)
            meta?.apply { initialize() }
        }
    }
}

fun interface MetaBuilder {
    fun ItemMeta.initialize()
}