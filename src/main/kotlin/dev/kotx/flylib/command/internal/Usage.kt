/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package dev.kotx.flylib.command.internal

import dev.kotx.flylib.command.*

class Usage @JvmOverloads constructor(
    val args: Array<Argument<*>>,
    val description: String = "",
    val permission: Permission? = null,
    val playerOnly: Boolean? = null,
    val options: List<Option> = emptyList(),
    val action: CommandContext.Action? = null
) {
    @JvmOverloads
    constructor(
        args: Array<Argument<*>>,
        description: String = "",
        permission: Permission? = null,
        playerOnly: Boolean? = null,
        action: CommandContext.Action?
    ) : this(
        args, description, permission, playerOnly, emptyList(), action
    )

    class Builder {
        private var args = mutableListOf<Argument<*>>()
        private var description: String = ""
        private var permission: Permission? = null
        private var playerOnly: Boolean? = null
        private var options: List<Option> = emptyList()
        private var action: CommandContext.Action? = null

        fun description(description: String): Builder {
            this.description = description
            return this
        }

        fun permission(permission: Permission): Builder {
            this.permission = permission
            return this
        }

        fun playerOnly(playerOnly: Boolean): Builder {
            this.playerOnly = playerOnly
            return this
        }

        fun executes(action: CommandContext.Action): Builder {
            this.action = action
            return this
        }

        @JvmOverloads
        fun textArgument(name: String, type: Argument.Text.StringType = Argument.Text.StringType.WORD, tabComplete: Argument.Action? = null): Builder {
            args.add(Argument.Text(name, type, tabComplete))
            return this
        }

        fun textArgument(name: String, tabComplete: Argument.Action? = null): Builder {
            args.add(Argument.Text(name, tabComplete = tabComplete))
            return this
        }

        fun selectionArgument(name: String, vararg selections: String): Builder {
            args.add(Argument.Selection(name, *selections))
            return this
        }

        @JvmOverloads
        fun intArgument(name: String, min: Int = Int.MIN_VALUE, max: Int = Int.MAX_VALUE, tabComplete: Argument.Action? = null): Builder {
            args.add(Argument.Integer(name, min, max, tabComplete))
            return this
        }

        fun intArgument(name: String, tabComplete: Argument.Action? = null): Builder {
            args.add(Argument.Integer(name, tabComplete = tabComplete))
            return this
        }

        @JvmOverloads
        fun longArgument(name: String, min: Long = Long.MIN_VALUE, max: Long = Long.MAX_VALUE, tabComplete: Argument.Action? = null): Builder {
            args.add(Argument.Long(name, min, max, tabComplete))
            return this
        }

        fun longArgument(name: String, tabComplete: Argument.Action? = null): Builder {
            args.add(Argument.Long(name, tabComplete = tabComplete))
            return this
        }

        @JvmOverloads
        fun floatArgument(name: String, min: Float = Float.MIN_VALUE, max: Float = Float.MAX_VALUE, tabComplete: Argument.Action? = null): Builder {
            args.add(Argument.Float(name, min, max, tabComplete))
            return this
        }

        fun floatArgument(name: String, tabComplete: Argument.Action? = null): Builder {
            args.add(Argument.Float(name, tabComplete = tabComplete))
            return this
        }

        @JvmOverloads
        fun doubleArgument(name: String, min: Double = Double.MIN_VALUE, max: Double = Double.MAX_VALUE, tabComplete: Argument.Action? = null): Builder {
            args.add(Argument.Double(name, min, max, tabComplete))
            return this
        }

        fun doubleArgument(name: String, tabComplete: Argument.Action? = null): Builder {
            args.add(Argument.Double(name, tabComplete = tabComplete))
            return this
        }

        @JvmOverloads
        fun boolArgument(name: String, tabComplete: Argument.Action? = null): Builder {
            args.add(Argument.Boolean(name, tabComplete = tabComplete))
            return this
        }

        @JvmOverloads
        fun anchorArgument(name: String, tabComplete: Argument.Action? = null): Builder {
            args.add(Argument.Anchor(name, tabComplete = tabComplete))
            return this
        }

        @JvmOverloads
        fun angleArgument(name: String, tabComplete: Argument.Action? = null): Builder {
            args.add(Argument.Angle(name, tabComplete = tabComplete))
            return this
        }

        @JvmOverloads
        fun blockArgument(name: String, tabComplete: Argument.Action? = null): Builder {
            args.add(Argument.Block(name, tabComplete = tabComplete))
            return this
        }

        @JvmOverloads
        fun chatArgument(name: String, tabComplete: Argument.Action? = null): Builder {
            args.add(Argument.Chat(name, tabComplete = tabComplete))
            return this
        }

        @JvmOverloads
        fun chatComponentArgument(name: String, tabComplete: Argument.Action? = null): Builder {
            args.add(Argument.ChatComponent(name, tabComplete = tabComplete))
            return this
        }

        @JvmOverloads
        fun entityArgument(name: String, tabComplete: Argument.Action? = null): Builder {
            args.add(Argument.Entity(name, tabComplete = tabComplete))
            return this
        }

        @JvmOverloads
        fun entityNameArgument(name: String, tabComplete: Argument.Action? = null): Builder {
            args.add(Argument.EntityName(name, tabComplete = tabComplete))
            return this
        }

        @JvmOverloads
        fun itemArgument(name: String, tabComplete: Argument.Action? = null): Builder {
            args.add(Argument.Item(name, tabComplete = tabComplete))
            return this
        }

        @JvmOverloads
        fun mathArgument(name: String, tabComplete: Argument.Action? = null): Builder {
            args.add(Argument.Math(name, tabComplete = tabComplete))
            return this
        }

        @JvmOverloads
        fun mobEffectArgument(name: String, tabComplete: Argument.Action? = null): Builder {
            args.add(Argument.MobEffect(name, tabComplete = tabComplete))
            return this
        }

        @JvmOverloads
        fun particleArgument(name: String, tabComplete: Argument.Action? = null): Builder {
            args.add(Argument.Particle(name, tabComplete = tabComplete))
            return this
        }

        @JvmOverloads
        fun positionArgument(name: String, tabComplete: Argument.Action? = null): Builder {
            args.add(Argument.Position(name, tabComplete = tabComplete))
            return this
        }

        @JvmOverloads
        fun playerArgument(name: String, tabComplete: Argument.Action? = null): Builder {
            args.add(Argument.Player(name, tabComplete = tabComplete))
            return this
        }

        @JvmOverloads
        fun rotationArgument(name: String, tabComplete: Argument.Action? = null): Builder {
            args.add(Argument.Rotation(name, tabComplete = tabComplete))
            return this
        }

        @JvmOverloads
        fun vec2Argument(name: String, tabComplete: Argument.Action? = null): Builder {
            args.add(Argument.Vec2(name, tabComplete = tabComplete))
            return this
        }

        @JvmOverloads
        fun vec3Argument(name: String, tabComplete: Argument.Action? = null): Builder {
            args.add(Argument.Vec3(name, tabComplete = tabComplete))
            return this
        }

        @JvmOverloads
        fun uuidArgument(name: String, tabComplete: Argument.Action? = null): Builder {
            args.add(Argument.UUID(name, tabComplete = tabComplete))
            return this
        }

        @JvmOverloads
        fun dimensionArgument(name: String, tabComplete: Argument.Action? = null): Builder {
            args.add(Argument.Dimension(name, tabComplete = tabComplete))
            return this
        }

        @JvmOverloads
        fun enchantmentArgument(name: String, tabComplete: Argument.Action? = null): Builder {
            args.add(Argument.Enchantment(name, tabComplete = tabComplete))
            return this
        }

        fun build() = Usage(
            args.toTypedArray(), description, permission, playerOnly, options, action
        )

        fun interface Action {
            fun Builder.initialize()
        }
    }
}