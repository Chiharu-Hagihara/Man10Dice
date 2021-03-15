package chiharu.hagihara.man10dice.dice

import chiharu.hagihara.man10dice.Man10Dice.Companion.plugin
import chiharu.hagihara.man10dice.Man10Dice.Companion.prefix
import chiharu.hagihara.man10dice.Util.canDice
import chiharu.hagihara.man10dice.Util.rollDice
import com.github.syari.spigot.api.util.string.toColor
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable

object GlobalDice {

    var nowGD = false

    fun globalDice(p: Player, number: String) {

        if (!canDice(p, number)) return

        val result = rollDice(number.toInt())

        Bukkit.broadcastMessage("${prefix}&l${p.displayName}がダイスを振っています・・・&k&lxx".toColor())

        nowGD = true

        object : BukkitRunnable() {
            override fun run() {
                Bukkit.broadcastMessage(("${prefix}&3&l${p.displayName}&3&lは&e&l${number}&3&l面サイコロを振って&e&l${result}&3&lが出た".toColor()))
                nowGD = false
            }
        }.runTaskLater(plugin, 20 * 3)

        return
    }
}