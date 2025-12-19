package dev.slne.surf.timer.command.argument

import dev.jorel.commandapi.CommandTree
import dev.jorel.commandapi.arguments.Argument
import dev.jorel.commandapi.arguments.ArgumentSuggestions
import dev.jorel.commandapi.arguments.CustomArgument
import dev.jorel.commandapi.arguments.StringArgument
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText
import dev.slne.surf.timer.data.Timer
import dev.slne.surf.timer.manager.timerManager

class TimerArgument(nodeName: String) :
    CustomArgument<Timer, String>(StringArgument(nodeName), { info ->
        timerManager.getTimerById(info.input)
            ?: throw CustomArgumentException.fromAdventureComponent(buildText {
                appendPrefix()
                error("Der Timer existiert nicht.")
            })
    }) {
    init {
        this.replaceSuggestions(
            ArgumentSuggestions.stringCollection {
                timerManager.getTimers().map { it.id }
            }
        )
    }
}

inline fun CommandTree.timerArgument(
    nodeName: String,
    optional: Boolean = false,
    block: Argument<*>.() -> Unit = {}
): CommandTree = then(
    TimerArgument(nodeName).setOptional(optional).apply(block)
)

inline fun Argument<*>.timerArgument(
    nodeName: String,
    optional: Boolean = false,
    block: Argument<*>.() -> Unit = {}
): Argument<*> = then(
    TimerArgument(nodeName).setOptional(optional).apply(block)
)