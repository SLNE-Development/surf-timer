package dev.slne.surf.timer

import dev.slne.surf.surfapi.bukkit.api.util.forEachPlayer
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText
import net.kyori.adventure.text.Component
import java.time.Duration

data class Timer(
    var seconds: Long,
) {
    var remainingSeconds: Long = seconds
    var chat: Boolean = false
    var actionbar: Boolean = false
    var hologram: Boolean = false
    var paused: Boolean = false
    var chatFormat: Component = buildText {
        info("noch")
        appendSpace()
        variableValue("<time>")
        appendSpace()
        info("verbleibend")
    }
    var actionbarFormat: Component = buildText {
        spacer("»")
        appendSpace()
        info("noch")
        appendSpace()
        variableValue("<time>")
        appendSpace()
        info("verbleibend")
        appendSpace()
        spacer("«")
    }

    fun removeSecond() {
        if (!paused) {
            remainingSeconds--
        }
    }

    fun update() {
        if (chat) {
            if (shouldNotify()) {
                broadcastChat()
            }
        }

        if (actionbar) {
            broadcastActionbar()
        }
    }

    private fun broadcastChat() {
        val message = chatFormat.replaceText {
            it.matchLiteral("<time>").replacement(formattedTimeMillis())
        }
        forEachPlayer {
            it.sendText {
                append(message)
            }
        }
    }

    private fun broadcastActionbar() {
        val message = actionbarFormat.replaceText {
            it.matchLiteral("<time>").replacement(formattedTimeMillis())
        }

        forEachPlayer {
            it.sendText {
                append(message)
            }
        }
    }

    private fun formattedTimeMillis(): String {
        val duration = Duration.ofSeconds(remainingSeconds)
        if (duration.isNegative) return "Unbegrenzt"

        var millis = duration.toMillis()

        val days = millis / 86_400_000
        millis %= 86_400_000

        val hours = millis / 3_600_000
        millis %= 3_600_000

        val minutes = millis / 60_000
        millis %= 60_000

        val seconds = millis / 1000
        millis %= 1000

        val parts = mutableListOf<String>()
        if (days > 0) parts.add("$days ${if (days == 1L) "Tag" else "Tage"}")
        if (hours > 0) parts.add("$hours ${if (hours == 1L) "Stunde" else "Stunden"}")
        if (minutes > 0) parts.add("$minutes ${if (minutes == 1L) "Minute" else "Minuten"}")
        if (seconds > 0) parts.add("$seconds ${if (seconds == 1L) "Sekunde" else "Sekunden"}")
        if (parts.isEmpty() && millis > 0) parts.add("$millis Millisekunden")

        return parts.joinToString(", ")
    }

    private fun shouldNotify(): Boolean {
        val remaining = Duration.ofSeconds(remainingSeconds)
        val total = Duration.ofSeconds(seconds)
        if (remaining.isZero || remaining.isNegative) {
            return false
        }

        if (remaining.seconds == total.seconds) {
            return true
        }

        val remainingSeconds = remaining.seconds
        val totalSeconds = total.seconds

        val thresholds = mutableSetOf<Long>()

        var current = totalSeconds
        while (current > 600) {
            current /= 2
            thresholds.add(current)
        }

        val finerSteps = listOf(300L, 180L, 120L, 60L, 30L, 15L, 10L, 5L, 3L, 2L, 1L)
        thresholds.addAll(finerSteps.filter { it < totalSeconds })

        return remainingSeconds in thresholds
    }
}