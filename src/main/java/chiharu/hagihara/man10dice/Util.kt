package chiharu.hagihara.man10dice

import chiharu.hagihara.man10dice.Man10Dice.Companion.plugin
import com.github.syari.spigot.api.string.toColor
import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.ComponentBuilder
import net.md_5.bungee.api.chat.HoverEvent
import net.md_5.bungee.api.chat.hover.content.Text
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import java.util.*


object Util {

    val prefix = "&l[&d&lM&f&la&a&ln&f&l10&5&lDice&f&l]&f".toColor()

    val config = YamlConfiguration()


    fun rollDice(number: Int): Int {
        val r = Random()
        return r.nextInt(number - 1 + 1) + 1
    }


    fun showHelp(player: Player) {
        player.sendMessage("""
${prefix}&e==============ヘルプ==============
${prefix}/mdice local [数字] : 結果を設定された半径のなかにいるプレイヤーに通知します。
${prefix}/mdice global [数字] : 結果を全体チャットで通知します。
${prefix}/mdice admin [数字] : Adminの振るダイスの結果を言い当てるゲームです。
${prefix}/mdice answer [数字] : AdminDiceの回答をします。
${prefix}/mdice admin cancel : AdminDiceをキャンセルできます。
${prefix}/mdice reload : Configをリロードします。
${prefix}&e=================================
${prefix}Latest update on 2021/3/14
${prefix}Created by Chiharu-Hagihara
        """.trimIndent().toColor())
    }

    fun canDice(player: Player, number: String): Boolean {
        if (!(isNumber(number))) {
            player.sendMessage("${prefix}&c数字を入力してください。".toColor())
            return false
        }

        if (number.toInt() < 0) {
            player.sendMessage("${prefix}&c0より大きい数字を入力してください。".toColor())
            return false
        }

        if (number.toInt() > 2147483647) {
            player.sendMessage("${prefix}&c2147483647より小さい数字を入力してください。".toColor())
            return false
        }
        return true
    }

    fun isNumber(target: String): Boolean {
        try {
            target.toInt()
        } catch (ex: NumberFormatException) {
            return false
        }
        return true
    }

    fun reloadConfig() {
        plugin.reloadConfig()
    }

    fun hasPerm(player: Player, perm: String): Boolean {
        if (player.hasPermission(perm)) {
            return true
        }

        player.sendMessage("${prefix}&cあなたは権限を持っていません。".toColor())
        return false
    }

    fun sendSuggestCommand(p: Player, text: String?, hoverText: String?, command: String?) {

        //////////////////////////////////////////
        //      ホバーテキストとイベントを作成する
        var hoverEvent: HoverEvent? = null
        if (hoverText != null) {
            val hover = Text(hoverText)
            hoverEvent = HoverEvent(HoverEvent.Action.SHOW_TEXT, hover)
        }

        //////////////////////////////////////////
        //   クリックイベントを作成する
        var clickEvent: ClickEvent? = null
        if (command != null) {
            clickEvent = ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, command)
        }
        val message = ComponentBuilder(text).event(hoverEvent).event(clickEvent).create()
        p.spigot().sendMessage(*message)
    }
}