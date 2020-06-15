package chiharu.hagihara.man10dice

import net.md_5.bungee.api.ChatColor
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import java.util.*


class Man10Dice : JavaPlugin() {
    override fun onEnable() {
        // Plugin startup logic
        this.saveDefaultConfig()
        radius = try {
            config.getInt("radius")
        }catch (e:NullPointerException) {
            e.printStackTrace()
            50
        }
    }

    override fun onDisable() {
        // Plugin shutdown logic
        config.set("radius",radius)
        config.set("cooldown",waittime)
        this.saveConfig()
    }

    companion object{
        lateinit var plugin: Man10Dice
    }
    var radius:Int = 50
    var timer = Timer()
    var waittime = false
    val prefix = "§l[§d§lM§f§la§a§ln§f§l10§5§lDice§f§l]"
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        val p = sender as Player

        if (args.isEmpty() || args[0] == "help") {
            showHelp(p)
        } else if (args[0] == "rangeset") {
            var r: Int = 0
            try {
                r = args[1].toInt()
            } catch (e: NumberFormatException) {
                sender.sendMessage("$prefix§6エラーです。")
                return false
            }
            if (r <= 0) {
                sender.sendMessage("$prefix§c1以上で指定してください。")
                return false
            }
            plugin.radius = r
            sender.sendMessage("$prefix§a半径を${r}に設定しました。")
        }else {
            val put = args[0].toInt()
            if (put <= 0) {
                p.sendMessage(("$prefix§c１以上の数字を入力してください！"))
                return true
            } else if (!p.hasPermission("mdice.op")) {
                if (waittime) {
                    p.sendMessage(("$prefix§c現在クールタイム中です！"))
                    return true
                }
                waitstart()
            } else when (args[1]) {
                "local" -> {
                    rollLocalDice(p, 1, put, plugin)
                }
                "global" -> {
                    rollGlobalDice(p, 1, put)
                }
                else -> {
                    rollGlobalDice(p, 1, put)
                }
            }
        }
        return true
    }

    fun showHelp(p: Player) {
        p.sendMessage("$prefix §e=====ヘルプメニュー=====")
        p.sendMessage("$prefix §f/mdice [数字] : ダイスを全体チャットで通知します。")
        p.sendMessage("$prefix §f/mdice [数字] global : ダイスを全体チャットで通知します。")
        p.sendMessage("$prefix §f/mdice local [数字] : ダイスを設定された半径のなかにいるプレイヤーに通知します。")
        p.sendMessage("$prefix §e=====================")
        p.sendMessage("$prefix §fCreated By Mr_El_Capitan")
    }

    fun rollDice(min: Int, max: Int): Int {
        val r = Random()
        return r.nextInt(max - min + 1) + min
    }

    fun waitstart() {
        waittime = true
        val dtask: TimerTask = object : TimerTask() {
            override fun run() {
                waittime = false
            }
        }
        timer = Timer()
        timer.schedule(dtask, 4000)
    }

    fun rollGlobalDice(p: Player, min: Int, max: Int): Int {
        val result = rollDice(min, max)
        Bukkit.broadcastMessage((prefix + "§3§l" + p.displayName + "§3§lは§l" + ChatColor.YELLOW + "§l" + max + "§3§l面サイコロを振って" + ChatColor.YELLOW + "§l" + result + "§3§lが出た"))
        return result
    }

    fun rollLocalDice(p: Player, min: Int, max: Int, plugin: Man10Dice): Int {
        for (players in p.getNearbyEntities(plugin.radius.toDouble(), plugin.radius.toDouble(), plugin.radius.toDouble())) {
            if (players is Player) {
                val result = rollDice(min, max)
                p.sendMessage((prefix + "§3§l" + p.displayName + "§3§lは§l" + ChatColor.YELLOW + "§l" + max + "§3§l面サイコロを振って" + ChatColor.YELLOW + "§l" + result + "§3§lが出た"))
                return result
            }
        }
        return 0
    }
}


/**
 * [21:54:59 ERROR]: null
org.bukkit.command.CommandException: Unhandled exception executing command 'mdice' in plugin Man10Dice v${project.version}
at org.bukkit.command.PluginCommand.execute(PluginCommand.java:47) ~[patched_1.15.2.jar:git-Paper-126]
at org.bukkit.command.SimpleCommandMap.dispatch(SimpleCommandMap.java:159) ~[patched_1.15.2.jar:git-Paper-126]
at org.bukkit.craftbukkit.v1_15_R1.CraftServer.dispatchCommand(CraftServer.java:742) ~[patched_1.15.2.jar:git-Paper-126]
at net.minecraft.server.v1_15_R1.PlayerConnection.handleCommand(PlayerConnection.java:1825) ~[patched_1.15.2.jar:git-Paper-126]
at net.minecraft.server.v1_15_R1.PlayerConnection.a(PlayerConnection.java:1633) ~[patched_1.15.2.jar:git-Paper-126]
at net.minecraft.server.v1_15_R1.PacketPlayInChat.a(PacketPlayInChat.java:47) ~[patched_1.15.2.jar:git-Paper-126]
at net.minecraft.server.v1_15_R1.PacketPlayInChat.a(PacketPlayInChat.java:5) ~[patched_1.15.2.jar:git-Paper-126]
at net.minecraft.server.v1_15_R1.PlayerConnectionUtils.lambda$ensureMainThread$0(PlayerConnectionUtils.java:23) ~[patched_1.15.2.jar:git-Paper-126]
at net.minecraft.server.v1_15_R1.TickTask.run(SourceFile:18) ~[patched_1.15.2.jar:git-Paper-126]
at net.minecraft.server.v1_15_R1.IAsyncTaskHandler.executeTask(IAsyncTaskHandler.java:136) ~[patched_1.15.2.jar:git-Paper-126]
at net.minecraft.server.v1_15_R1.IAsyncTaskHandlerReentrant.executeTask(SourceFile:23) ~[patched_1.15.2.jar:git-Paper-126]
at net.minecraft.server.v1_15_R1.IAsyncTaskHandler.executeNext(IAsyncTaskHandler.java:109) ~[patched_1.15.2.jar:git-Paper-126]
at net.minecraft.server.v1_15_R1.MinecraftServer.ba(MinecraftServer.java:1038) ~[patched_1.15.2.jar:git-Paper-126]
at net.minecraft.server.v1_15_R1.MinecraftServer.executeNext(MinecraftServer.java:1031) ~[patched_1.15.2.jar:git-Paper-126]
at net.minecraft.server.v1_15_R1.IAsyncTaskHandler.awaitTasks(IAsyncTaskHandler.java:119) ~[patched_1.15.2.jar:git-Paper-126]
at net.minecraft.server.v1_15_R1.MinecraftServer.sleepForTick(MinecraftServer.java:1015) ~[patched_1.15.2.jar:git-Paper-126]
at net.minecraft.server.v1_15_R1.MinecraftServer.run(MinecraftServer.java:938) ~[patched_1.15.2.jar:git-Paper-126]
at java.lang.Thread.run(Unknown Source) [?:1.8.0_241]
Caused by: java.lang.ArrayIndexOutOfBoundsException: 0
at chiharu.hagihara.man10dice.Man10Dice.onCommand(Man10Dice.kt:24) ~[?:?]
at org.bukkit.command.PluginCommand.execute(PluginCommand.java:45) ~[patched_1.15.2.jar:git-Paper-126]
... 17 more

 これをヌルぽと呼んでた俺がいた。
 */
/// はっず