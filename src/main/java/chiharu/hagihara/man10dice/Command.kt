package chiharu.hagihara.man10dice

import chiharu.hagihara.man10dice.Man10Dice.Companion.plugin
import chiharu.hagihara.man10dice.Man10Dice.Companion.radius
import chiharu.hagihara.man10dice.Util.config
import chiharu.hagihara.man10dice.Util.hasPerm
import chiharu.hagihara.man10dice.Util.prefix
import chiharu.hagihara.man10dice.Util.reloadConfig
import chiharu.hagihara.man10dice.Util.showHelp
import chiharu.hagihara.man10dice.dice.AdminDice.adminDice
import chiharu.hagihara.man10dice.dice.AdminDice.answerAdminDice
import chiharu.hagihara.man10dice.dice.AdminDice.cancelAD
import chiharu.hagihara.man10dice.dice.GlobalDice.globalDice
import chiharu.hagihara.man10dice.dice.LocalDice.localDice
import com.github.syari.spigot.api.command.command
import com.github.syari.spigot.api.string.toColor
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
                        if (!hasPerm(p, "mdice.op")) return@execute

                        reloadConfig()

                        val config = config
                        radius = config.getInt("radius")

                        p.sendMessage("${prefix}&aコンフィグを再読み込みしました。".toColor())
                        return@execute
                    }

                    "global" -> {
                        if (!hasPerm(p, "mdice.global")) return@execute

                        if (args.size != 2) {
                            p.sendMessage("${prefix}&c引数が誤っています。".toColor())
                            return@execute
                        }

                        globalDice(p, args[1])
                        return@execute
                    }

                    "local" -> {
                        if (!hasPerm(p, "mdice.local")) return@execute

                        if (args.size != 2) {
                            p.sendMessage("${prefix}&c引数が間違っています。".toColor())
                            return@execute
                        }

                        localDice(p, args[1])
                        return@execute
                    }

                    "admin" -> {
                        if (!hasPerm(p, "mdice.admin")) return@execute

                        if (args.size != 2) {
                            p.sendMessage("${prefix}&c引数が間違っています。".toColor())
                            return@execute
                        }

                        if (args[1] == "cancel") {
                            cancelAD()
                            p.sendMessage("${prefix}&cAdminDiceをキャンセルしました。".toColor())
                            return@execute
                        }

                        adminDice(p, args[1])
                        return@execute
                    }

                    "answer" -> {
                        if (args.size != 2) {
                            p.sendMessage("${prefix}&c引数が間違っています。".toColor())
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