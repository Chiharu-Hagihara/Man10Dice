package chiharu.hagihara.man10dice

import chiharu.hagihara.man10dice.Util.radius
import org.bukkit.plugin.java.JavaPlugin


class Man10Dice : JavaPlugin() {

    companion object{
        lateinit var plugin:Man10Dice
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