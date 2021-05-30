package chiharu.hagihara.man10dice.dice

import chiharu.hagihara.man10dice.Man10Dice.Companion.plugin
import chiharu.hagihara.man10dice.Util
import chiharu.hagihara.man10dice.Util.canDice
import chiharu.hagihara.man10dice.Util.prefix
import chiharu.hagihara.man10dice.Util.rollDice
import chiharu.hagihara.man10dice.Util.sendBroadCast
import chiharu.hagihara.man10dice.Util.sendMsg
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
            p.sendMsg("&c現在ほかの方がAdminDiceを開催しています。")
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

            host!!.sendMsg("&a${Dmax}Dを開催しました。")

            for (player in Bukkit.getOnlinePlayers()) {
                player.sendMsg("${host!!.name}&d&lが&e&l${Dmax}D&d&lを開催しました！")
                player.sendMsg("&a&l/mdice answer <number> で回答することができます。")
                sendSuggestCommand(player, "&c&l[ここをクリックで自動補完します]".toColor(), "&a&lClick me!", "/mdice answer ")
            }

            sendBroadCast("&e&lAdminDice回答募集終了まで残り1分です！")

            Thread.sleep(30 * 1000)

            sendBroadCast("&e&lAdminDice回答募集終了まで残り30秒です！")

            Thread.sleep(10 *1000)

            sendBroadCast("&e&lAdminDice回答募集終了まで残り20秒です！")

            Thread.sleep(10 * 1000)

            sendBroadCast("&e&lAdminDice回答募集終了まで残り10秒です！")

            Thread.sleep(5 * 1000)

            sendBroadCast("&e&lAdminDice回答募集終了まで・・・")
            sendBroadCast("&e&l  &n5")

            Thread.sleep(1000)

            sendBroadCast("&e&l  &n4")

            Thread.sleep(1000)

            sendBroadCast("&e&l  &n3")

            Thread.sleep(1000)

            sendBroadCast("&e&l  &n2")

            Thread.sleep(1000)

            sendBroadCast("&e&l  &n1")

            Thread.sleep(1000)

            sendBroadCast("${prefix}&e&l&n回答を締め切りました！")

            Thread.sleep(3 * 1000)
            
            sendBroadCast("&l${p.displayName()}がダイスを振っています・・・&k&lxx")
            
            Thread.sleep(5 *1000)

            sendBroadCast("&3&l${p.displayName()}&3&lは&e&l${number}&3&l面サイコロを振って&e&l${result}&3&lが出た")

            Thread.sleep(1 *1000)

            sendBroadCast("&e&l当選者を検索中です・・・")

            Thread.sleep(3 *1000)

            if (DMap.containsKey(result)) {
                val winner = DMap[result]

                if (winner != null) {
                    // winner.let { Bukkit.getPlayer(it)?.displayName() ?: return@runTask }
                    val winnerName: String = Bukkit.getPlayer(winner)?.displayName().toString()
                    sendBroadCast("&e&l&n${winnerName}&5&l&nはピッタリで当てました！！ｷﾀ――(ﾟ∀ﾟ)――!!")
                }

                thereisWinner = true
            }

            if (DMap.containsKey(result + 1)) {
                val winner = DMap[result + 1]

                if (winner != null) {
                    val winnerName: String = Bukkit.getPlayer(winner)?.displayName().toString()
                    sendBroadCast("&e&l&n${winnerName}&2&lは1多い誤差で当てました！！")
                }

                thereisWinner = true
            }

            if (DMap.containsKey(result - 1)) {
                val winner = DMap[result - 1]

                if (winner != null) {
                    val winnerName: String = Bukkit.getPlayer(winner)?.displayName().toString()
                    sendBroadCast("&e&l&n${winnerName}&2&lは1少ない誤差で当てました！！")
                }

                thereisWinner = true
            }

            if (!thereisWinner) {
                sendBroadCast("&7&l当選者はいませんでした。")
            }

            thereisWinner = false
            host = null
            nowAD = false
            DMap.clear()
        }
    }

    fun answerAdminDice(p: Player, number: String) {
        if (!nowAD) {
            p.sendMsg("&c現在はAdminDiceが開催されていません。")
            return
        }

        if (p == host) {
            p.sendMsg("&c開催者は回答できません。")
            return
        }

        if (!Util.isNumber(number)) {
            p.sendMsg("&c数字で回答してください。")
            return
        }

        val answer = number.toInt()

        if (answer <= 0 || answer > Dmax) {
            p.sendMsg("&c1~${Dmax}で指定してください。")
            return
        }

        if (DMap.containsKey(answer)) {
            p.sendMsg("&c&lすでにその数字は回答されています。")
            return
        }

        if (DMap.containsValue(p.uniqueId)) {
            p.sendMsg("&c&lあなたはすでに回答しています。")
            return
        }

        DMap[answer] = p.uniqueId
        p.sendMsg("&e&l${answer}&a&lと回答しました。")
        host!!.sendMsg("&e&l${p.displayName()}&a&lが&e&l${answer}&a&lと回答しました。")
    }

    fun cancelAD(){
        host = null
        nowAD = false
        DMap.clear()
        sendBroadCast("&c&lAdminDiceがキャンセルされました。")
    }
}