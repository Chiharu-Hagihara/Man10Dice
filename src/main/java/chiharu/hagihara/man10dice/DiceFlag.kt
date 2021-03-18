package chiharu.hagihara.man10dice

import chiharu.hagihara.man10dice.Man10Dice.Companion.plugin
import org.bukkit.entity.Player
import org.bukkit.metadata.FixedMetadataValue

object DiceFlag {

    fun setLocalFlag(p: Player, bool: Boolean) {
        p.setMetadata("localDice", FixedMetadataValue(plugin, bool))
    }

    fun setGlobalFlag(p: Player, bool: Boolean) {
        p.setMetadata("globalDice", FixedMetadataValue(plugin, bool))
    }

    fun isThereHasLocalDiceFlagPlayer(p: Player): Boolean {
        return p.getMetadata("localDice")[0].value() as Boolean
    }

    fun isThereHasGlobalDiceFlagPlayer(p: Player): Boolean {
        return p.getMetadata("globalDice")[0].value() as Boolean
    }

}