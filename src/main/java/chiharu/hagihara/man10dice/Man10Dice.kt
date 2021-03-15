package chiharu.hagihara.man10dice

import chiharu.hagihara.man10dice.Command.registerCommand
import com.github.syari.spigot.api.util.string.toColor
import org.bukkit.plugin.java.JavaPlugin


class Man10Dice : JavaPlugin() {

    companion object{
        lateinit var plugin:Man10Dice

        val prefix = "&l[&d&lM&f&la&a&ln&f&l10&5&lDice&f&l]&f".toColor()

        var radius: Int = 10
    }

    override fun onEnable() {
        // Plugin startup logic
        registerCommand()
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