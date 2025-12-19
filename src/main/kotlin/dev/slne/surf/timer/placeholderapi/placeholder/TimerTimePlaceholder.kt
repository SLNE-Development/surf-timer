package dev.slne.surf.timer.placeholderapi.placeholder

import dev.slne.surf.surfapi.bukkit.api.hook.papi.expansion.PapiPlaceholder
import dev.slne.surf.timer.manager.timerManager
import org.bukkit.OfflinePlayer

object TimerTimePlaceholder : PapiPlaceholder("time") {
    override fun parse(
        player: OfflinePlayer,
        args: List<String>
    ): String {
        val timerName = args.getOrNull(0) ?: return "Unknown Timer"
        val timer = timerManager.getTimerById(timerName) ?: return "Unknown Timer"

        return formatTime(timer.remainingSeconds)
    }

    private fun formatTime(seconds: Long): String {
        val hrs = seconds / 3600
        val mins = (seconds % 3600) / 60
        val secs = seconds % 60

        return "%02d:%02d:%02d".format(hrs, mins, secs)
    }
}