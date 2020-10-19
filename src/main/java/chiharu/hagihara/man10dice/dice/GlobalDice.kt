package chiharu.hagihara.man10dice.dice

import chiharu.hagihara.man10dice.Man10Dice
import chiharu.hagihara.man10dice.Util
import chiharu.hagihara.man10dice.Util.prefix
import chiharu.hagihara.man10dice.Util.waittime
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable

object GlobalDice {
    fun globaldice(p: Player, min: Int, max: Int): Int {
        val result = Util.rollDice(min, max)
        Bukkit.broadcastMessage("${prefix}§l${p.displayName}がダイスを振っています・・・§k§lxx")
        waittime = true
        object : BukkitRunnable() {
            override fun run() {
                Bukkit.broadcastMessage(("${prefix}§3§l${p.displayName}§3§lは§e§l${max}§3§l面サイコロを振って§e§l${result}§3§lが出た"))
                waittime = false
            }
        }.runTaskLater(Man10Dice.plugin, 20 * 3)
        return result
    }
}