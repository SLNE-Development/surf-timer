package dev.slne.surf.timer.placeholderapi

import dev.slne.surf.surfapi.bukkit.api.hook.papi.expansion.PapiExpansion
import dev.slne.surf.timer.placeholderapi.placeholder.TimerTimePlaceholder

object TimerPapiExpansion : PapiExpansion(
    "timer",
    listOf(
        TimerTimePlaceholder
    )
)