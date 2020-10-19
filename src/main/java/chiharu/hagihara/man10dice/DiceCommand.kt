package chiharu.hagihara.man10dice

import chiharu.hagihara.man10dice.Util.Dmax
import chiharu.hagihara.man10dice.Util.canDice
import chiharu.hagihara.man10dice.Util.config
import chiharu.hagihara.man10dice.Util.host
import chiharu.hagihara.man10dice.Util.hostname
import chiharu.hagihara.man10dice.Util.nowAD
import chiharu.hagihara.man10dice.Util.prefix
import chiharu.hagihara.man10dice.Util.radius
import chiharu.hagihara.man10dice.Util.reloadConfig
import chiharu.hagihara.man10dice.Util.showHelp
import chiharu.hagihara.man10dice.Util.waittime
import chiharu.hagihara.man10dice.dice.AdminDice.admindice
import chiharu.hagihara.man10dice.dice.AdminDice.cancelAD
import chiharu.hagihara.man10dice.dice.GlobalDice.globaldice
import chiharu.hagihara.man10dice.dice.LocalDice.localdice
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Player

object DiceCommand : CommandExecutor {
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
            showHelp(sender)
            return true
        }

        if (cmd == "reload") {
            if (!sender.hasPermission("mdice.op")) {
                sender.sendMessage("§cYou do not have permission to use this command.")
                return false
            }
            reloadConfig()
            val config = config
            radius = config.getInt("radius")
        }

        if (args.size == 1) return false

        //globaldice
        if (cmd == "global") {
            if (!sender.hasPermission("mdice.global")) {
                sender.sendMessage("§cYou do not have permission to use this command.")
                return false
            }
            if (!canDice(args, 1)) return false
            val put = args[1].toInt()
            if (waittime) {
                sender.sendMessage("$prefix §c§lほかの人がサイコロを振っています！")
                return false
            }
            globaldice(sender, 1, put)
        }

        //localdice
        if (cmd == "local") {
            if (!sender.hasPermission("mdice.local")) {
                sender.sendMessage("§cYou do not have permission to use this command.")
                return false
            }
            if (!canDice(args, 1)) return false
            val put = args[1].toInt()
            if (waittime) {
                sender.sendMessage("$prefix §c§lほかの人がサイコロを振っています！")
                return false
            }
            localdice(sender, 1, put)
        }

        //AdminDice
        if (cmd == "admindice") {
            if (!sender.hasPermission("mdice.op")) {
                sender.sendMessage("§cYou do not have permission to use this command.")
                return false
            }
            if (args[1] == "cancel") {
                cancelAD()
                return true
            }
            if (!canDice(args, 1)) return false
            if (nowAD) {
                sender.sendMessage("$prefix§c現在AdminDice中です！")
                return false
            }
            nowAD = true
            Dmax = args[1].toInt()
            host = sender
            hostname = sender.displayName
            host?.sendMessage("${prefix}§a${Dmax}Dを開始しました！")
            Bukkit.broadcastMessage("${prefix}${hostname}§d§lさんが§e§l${Dmax}D§d§lをスタートしました！§a§l(半角数字のみだけ入力してください！)")
            admindice(sender, 1, Dmax)
        }

        //AdminDice回答
        if (cmd == "answer"){
            if (!nowAD){
                sender.sendMessage("§c現在はAdminDiceが開催されていません。")
                return false
            }
        }
        return true
    }
}