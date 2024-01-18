package pl.brawlsmons.brawlseqdrops;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Random;
import java.util.logging.Logger;

public final class BrawlsEQDrops extends JavaPlugin implements Listener {

    private double dropPercentage;
    private Logger log;

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();
        dropPercentage = getConfig().getDouble("dropPercentage", 0.5);
        getServer().getPluginManager().registerEvents(this, this);

        log = this.getLogger();
        log.info("Plugin has been enabled");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        log.info("Plugin has been disabled");
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        // When a player dies, this method will be called
        Player player = event.getEntity();
        if (!player.isOnline()) {
            return;
        }

        synchronized (player.getInventory()) {
            int itemCount = player.getInventory().getSize();
            int itemsToDrop = (int) (itemCount * dropPercentage);

            Random random = new Random();
            for (int i = 0; i < itemsToDrop; i++) {
                int itemIndex = random.nextInt(itemCount);
                ItemStack item = player.getInventory().getItem(itemIndex);
                if (item != null) {
                    try {
                        player.getWorld().dropItemNaturally(player.getLocation(), item);
                        player.getInventory().clear(itemIndex);
                    } catch (Exception e) {
                        log.warning("Failed to drop item: " + e.getMessage());
                    }
                }
            }
        }
    }
}
