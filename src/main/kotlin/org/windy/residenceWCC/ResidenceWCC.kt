package org.windy.residenceWCC

import com.bekvon.bukkit.residence.Residence
import com.bekvon.bukkit.residence.protection.ClaimedResidence
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.plugin.java.JavaPlugin

class ResidenceWCC : JavaPlugin(), Listener {

    private lateinit var enabledWorlds: List<String>

    override fun onEnable() {
        // Save the default config if it does not exist
        saveDefaultConfig()

        // Load the enabled worlds from the config
        enabledWorlds = config.getStringList("enabled-worlds")

        // Register event listeners
        server.pluginManager.registerEvents(this, this)
    }

    @EventHandler
    fun onBlockBreak(event: BlockBreakEvent) {
        val player = event.player
        val block = event.block
        val location = block.location

        // 如果当前世界不在启用的世界列表中，则不进行任何操作
        if (location.world?.name !in enabledWorlds) return

        // 获取当前位置的领地
        val residence: ClaimedResidence? = Residence.getInstance().residenceManager.getByLoc(location)

        // 如果当前位置不在任何领地内，则取消破坏事件
        if (residence == null) {
            event.isCancelled = true
            player.sendMessage("你不能在此处破坏方块，因为这里不在任何领地内！")
        }
    }

    @EventHandler
    fun onBlockPlace(event: BlockPlaceEvent) {
        val player = event.player
        val block = event.block
        val location = block.location

        // 如果当前世界不在启用的世界列表中，则不进行任何操作
        if (location.world?.name !in enabledWorlds) return

        // 获取当前位置的领地
        val residence: ClaimedResidence? = Residence.getInstance().residenceManager.getByLoc(location)

        // 如果当前位置不在任何领地内，则取消建造事件
        if (residence == null) {
            event.isCancelled = true
            player.sendMessage("你不能在此处建造方块，因为这里不在任何领地内！")
        }
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}