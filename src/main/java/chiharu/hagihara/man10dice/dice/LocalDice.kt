package chiharu.hagihara.man10dice.dice

import chiharu.hagihara.man10dice.Man10Dice.Companion.plugin
import chiharu.hagihara.man10dice.Util.nowLD
import chiharu.hagihara.man10dice.Util.prefix
import chiharu.hagihara.man10dice.Util.radius
import chiharu.hagihara.man10dice.Util.rollDice
import net.md_5.bungee.api.ChatColor
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable

object LocalDice {
    fun localdice(p: Player, min: Int, max: Int): Int {
        val result = rollDice(min, max)
        nowLD[p.uniqueId] = true
        p.sendMessage("${prefix}§l${p.displayName}がダイスを振っています・・・§k§lxx")
        for (players in p.getNearbyEntities(radius.toDouble(), radius.toDouble(), radius.toDouble())) {
            if (players is Player) {
                players.sendMessage("${prefix}§l${p.displayName}がダイスを振っています・・・§k§lxx")
            }
        }
        object : BukkitRunnable() {
            override fun run() {
                p.sendMessage(("${prefix}§3§l${p.displayName}§3§lは§e§l${max}§3§l面サイコロを振って${ChatColor.YELLOW}§l${result}§3§lが出た"))
                for (players in p.getNearbyEntities(radius.toDouble(), radius.toDouble(), radius.toDouble())) {
                    if (players is Player) {
                        players.sendMessage(("${prefix}§3§l${p.displayName}§3§lは§e§l${max}§3§l面サイコロを振って§e§l${result}§3§lが出た"))
                    }
                }
                nowLD.remove(p.uniqueId)
            }
        }.runTaskLater(plugin, 20 * 3)
        return result
    }
}