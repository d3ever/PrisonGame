package sexy.criss.game.prison.additional.autopickup.listeners;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.inventory.ItemStack;
import sexy.criss.game.prison.Main;
import sexy.criss.game.prison.additional.autopickup.AutoPickup;
import sexy.criss.game.prison.additional.autopickup.utils.AutoSmelt;
import sexy.criss.game.prison.boosters.SellEvent;
import sexy.criss.game.prison.boosters.Shop;
import sexy.criss.game.prison.language.Reference;

import java.util.List;

public class BlockDropItemEventListener implements Listener {

    private static final AutoPickup PLUGIN = Main.getAutoPickup;

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onDrop(BlockDropItemEvent e) {
        Player player = e.getPlayer();
        Block block = e.getBlock();

        boolean voidOnFullInv = false;
        boolean doSmelt = PLUGIN.auto_smelt_blocks.contains(player);

        Location loc = block.getLocation();
        if (AutoPickup.worldsBlacklist!=null && AutoPickup.worldsBlacklist.contains(loc.getWorld().getName())) {
            return;
        }

        if (block.getState() instanceof Container) {
            return; // Containers are handled in block break event
        }

        if (PLUGIN.autopickup_list.contains(player)) { // Player has auto enabled
            for (Entity en : e.getItems()) {

                Item i = (Item) en;
                ItemStack drop = i.getItemStack();
                if(Main.getAutoPickup.autosell_list.contains(player) && drop != null) {
                    Shop.getShop("a").sell(player, true, SellEvent.Type.AUTOSELL, drop);
                    i.remove();
                    return;
                }
                if (player.getInventory().firstEmpty() == -1) { // Checks for inventory space
                    //Player has no space
                    player.sendMessage(Reference.CLEAR_INVENTORY_NOTIFY.get());

                    if (voidOnFullInv) {
                        i.remove();
                    }

                    return;
                }

                if (doSmelt) {
                    player.getInventory().addItem(AutoSmelt.smelt(drop, player));
                } else {
                    player.getInventory().addItem(drop);
                }
                i.remove();
            }

        }

    }
}