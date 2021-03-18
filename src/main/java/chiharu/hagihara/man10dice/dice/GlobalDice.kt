package chiharu.hagihara.man10dice.dice

import chiharu.hagihara.man10dice.DiceFlag.isThereHasGlobalDiceFlagPlayer
import chiharu.hagihara.man10dice.DiceFlag.setGlobalFlag
import chiharu.hagihara.man10dice.Man10Dice.Companion.plugin
import chiharu.hagihara.man10dice.Util.canDice
import chiharu.hagihara.man10dice.Util.prefix
import chiharu.hagihara.man10dice.Util.rollDice
import com.github.syari.spigot.api.string.toColor
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable

object GlobalDice {

    fun globalDice(p: Player, number: String) {

        if (!canDice(p, number)) return

        val result = rollDice(number.toInt())

        Bukkit.broadcastMessage("${prefix}&l${p.displayName}がダイスを振っています・・・&k&lxx".toColor())

        Bukkit.getServer().onlinePlayers.forEach {
            if (isThereHasGlobalDiceFlagPlayer(it)) {
                p.sendMessage("${prefix}&c現在ほかのプレイヤーがダイスを振っています。")
                return
            }
        }

        setGlobalFlag(p, true)

        object : BukkitRunnable() {
            override fun run() {
                Bukkit.broadcastMessage(("${prefix}&3&l${p.displayName}&3&lは&e&l${number}&3&l面サイコロを振って&e&l${result}&3&lが出た".toColor()))
                setGlobalFlag(p, false)
            }
        }.runTaskLater(plugin, 20 * 3)

        return
    }
}