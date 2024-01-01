package jay.spawn.config

import org.bukkit.ChatColor
import org.bukkit.plugin.Plugin

class MessageConfig(private val plugin: Plugin) {
    private fun formatMessage(message: String?): String = message?.let { ChatColor.translateAlternateColorCodes('&', it) } ?: ""

    fun getTeleportedMessage(): String = formatMessage(plugin.config.getString("messages.teleported"))
    fun getSpawnNotSetMessage(): String = formatMessage(plugin.config.getString("messages.spawn_not_set"))
    fun getPlayerNotFoundMessage(): String = formatMessage(plugin.config.getString("messages.player_not_found"))
    fun getSpawnSetMessage(): String = formatMessage(plugin.config.getString("messages.spawn_set"))
    fun getTeleportedOtherMessage(playerName: String): String =
        formatMessage(plugin.config.getString("messages.teleported_other")?.replace("{player}", playerName))

    // New method to get the prefix from the configuration
    fun getReloadMessage(): String = formatMessage(plugin.config.getString("reload-message", "&aConfiguration reloaded successfully."))
    fun getPrefix(): String = formatMessage(plugin.config.getString("prefix", "&7[SPAWN]&r ") ?: "")
}