package jay.spawn.utils

import org.bukkit.ChatColor

enum class Colour(private val bukkitColor: ChatColor) {
    BLACK(ChatColor.BLACK),
    RED(ChatColor.RED),
    GREEN(ChatColor.GREEN),
    YELLOW(ChatColor.YELLOW),
    BLUE(ChatColor.BLUE),
    PURPLE(ChatColor.DARK_PURPLE),
    CYAN(ChatColor.DARK_AQUA),
    WHITE(ChatColor.WHITE);

    fun apply(text: String): String {
        return bukkitColor.toString() + text
    }
}
