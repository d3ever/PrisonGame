package sexy.criss.game.prison.timedchest;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import sexy.criss.game.prison.configuration.Configs;
import sexy.criss.game.prison.prison_data.PrisonItem;
import sexy.criss.game.prison.prison_data.PrisonPlayer;
import sexy.criss.gen.util.Util;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;

import static sexy.criss.game.prison.Main.getInstance;

public class LockChest {
    private FileConfiguration config = Configs.CONFIG.getConfig();
    private List<Location> locs = Lists.newArrayList();
    private Map<ItemStack, Integer> items = Maps.newHashMap();

    public LockChest() {
        loadLockedChest();
    }

    public void addLocation(Location l) {
        this.locs.add(l);
    }

    public void open(Player player) {
        Inventory inv = Bukkit.createInventory(player, InventoryType.CHEST);
        Random rand = new Random();
        Set<ItemStack> drop = new HashSet();
        Iterator var5;
        if (items.size() > 0) {
            var5 = items.entrySet().iterator();

            while(var5.hasNext()) {
                Map.Entry<ItemStack, Integer> cEntry = (Map.Entry)var5.next();
                if (drop.size() >= 2) {
                    break;
                }

                if (rand.nextInt(100) < cEntry.getValue()) {
                    drop.add(new ItemStack(cEntry.getKey()));
                }
            }
        }

        var5 = drop.iterator();

        while(var5.hasNext()) {
            ItemStack is = (ItemStack)var5.next();
            inv.addItem(is);
        }

        PrisonPlayer.getPlayer(player.getUniqueId()).addGold(rand.nextInt(9) + 1);
        player.openInventory(inv);
    }

    public void save() {
        List<String> list = Lists.newArrayList();
        this.locs.forEach(location -> {
            String str = location.getWorld().getName() + " " +
                    location.getX() + " " +
                    location.getY() + " " +
                    location.getZ();
            list.add(str);
        });
        config.set("locations", list);
        Configs.CONFIG.save();
    }

    private void loadLockedChest() {
        ConfigurationSection locked_chest = config.getConfigurationSection("locked-chest");
        if (locked_chest.contains("locations")) {
            AtomicInteger i = new AtomicInteger(0);
            locked_chest.getStringList("locations").forEach(s -> {
                String[] a = s.split(" ");
                locs.add(new Location(Bukkit.getWorld(a[0]), Double.parseDouble(a[1]), Double.parseDouble(a[2]),
                        Double.parseDouble(a[3])));
                i.incrementAndGet();
            });
            getInstance().getLogger().log(Level.INFO, "Locket chest location found and loaded.");
            getInstance().getLogger().log(Level.INFO, "Locket chest locations size = "+i.get());
        } else {
            getInstance().getLogger().log(Level.INFO, "Locket chest location not found. You need to set it with /setlockchest");
        }

        if (!locked_chest.contains("items") && !locked_chest.contains("custom_items")) {
            getInstance().getLogger().log(Level.INFO, "No items for locked chest. Add some in config.");
        } else {
            getInstance().getLogger().log(Level.INFO, "Locket chest items founded.");
            ConfigurationSection custom_items;
            String strItem;
            Iterator var4;
            if (locked_chest.contains("items")) {
                getInstance().getLogger().log(Level.INFO, "Locket chest items founded(Common Items).");
                custom_items = locked_chest.getConfigurationSection("items");
                var4 = custom_items.getKeys(false).iterator();

                while(var4.hasNext()) {
                    strItem = (String)var4.next();
                    getInstance().getLogger().log(Level.INFO, "Locket chest item found: " + strItem);
                    ConfigurationSection item = custom_items.getConfigurationSection(strItem);
                    ItemStack is = new ItemStack(Material.getMaterial(strItem));
                    if (item.contains("name")) {
                        ItemMeta im = is.getItemMeta();
                        im.setDisplayName(Util.f(item.getString("name")));
                        is.setItemMeta(im);
                    }

                    if (item.contains("data")) {
                        is.setDurability((short)item.getInt("data"));
                    }

                    if (item.contains("amount")) {
                        is.setAmount(item.getInt("amount"));
                    }

                    if (item.contains("chance")) {
                        items.put(is, item.getInt("chance"));
                    } else {
                        getInstance().getLogger().log(Level.INFO, "NO CHANCE FOR ITEM " + strItem);
                    }
                }
            }

            if (locked_chest.contains("custom_items")) {
                getInstance().getLogger().log(Level.INFO, "Locket chest items founded(CUSTOM Items).");
                custom_items = locked_chest.getConfigurationSection("custom_items");
                var4 = custom_items.getKeys(false).iterator();

                while(var4.hasNext()) {
                    strItem = (String)var4.next();
                    if (PrisonItem.isAvailable(strItem)) {
                        items.put(PrisonItem.getPrisonItem(strItem).getUsableItem(), custom_items.getInt(strItem));
                    } else {
                        getInstance().getLogger().log(Level.INFO, "Custom item " + strItem + " not found.");
                    }
                }
            }
        }

    }

}
