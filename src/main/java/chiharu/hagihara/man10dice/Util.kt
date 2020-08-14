package chiharu.hagihara.man10dice

import chiharu.hagihara.man10dice.Man10Dice.Companion.helder
import chiharu.hagihara.man10dice.Man10Dice.Companion.nowAD
import chiharu.hagihara.man10dice.Man10Dice.Companion.plugin
import chiharu.hagihara.man10dice.Man10Dice.Companion.prefix
import chiharu.hagihara.man10dice.Man10Dice.Companion.radius
import chiharu.hagihara.man10dice.Man10Dice.Companion.waittime
import chiharu.hagihara.man10dice.Man10Dice.Companion.xdNumbers
import net.md_5.bungee.api.ChatColor
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import java.util.*

class Util {
    companion object{
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
            for (players in p.getNearbyEntities(radius.toDouble(), radius.toDouble(), radius.toDouble())) {
                if (players is Player) {
                    players.sendMessage("$prefix §l${p.displayName}がダイスを振っています・・・§k§lxx")
                }
            }
            object: BukkitRunnable(){
                override fun run(){
                    p.sendMessage(("$prefix §3§l${p.displayName}§3§lは§e§l${max}§3§l面サイコロを振って${ChatColor.YELLOW}§l${result}§3§lが出た"))
                    for (players in p.getNearbyEntities(radius.toDouble(), radius.toDouble(), radius.toDouble())) {
                        if (players is Player) {
                            players.sendMessage(("$prefix §3§l${p.displayName}§3§lは§e§l${max}§3§l面サイコロを振って§e§l${result}§3§lが出た"))
                        }
                    }
                    waittime = false
                }
            }.runTaskLater(plugin,20*3)
            return result
        }

        fun AdminDice(p: Player, min: Int, max: Int): Int {
            val result = rollDice(min, max)
            Bukkit.broadcastMessage("$prefix §l${p.displayName}がダイスを振っています・・・§k§lxx")
            object : BukkitRunnable() {
                override fun run() {
                    Bukkit.broadcastMessage(("$prefix §3§l${p.displayName}§3§lは§e§l${max}§3§l面サイコロを振って§e§l${result}§3§lが出た"))

                    var winner = false

                    if (xdNumbers?.get(result - 1) != null) {
                        Bukkit.broadcastMessage(prefix + "§e§l§n" + xdNumbers!![result - 1].name + "§5§l§nはピッタリで当てました！！ｷﾀ――(ﾟ∀ﾟ)――!!")
                        winner = true
                    }

                    if (xdNumbers?.size != result && xdNumbers?.get(result) != null) {
                        Bukkit.broadcastMessage(prefix + "§e§l" + xdNumbers!![result].name + "§2§lは1多い誤差で当てました！！")
                        winner = true
                    }

                    if (result >= 2 && xdNumbers?.get(result - 2) != null) {
                        Bukkit.broadcastMessage(prefix + "§e§l" + xdNumbers!![result - 2].name + "§2§lは1少ない誤差で当てました！！")
                        winner = true
                    }

                    if (!winner) Bukkit.broadcastMessage("$prefix§7§l当選者はいませんでした。")
                    helder = null
                    nowAD = false
                }
            }.runTaskLater(plugin, 20 * 3)
            return result
        }

        //ダイスをふる
        fun rollDice(min: Int, max: Int): Int {
            val r = Random()
            return r.nextInt(max - min + 1) + min
        }


        fun showHelp(p: Player){
            p.sendMessage("$prefix §e=====ヘルプメニュー=====")
            p.sendMessage("$prefix §f/mdice local [数字] : ダイスを設定された半径のなかにいるプレイヤーに通知します。")
            p.sendMessage("$prefix §f/mdice global [数字] : ダイスを全体チャットで通知します。")
            p.sendMessage("$prefix §f/mdice admindice [数字] : みんな大好きAdminDiceです。")
            p.sendMessage("$prefix §e=====================")
            p.sendMessage("$prefix §fCreated By Mr_El_Capitan")
        }

        fun canDice(args: Array<out String>, start: Int):Boolean{
            isNumber(args[start])
            if (args[start] < 0.toString())return false
            return true
        }

        fun isNumber(target: String): Boolean {
            try {
                target.toInt()
            } catch (ex: NumberFormatException) {
                return false
            }
            return true
        }

        fun reloadConfig(){
            reloadConfig()
        }

    }
}