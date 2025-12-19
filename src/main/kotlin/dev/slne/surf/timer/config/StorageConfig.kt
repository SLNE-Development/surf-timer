package dev.slne.surf.timer.config

import dev.slne.surf.surfapi.core.api.config.SpongeYmlConfigClass
import dev.slne.surf.timer.data.Timer
import dev.slne.surf.timer.manager.timerManager
import dev.slne.surf.timer.plugin
import org.spongepowered.configurate.objectmapping.ConfigSerializable

@ConfigSerializable
data class StorageConfig(
    var timers: List<Timer>
) {
    companion object : SpongeYmlConfigClass<StorageConfig>(
        StorageConfig::class.java,
        plugin.dataPath,
        "storage.yml"
    ) {
        fun saveTimers() {
            edit {
                timers = timerManager.getTimers()
            }
        }
    }
}
