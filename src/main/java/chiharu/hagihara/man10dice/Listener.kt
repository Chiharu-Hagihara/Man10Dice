package chiharu.hagihara.man10dice

import chiharu.hagihara.man10dice.dice.AdminDice.cancelAD
import chiharu.hagihara.man10dice.dice.AdminDice.host
import chiharu.hagihara.man10dice.dice.AdminDice.nowAD
import com.github.syari.spigot.api.event.EventRegister
import com.github.syari.spigot.api.event.Events
import org.bukkit.event.player.PlayerQuitEvent

object Listener : EventRegister {

    override fun Events.register() {

        event<PlayerQuitEvent> {
            if (!nowAD) return@event

            // ホストログアウト
            if (it.player == host){
                cancelAD()
            }
        }

    }
}
