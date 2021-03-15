package chiharu.hagihara.man10dice.dice

import chiharu.hagihara.man10dice.Man10Dice.Companion.plugin
import chiharu.hagihara.man10dice.Man10Dice.Companion.prefix
import chiharu.hagihara.man10dice.Man10Dice.Companion.radius
import chiharu.hagihara.man10dice.Util.canDice
import chiharu.hagihara.man10dice.Util.rollDice
import com.github.syari.spigot.api.util.string.toColor
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import java.util.*

object LocalDice {

    var nowLD = HashMap<UUID, Boolean>()

    fun localDice(p: Player, number: String) {

        if (!canDice(p, number)) return

        val result = rollDice(number.toInt())

        nowLD[p.uniqueId] = true

        val players = mutableListOf<Entity>()

        for (player in p.getNearbyEntities(radius.toDouble(), radius.toDouble(), radius.toDouble())) {
            if (player !is Player) return
            players.add(player)
        }

        p.sendMessage("${prefix}&l${p.displayName}がダイスを振っています・・・&k&lxx".toColor())

        for (i in 0 until players.size) {
            players[i].sendMessage("${prefix}&l${p.displayName}がダイスを振っています・・・&k&lxx".toColor())
        }

        object : BukkitRunnable() {
            override fun run() {
                p.sendMessage(("${prefix}&3&l${p.displayName}&3&lは&e&l${number}&3&l面サイコロを振って&e&l${result}&3&lが出た".toColor()))

                for (i in 0 until players.size) {
                    players[i].sendMessage(("${prefix}&3&l${p.displayName}&3&lは&e&l${number}&3&l面サイコロを振って&e&l${result}&3&lが出た".toColor()))
                }

                nowLD.remove(p.uniqueId)
            }
        }.runTaskLater(plugin, 20 * 3)
    }
}