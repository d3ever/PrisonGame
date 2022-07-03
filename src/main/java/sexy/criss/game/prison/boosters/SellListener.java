/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  me.mrCookieSlime.CSCoreLibPlugin.Configuration.Variable
 *  me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.InvUtils
 *  me.mrCookieSlime.CSCoreLibPlugin.general.Math.DoubleHandler
 *  org.bukkit.ChatColor
 *  org.bukkit.GameMode
 *  org.bukkit.Material
 *  org.bukkit.block.Sign
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.block.Action
 *  org.bukkit.event.block.SignChangeEvent
 *  org.bukkit.event.inventory.InventoryClickEvent
 *  org.bukkit.event.inventory.InventoryCloseEvent
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.event.player.PlayerQuitEvent
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.plugin.Plugin
 */
package sexy.criss.game.prison.boosters;

import java.util.ArrayList;
import java.util.List;

import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Variable;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.InvUtils;
import me.mrCookieSlime.CSCoreLibPlugin.general.Math.DoubleHandler;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import sexy.criss.game.prison.Main;

public class SellListener implements Listener
{
    public SellListener() {
        Main.getInstance().getServer().getPluginManager().registerEvents(this, Main.getInstance());
    }

    @EventHandler
    public void onSignCreate(final SignChangeEvent e) {
        String prefix = ChatColor.translateAlternateColorCodes('&', QuickSell.cfg.getString("options.sign-prefix"));
        if (e.getLines()[0].equalsIgnoreCase(ChatColor.stripColor(prefix))) {
            if (e.getPlayer().hasPermission("QuickSell.sign.create")) {
                e.setLine(0, prefix);
            }
            else {
                e.setCancelled(true);
                QuickSell.local.sendTranslation((CommandSender)e.getPlayer(), "messages.no-permission", false, new Variable[0]);
            }
        }
        prefix = ChatColor.translateAlternateColorCodes('&', QuickSell.cfg.getString("options.sellall-sign-prefix"));
        if (e.getLines()[0].equalsIgnoreCase(ChatColor.stripColor(prefix))) {
            if (e.getPlayer().hasPermission("QuickSell.sign.create")) {
                e.setLine(0, prefix);
            }
            else {
                e.setCancelled(true);
                QuickSell.local.sendTranslation((CommandSender)e.getPlayer(), "messages.no-permission", false, new Variable[0]);
            }
        }
    }

