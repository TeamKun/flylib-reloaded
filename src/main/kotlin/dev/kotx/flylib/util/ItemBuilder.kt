/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package dev.kotx.flylib.util

import org.bukkit.*
import org.bukkit.enchantments.*
import org.bukkit.inventory.*
import org.bukkit.inventory.meta.*

class ItemBuilder(
    private val material: Material
) {
    private var amount = 1
    private var meta: MetaBuilder? = null
    private val flags = mutableListOf<ItemFlag>()
    private val enchants = mutableMapOf<Enchantment, Int>()

    fun amount(amount: Int): ItemBuilder {
        this.amount = amount
        return this
    }

    fun meta(metaBuilder: MetaBuilder): ItemBuilder {
        this.meta = metaBuilder
        return this
    }

    fun flag(vararg flag: ItemFlag): ItemBuilder {
        this.flags.addAll(flag)
        return this
    }

    fun enchant(enchantment: Enchantment, level: Int): ItemBuilder {
        this.enchants[enchantment] = level
        return this
    }

    fun enchant(vararg enchantment: Enchantment): ItemBuilder {
        this.enchants.putAll(enchantment.map { it to 1 })
        return this
    }

    fun build() = ItemStack(material).apply {
        amount = this@ItemBuilder.amount
        itemMeta = itemMeta.apply { meta?.apply { initialize() } }
        flags.addAll(this@ItemBuilder.flags)
        addEnchantments(enchants)
    }
}

fun interface MetaBuilder {
    fun ItemMeta.initialize()
}