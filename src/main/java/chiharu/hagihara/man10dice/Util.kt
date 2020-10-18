package chiharu.hagihara.man10dice

import chiharu.hagihara.man10dice.Man10Dice.Companion.DMap
import chiharu.hagihara.man10dice.Man10Dice.Companion.helder
import chiharu.hagihara.man10dice.Man10Dice.Companion.nowAD
import chiharu.hagihara.man10dice.Man10Dice.Companion.plugin
import chiharu.hagihara.man10dice.Man10Dice.Companion.prefix
import chiharu.hagihara.man10dice.Man10Dice.Companion.radius
import chiharu.hagihara.man10dice.Man10Dice.Companion.thereisWinner
import chiharu.hagihara.man10dice.Man10Dice.Companion.waittime
import net.md_5.bungee.api.ChatColor
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import java.util.*

class Util {
    //グローバルダイス
    fun GlobalDice(p: Player, min: Int, max: Int): Int {
        val result = rollDice(min, max)
        Bukkit.broadcastMessage("$prefix §l${p.displayName}がダイスを振っています・・・§k§lxx")
        waittime = true
        object : BukkitRunnable() {
            override fun run() {
                Bukkit.broadcastMessage(("$prefix §3§l${p.displayName}§3§lは§e§l${max}§3§l面サイコロを振って§e§l${result}§3§lが出た"))
                waittime = false
            }
        }.runTaskLater(plugin, 20 * 3)
        return result
    }

    //ローカルダイス
    fun LocalDice(p: Player, min: Int, max: Int): Int {
        val result = rollDice(min, max)
        waittime = true
        p.sendMessage("$prefix §l${p.displayName}がダイスを振っています・・・§k§lxx")
        for (players in p.getNearbyEntities(radius.toDouble(), radius.toDouble(), radius.toDouble())) {
            if (players is Player) {
                players.sendMessage("$prefix §l${p.displayName}がダイスを振っています・・・§k§lxx")
            }
        }
        object : BukkitRunnable() {
            override fun run() {
                p.sendMessage(("$prefix §3§l${p.displayName}§3§lは§e§l${max}§3§l面サイコロを振って${ChatColor.YELLOW}§l${result}§3§lが出た"))
                for (players in p.getNearbyEntities(radius.toDouble(), radius.toDouble(), radius.toDouble())) {
                    if (players is Player) {
                        players.sendMessage(("$prefix §3§l${p.displayName}§3§lは§e§l${max}§3§l面サイコロを振って§e§l${result}§3§lが出た"))
                    }
                }
                waittime = false
            }
        }.runTaskLater(plugin, 20 * 3)
        return result
    }

    fun AdminDice(p: Player, min: Int, max: Int): Int {
        val result = rollDice(min, max)
        Bukkit.broadcastMessage("$prefix §l${p.displayName}がダイスを振っています・・・§k§lxx")
        object : BukkitRunnable() {
            override fun run() {
                Bukkit.broadcastMessage(("$prefix §3§l${p.displayName}§3§lは§e§l${max}§3§l面サイコロを振って§e§l${result}§3§lが出た"))

                if (DMap.containsKey(result)) {
                    val winner = DMap[result]
                    if (winner != null) {
                        val winnerName: String = winner.let { Bukkit.getPlayer(it)?.displayName ?: return }
                        Bukkit.broadcastMessage("${prefix}§e§l§n${winnerName}§5§l§nはピッタリで当てました！！ｷﾀ――(ﾟ∀ﾟ)――!!")
                    }
                    thereisWinner = true
                }
                if (DMap.containsKey(result + 1)) {
                    val winner = DMap[result + 1]
                    if (winner != null) {
                        val winnerName: String? = winner.let { Bukkit.getPlayer(it)?.displayName ?: return }
                        Bukkit.broadcastMessage("${prefix}§e§l§n${winnerName}§2§lは1多い誤差で当てました！！")
                    }
                    thereisWinner = true
                }
                if (DMap.containsKey(result - 1)) {
                    val winner = DMap[result - 1]
                    if (winner != null) {
                        val winnerName: String = winner.let { Bukkit.getPlayer(it)?.displayName ?: return }
                        Bukkit.broadcastMessage("${prefix}§e§l§n${winnerName}§2§lは1少ない誤差で当てました！！")
                    }
                    thereisWinner = true
                }
                if (!thereisWinner) {
                    Bukkit.broadcastMessage("$prefix§7§l当選者はいませんでした。")
                }

                if (thereisWinner) thereisWinner = false
                helder = null
                nowAD = false
                DMap.clear()
            }
        }.runTaskLater(plugin, 20 * 3)
        return result
    }

    //ダイスをふる
    fun rollDice(min: Int, max: Int): Int {
        val r = Random()
        return r.nextInt(max - min + 1) + min
    }


    fun showHelp(p: Player) {
        p.sendMessage("$prefix §e=====ヘルプメニュー=====")
        p.sendMessage("$prefix §f/mdice local [数字] : ダイスを設定された半径のなかにいるプレイヤーに通知します。")
        p.sendMessage("$prefix §f/mdice global [数字] : ダイスを全体チャットで通知します。")
        p.sendMessage("$prefix §f/mdice admindice [数字] : みんな大好きAdminDiceです。")
        p.sendMessage("$prefix §f/mdice admindice cancel : AdminDiceをキャンセルできます。")
        p.sendMessage("$prefix §e=====================")
        p.sendMessage("$prefix §fVersion: 2.2")
        p.sendMessage("$prefix §fCreated By MEC11")
    }

    fun canDice(args: Array<out String>, start: Int): Boolean {
        isNumber(args[start])
        if (args[start].toInt() < 0) return false
        if (args[start].toInt() > 2147483647) return false
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

    fun reloadConfig() {
        reloadConfig()
    }
}