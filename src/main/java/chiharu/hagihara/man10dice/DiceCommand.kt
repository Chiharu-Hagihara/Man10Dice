package chiharu.hagihara.man10dice

import chiharu.hagihara.man10dice.Util.DMap
import chiharu.hagihara.man10dice.Util.Dmax
import chiharu.hagihara.man10dice.Util.canDice
import chiharu.hagihara.man10dice.Util.config
import chiharu.hagihara.man10dice.Util.host
import chiharu.hagihara.man10dice.Util.hostname
import chiharu.hagihara.man10dice.Util.isNumber
import chiharu.hagihara.man10dice.Util.nowAD
import chiharu.hagihara.man10dice.Util.nowGD
import chiharu.hagihara.man10dice.Util.nowLD
import chiharu.hagihara.man10dice.Util.prefix
import chiharu.hagihara.man10dice.Util.radius
import chiharu.hagihara.man10dice.Util.reloadConfig
import chiharu.hagihara.man10dice.Util.sendSuggestCommand
import chiharu.hagihara.man10dice.Util.showHelp
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

        if (args.isEmpty()) {
            showHelp(sender)
            return true
        }

        val cmd = args[0]

        //ヘルプ表示
        if (cmd == "help") {
            showHelp(sender)
            return true
        }

        if (cmd == "reload") {
            if (!sender.hasPermission("mdice.op")) {
                sender.sendMessage("$prefix§cYou do not have permission to use this command.")
                return false
            }
            reloadConfig()
            val config = config
            radius = config.getInt("radius")
        }

        if (args.size == 1) return false

        //////////////////////////
        // GlobalDice
        ////////////////
        if (cmd == "global") {
            if (!sender.hasPermission("mdice.global")) {
                sender.sendMessage("$prefix§cYou do not have permission to use this command.")
                return false
            }
            if (!canDice(args, 1)) return false
            val put = args[1].toInt()
            if (nowGD) {
                sender.sendMessage("$prefix§c§lほかの人がサイコロを振っています！")
                return false
            }
            globaldice(sender, 1, put)
        }

        /////////////////////////
        // LocalDice
        /////////////
        if (cmd == "local") {
            if (!sender.hasPermission("mdice.local")) {
                sender.sendMessage("$prefix§cYou do not have permission to use this command.")
                return false
            }
            if (!canDice(args, 1)) return false
            val put = args[1].toInt()
            if (nowLD.containsKey(sender.uniqueId)){
                sender.sendMessage("$prefix§c§l同時に2個以上サイコロを振れません！")
                return false
            }
            for (players in sender.getNearbyEntities(radius.toDouble(), radius.toDouble(), radius.toDouble())) {
                if (players is Player) {
                    if (nowLD.containsKey(players.uniqueId)){
                        sender.sendMessage("$prefix§c§lほかの人がサイコロを振っています！")
                        return false
                    }
                }
            }
            nowLD[sender.uniqueId] = true
            localdice(sender, 1, put)
            nowLD.remove(sender.uniqueId)
        }

        //////////////////////
        // AdminDice
        /////////////
        if (cmd == "admindice") {
            if (!sender.hasPermission("mdice.op")) {
                sender.sendMessage("$prefix§cYou do not have permission to use this command.")
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
            for (player in Bukkit.getOnlinePlayers()) {
                player.sendMessage("${prefix}${hostname}§d§lさんが§e§l${Dmax}D§d§lをスタートしました！")
                player.sendMessage("$prefix§a§l/mdice answer <数字> で回答することができます。")
                sendSuggestCommand(player, "§e§l[ここをクリックで自動補完します]", "§a§lClick!", "/mdice answer ")
            }
            admindice(sender, 1, Dmax)
        }

        ///////////////////////
        // AdminDice回答
        ///////////////
        if (cmd == "answer"){
            if (!nowAD){
                sender.sendMessage("$prefix§c現在はAdminDiceが開催されていません。")
                return false
            }

            if (sender == host){
                sender.sendMessage("$prefix§c開催者は回答できません。")
                return false
            }

            if (!isNumber(args[1])){
                sender.sendMessage("$prefix§c数字で回答してください。")
                return false
            }

            val answer = args[1].toInt()

            if (answer <= 0 || answer > Dmax){
                sender.sendMessage("${prefix}§c1~${Dmax}で指定してください！")
                return false
            }

            if (DMap.containsKey(answer)){
                sender.sendMessage("$prefix§c§lすでにその数字は言われています！")
                return false
            }

            if (DMap.containsValue(sender.uniqueId)){
                sender.sendMessage("$prefix§a§lあなたはもう数字を言いました。")
                return false
            }

            DMap[answer] = sender.uniqueId
            sender.sendMessage("$prefix§e§l$answer§a§lと回答しました！")
            host!!.sendMessage("$prefix§e§l${sender.name}§a§lが§e§l${answer}§a§lと回答しました。")
            return true
        }
        return true
    }
}