    @EventHandler
    public void onInteract(final PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (e.getClickedBlock().getType() == Material.OAK_WALL_SIGN || e.getClickedBlock().getType() == Material.OAK_SIGN) {
                if (((Sign)e.getClickedBlock().getState()).getLine(0).equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', QuickSell.cfg.getString("options.sign-prefix")))) {
                    final Shop shop = Shop.getShop(((Sign)e.getClickedBlock().getState()).getLine(1));
                    if (shop != null) {
                        ShopMenu.open(e.getPlayer(), shop);
                    }
                    else {
                        ShopMenu.openMenu(e.getPlayer());
                    }
                    e.setCancelled(true);
                }
                else if (((Sign)e.getClickedBlock().getState()).getLine(0).equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', QuickSell.cfg.getString("options.sellall-sign-prefix")))) {
                    final Shop shop = Shop.getShop(((Sign)e.getClickedBlock().getState()).getLine(1));
                    if (shop != null) {
                        if (shop.hasUnlocked(e.getPlayer())) {
                            String item = ((Sign)e.getClickedBlock().getState()).getLine(2);
                            item = item.toUpperCase();
                            if (item.contains(" ")) {
                                item = item.replace(" ", "_");
                            }
                            shop.sellall(e.getPlayer(), item, SellEvent.Type.SELLALL);
                        }
                        else {
                            QuickSell.local.sendTranslation((CommandSender)e.getPlayer(), "messages.no-access", false, new Variable[0]);
                        }
                    }
                    else if (QuickSell.cfg.getBoolean("options.open-only-shop-with-permission")) {
                        if (Shop.getHighestShop(e.getPlayer()) != null) {
                            String item = ((Sign)e.getClickedBlock().getState()).getLine(2);
                            item = item.toUpperCase();
                            if (item.contains(" ")) {
                                item = item.replace(" ", "_");
                            }
                            Shop.getHighestShop(e.getPlayer()).sellall(e.getPlayer(), item, SellEvent.Type.SELLALL);
                        }
                        else {
                            QuickSell.local.sendTranslation((CommandSender)e.getPlayer(), "messages.no-access", false, new Variable[0]);
                        }
                    }
                    else {
                        QuickSell.local.sendTranslation((CommandSender)e.getPlayer(), "messages.unknown-shop", false, new Variable[0]);
                    }
                    e.setCancelled(true);
                }
            }
        }
        else if (e.getAction() == Action.LEFT_CLICK_BLOCK && e.getPlayer().getGameMode() != GameMode.CREATIVE && (e.getClickedBlock().getType() == Material.OAK_WALL_SIGN || e.getClickedBlock().getType() == Material.OAK_SIGN)) {
            if (((Sign)e.getClickedBlock().getState()).getLine(0).equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', QuickSell.cfg.getString("options.sign-prefix")))) {
                final Shop shop = Shop.getShop(((Sign)e.getClickedBlock().getState()).getLine(1));
                if (shop != null) {
                    shop.showPrices(e.getPlayer());
                }
                else if (QuickSell.cfg.getBoolean("options.open-only-shop-with-permission")) {
                    if (Shop.getHighestShop(e.getPlayer()) != null) {
                        Shop.getHighestShop(e.getPlayer()).showPrices(e.getPlayer());
                    }
                    else {
                        QuickSell.local.sendTranslation((CommandSender)e.getPlayer(), "messages.no-access", false, new Variable[0]);
                    }
                }
            }
            else if (((Sign)e.getClickedBlock().getState()).getLine(0).equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', QuickSell.cfg.getString("options.sellall-sign-prefix")))) {
                final Shop shop = Shop.getShop(((Sign)e.getClickedBlock().getState()).getLine(1));
                if (shop != null) {
                    if (shop.hasUnlocked(e.getPlayer())) {
                        shop.showPrices(e.getPlayer());
                    }
                    else {
                        QuickSell.local.sendTranslation((CommandSender)e.getPlayer(), "messages.no-access", false, new Variable[0]);
                    }
                }
                else if (QuickSell.cfg.getBoolean("options.open-only-shop-with-permission")) {
                    if (Shop.getHighestShop(e.getPlayer()) != null) {
                        Shop.getHighestShop(e.getPlayer()).showPrices(e.getPlayer());
                    }
                    else {
                        QuickSell.local.sendTranslation((CommandSender)e.getPlayer(), "messages.no-access", false, new Variable[0]);
                    }
                }
                else {
                    QuickSell.local.sendTranslation((CommandSender)e.getPlayer(), "messages.unknown-shop", false, new Variable[0]);
                }
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onClose(final InventoryCloseEvent e) {
        final Player p = (Player)e.getPlayer();
        if (QuickSell.shop.containsKey(p.getUniqueId())) {
            final List<ItemStack> items = new ArrayList<ItemStack>();
            int size = e.getInventory().getSize();
            if (QuickSell.cfg.getBoolean("options.enable-menu-line")) {
                size -= 9;
            }
            for (int i = 0; i < size; ++i) {
                items.add(e.getInventory().getContents()[i]);
            }
            final Shop shop = QuickSell.shop.get(p.getUniqueId());
            QuickSell.shop.remove(p.getUniqueId());
            shop.sell(p, false, SellEvent.Type.SELL, items.toArray(new ItemStack[0]));
        }
    }

    @EventHandler
    public void onClick(final InventoryClickEvent e) {
        if (QuickSell.cfg.getBoolean("options.enable-menu-line") && e.getRawSlot() < e.getInventory().getSize()) {
            final Player p = (Player)e.getWhoClicked();
            if (QuickSell.shop.containsKey(p.getUniqueId())) {
                final Shop shop = QuickSell.shop.get(p.getUniqueId());
                if (e.getSlot() == 9 * QuickSell.cfg.getInt("options.sell-gui-rows") - 9) {
                    e.setCancelled(true);
                }
                if (e.getSlot() == 9 * QuickSell.cfg.getInt("options.sell-gui-rows") - 8) {
                    e.setCancelled(true);
                    p.closeInventory();
                }
                if (e.getSlot() == 9 * QuickSell.cfg.getInt("options.sell-gui-rows") - 7) {
                    e.setCancelled(true);
                }
                if (e.getSlot() == 9 * QuickSell.cfg.getInt("options.sell-gui-rows") - 6) {
                    e.setCancelled(true);
                }
                if (e.getSlot() == 9 * QuickSell.cfg.getInt("options.sell-gui-rows") - 5) {
                    e.setCancelled(true);
                    double money = 0.0;
                    for (int i = 0; i < e.getInventory().getSize() - 9; ++i) {
                        final ItemStack item = e.getInventory().getContents()[i];
                        if (item != null) {
                            money += shop.getPrices().getPrice(item);
                        }
                    }
                    money = DoubleHandler.fixDouble(money, 2);
                    if (money > 0.0) {
                        for (final Booster booster : Booster.getBoosters(p.getName(), Booster.BoosterType.MONETARY)) {
                            money += money * (booster.getMultiplier() - 1.0);
                        }
                    }
                    QuickSell.local.sendTranslation((CommandSender)p, "messages.estimate", false, new Variable[] { new Variable("{MONEY}", String.valueOf(DoubleHandler.fixDouble(money, 2))) });
                }
                if (e.getSlot() == 9 * QuickSell.cfg.getInt("options.sell-gui-rows") - 4) {
                    e.setCancelled(true);
                }
                if (e.getSlot() == 9 * QuickSell.cfg.getInt("options.sell-gui-rows") - 3) {
                    e.setCancelled(true);
                }
                if (e.getSlot() == 9 * QuickSell.cfg.getInt("options.sell-gui-rows") - 2) {
                    e.setCancelled(true);
                    QuickSell.shop.remove(p.getUniqueId());
                    for (int j = 0; j < e.getInventory().getSize() - 9; ++j) {
                        final ItemStack item2 = e.getInventory().getContents()[j];
                        if (item2 != null && item2.getType() != Material.AIR) {
                            if (InvUtils.fits((Inventory)p.getInventory(), item2)) {
                                p.getInventory().addItem(new ItemStack[] { item2 });
                            }
                            else {
                                p.getWorld().dropItemNaturally(p.getLocation(), item2);
                            }
                        }
                    }
                    p.closeInventory();
                }
                if (e.getSlot() == 9 * QuickSell.cfg.getInt("options.sell-gui-rows") - 1) {
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onQuit(final PlayerQuitEvent e) {
        SellProfile.getProfile(e.getPlayer()).unregister();
    }
}

