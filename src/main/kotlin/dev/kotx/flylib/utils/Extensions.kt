/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

@file:JvmName("Utils")
@file:JvmMultifileClass

package dev.kotx.flylib.utils

import com.mojang.brigadier.context.CommandContext
import dev.kotx.flylib.command.*
import dev.kotx.flylib.command.internal.*
import net.kyori.adventure.text.*
import net.minecraft.server.v1_16_R3.*
import org.bukkit.Material
import org.bukkit.inventory.*
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.*

operator fun List<Command>.get(query: String) =
    find { it.name.equals(query, true) } ?: find { it -> it.aliases.any { it == query } }

fun CommandContext<CommandListenerWrapper>.asFlyLibContext(command: Command, args: List<Argument<*>>, depth: Int = 0): dev.kotx.flylib.command.CommandContext {
    val replaced = input.replaceFirst("/", "")
    return CommandContext(
        command,
        command.plugin,
        source.bukkitSender,
        replaced,
        replaced.split(" ").drop(1 + depth).toTypedArray(),
        try {
            args.map { it.parser(this, it.name) }.toTypedArray()
        } catch (e: Exception) {
            emptyArray()
        }
    )
}

fun <T> List<T>.joint(other: T): List<T> {
    val res = mutableListOf<T>()
    forEachIndexed { i, it ->
        res.add(it)
        if (i < size - 1)
            res.add(other)
    }

    return res.toList()
}

fun <T, E> List<T>.joint(joiner: E, target: (T) -> E) {
    map(target).joint(joiner)
}

@JvmName("jointT")
fun <T> List<T>.joint(joiner: T, action: (T) -> Unit) {
    joint(joiner).forEach(action)
}

fun <T> List<T>.joint(other: (T) -> T): List<T> {
    val res = mutableListOf<T>()
    forEachIndexed { i, it ->
        res.add(it)
        if (i < size - 1)
            res.add(other(it))
    }

    return res.toList()
}

fun item(material: Material) = ItemStack(material)
fun item(material: Material, action: ItemBuilder.Action) = ItemBuilder(material).apply { action.apply { initialize() } }.build()

class ItemBuilder(private val material: Material) {
    private var displayName: Component? = null
    private val lores = mutableListOf<Component>()
    private val enchants = mutableListOf<Enchantment>()
    private val flags = mutableListOf<ItemFlag>()
    private var amount = 1
    private var meta: ItemMetaAction? = null

    fun displayName(name: String): ItemBuilder {
        this.displayName = name.component()
        return this
    }

    fun displayName(name: Component): ItemBuilder {
        this.displayName = name
        return this
    }

    fun lore(vararg lore: String): ItemBuilder {
        lores.addAll(lore.map { it.component() })
        return this
    }

    fun lore(lore: Component): ItemBuilder {
        this.lores.add(lore)
        return this
    }

    @JvmOverloads
    fun enchant(enchantment: org.bukkit.enchantments.Enchantment, level: Int = 0): ItemBuilder {
        this.enchants.add(
            Enchantment(enchantment, level)
        )

        return this
    }

    fun amount(amount: Int): ItemBuilder {
        this.amount = amount
        return this
    }

    fun flag(vararg flags: ItemFlag): ItemBuilder {
        this.flags.addAll(flags)
        return this
    }

    fun meta(action: ItemMetaAction): ItemBuilder {
        this.meta = action
        return this
    }

    fun build() = ItemStack(material, amount).apply {
        itemMeta = itemMeta.apply {
            this.displayName(this@ItemBuilder.displayName)
            this@ItemBuilder.enchants.forEach {
                addEnchant(it.enchantment, it.level, true)
            }

            flags.forEach {
                addItemFlags(it)
            }

            this.lore(lores)

            this@ItemBuilder.meta?.apply { initialize() }
        }
    }

    private class Enchantment(
        val enchantment: org.bukkit.enchantments.Enchantment,
        val level: Int,
    )

    fun interface Action {
        fun ItemBuilder.initialize()
    }

    fun interface ItemMetaAction {
        fun ItemMeta.initialize()
    }
}

fun BookMeta.page(text: String): BookMeta {
    addPages(text { append(text) })
    return this
}

fun BookMeta.page(block: TextComponentAction): BookMeta {
    addPages(text(block))
    return this
}

fun BookMeta.clear(): BookMeta {
    pages(emptyList())
    return this
}