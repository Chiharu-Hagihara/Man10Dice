package chiharu.hagihara.man10dice

import chiharu.hagihara.man10dice.Man10Dice.Companion.plugin
import com.github.syari.spigot.api.string.toColor
import net.kyori.adventure.text.Component
import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.ComponentBuilder
import net.md_5.bungee.api.chat.HoverEvent
import net.md_5.bungee.api.chat.hover.content.Text
import org.bukkit.Bukkit
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import java.security.SecureRandom


object Util {

    val prefix = "&l[&d&lM&f&la&a&ln&f&l10&5&lDice&f&l]&f".toColor()

    val config = YamlConfiguration()


    fun rollDice(number: Int): Int {
        return SecureRandom.getInstance("NativePRNGNonBlocking").nextInt(number) + 1
    }

    fun Player.sendMsg(msg: String) {
        this.sendMessage(Component.text(prefix + msg.toColor()))
    }


    fun showHelp(player: Player) {
        player.sendMsg("&e==============ヘルプ==============")
        player.sendMsg("/mdice local [数字] : 結果を設定された半径のなかにいるプレイヤーに通知します。")
        player.sendMsg("/mdice global [数字] : 結果を全体チャットで通知します。")
        player.sendMsg("/mdice admin [数字] : Adminの振るダイスの結果を言い当てるゲームです。")
        player.sendMsg("/mdice answer [数字] : AdminDiceの回答をします。")
        player.sendMsg("/mdice admin cancel : AdminDiceをキャンセルできます。")
        player.sendMsg("/mdice reload : Configをリロードします。")
        player.sendMsg("&e=================================")
        player.sendMsg("Latest update on 2021/3/14")
        player.sendMsg("Created by Chiharu-Hagihara")
    }

    fun canDice(player: Player, number: String): Boolean {
        if (!(isNumber(number))) {
            player.sendMsg("&c数字を入力してください。")
            return false
        }

        if (number.toInt() < 0) {
            player.sendMsg("&c0より大きい数字を入力してください。")
            return false
        }

        if (number.toInt() > 2147483647) {
            player.sendMsg("&c2147483647より小さい数字を入力してください。")
            return false
        }
        return true
    }

    fun isNumber(target: String): Boolean {
        target.toIntOrNull() ?: return false
        return true
    }

    fun reloadConfig() {
        plugin.reloadConfig()
    }

    fun Player.hasPerm(perm: String): Boolean {
        return if (this.hasPermission(perm)) true

        else {
            this.sendMsg("&cあなたは権限を持っていません。")
            false
        }
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

    fun sendBroadCast(msg: String) {
        Bukkit.getOnlinePlayers().forEach {
            it.sendMsg(msg)
        }
    }
}