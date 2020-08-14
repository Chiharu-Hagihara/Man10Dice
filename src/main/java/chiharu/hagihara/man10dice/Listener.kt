package chiharu.hagihara.man10dice

import chiharu.hagihara.man10dice.Man10Dice.Companion.DMap
import chiharu.hagihara.man10dice.Man10Dice.Companion.Dmax
import chiharu.hagihara.man10dice.Man10Dice.Companion.helder
import chiharu.hagihara.man10dice.Man10Dice.Companion.nowAD
import chiharu.hagihara.man10dice.Man10Dice.Companion.prefix
import chiharu.hagihara.man10dice.Util.Companion.isNumber
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent

class Listener(pl: Man10Dice) : Listener {

    init {
        pl.server.pluginManager.registerEvents(this, pl)
    }

    @EventHandler
    fun adminDiceChat(e: AsyncPlayerChatEvent): Boolean {

        if (!nowAD) return false

        if (!isNumber(e.message)) return false

        if (e.player == helder) {
            e.player.sendMessage("$prefix§c開催者は回答できません！")
            e.isCancelled = true
            return false
        }

        val answer = e.message.toInt()

        if (answer <= 0 || answer > Dmax) {
            e.player.sendMessage("${prefix}§c1~${Dmax}で指定してください！")
            e.isCancelled = true
            return false
        } else if (DMap.containsKey(answer)) {
            e.player.sendMessage("$prefix§c§lすでにその数字は言われています！")
            e.isCancelled = true
            return false
        } else if (DMap.containsValue(e.player.uniqueId)) {
            e.player.sendMessage("$prefix§a§lあなたはもう数字を言いました。")
            e.isCancelled = true
            return false
        } else {
            DMap[answer] = e.player.uniqueId
            e.player.sendMessage("$prefix§e§l$answer§a§lと回答しました！")
            helder!!.sendMessage("$prefix§e§l${e.player.name}§a§lが§e§l${answer}§a§lと回答しました。")
            e.isCancelled = true
            return true
        }
    }
}
