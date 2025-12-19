package dev.slne.surf.timer

import com.github.shynixn.mccoroutine.folia.SuspendingJavaPlugin
import dev.slne.surf.surfapi.bukkit.api.hook.papi.papiHook
import dev.slne.surf.timer.command.timerCommand
import dev.slne.surf.timer.config.StorageConfig
import dev.slne.surf.timer.manager.TimerManager
import dev.slne.surf.timer.placeholderapi.TimerPapiExpansion
import org.bukkit.plugin.java.JavaPlugin

val plugin get() = JavaPlugin.getPlugin(PaperMain::class.java)

class PaperMain : SuspendingJavaPlugin() {
    override fun onEnable() {
        TimerManager.startUpdating()
        StorageConfig.loadTimers()

        timerCommand()

        papiHook.register(TimerPapiExpansion)
    }

    override fun onDisable() {
        TimerManager.stopUpdating()
        StorageConfig.saveTimers()
    }
}