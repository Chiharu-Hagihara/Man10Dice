package chiharu.hagihara.man10dice.dice

import chiharu.hagihara.man10dice.DiceFlag.isThereHasLocalDiceFlagPlayer
import chiharu.hagihara.man10dice.DiceFlag.setLocalFlag
import chiharu.hagihara.man10dice.Man10Dice.Companion.plugin
import chiharu.hagihara.man10dice.Man10Dice.Companion.radius
import chiharu.hagihara.man10dice.Util.canDice
import chiharu.hagihara.man10dice.Util.rollDice
import chiharu.hagihara.man10dice.Util.sendBroadCast
import chiharu.hagihara.man10dice.Util.sendMsg
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable

object LocalDice {

    fun localDice(p: Player, number: String) {

        if (!canDice(p, number)) return

        val players = mutableListOf<Player>()

        p.getNearbyEntities(radius.toDouble(), radius.toDouble(), radius.toDouble()).forEach {
            if (it !is Player) return@forEach
            if (it == p) return@forEach
            players.add(it)
        }

        if (isThereHasLocalDiceFlagPlayer(p)) {
            p.sendMsg("&c現在ほかのプレイヤーがダイスを振っています。")
            return
        }

        players.forEach {
            if (isThereHasLocalDiceFlagPlayer(it)) {
                p.sendMsg("&c現在ほかのプレイヤーがダイスを振っています。")
                return
            }
        }

        setLocalFlag(p, true)

        val result = rollDice(number.toInt())

        p.sendMsg("&l${p.displayName()}がダイスを振っています・・・&k&lxx")

        for (i in 0 until players.size) {
            players[i].sendMsg("&l${p.displayName()}がダイスを振っています・・・&k&lxx")
        }

        object : BukkitRunnable() {
            override fun run() {
                p.sendMsg("&3&l${p.displayName()}&3&lは&e&l${number}&3&l面サイコロを振って&e&l${result}&3&lが出た")

                    sendBroadCast("&3&l${p.displayName()}&3&lは&e&l${number}&3&l面サイコロを振って&e&l${result}&3&lが出た")


                setLocalFlag(p, false)
            }
        }.runTaskLater(plugin, 20 * 3)
    }
}