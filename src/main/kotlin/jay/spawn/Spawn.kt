package jay.spawn

import jay.spawn.commands.SpawnCommand
import jay.spawn.config.MessageConfig
import jay.spawn.utils.Colour
import jay.spawn.utils.CommandType
import org.bukkit.plugin.java.JavaPlugin

class Spawn : JavaPlugin() {
    private val messageConfig: MessageConfig by lazy { MessageConfig(this) }
    private val prefix: String by lazy { messageConfig.getPrefix() }

    override fun onEnable() {
        loadConfig()
        registerCommands()
        logger.info("${Colour.GREEN}Plugin has been enabled!")
    }

    override fun onDisable() {
        logger.info("${Colour.RED}Plugin has been disabled!")
    }

    fun loadConfig() {
        saveDefaultConfig()
        messageConfig
    }

    private fun registerCommands() {
        getCommand("spawn")?.executor = SpawnCommand(this, CommandType.TELEPORT_TO_SPAWN)
        getCommand("setspawn")?.executor = SpawnCommand(this, CommandType.SET_SPAWN)
        getCommand("reload")?.executor = SpawnCommand(this, CommandType.RELOAD)
    }

    // method to get the prefix from the configuration
    @JvmName("getMessageConfigSpawnPlugin")
    fun getMessageConfig(): MessageConfig = messageConfig
    @JvmName("getPrefixSpawnPlugin")
    fun getPrefix(): String = prefix
}