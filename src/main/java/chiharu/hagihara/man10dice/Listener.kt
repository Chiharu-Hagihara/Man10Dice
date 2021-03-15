package chiharu.hagihara.man10dice

import chiharu.hagihara.man10dice.dice.AdminDice.cancelAD
import chiharu.hagihara.man10dice.dice.AdminDice.host
import chiharu.hagihara.man10dice.dice.AdminDice.nowAD
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent

class Listener(pl: Man10Dice) : Listener {

    init {
        pl.server.pluginManager.registerEvents(this, pl)
    }

    @EventHandler
    fun onLogout(e: PlayerQuitEvent){
        if (!nowAD) return

        // ホストログアウト
        if (e.player == host){
            cancelAD()
        }
    }
}
