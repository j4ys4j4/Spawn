package jay.spawn.commands

import jay.spawn.SpawnPlugin
import jay.spawn.utils.Colour
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

enum class CommandType {
    SET_SPAWN,
    TELEPORT_TO_SPAWN,
}

class SpawnCommand(private val plugin: SpawnPlugin, private val commandType: CommandType) : CommandExecutor {

    private val messageConfig = plugin.getMessageConfig()
    private val prefix = plugin.getPrefix()

    override fun onCommand(sender: CommandSender, cmd: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage(colouredMessage(messageConfig.getInvalidPlayerMessage(), Colour.RED))
            return true
        }

        when (commandType) {
            CommandType.SET_SPAWN -> setSpawnLocation(sender.location, sender)
            CommandType.TELEPORT_TO_SPAWN -> teleportToSpawn(sender, args)
        }

        return true
    }

    private fun setSpawnLocation(location: Location, sender: Player) {
        plugin.config.set("spawn", location.toConfigString())
        plugin.saveConfig()
        sender.sendMessage(colouredMessage(messageConfig.getSpawnSetMessage(), Colour.GREEN))
    }

    private fun getSpawnLocation(): Location? {
        return plugin.config.getString("spawn")?.toLocation()
    }

    private fun teleportToSpawn(player: Player, args: Array<out String>) {
        val spawnLocation = getSpawnLocation()
        if (spawnLocation != null) {
            if (args.isNotEmpty()) {
                val targetPlayer = Bukkit.getPlayer(args[0])
                if (targetPlayer == null || !targetPlayer.isOnline) {
                    player.sendMessage(colouredMessage(messageConfig.getPlayerNotFoundMessage(), Colour.RED))
                    return
                }
                targetPlayer.teleport(spawnLocation)
                val teleportedOtherMessage = messageConfig.getTeleportedOtherMessage(targetPlayer.name)
                player.sendMessage(colouredMessage(teleportedOtherMessage, Colour.GREEN))
            } else {
                player.teleport(spawnLocation)
                player.sendMessage(colouredMessage(messageConfig.getTeleportedMessage(), Colour.GREEN))
            }
        } else {
            player.sendMessage(colouredMessage(messageConfig.getSpawnNotSetMessage(), Colour.RED))
        }
    }

    private fun colouredMessage(message: String?, colour: Colour): String {
        return if (message != null) {
            "${prefix}${colour.apply(message)}"
        } else {
            ""
        }
    }
}

// Extension function to convert Location to Config string
fun Location.toConfigString(): String {
    return "${this.world.name},${this.x},${this.y},${this.z},${this.yaw},${this.pitch}"
}

// Extension function to convert Config string to Location
fun String.toLocation(): Location? {
    val split = this.split(",")
    if (split.size != 6) return null

    val world = Bukkit.getWorld(split[0]) ?: return null
    val x = split[1].toDouble()
    val y = split[2].toDouble()
    val z = split[3].toDouble()
    val yaw = split[4].toFloat()
    val pitch = split[5].toFloat()

    return Location(world, x, y, z, yaw, pitch)
}