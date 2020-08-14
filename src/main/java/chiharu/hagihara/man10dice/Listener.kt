package chiharu.hagihara.man10dice

import chiharu.hagihara.man10dice.Man10Dice.Companion.Dmax
import chiharu.hagihara.man10dice.Man10Dice.Companion.helder
import chiharu.hagihara.man10dice.Man10Dice.Companion.nowAD
import chiharu.hagihara.man10dice.Man10Dice.Companion.prefix
import chiharu.hagihara.man10dice.Man10Dice.Companion.xdNumbers
import chiharu.hagihara.man10dice.Util.Companion.isNumber
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent

class Listener(pl: Man10Dice) : Listener {

    init {
        pl.server.pluginManager.registerEvents(this, pl)
    }

    @EventHandler
    fun adminDiceChat(e: AsyncPlayerChatEvent){
        if (!nowAD)return

        e.isCancelled = true

        if (e.player == helder){
            e.player.sendMessage("$prefix§c開催者は回答できません！")
            return
        }

        isNumber(e.message)

        val answer: Int = e.message.toInt()

        if(answer <= 0 || answer > Dmax) {
            e.player.sendMessage("${prefix}§c1~${Dmax}で指定してください！")
            return
        }

        if(xdNumbers?.get(answer - 1) != null) {
            e.player.sendMessage("$prefix§c§lすでにその数字は言われています！")
            return
        }

        if(xdNumbers?.contains(e.player)!!){
            e.player.sendMessage("$prefix§a§lあなたはもう数字を言いました。")
            return
        }

        xdNumbers!![answer - 1] = e.player
        e.player.sendMessage("$prefix§e§l$answer§a§lと回答しました！")
        helder!!.sendMessage("$prefix§e§l${e.player.name}§a§lが§e§l${answer}§a§lと回答しました。")
    }
}
