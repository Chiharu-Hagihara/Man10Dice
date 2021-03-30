package chiharu.hagihara.man10dice

import chiharu.hagihara.man10dice.Command.registerCommand
import com.github.syari.spigot.api.event.EventRegister.Companion.registerEvents
import org.bukkit.plugin.java.JavaPlugin


class Man10Dice : JavaPlugin() {

    companion object{
        lateinit var plugin: JavaPlugin

        var radius: Int = 10
    }

    override fun onEnable() {
        plugin = this
        registerCommand()
        registerEvents(Listener)
        saveDefaultConfig()
        val config = config
        radius = config.getInt("radius")
    }

    override fun onDisable() {
    }
}