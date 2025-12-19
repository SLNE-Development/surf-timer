package dev.slne.surf.timer.command.argument

import dev.jorel.commandapi.CommandTree
import dev.jorel.commandapi.arguments.Argument
import dev.jorel.commandapi.arguments.CustomArgument
import dev.jorel.commandapi.arguments.StringArgument
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText
import dev.slne.surf.timer.data.TimerDisplay

class TimerDisplayArgument(nodeName: String) :
    CustomArgument<TimerDisplay, String>(StringArgument(nodeName), { info ->
        TimerDisplay.entries.find { it.name.equals(info.input, ignoreCase = true) }
            ?: throw CustomArgumentException.fromAdventureComponent(buildText {
                appendPrefix()
                error("Diese Timer-Anzeige existiert nicht.")
            })
    })

inline fun CommandTree.timerDisplayArgument(
    nodeName: String,
    optional: Boolean = false,
    block: Argument<*>.() -> Unit = {}
): CommandTree = then(
    TimerDisplayArgument(nodeName).setOptional(optional).apply(block)
)