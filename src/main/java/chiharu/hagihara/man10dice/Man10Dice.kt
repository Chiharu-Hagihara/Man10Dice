package chiharu.hagihara.man10dice

import org.bukkit.Bukkit
import net.md_5.bungee.api.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import java.util.*


class Man10Dice : JavaPlugin() {

    val prefix = "§l[§d§lM§f§la§a§ln§f§l10§5§lDice§f§l]"

    override fun onEnable() {
        // Plugin startup logic
        getCommand("mdice")?.setExecutor(this)
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        val p = sender as Player
        val cmd = args[0]
        val cmd1 = args[1]

        if (args.isEmpty()){
            showHelp(p)
            return false
        }

        if (cmd == "help"){
            showHelp(p)
            return true
        }

        if (cmd == "global"){
            if (!p.hasPermission("mdice.global"))return false
            if (args.size == 1)return false
            val put = cmd1.toInt()
            GlobalDice(p, 1, put)
        }

        return false
    }

    fun GlobalDice(p: Player, min: Int, max: Int): Int{
        val result = rollDice(min, max)
        Bukkit.broadcastMessage("$prefix §l${p.displayName}がダイスを振っています・・・§k§lxx")
        Bukkit.broadcastMessage(("$prefix §3§l${p.displayName}§3§lは§l${ChatColor.YELLOW}§l${max}§3§l面サイコロを振って${ChatColor.YELLOW}§l${result}§3§lが出た"))
        return result
    }

    fun rollDice(min: Int, max: Int): Int {
        val r = Random()
        return r.nextInt(max - min + 1) + min
    }


    fun showHelp(p: Player){

    }
}