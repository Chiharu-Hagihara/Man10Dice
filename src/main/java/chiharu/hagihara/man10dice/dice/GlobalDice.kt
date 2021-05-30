package chiharu.hagihara.man10dice.dice

import chiharu.hagihara.man10dice.DiceFlag.isThereHasGlobalDiceFlagPlayer
import chiharu.hagihara.man10dice.DiceFlag.setGlobalFlag
import chiharu.hagihara.man10dice.Man10Dice.Companion.plugin
import chiharu.hagihara.man10dice.Util.canDice
import chiharu.hagihara.man10dice.Util.rollDice
import chiharu.hagihara.man10dice.Util.sendBroadCast
import chiharu.hagihara.man10dice.Util.sendMsg
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable

object GlobalDice {

    fun globalDice(p: Player, number: String) {

        if (!canDice(p, number)) return

        if (isThereHasGlobalDiceFlagPlayer(p)) {
            p.sendMsg("&c現在ほかのプレイヤーがダイスを振っています。")
            return
        }

        Bukkit.getServer().onlinePlayers.forEach {
            if (isThereHasGlobalDiceFlagPlayer(it)) {
                p.sendMsg("&c現在ほかのプレイヤーがダイスを振っています。")
                return
            }
        }

        setGlobalFlag(p, true)

        val result = rollDice(number.toInt())

        sendBroadCast("&l${p.displayName()}がダイスを振っています・・・&k&lxx")

        object : BukkitRunnable() {
            override fun run() {
                sendBroadCast("&3&l${p.displayName()}&3&lは&e&l${number}&3&l面サイコロを振って&e&l${result}&3&lが出た")
                setGlobalFlag(p, false)
            }
        }.runTaskLater(plugin, 20 * 3)

        return
    }
}