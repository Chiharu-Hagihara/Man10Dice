package chiharu.hagihara.man10dice.dice

import chiharu.hagihara.man10dice.Man10Dice.Companion.plugin
import chiharu.hagihara.man10dice.Util
import chiharu.hagihara.man10dice.Util.canDice
import chiharu.hagihara.man10dice.Util.prefix
import chiharu.hagihara.man10dice.Util.rollDice
import chiharu.hagihara.man10dice.Util.sendSuggestCommand
import com.github.syari.spigot.api.scheduler.runTask
import com.github.syari.spigot.api.string.toColor
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.*
import java.util.concurrent.ConcurrentHashMap

object AdminDice {

    var nowAD = false

    val DMap: ConcurrentHashMap<Int, UUID> = ConcurrentHashMap()
    var host: Player? = null
    var Dmax = 0
    var thereisWinner = false

    fun adminDice(p: Player, number: String) {

        if (!canDice(p, number)) return

        if (nowAD) {
            p.sendMessage("${prefix}&c現在ほかの方がAdminDiceを開催しています。".toColor())
            return
        }

        val result = rollDice(number.toInt())

        plugin.runTask(true) {

            nowAD = true
            Dmax = number.toInt()
            host = p

            if (host == null) {
                cancelAD()
                return@runTask
            }

            host!!.sendMessage("${prefix}&a${Dmax}Dを開催しました。".toColor())

            for (player in Bukkit.getOnlinePlayers()) {
                player.sendMessage("""
${prefix}${host!!.name}&d&lが&e&l${Dmax}D&d&lを開催しました！
${prefix}&a&l/mdice answer <number> で回答することができます。
                        """.trimIndent().toColor())
                sendSuggestCommand(player, "&c&l[ここをクリックで自動補完します]".toColor(), "&a&lClick me!", "/mdice answer ")
            }

            Bukkit.broadcastMessage("${prefix}&e&lAdminDice回答募集終了まで残り1分です！".toColor())

            Thread.sleep(30 * 1000)

            Bukkit.broadcastMessage("${prefix}&e&lAdminDice回答募集終了まで残り30秒です！".toColor())

            Thread.sleep(10 *1000)

            Bukkit.broadcastMessage("${prefix}&e&lAdminDice回答募集終了まで残り20秒です！".toColor())

            Thread.sleep(10 * 1000)

            Bukkit.broadcastMessage("${prefix}&e&lAdminDice回答募集終了まで残り10秒です！".toColor())

            Thread.sleep(5 * 1000)

            Bukkit.broadcastMessage("${prefix}&e&lAdminDice回答募集終了まで・・・".toColor())
            Bukkit.broadcastMessage("${prefix}&e&l  &n5".toColor())

            Thread.sleep(1000)

            Bukkit.broadcastMessage("${prefix}&e&l  &n4".toColor())

            Thread.sleep(1000)

            Bukkit.broadcastMessage("${prefix}&e&l  &n3".toColor())

            Thread.sleep(1000)

            Bukkit.broadcastMessage("${prefix}&e&l  &n2".toColor())

            Thread.sleep(1000)

            Bukkit.broadcastMessage("${prefix}&e&l  &n1".toColor())

            Thread.sleep(1000)

            Bukkit.broadcastMessage("${prefix}&e&l&n回答を締め切りました！".toColor())

            Thread.sleep(3 * 1000)

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