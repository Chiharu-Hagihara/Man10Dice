package chiharu.hagihara.man10dice.dice

import chiharu.hagihara.man10dice.Man10Dice
import chiharu.hagihara.man10dice.Util
import chiharu.hagihara.man10dice.Util.prefix
import chiharu.hagihara.man10dice.Util.radius
import chiharu.hagihara.man10dice.Util.waittime
import net.md_5.bungee.api.ChatColor
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable

object LocalDice {
    fun localdice(p: Player, min: Int, max: Int): Int {
        val result = Util.rollDice(min, max)
        waittime = true
        p.sendMessage("${prefix} §l${p.displayName}がダイスを振っています・・・§k§lxx")
        for (players in p.getNearbyEntities(radius.toDouble(), radius.toDouble(), radius.toDouble())) {
            if (players is Player) {
                players.sendMessage("${prefix} §l${p.displayName}がダイスを振っています・・・§k§lxx")
            }
        }
        object : BukkitRunnable() {
            override fun run() {
                p.sendMessage(("${prefix} §3§l${p.displayName}§3§lは§e§l${max}§3§l面サイコロを振って${ChatColor.YELLOW}§l${result}§3§lが出た"))
                for (players in p.getNearbyEntities(radius.toDouble(), radius.toDouble(), radius.toDouble())) {
                    if (players is Player) {
                        players.sendMessage(("${prefix} §3§l${p.displayName}§3§lは§e§l${max}§3§l面サイコロを振って§e§l${result}§3§lが出た"))
                    }
                }
                waittime = false
            }
        }.runTaskLater(Man10Dice.plugin, 20 * 3)
        return result
    }
}