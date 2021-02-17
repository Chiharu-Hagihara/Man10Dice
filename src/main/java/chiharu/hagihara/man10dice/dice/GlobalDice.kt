package chiharu.hagihara.man10dice.dice

import chiharu.hagihara.man10dice.Man10Dice.Companion.plugin
import chiharu.hagihara.man10dice.Util.nowGD
import chiharu.hagihara.man10dice.Util.prefix
import chiharu.hagihara.man10dice.Util.rollDice
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable

object GlobalDice {
    fun globaldice(p: Player, min: Int, max: Int): Int {
        val result = rollDice(min, max)
        Bukkit.broadcastMessage("${prefix}§l${p.displayName}がダイスを振っています・・・§k§lxx")
        nowGD = true
        object : BukkitRunnable() {
            override fun run() {
                Bukkit.broadcastMessage(("${prefix}§3§l${p.displayName}§3§lは§e§l${max}§3§l面サイコロを振って§e§l${result}§3§lが出た"))
                nowGD = false
            }
        }.runTaskLater(plugin, 20 * 3)
        return result
    }
}