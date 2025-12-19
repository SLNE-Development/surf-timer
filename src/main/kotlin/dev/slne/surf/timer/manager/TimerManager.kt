package dev.slne.surf.timer.manager

import dev.slne.surf.timer.data.Timer
import dev.slne.surf.timer.plugin
import io.papermc.paper.threadedregions.scheduler.ScheduledTask
import org.bukkit.Bukkit
import java.util.concurrent.TimeUnit

val timerManager = TimerManager()

class TimerManager {
    private val timers = mutableListOf<Timer>()

    fun addTimer(timer: Timer) {
        timers.add(timer)
    }

    fun removeTimer(timer: Timer) {
        timers.remove(timer)
    }

    fun getTimers() = timers
    fun exists(id: String) = timers.any { it.id == id }
    fun getTimerById(id: String) = timers.find { it.id == id }

    fun update() {
        timers.forEach {
            it.removeSecond()
            it.update()
        }
    }

    companion object {
        private lateinit var updateTask: ScheduledTask

        fun startUpdating() {
            if (::updateTask.isInitialized && !updateTask.isCancelled) {
                return
            }

            updateTask = Bukkit.getAsyncScheduler().runAtFixedRate(plugin, {
                timerManager.update()
            }, 0L, 1, TimeUnit.SECONDS)
        }

        fun stopUpdating() {
            if (::updateTask.isInitialized && !updateTask.isCancelled) {
                updateTask.cancel()
            }
        }
    }
}