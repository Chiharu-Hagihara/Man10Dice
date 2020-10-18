package chiharu.hagihara.man10dice

import org.bukkit.command.CommandSender
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.java.JavaPlugin
import java.util.*
import java.util.concurrent.ConcurrentHashMap


class Man10Dice : JavaPlugin() {

    companion object{
        lateinit var plugin:Man10Dice

        val prefix = "§l[§d§lM§f§la§a§ln§f§l10§5§lDice§f§l]"

        val config = YamlConfiguration()

        var waittime = false

        var nowAD = false
        val DMap: ConcurrentHashMap<Int, UUID> = ConcurrentHashMap()
        var helder : CommandSender? = null
        var heldername : String? = null
        var Dmax = 0
        var thereisWinner = false

        var radius:Int = 50
    }

    override fun onEnable() {
        // Plugin startup logic
        getCommand("mdice")?.setExecutor(DiceCommand)
        Listener(this)
        plugin = this
        saveDefaultConfig()
        val config = config
        radius = config.getInt("radius")
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}