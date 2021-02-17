package chiharu.hagihara.man10dice.dice

import chiharu.hagihara.man10dice.Man10Dice.Companion.plugin
import chiharu.hagihara.man10dice.Util.DMap
import chiharu.hagihara.man10dice.Util.host
import chiharu.hagihara.man10dice.Util.nowAD
import chiharu.hagihara.man10dice.Util.prefix
import chiharu.hagihara.man10dice.Util.rollDice
import chiharu.hagihara.man10dice.Util.thereisWinner
import org.bukkit.Bukkit
import org.bukkit.entity.Player

object AdminDice {
    fun admindice(p: Player, min: Int, max: Int): Int {
        val result = rollDice(min, max)
        Bukkit.getScheduler().runTaskAsynchronously(plugin, Runnable {
            Thread.sleep(60 *1000)
            Bukkit.broadcastMessage("${prefix}§l${p.displayName}がダイスを振っています・・・§k§lxx")

            Thread.sleep(5 *1000)
            Bukkit.broadcastMessage(("${prefix}§3§l${p.displayName}§3§lは§e§l${max}§3§l面サイコロを振って§e§l${result}§3§lが出た"))

            Thread.sleep(1 *1000)
            Bukkit.broadcastMessage("$prefix§e§l当選者を検索中です・・・")

            Thread.sleep(3 *1000)
            if (DMap.containsKey(result)) {
                val winner = DMap[result]
                if (winner != null) {
                    val winnerName: String = winner.let { Bukkit.getPlayer(it)?.displayName ?: return@Runnable }
                    Bukkit.broadcastMessage("${prefix}§e§l§n${winnerName}§5§l§nはピッタリで当てました！！ｷﾀ――(ﾟ∀ﾟ)――!!")
                }
                thereisWinner = true
            }
            if (DMap.containsKey(result + 1)) {
                val winner = DMap[result + 1]
                if (winner != null) {
                    val winnerName: String? = winner.let { Bukkit.getPlayer(it)?.displayName ?: return@Runnable }
                    Bukkit.broadcastMessage("${prefix}§e§l§n${winnerName}§2§lは1多い誤差で当てました！！")
                }
                thereisWinner = true
            }
            if (DMap.containsKey(result - 1)) {
                val winner = DMap[result - 1]
                if (winner != null) {
                    val winnerName: String = winner.let { Bukkit.getPlayer(it)?.displayName ?: return@Runnable }
                    Bukkit.broadcastMessage("${prefix}§e§l§n${winnerName}§2§lは1少ない誤差で当てました！！")
                }
                thereisWinner = true
            }
            if (!thereisWinner) {
                Bukkit.broadcastMessage("${prefix}§7§l当選者はいませんでした。")
            }

            if (thereisWinner) thereisWinner = false
            host = null
            nowAD = false
            DMap.clear()
        })
        return result
    }

    fun cancelAD(){
        host = null
        nowAD = false
        DMap.clear()
        Bukkit.broadcastMessage("$prefix§c§lAdminDiceがキャンセルされました。")
    }
}