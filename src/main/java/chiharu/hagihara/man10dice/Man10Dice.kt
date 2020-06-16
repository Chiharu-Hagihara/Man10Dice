package chiharu.hagihara.man10dice

import net.md_5.bungee.api.ChatColor
import org.apache.commons.lang.math.NumberUtils
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import java.util.*


class Man10Dice : JavaPlugin() {
    override fun onEnable() {
        // Plugin startup logic
        this.saveDefaultConfig()
        radius = try {
            config.getInt("radius")
        }catch (e:NullPointerException) {
            e.printStackTrace()
            50
        }
        getCommand("mdice")?.setExecutor(this)
    }

    override fun onDisable() {
        // Plugin shutdown logic
        config.set("radius",radius)
        this.saveConfig()
    }

    companion object{
        lateinit var plugin: Man10Dice
    }
    var radius:Int = 50
    var timer = Timer()
    var waittime = false
    val prefix = "§l[§d§lM§f§la§a§ln§f§l10§5§lDice§f§l]"
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        val p = sender as Player

        if (args.isEmpty()) {
            showHelp(p)
            return false
        }
        when (args[0]) {
            "help" -> {
                showHelp(p)
            }
            "local" -> {
                if(!canDice(args,sender,1))return false
                if (p.hasPermission("mdice.local")) {
                    val put = args[1].toInt()
                        if (put <= 0) {
                            p.sendMessage(("$prefix§c１以上の数字を入力してください！"))
                        } else if (!p.hasPermission("mdice.op")) {
                            if (waittime) {
                                p.sendMessage(("$prefix§c現在クールタイム中です！"))
                            }
                            waitstart()
                        }
                        rollLocalDice(p, 1, put, plugin)
                } else {
                    p.sendMessage("$prefix §4§lYou don't have permission.")
                }
            }
            "global" -> {
                if(!canDice(args,sender,1))return false
                if (p.hasPermission("mdice.global")) {
                    val put = args[1].toInt()
                        if (put <= 0) {
                            p.sendMessage(("$prefix§c１以上の数字を入力してください！"))
                        } else if (!p.hasPermission("mdice.op")) {
                            if (waittime) {
                                p.sendMessage(("$prefix§c現在クールタイム中です！"))
                            }
                            waitstart()
                        }
                        rollGlobalDice(p, 1, put)
                } else {
                    p.sendMessage("$prefix §4§lYou don't have permission.")
                }
            }
            else -> {
                p.sendMessage("$prefix §c§lコマンドが間違っています！")
            }
        }
        return true
    }

    fun showHelp(p: Player) {
        if (p.hasPermission("mdice.local")|| p.hasPermission("mdice.global")|| p.hasPermission("mdice.op")) {
            p.sendMessage("$prefix §e=====ヘルプメニュー=====")
            if (p.hasPermission("mdice.local")) {
                p.sendMessage("$prefix §f/mdice local [数字] : ダイスを設定された半径のなかにいるプレイヤーに通知します。")
            }
            if (p.hasPermission("mdice.global")){
                p.sendMessage("$prefix §f/mdice global [数字] : ダイスを全体チャットで通知します。")
            }
            if (p.hasPermission("mdice.op")) {
                p.sendMessage("$prefix §f/mdice rangeset [数字] : localdiceの通知範囲を設定します。")
            }
            p.sendMessage("$prefix §e=====================")
            p.sendMessage("$prefix §fCreated By Mr_El_Capitan")
        }else{
            p.sendMessage("$prefix §4§lYou don't have permission.")
        }
    }

    fun rollDice(min: Int, max: Int): Int {
        val r = Random()
        return r.nextInt(max - min + 1) + min
    }

    fun waitstart() {
        waittime = true
        val dtask: TimerTask = object : TimerTask() {
            override fun run() {
                waittime = false
            }
        }
        timer = Timer()
        timer.schedule(dtask, 4000)
    }

    fun rollGlobalDice(p: Player, min: Int, max: Int): Int {
        val result = rollDice(min, max)
        Bukkit.broadcastMessage((prefix + "§3§l" + p.displayName + "§3§lは§l" + ChatColor.YELLOW + "§l" + max + "§3§l面サイコロを振って" + ChatColor.YELLOW + "§l" + result + "§3§lが出た"))
        return result
    }

    fun rollLocalDice(p: Player, min: Int, max: Int, plugin: Man10Dice): Int {
        for (players in p.getNearbyEntities(plugin.radius.toDouble(), plugin.radius.toDouble(), plugin.radius.toDouble())) {
            if (players is Player) {
                val result = rollDice(min, max)
                p.sendMessage((prefix + "§3§l" + p.displayName + "§3§lは§l" + ChatColor.YELLOW + "§l" + max + "§3§l面サイコロを振って" + ChatColor.YELLOW + "§l" + result + "§3§lが出た"))
                return result
            }
        }
        return 0
    }

    private fun canDice(args: Array<out String>,p: Player,start:Int):Boolean{
        val max = start + 1
        if (args.size != max) {
            p.sendMessage("$prefix §c§lコマンドが間違っています！")
            return false
        }
        //  正規表現
        if(!checkNumber(args[start])){
            p.sendMessage("$prefix §c§lコマンドが間違っています！")
            return false
        }
        if (args.isEmpty()){
            p.sendMessage("$prefix §c§lコマンドが間違っています！")
            return false
        }
        return true
    }
    private fun checkNumber(s: String): Boolean {
        return NumberUtils.isNumber(s)
    }

}

/// はっず