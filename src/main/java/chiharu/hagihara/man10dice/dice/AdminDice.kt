package chiharu.hagihara.man10dice.dice

import chiharu.hagihara.man10dice.Man10Dice.Companion.plugin
import chiharu.hagihara.man10dice.Man10Dice.Companion.prefix
import chiharu.hagihara.man10dice.Util
import chiharu.hagihara.man10dice.Util.canDice
import chiharu.hagihara.man10dice.Util.rollDice
import com.github.syari.spigot.api.scheduler.runTask
import com.github.syari.spigot.api.util.string.toColor
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.*
import java.util.concurrent.ConcurrentHashMap

object AdminDice {

    var nowAD = false

    val DMap: ConcurrentHashMap<Int, UUID> = ConcurrentHashMap()
    var host: CommandSender? = null
    var hostname: String? = null
    var Dmax = 0
    var thereisWinner = false

    fun adminDice(p: Player, number: String) {

        if (!canDice(p, number)) return

        val result = rollDice(number.toInt())

        plugin.runTask(true) {

            Thread.sleep(60 *1000)

            Bukkit.broadcastMessage("${prefix}&l${p.displayName}がダイスを振っています・・・&k&lxx".toColor())

            Thread.sleep(5 *1000)

            Bukkit.broadcastMessage(("${prefix}&3&l${p.displayName}&3&lは&e&l${number}&3&l面サイコロを振って&e&l${result}&3&lが出た".toColor()))

            Thread.sleep(1 *1000)

            Bukkit.broadcastMessage("$prefix&e&l当選者を検索中です・・・".toColor())

            Thread.sleep(3 *1000)

            if (DMap.containsKey(result)) {
                val winner = DMap[result]

                if (winner != null) {
                    val winnerName: String = winner.let { Bukkit.getPlayer(it)?.displayName ?: return@runTask }
                    Bukkit.broadcastMessage("${prefix}&e&l&n${winnerName}&5&l&nはピッタリで当てました！！ｷﾀ――(ﾟ∀ﾟ)――!!".toColor())
                }

                thereisWinner = true
            }

            if (DMap.containsKey(result + 1)) {
                val winner = DMap[result + 1]

                if (winner != null) {
                    val winnerName: String? = winner.let { Bukkit.getPlayer(it)?.displayName ?: return@runTask }
                    Bukkit.broadcastMessage("${prefix}&e&l&n${winnerName}&2&lは1多い誤差で当てました！！".toColor())
                }

                thereisWinner = true
            }

            if (DMap.containsKey(result - 1)) {
                val winner = DMap[result - 1]

                if (winner != null) {
                    val winnerName: String = winner.let { Bukkit.getPlayer(it)?.displayName ?: return@runTask }
                    Bukkit.broadcastMessage("${prefix}&e&l&n${winnerName}&2&lは1少ない誤差で当てました！！".toColor())
                }

                thereisWinner = true
            }

            if (!thereisWinner) {
                Bukkit.broadcastMessage("${prefix}&7&l当選者はいませんでした。".toColor())
            }

            thereisWinner = false
            host = null
            nowAD = false
            DMap.clear()
        }
    }

    fun answerAdminDice(p: Player, number: String) {
        if (!nowAD) {
            p.sendMessage("${prefix}&c現在はAdminDiceが開催されていません。".toColor())
            return
        }

        if (p == host) {
            p.sendMessage("${prefix}&c開催者は回答できません。".toColor())
            return
        }

        if (!Util.isNumber(number)) {
            p.sendMessage("${prefix}&c数字で回答してください。".toColor())
            return
        }

        val answer = number.toInt()

        if (answer <= 0 || answer > Dmax) {
            p.sendMessage("${prefix}&c1~${Dmax}で指定してください。".toColor())
            return
        }

        if (DMap.containsKey(answer)) {
            p.sendMessage("${prefix}&c&lすでにその数字は回答されています。".toColor())
            return
        }

        if (DMap.containsValue(p.uniqueId)) {
            p.sendMessage("${prefix}&c&lあなたはすでに回答しています。".toColor())
            return
        }

        DMap[answer] = p.uniqueId
        p.sendMessage("${prefix}&e&l${answer}&a&lと回答しました。".toColor())
        host!!.sendMessage("${prefix}&e&l${p.displayName}&a&lが&e&l${answer}&a&lと回答しました。".toColor())
    }

    fun cancelAD(){
        host = null
        nowAD = false
        DMap.clear()
        Bukkit.broadcastMessage("$prefix&c&lAdminDiceがキャンセルされました。".toColor())
    }
}