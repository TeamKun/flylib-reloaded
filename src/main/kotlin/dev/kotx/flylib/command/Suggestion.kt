/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package dev.kotx.flylib.command

/**
 * Command suggestions. You can also set a tooltip (text displayed when hovering the mouse).
 */
class Suggestion(
    /**
     * The content of the proposal.
     */
    val content: String,
    /**
     * Suggested tooltip. Displayed when hovering the mouse.
     */
    val tooltip: String? = null
)