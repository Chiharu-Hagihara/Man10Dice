package chiharu.hagihara.man10dice

import chiharu.hagihara.man10dice.Man10Dice.Companion.plugin
import chiharu.hagihara.man10dice.Man10Dice.Companion.radius
import chiharu.hagihara.man10dice.Util.config
import chiharu.hagihara.man10dice.Util.hasPerm
import chiharu.hagihara.man10dice.Util.prefix
import chiharu.hagihara.man10dice.Util.reloadConfig
import chiharu.hagihara.man10dice.Util.sendMsg
import chiharu.hagihara.man10dice.Util.showHelp
import chiharu.hagihara.man10dice.dice.AdminDice.adminDice
import chiharu.hagihara.man10dice.dice.AdminDice.answerAdminDice
import chiharu.hagihara.man10dice.dice.AdminDice.cancelAD
import chiharu.hagihara.man10dice.dice.GlobalDice.globalDice
import chiharu.hagihara.man10dice.dice.LocalDice.localDice
import com.github.syari.spigot.api.command.command
import org.bukkit.entity.Player

object Command {

    fun registerCommand() {

        plugin.command("mdice") {

            aliases = listOf("dice")

            execute {

                // プレイヤー以外からの実行を拒否
                if (sender !is Player) return@execute

                val p = sender as Player

                if (args.isNullOrEmpty()) {
                    showHelp(p)
                    return@execute
                }

                when (args[0]) {

                    "help" -> {
                        showHelp(p)
                        return@execute
                    }

                    "reload" -> {
                        if (!p.hasPerm("mdice.op")) return@execute

                        reloadConfig()

                        val config = config
                        radius = config.getInt("radius")

                        p.sendMsg("&aコンフィグを再読み込みしました。")
                        return@execute
                    }

                    "global" -> {
                        if (!p.hasPerm("mdice.global")) return@execute

                        if (args.size != 2) {
                            p.sendMsg("&c引数が誤っています。")
                            return@execute
                        }

                        globalDice(p, args[1])
                        return@execute
                    }

                    "local" -> {
                        if (!p.hasPerm("mdice.local")) return@execute

                        if (args.size != 2) {
                            p.sendMsg("&c引数が間違っています。")
                            return@execute
                        }

                        localDice(p, args[1])
                        return@execute
                    }

                    "admin" -> {
                        if (!p.hasPerm("mdice.admin")) return@execute

                        if (args.size != 2) {
                            p.sendMsg("&c引数が間違っています。")
                            return@execute
                        }

                        if (args[1] == "cancel") {
                            cancelAD()
                            p.sendMsg("&cAdminDiceをキャンセルしました。")
                            return@execute
                        }

                        adminDice(p, args[1])
                        return@execute
                    }

                    "answer" -> {
                        if (args.size != 2) {
                            p.sendMsg("${prefix}&c引数が間違っています。")
                            return@execute
                        }

                        answerAdminDice(p, args[1])
                        return@execute
                    }
                }

                showHelp(p)

            }

        }

    }

}