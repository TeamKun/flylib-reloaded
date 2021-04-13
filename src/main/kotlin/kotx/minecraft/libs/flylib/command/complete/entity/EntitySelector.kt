/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package kotx.minecraft.libs.flylib.command.complete.entity

import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class EntitySelector(
    val name: String,
    val valueType: EntitySelectorType,
    val defaultValues: (JavaPlugin, String?) -> List<String> = { _, _ -> emptyList() }
) {
    companion object {
        private val suggestOptions = listOf(
            EntitySelector("advancements", EntitySelectorType.TEXT) { _, _ -> listOf("{}") },
            EntitySelector("distance", EntitySelectorType.RANGE) { _, s ->
                if (s?.toIntOrNull() ?: 0 > 0)
                    listOf(s + "0", s + "00")
                else
                    emptyList()
            },
            EntitySelector("dx", EntitySelectorType.NUMBER) { _, s ->
                if (s?.toIntOrNull() ?: 0 > 0)
                    listOf(s + "0", s + "00")
                else
                    emptyList()
            },
            EntitySelector("dy", EntitySelectorType.NUMBER) { _, s ->
                if (s?.toIntOrNull() ?: 0 > 0)
                    listOf(s + "0", s + "00")
                else
                    emptyList()
            },
            EntitySelector("dz", EntitySelectorType.NUMBER) { _, s ->
                if (s?.toIntOrNull() ?: 0 > 0)
                    listOf(s + "0", s + "00")
                else
                    emptyList()
            },
            EntitySelector("gamemode", EntitySelectorType.GAMEMODE) { _, _ ->
                listOf(
                    "survival",
                    "creative",
                    "adventure",
                    "spectator"
                )
            },
            EntitySelector("level", EntitySelectorType.RANGE) { p, _ ->
                p.server.onlinePlayers.map { it.level.toString() }
            },
            EntitySelector("limit", EntitySelectorType.NUMBER) { p, s ->
                listOf((p.server.onlinePlayers.size).toString(), (p.server.onlinePlayers.size / 2).toString())
            },
            EntitySelector("name", EntitySelectorType.TEXT) { p, _ ->
                p.server.onlinePlayers.map { it.name }
            },
            EntitySelector("nbt", EntitySelectorType.TEXT) { _, _ -> listOf("{}") },
            EntitySelector("predicate", EntitySelectorType.TEXT),
            EntitySelector("scores", EntitySelectorType.TEXT),
            EntitySelector("sort", EntitySelectorType.RANGE) { _, _ ->
                listOf("nearest", "furthest", "random", "arbitrary")
            },
            EntitySelector("tags", EntitySelectorType.TEXT),
            EntitySelector("team", EntitySelectorType.TEXT) { _, _ ->
                Bukkit.getScoreboardManager().mainScoreboard.teams.map { it.name }
            },
            EntitySelector("x", EntitySelectorType.NUMBER) { _, s ->
                if (s?.toIntOrNull() ?: 0 > 0)
                    listOf(s + "0", s + "00")
                else
                    emptyList()
            },
            EntitySelector("x_rotation", EntitySelectorType.RANGE) { _, s ->
                if (s?.toIntOrNull() ?: 0 in -9..9)
                    listOf(s + "0", "-" + s + "0")
                else
                    emptyList()
            },
            EntitySelector("y", EntitySelectorType.NUMBER) { _, s ->
                if (s?.toIntOrNull() ?: 0 > 0)
                    listOf(s + "0", s + "00")
                else
                    emptyList()
            },
            EntitySelector("y_rotation", EntitySelectorType.RANGE) { _, s ->
                if (s?.toIntOrNull() ?: 0 in -9..9)
                    listOf(s + "0", "-" + s + "0")
                else
                    emptyList()
            },
            EntitySelector("z", EntitySelectorType.NUMBER) { _, s ->
                if (s?.toIntOrNull() ?: 0 > 0)
                    listOf(s + "0", s + "00")
                else
                    emptyList()
            },
            EntitySelector("z_rotation", EntitySelectorType.RANGE) { _, s ->
                if (s?.toIntOrNull() ?: 0 in -9..9)
                    listOf(s + "0", "-" + s + "0")
                else
                    emptyList()
            },
        )

        fun suggestEntities(input: String, plugin: JavaPlugin): MutableList<String> {
            val selectors = listOf("@a", "@e", "@s", "@r")
            val suggestions = mutableListOf<String>()

            if (input.isBlank()) {
                suggestions.addAll(plugin.server.onlinePlayers.map { it.name }.toMutableList())
                suggestions.addAll(selectors)

                return suggestions.map { "$input$it" }.toMutableList()
            }

            if (input == "@") {
                suggestions.add("a")
                suggestions.add("e")
                suggestions.add("s")
                suggestions.add("r")

                return suggestions.map { "$input$it" }.toMutableList()
            }

            if (selectors.contains(input.toLowerCase())) {
                suggestions.add("[]")
                return suggestions.map { "$input$it" }.toMutableList()
            }

            if (selectors.any { input.toLowerCase().startsWith("$it[") && input.endsWith("]") }) {
                return mutableListOf()
            }

            val selector = selectors.find { input.toLowerCase().startsWith("$it[") }
            if (selector != null) {
                val option = input.replace("$selector[", "").run {
                    if (lastOrNull() == ']')
                        substring(0 until length)
                    else
                        this
                }

                val currentOption = option.split(",").last()
                val currentOptionKey = currentOption.split("=").getOrNull(0)
                val currentOptionValue = currentOption.split("=").getOrNull(1)
                val currentOptionInfo = suggestOptions.find { it.name == currentOptionKey }

                when {
                    currentOption.split("=").size == 1 -> {
                        val options = suggestOptions.filter { it.name.startsWith(currentOption, true) }
                        if (options.size == 1) {
                            suggestions.addAll(options.first().defaultValues(plugin, null).map {
                                "${options.first().name.drop(currentOption.length)}=${it}"
                            })
                        }
                        suggestions.addAll(options.map { "${it.name.drop(currentOption.length)}=" })
                    }
                    currentOptionInfo != null && currentOptionValue.isNullOrEmpty() -> suggestions.addAll(
                        currentOptionInfo.defaultValues(
                            plugin,
                            null
                        )
                    )
                    currentOptionInfo != null && !currentOptionValue.isNullOrEmpty() -> suggestions.addAll(
                        currentOptionInfo.defaultValues(
                            plugin,
                            currentOptionValue
                        ).filter { it.startsWith(currentOptionValue, true) }
                            .map { it.drop(currentOptionValue.length) })
                }
            } else {
                suggestions.addAll(plugin.server.onlinePlayers.map { it.name }.filter { it.startsWith(input, true) }
                    .map { it.drop(input.length) })
            }
            return suggestions.map { "$input$it" }.toMutableList()
        }
    }
}