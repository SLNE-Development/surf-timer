package dev.slne.surf.timer

import com.github.shynixn.mccoroutine.folia.SuspendingJavaPlugin
import dev.slne.surf.timer.config.StorageConfig
import dev.slne.surf.timer.manager.TimerManager
import dev.slne.surf.timer.manager.timerManager
import org.bukkit.plugin.java.JavaPlugin

val plugin get() = JavaPlugin.getPlugin(PaperMain::class.java)

class PaperMain : SuspendingJavaPlugin() {
    override fun onEnable() {
        TimerManager.startUpdating()
    }

    override fun onDisable() {
        TimerManager.stopUpdating()
        StorageConfig.saveTimers()
    }
}