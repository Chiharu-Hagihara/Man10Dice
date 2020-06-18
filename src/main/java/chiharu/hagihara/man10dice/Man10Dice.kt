package chiharu.hagihara.man10dice

import net.md_5.bungee.api.ChatColor
import org.apache.commons.lang.math.NumberUtils
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable
import java.util.*


class Man10Dice : JavaPlugin() {

    val prefix = "§l[§d§lM§f§la§a§ln§f§l10§5§lDice§f§l]"
    var waittime = false
    var radius:Int = 50

    companion object{
        lateinit var plugin:Man10Dice
    }

    override fun onEnable() {
        // Plugin startup logic
        getCommand("mdice")?.setExecutor(this)
        plugin = this
        saveDefaultConfig()
        val config = config
        radius = config.getInt("radius")
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        val p = sender as Player

        //コンソールからのコマンドをキャンセル
        if (sender is ConsoleCommandSender) {
            sender.sendMessage("This command can only be executed by a player.")
            return false
        }

        if (args.isEmpty())return false

        val cmd = args[0]

        //ヘルプ表示
        if (cmd == "help"){
            showHelp(p)
            return true
        }

        if (cmd == "reload"){
            if (!p.hasPermission("mdice.reload"))return false
            reloadConfig()
            val config = config
            radius = config.getInt("radius")
        }

        //globaldice
        if (cmd == "global"){
            if (!p.hasPermission("mdice.global"))return false
            if (args.size == 1)return false
            if (!canDice(args, 1))return false
            val put = args[1].toInt()
            if (put < 0)return false
            if (waittime){
                p.sendMessage("$prefix §c§lほかの人がサイコロを振っています！")
                return false
            }
            GlobalDice(p, 1, put)
        }

        //localdice
        if (cmd == "local"){
            if (!p.hasPermission("mdice.local"))return false
            if (args.size == 1)return false
            if (!canDice(args, 1))return false
            val put = args[1].toInt()
            if (put < 0)return false
            if (waittime){
                p.sendMessage("$prefix §c§lほかの人がサイコロを振っています！")
                return false
            }
            LocalDice(p, 1, put)
        }

        return false
    }

    //グローバルダイス
    fun GlobalDice(p: Player, min: Int, max: Int): Int{
        val result = rollDice(min, max)
        Bukkit.broadcastMessage("$prefix §l${p.displayName}がダイスを振っています・・・§k§lxx")
        waittime = true
        object: BukkitRunnable(){
            override fun run(){
                Bukkit.broadcastMessage(("$prefix §3§l${p.displayName}§3§lは§e§l${max}§3§l面サイコロを振って§e§l${result}§3§lが出た"))
                waittime = false
            }
        }.runTaskLater(plugin,20*3)
        return result
    }

    //ローカルダイス
    fun LocalDice(p: Player, min: Int, max: Int): Int{
        val result = rollDice(min, max)
        waittime = true
        p.sendMessage("$prefix §l${p.displayName}がダイスを振っています・・・§k§lxx")
        for (players in p.getNearbyEntities(plugin.radius.toDouble(), plugin.radius.toDouble(), plugin.radius.toDouble())) {
            if (players is Player) {
                players.sendMessage("$prefix §l${p.displayName}がダイスを振っています・・・§k§lxx")
            }
        }
        object: BukkitRunnable(){
            override fun run(){
                p.sendMessage(("$prefix §3§l${p.displayName}§3§lは§e§l${max}§3§l面サイコロを振って${ChatColor.YELLOW}§l${result}§3§lが出た"))
                for (players in p.getNearbyEntities(plugin.radius.toDouble(), plugin.radius.toDouble(), plugin.radius.toDouble())) {
                    if (players is Player) {
                        players.sendMessage(("$prefix §3§l${p.displayName}§3§lは§e§l${max}§3§l面サイコロを振って§e§l${result}§3§lが出た"))
                    }
                }
                waittime = false
            }
        }.runTaskLater(plugin,20*3)
        return result
    }

    //ダイスをふる
    fun rollDice(min: Int, max: Int): Int {
        val r = Random()
        return r.nextInt(max - min + 1) + min
    }


    fun showHelp(p: Player){
        p.sendMessage("$prefix §e=====ヘルプメニュー=====")
        if (p.hasPermission("mdice.local")) {
            p.sendMessage("$prefix §f/mdice local [数字] : ダイスを設定された半径のなかにいるプレイヤーに通知します。")
        }
        if (p.hasPermission("mdice.global")) {
            p.sendMessage("$prefix §f/mdice global [数字] : ダイスを全体チャットで通知します。")
        }
        p.sendMessage("$prefix §e=====================")
        p.sendMessage("$prefix §fCreated By Mr_El_Capitan")
    }

    fun canDice(args: Array<out String>, start: Int):Boolean{
        if(!checkNumber(args[start])) return false
        return true
    }

    fun checkNumber(s: String): Boolean {
        return NumberUtils.isNumber(s)
    }

}