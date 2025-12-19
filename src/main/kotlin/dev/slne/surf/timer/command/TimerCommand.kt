package dev.slne.surf.timer.command

import dev.jorel.commandapi.kotlindsl.*
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText
import dev.slne.surf.surfapi.core.api.messages.builder.SurfComponentBuilder
import dev.slne.surf.surfapi.core.api.messages.pagination.Pagination
import dev.slne.surf.timer.command.argument.timerArgument
import dev.slne.surf.timer.command.argument.timerDisplayArgument
import dev.slne.surf.timer.data.Timer
import dev.slne.surf.timer.data.TimerDisplay
import dev.slne.surf.timer.manager.timerManager
import net.kyori.adventure.text.format.TextDecoration

fun timerCommand() = commandTree("timer") {
    literalArgument("create") {
        stringArgument("id") {
            integerArgument("seconds") {
                anyExecutor { executor, args ->
                    val id: String by args
                    val seconds: Int by args

                    if (timerManager.exists(id)) {
                        executor.sendText {
                            appendPrefix()
                            error("Ein Timer mit der ID existiert bereits.")
                        }
                        return@anyExecutor
                    }

                    timerManager.addTimer(
                        Timer(
                            id = id,
                            seconds = seconds.toLong()
                        ).apply {
                            actionbar = true
                            paused = true
                            chat = true
                        }
                    )

                    executor.sendText {
                        appendPrefix()
                        success("Der Timer wurde erstellt und ")
                        error("pausiert!", TextDecoration.BOLD)
                    }
                }
            }
        }
    }

    literalArgument("pause") {
        timerArgument("timer") {
            anyExecutor { executor, args ->
                val timer: Timer by args

                if (timer.paused) {
                    executor.sendText {
                        appendPrefix()
                        error("Der Timer ist bereits pausiert.")
                    }
                    return@anyExecutor
                }

                timer.paused = true

                executor.sendText {
                    appendPrefix()
                    success("Der Timer wurde pausiert.")
                }
            }
        }
    }

    literalArgument("resume") {
        timerArgument("timer") {
            anyExecutor { executor, args ->
                val timer: Timer by args

                if (!timer.paused) {
                    executor.sendText {
                        appendPrefix()
                        error("Der Timer läuft bereits.")
                    }
                    return@anyExecutor
                }

                if (timer.remainingSeconds == 0L) {
                    timer.remainingSeconds = timer.seconds
                    timer.paused = false

                    executor.sendText {
                        appendPrefix()
                        error("Der Timer wurde neu gestartet, da er bereits abgelaufen ist.")
                    }
                    return@anyExecutor
                }

                timer.paused = false

                executor.sendText {
                    appendPrefix()
                    success("Der Timer wurde fortgesetzt.")
                }
            }
        }
    }

    literalArgument("display") {
        timerArgument("timer") {
            timerDisplayArgument("display") {
                anyExecutor { executor, args ->
                    val timer: Timer by args
                    val display: TimerDisplay by args

                    val enabled = timer.toggleDisplay(display)

                    executor.sendText {
                        appendPrefix()
                        if (enabled) {
                            success("Die Anzeige ")
                            variableValue(display.displayName)
                            success(" wurde für den Timer aktiviert.")
                        } else {
                            success("Die Anzeige ")
                            variableValue(display.displayName)
                            success(" wurde für den Timer deaktiviert.")
                        }
                    }
                }
            }
        }
    }

    literalArgument("list") {
        anyExecutor { executor, _ ->
            val timers = timerManager.getTimers()

            executor.sendText {
                append(Pagination<Timer> {
                    title {
                        primary("Timer", TextDecoration.BOLD)
                    }
                    rowRenderer { timer, _ ->
                        listOf(buildText {
                            spacer("-")
                            appendSpace()
                            variableKey(timer.id)

                            if (timer.paused) {
                                appendSpace()
                                error("(Pausiert)")
                            } else {
                                appendSpace()
                                success("(${timer.remainingSeconds}s verbleibend)")
                            }
                        })
                    }
                }.renderComponent(timers))
            }

        }
    }

    literalArgument("delete") {
        timerArgument("timer") {
            anyExecutor { executor, args ->
                val timer: Timer by args

                timerManager.removeTimer(timer)

                executor.sendText {
                    appendPrefix()
                    success("Der Timer wurde erfolgreich gelöscht.")
                }
            }
        }
    }

    literalArgument("info") {
        timerArgument("timer") {
            anyExecutor { executor, args ->
                val timer: Timer by args

                executor.sendText {
                    appendPrefix()
                    info("Timer-Informationen:")
                    appendArrowedLine()
                    info("ID: ")
                    variableValue(timer.id)
                    appendArrowedLine()
                    info("Ursprüngliche Zeit: ")
                    variableValue("${timer.seconds} Sekunden")
                    appendArrowedLine()
                    info("Verbleibende Zeit: ")
                    variableValue("${timer.remainingSeconds} Sekunden")
                    appendArrowedLine()
                    info("Chat-Benachrichtigungen: ")
                    variableValue(if (timer.chat) "Aktiviert" else "Deaktiviert")
                    appendArrowedLine()
                    info("Actionbar-Benachrichtigungen: ")
                    variableValue(if (timer.actionbar) "Aktiviert" else "Deaktiviert")
                    appendArrowedLine()
                    info("Hologramm-Updates: ")
                    variableValue(if (timer.hologram) "Aktiviert" else "Deaktiviert")
                    appendArrowedLine()
                    info("Pausiert: ")
                    variableValue(if (timer.paused) "Ja" else "Nein")
                }
            }
        }
    }
}

fun SurfComponentBuilder.appendArrowedLine() = append {
    spacer("»")
    appendSpace()
}