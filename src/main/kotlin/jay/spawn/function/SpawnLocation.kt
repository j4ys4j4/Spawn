package jay.spawn.function

import org.bukkit.Bukkit
import org.bukkit.Location

// Extension function to convert Location to Config string
fun Location.toConfigString(): String =
    "${world.name},${x},${y},${z},${yaw},${pitch}"

// Extension function to convert Config string to Location
fun String.toLocation(): Location? {
    val split = split(",")
    if (split.size != 6) return null

    val world = Bukkit.getWorld(split[0]) ?: return null
    val x = split[1].toDouble()
    val y = split[2].toDouble()
    val z = split[3].toDouble()
    val yaw = split[4].toFloat()
    val pitch = split[5].toFloat()

    return Location(world, x, y, z, yaw, pitch)
}