package jay.spawn.config

import org.bukkit.ChatColor
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.plugin.Plugin

class MessageConfig(private val plugin: Plugin) {

    private val messages: ConfigurationSection = plugin.config.getConfigurationSection("messages")
        ?: plugin.config.createSection("messages")

    fun getTeleportedMessage(): String? = messages.getString("teleported")?.let { formatMessage(it) }
    fun getSpawnNotSetMessage(): String? = messages.getString("spawn_not_set")?.let { formatMessage(it) }
    fun getPlayerNotFoundMessage(): String? = messages.getString("player_not_found")?.let { formatMessage(it) }
    fun getUsageTeleportOtherMessage(): String? = messages.getString("usage_teleport_other")?.let { formatMessage(it) }
    fun getSpawnSetMessage(): String? = messages.getString("spawn_set")?.let { formatMessage(it) }
    fun getInvalidPlayerMessage(): String? = messages.getString("invalid_player")?.let { formatMessage(it) }
    fun getTeleportedOtherMessage(playerName: String): String? = messages.getString("teleported_other")?.replace("{player}", playerName)?.let { formatMessage(it) }

    private fun formatMessage(message: String): String = ChatColor.translateAlternateColorCodes('&', message)
}

