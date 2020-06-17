package chiharu.hagihara.man10dice

import net.md_5.bungee.api.ChatColor
import org.apache.commons.lang.math.NumberUtils
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import java.util.*


class Man10Dice : JavaPlugin() {

    val prefix = "§l[§d§lM§f§la§a§ln§f§l10§5§lDice§f§l]"
    var waittime = false

    override fun onEnable() {
        // Plugin startup logic
        getCommand("mdice")?.setExecutor(this)
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

        //globaldice
        if (cmd == "global"){
            if (!p.hasPermission("mdice.global"))return false
            if (args.size == 1)return false
            val put = args[1].toInt()
            if (waittime){
                p.sendMessage("$prefix §c§lほかの人がサイコロを振っています！")
                return false
            }
            waittime = true
            GlobalDice(p, 1, put)
            waittime = false
        }

        //localdice
        if (cmd == "local"){
            if (!p.hasPermission("mdice.local"))return false
            if (args.size == 1)return false
            val put = args[1].toInt()
            if (waittime){
                p.sendMessage("$prefix §c§lほかの人がサイコロを振っています！")
                return false
            }
            waittime = true
            LocalDice(p, 1, put)
            waittime = false
        }

        return false
    }

    //グローバルダイス
    fun GlobalDice(p: Player, min: Int, max: Int): Int{
        val result = rollDice(min, max)
        Bukkit.broadcastMessage("$prefix §l${p.displayName}がダイスを振っています・・・§k§lxx")
        onWait()
        Bukkit.broadcastMessage(("$prefix §3§l${p.displayName}§3§lは§l${ChatColor.YELLOW}§l${max}§3§l面サイコロを振って${ChatColor.YELLOW}§l${result}§3§lが出た"))
        return result
    }

    //ローカルダイス
    fun LocalDice(p: Player, min: Int, max: Int): Int{
        val result = rollDice(min, max)
        Bukkit.broadcastMessage("$prefix §l${p.displayName}がダイスを振っています・・・§k§lxx")
        onWait()
        Bukkit.broadcastMessage(("$prefix §3§l${p.displayName}§3§lは§l${ChatColor.YELLOW}§l${max}§3§l面サイコロを振って${ChatColor.YELLOW}§l${result}§3§lが出た"))
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

    var time:Long = 0
    fun onWait():Boolean{
        val now = System.currentTimeMillis()
        return if(now - time < 3000){
            false
        }else{
            time = now
            true
        }
    }

    private fun checkNumber(s: String): Boolean {
        return NumberUtils.isNumber(s)
    }

}