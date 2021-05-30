package chiharu.hagihara.man10dice

import chiharu.hagihara.man10dice.Man10Dice.Companion.plugin
import chiharu.hagihara.man10dice.dice.AdminDice.cancelAD
import chiharu.hagihara.man10dice.dice.AdminDice.host
import chiharu.hagihara.man10dice.dice.AdminDice.nowAD
import com.github.syari.spigot.api.event.events
import org.bukkit.event.player.PlayerQuitEvent

object Listener {

    fun register() {
        plugin.events {
            event<PlayerQuitEvent> {
                if (!nowAD) return@event

                // ホストログアウト
                if (it.player == host){
                    cancelAD()
                }
            }
        }
    }
}
