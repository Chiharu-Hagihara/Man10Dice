package chiharu.hagihara.man10dice

import chiharu.hagihara.man10dice.Man10Dice.Companion.plugin
import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.ComponentBuilder
import net.md_5.bungee.api.chat.HoverEvent
import net.md_5.bungee.api.chat.hover.content.Text
import org.bukkit.command.CommandSender
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.metadata.FixedMetadataValue
import java.util.*
import java.util.concurrent.ConcurrentHashMap

object Util {

    val prefix = "§l[§d§lM§f§la§a§ln§f§l10§5§lDice§f§l]"

    val config = YamlConfiguration()

    var nowGD = false

    var nowAD = false
    val DMap: ConcurrentHashMap<Int, UUID> = ConcurrentHashMap()
    var host: CommandSender? = null
    var hostname: String? = null
    var Dmax = 0
    var thereisWinner = false

    var radius: Int = 10

    fun rollDice(min: Int, max: Int): Int {
        val r = Random()
        return r.nextInt(max - min + 1) + min
    }


    fun showHelp(p: Player) {
        p.sendMessage("$prefix §e=====ヘルプメニュー=====")
        p.sendMessage("$prefix §f/mdice local [数字] : ダイスを設定された半径のなかにいるプレイヤーに通知します。")
        p.sendMessage("$prefix §f/mdice global [数字] : ダイスを全体チャットで通知します。")
        p.sendMessage("$prefix §f/mdice admindice [数字] : みんな大好きAdminDiceです。")
        p.sendMessage("$prefix §f/mdice answer [数字] : AdminDiceの回答をします。")
        p.sendMessage("$prefix §f/mdice admindice cancel : AdminDiceをキャンセルできます。")
        p.sendMessage("$prefix §f/mdice reload : Configをリロードします。")
        p.sendMessage("$prefix §e=====================")
        p.sendMessage("$prefix §fVersion: 3.2.3")
        p.sendMessage("$prefix §fCreated By MEC11")
    }

    fun canDice(args: Array<out String>, start: Int): Boolean {
        isNumber(args[start])
        if (args[start].toInt() < 0) return false
        if (args[start].toInt() > 2147483647) return false
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
        reloadConfig()
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

    fun flagset(player: Player) {
        player.setMetadata("nowLD", FixedMetadataValue(plugin, true))
    }

    fun flagunset(player: Player) {
        player.setMetadata("nowLD", FixedMetadataValue(plugin, false))
    }

    fun flagget(player: Player): Boolean {
        return try {
            player.getMetadata("nowLD")[0].value() as Boolean
        } catch (e: Exception) {
            false
        }
    }
}