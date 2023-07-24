package jay.spawn

import jay.spawn.commands.CommandType
import jay.spawn.commands.SpawnCommand
import jay.spawn.config.MessageConfig
import jay.spawn.utils.Colour
import org.bukkit.plugin.java.JavaPlugin

class SpawnPlugin : JavaPlugin() {

    private lateinit var messageConfig: MessageConfig
    private lateinit var prefix: String

    override fun onEnable() {
        loadConfig()
        registerCommands()
        logger.info("${prefix}${Colour.GREEN}Plugin has been enabled!")
    }

    override fun onDisable() {
        logger.info("${prefix}${Colour.RED}Plugin has been disabled!")
    }

    private fun loadConfig() {
        saveDefaultConfig()
        messageConfig = MessageConfig(this)
        prefix = config.getString("prefix", "&7[SPAWN]&r ")
    }

    private fun registerCommands() {
        // Register commands
        getCommand("spawn")?.executor = SpawnCommand(this, CommandType.TELEPORT_TO_SPAWN)
        getCommand("setspawn")?.executor = SpawnCommand(this, CommandType.SET_SPAWN)
    }

    fun getMessageConfig(): MessageConfig {
        return messageConfig
    }

    fun getPrefix(): String {
        return prefix
    }
}

