package chiharu.hagihara.man10dice

import org.bukkit.Bukkit
import net.md_5.bungee.api.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender
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

        //コンソールからのコマンドをキャンセル
        if (sender is ConsoleCommandSender) {
            sender.sendMessage("This command can only be executed by a player.")
            return false
        }

        if (args.isEmpty())return false

        //ヘルプ表示
        if (cmd == "help"){
            showHelp(p)
        }

        //globaldice
        if (cmd == "global"){
            if (!p.hasPermission("mdice.global"))return false
            if (args.size == 1)return false
            val put = cmd1.toInt()
            GlobalDice(p, 1, put)
        }

        //localdice
        if (cmd == "local"){
            if (!p.hasPermission("mdice.local"))return false
            if (args.size == 1)return false
            val put = cmd1.toInt()
            LocalDice(p, 1, put)
        }

        return false
    }

    //グローバルダイス
    fun GlobalDice(p: Player, min: Int, max: Int): Int{
        val result = rollDice(min, max)
        Bukkit.broadcastMessage("$prefix §l${p.displayName}がダイスを振っています・・・§k§lxx")
        //３秒間のため
        Bukkit.broadcastMessage(("$prefix §3§l${p.displayName}§3§lは§l${ChatColor.YELLOW}§l${max}§3§l面サイコロを振って${ChatColor.YELLOW}§l${result}§3§lが出た"))
        return result
    }

    //ローカルダイス
    fun LocalDice(p: Player, min: Int, max: Int): Int{
        val result = rollDice(min, max)
        Bukkit.broadcastMessage("$prefix §l${p.displayName}がダイスを振っています・・・§k§lxx")
        //３秒間のため
        Bukkit.broadcastMessage(("$prefix §3§l${p.displayName}§3§lは§l${ChatColor.YELLOW}§l${max}§3§l面サイコロを振って${ChatColor.YELLOW}§l${result}§3§lが出た"))
        return result
    }

    //ダイスをふる
    fun rollDice(min: Int, max: Int): Int {
        val r = Random()
        return r.nextInt(max - min + 1) + min
    }


    fun showHelp(p: Player): Boolean {
        if (p.hasPermission("mdice.local") || p.hasPermission("mdice.global") || p.hasPermission("mdice.op")) return false
        p.sendMessage("$prefix §e=====ヘルプメニュー=====")
        if (p.hasPermission("mdice.local")) {
            p.sendMessage("$prefix §f/mdice local [数字] : ダイスを設定された半径のなかにいるプレイヤーに通知します。")
        }
        if (p.hasPermission("mdice.global")) {
            p.sendMessage("$prefix §f/mdice global [数字] : ダイスを全体チャットで通知します。")
        }
        if (p.hasPermission("mdice.op")) {
            p.sendMessage("$prefix §f/mdice rangeset [数字] : localdiceの通知範囲を設定します。")
        }
        p.sendMessage("$prefix §e=====================")
        p.sendMessage("$prefix §fCreated By Mr_El_Capitan")
        return true
    }
}