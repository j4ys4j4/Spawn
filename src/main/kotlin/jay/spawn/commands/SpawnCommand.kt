package jay.spawn.commands

import jay.spawn.Spawn
import jay.spawn.config.MessageConfig
import jay.spawn.function.toConfigString
import jay.spawn.function.toLocation
import jay.spawn.utils.Colour
import jay.spawn.utils.CommandType
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.entity.Player
import java.util.*

class SpawnCommand(private val plugin: Spawn, private val commandType: CommandType) : CommandExecutor {
    private val messageConfig: MessageConfig by lazy { plugin.getMessageConfig() }
    private val prefix: String by lazy { messageConfig.getPrefix() }
    private val lastLocations: MutableMap<UUID, Location> = HashMap()

    override fun onCommand(sender: CommandSender, cmd: Command, label: String, args: Array<out String>): Boolean {
            when (commandType) {
                CommandType.RELOAD -> {
                        if (sender.hasPermission("spawn.reload")) {
                            reloadConfig(sender)
                        } else {
                            sender.sendMessage(colouredMessage("You do not have permission to use this command.", Colour.RED))
                        }
                }
                CommandType.SET_SPAWN -> {
                    if (sender is Player) {
                        if (sender.hasPermission("spawn.setspawn")) {
                            setSpawnLocation(sender.location, sender)
                        } else {
                            sender.sendMessage(colouredMessage("You don't have permission to set spawn.", Colour.RED))
                        }
                    } else {
                        sender.sendMessage(colouredMessage("Only players can use this command.", Colour.RED))
                    }
                }
                CommandType.TELEPORT_TO_SPAWN -> {
                    if (sender is Player) {
                        teleportToSpawn(sender, args)
                    } else {
                        sender.sendMessage(colouredMessage("Only players can use this command.", Colour.RED))
                    }
                }
            }
        return true
    }

    private fun setSpawnLocation(location: Location, sender: Player) {
        val spawnConfig: ConfigurationSection? = plugin.config.getConfigurationSection("spawn")
        if (spawnConfig != null) {
            spawnConfig.set("location", location.toConfigString())
            plugin.saveConfig()
            sender.sendMessage(colouredMessage(plugin.getMessageConfig().getSpawnSetMessage(), Colour.GREEN))
        } else {
            sender.sendMessage(colouredMessage("Spawn configuration is missing.", Colour.RED))
        }
    }

    private fun getSpawnLocation(): Location? {
        val spawnConfig: ConfigurationSection? = plugin.config.getConfigurationSection("spawn")
        return spawnConfig?.getString("location")?.toLocation()
    }

    private fun teleportToSpawn(player: Player, args: Array<out String>) {
        val spawnLocation: Location? = getSpawnLocation()
        if (spawnLocation != null) {
            if (args.isNotEmpty()) {
                val targetPlayerName = args[0]
                if (targetPlayerName.equals("last", ignoreCase = true)) {
                    teleportBackToLastLocation(player)
                } else {
                    teleportPlayerToSpawn(player, targetPlayerName, spawnLocation)
                }
            } else {
                player.teleport(spawnLocation)
                player.sendMessage(colouredMessage(plugin.getMessageConfig().getTeleportedMessage(), Colour.GREEN))
            }
        } else {
            player.sendMessage(colouredMessage(plugin.getMessageConfig().getSpawnNotSetMessage(), Colour.RED))
        }
    }

    private fun teleportPlayerToSpawn(sender: Player, targetPlayerName: String, spawnLocation: Location) {
        val targetPlayer: Player? = Bukkit.getPlayer(targetPlayerName)
        if (targetPlayer == null || !targetPlayer.isOnline) {
            sender.sendMessage(colouredMessage(plugin.getMessageConfig().getPlayerNotFoundMessage(), Colour.RED))
            return
        }

        val teleportLocation = if (plugin.config.getBoolean("player-last-location", false)) {
            lastLocations[targetPlayer.uniqueId] ?: spawnLocation
        } else {
            spawnLocation
        }

        lastLocations[targetPlayer.uniqueId] = targetPlayer.location
        targetPlayer.teleport(teleportLocation)
        sender.sendMessage(colouredMessage(plugin.getMessageConfig().getTeleportedOtherMessage(targetPlayer.name), Colour.GREEN))
    }



    private fun teleportBackToLastLocation(player: Player) {
        val lastLocation: Location? = lastLocations[player.uniqueId]
        if (lastLocation != null) {
            player.teleport(lastLocation)
            lastLocations.remove(player.uniqueId)
        }
    }


    private fun reloadConfig(sender: CommandSender) {
        try {
            plugin.reloadConfig()
            plugin.loadConfig()
            sender.sendMessage(colouredMessage(messageConfig.getReloadMessage(), Colour.GREEN))
        } catch (e: Exception) {
            sender.sendMessage(colouredMessage("An error occurred while reloading the configuration.", Colour.RED))
            e.printStackTrace()
        }
    }

    private fun colouredMessage(message: String, colour: Colour): String {
        return prefix + colour.apply(message)
    }
}