package chiharu.hagihara.man10dice

import chiharu.hagihara.man10dice.Man10Dice.Companion.DMap
import chiharu.hagihara.man10dice.Man10Dice.Companion.Dmax
import chiharu.hagihara.man10dice.Man10Dice.Companion.config
import chiharu.hagihara.man10dice.Man10Dice.Companion.helder
import chiharu.hagihara.man10dice.Man10Dice.Companion.heldername
import chiharu.hagihara.man10dice.Man10Dice.Companion.nowAD
import chiharu.hagihara.man10dice.Man10Dice.Companion.prefix
import chiharu.hagihara.man10dice.Man10Dice.Companion.radius
import chiharu.hagihara.man10dice.Man10Dice.Companion.waittime
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Player

object DiceCommand : CommandExecutor {
    val util = Util()
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        //コンソールからのコマンドをキャンセル
        if (sender is ConsoleCommandSender) {
            sender.sendMessage("This command can only be executed by a player.")
            return false
        }

        if (sender !is Player) return false

        if (args.isEmpty()) return false

        val cmd = args[0]

        //ヘルプ表示
        if (cmd == "help") {
            util.showHelp(sender)
            return true
        }

        if (cmd == "reload") {
            if (!sender.hasPermission("mdice.reload")) return false
            util.reloadConfig()
            val config = config
            radius = config.getInt("radius")
        }

        if (args.size == 1) return false

        //globaldice
        if (cmd == "global") {
            if (!sender.hasPermission("mdice.global")) return false
            if (!util.canDice(args, 1)) return false
            val put = args[1].toInt()
            if (waittime) {
                sender.sendMessage("$prefix §c§lほかの人がサイコロを振っています！")
                return false
            }
            util.GlobalDice(sender, 1, put)
        }

        //localdice
        if (cmd == "local") {
            if (!sender.hasPermission("mdice.local")) return false
            if (!util.canDice(args, 1)) return false
            val put = args[1].toInt()
            if (waittime) {
                sender.sendMessage("$prefix §c§lほかの人がサイコロを振っています！")
                return false
            }
            util.LocalDice(sender, 1, put)
        }

        //AdminD
        if (cmd == "admindice") {
            if (!sender.hasPermission("mdice.op")) return false
            if (args[1] == "cancel") {
                helder = null
                nowAD = false
                DMap.clear()
                Bukkit.broadcastMessage("$prefix§c§lAdminDiceがキャンセルされました。")
                return true
            }
            if (!util.canDice(args, 1)) return false
            if (nowAD) {
                sender.sendMessage("$prefix§c現在AdminDice中です！")
                return false
            }
            Thread {
                nowAD = true
                Dmax = args[1].toInt()
                helder = sender
                heldername = sender.displayName
                helder?.sendMessage("${prefix}§a${Dmax}Dを開始しました！")
                Bukkit.broadcastMessage("${prefix}${heldername}§d§lさんが§e§l${Dmax}D§d§lをスタートしました！§a§l(半角数字のみだけ入力してください！)")
                util.AdminDice(sender, 1, Dmax)
            }.start()
        }
        return true
    }
}