package sexy.criss.game.prison.additional.autopickup.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.DoubleChestInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import sexy.criss.game.prison.Main;
import sexy.criss.game.prison.additional.autopickup.AutoPickup;
import sexy.criss.game.prison.additional.autopickup.utils.Mendable;
import sexy.criss.game.prison.additional.autopickup.utils.PickupObjective;
import sexy.criss.game.prison.additional.autopickup.utils.TallCrops;
import sexy.criss.game.prison.boosters.SellEvent;
import sexy.criss.game.prison.boosters.Shop;
import sexy.criss.game.prison.language.Reference;
import sexy.criss.gen.util.Util;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class BlockBreakEventListener implements Listener {

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBreak(BlockBreakEvent e) {
        Block block = e.getBlock();
        Player player = e.getPlayer();
        Location loc = e.getBlock().getLocation();

        if (AutoPickup.worldsBlacklist!=null && AutoPickup.worldsBlacklist.contains(loc.getWorld().getName())) return;

        if (player.getInventory().firstEmpty() == -1) {
            player.sendTitle(Util.f("&6Инвентарь"), Util.f("&6полностью заполнен"));
        }

        int xp = e.getExpToDrop();
        player.giveExp(xp);

        mend(player.getInventory().getItemInMainHand(), xp);
        mend(player.getInventory().getItemInOffHand(), xp);
        ItemStack armor[] = player.getInventory().getArmorContents();
        for (ItemStack i : armor)
            try { mend(i, xp);
            } catch (NullPointerException ignored) {}

        e.setExpToDrop(0);

        String key = loc.getBlockX()+";"+loc.getBlockY()+";"+loc.getBlockZ()+";"+loc.getWorld();
        AutoPickup.customItemPatch.put(key, new PickupObjective(loc, player, Instant.now()));

        if (block.getState() instanceof Container) {
            if (block.getState() instanceof ShulkerBox) return;

            e.setDropItems(false);

            if (((Container) block.getState()).getInventory() instanceof DoubleChestInventory) {
                Chest chest = (Chest) block.getState();
                org.bukkit.block.data.type.Chest chestType = (org.bukkit.block.data.type.Chest) chest.getBlockData();
                ArrayList<ItemStack> chestDrops = new ArrayList<>();
                if (chestType.getType().equals(org.bukkit.block.data.type.Chest.Type.RIGHT)) { // Right
                    for (int x=0; x<27; x++) {
                        chestDrops.add(chest.getInventory().getItem(x));
                        chest.getInventory().setItem(x, null);
                    }
                } else if (chestType.getType().equals(org.bukkit.block.data.type.Chest.Type.LEFT)) {
                    for (int x=27; x<54; x++) {
                        chestDrops.add(chest.getInventory().getItem(x));
                        chest.getInventory().setItem(x, null);
                    }
                }

                for (ItemStack items : chestDrops) {
                    if (items!=null) {
                        if (player.getInventory().firstEmpty()!=-1) player.getInventory().addItem(items);
                        else player.getWorld().dropItemNaturally(loc, items);
                    }
                }

            } else {
                for (ItemStack items : ((Container) e.getBlock().getState()).getInventory().getContents()) {

                    if (items!=null) {
                        if (player.getInventory().firstEmpty()!=-1) {
                            player.getInventory().addItem(items);
                        } else {
                            player.getWorld().dropItemNaturally(loc, items);
                        }
                    }

                    ((Container) e.getBlock().getState()).getInventory().clear();
                }
            }

            ItemStack drop = new ItemStack(e.getBlock().getType());
            if (player.getInventory().firstEmpty()!=-1) {
                player.getInventory().addItem(drop);
            } else {
                player.getWorld().dropItemNaturally(loc, drop);
            }

        }

        TallCrops crops = Main.getAutoPickup.getCrops();
        ArrayList<Material> verticalReq = crops.getVerticalReq();
        ArrayList<Material> verticalReqDown = crops.getVerticalReqDown();

        if (verticalReq.contains(e.getBlock().getType()) || verticalReqDown.contains(e.getBlock().getType())) {
            e.setDropItems(false);
            vertBreak(player, e.getBlock().getLocation());
        }


    }

    public static int mend(ItemStack item, int xp) {

        if (item.containsEnchantment(Enchantment.MENDING)) {
            ItemMeta meta = item.getItemMeta();
            Mendable mend[] = Mendable.values();
            for (Mendable m : mend) {

                if (item.equals(null)) {
                    continue;
                }

                if (item.getType().toString().equals(m.toString())) {
                    Damageable damage = (Damageable) meta;
                    int min = Math.min(xp, damage.getDamage());
                    if ((damage.getDamage() - min==0) && damage.hasDamage()) {
                        fix(item);
                    } else {
                        damage.setDamage(damage.getDamage() - min);
                    }
                    xp -= min;
                    item.setItemMeta(meta);
                }
            }
        }
        return xp;
    }

    private static void fix(ItemStack item) {
        new BukkitRunnable() {
            @Override
            public void run() {
                ItemMeta meta = item.getItemMeta();
                Damageable damage = (Damageable) meta;
                damage.setDamage(0);
                item.setItemMeta(meta);
            }
        }.runTaskLater(Main.getInstance(), 1);
    }

    private static int amt = 1;
    private static Material type;
    private static void vertBreak(Player player, Location loc) {
        TallCrops crops = Main.getAutoPickup.getCrops();
        ArrayList<Material> verticalReq = crops.getVerticalReq();
        ArrayList<Material> verticalReqDown = crops.getVerticalReqDown();

        type = TallCrops.checkAltType(loc.getBlock().getType());
        loc.getBlock().setType(Material.AIR, true);

        if (verticalReq.contains(loc.add(0,1,0).getBlock().getType())) {
            amt++;
            vertBreak(player, loc);
        } else if (verticalReqDown.contains(loc.subtract(0,2,0).getBlock().getType())) {
            amt++;
            vertBreak(player, loc);
        } else {
            if (player.getInventory().firstEmpty()!=-1) {
                player.getInventory().addItem(new ItemStack(type, amt));
            } else {
                player.getWorld().dropItemNaturally(loc, new ItemStack(type, amt));
            }
            type = null;
            amt = 1;

            loc.add(0,1,0);
            String key = loc.getBlockX()+";"+loc.getBlockY()+";"+loc.getBlockZ()+";"+loc.getWorld();
            AutoPickup.customItemPatch.put(key, new PickupObjective(loc, player, Instant.now()));
        }

    }

}