package sexy.criss.game.prison.boosters;

import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Variable;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ClickAction;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem;
import me.mrCookieSlime.CSCoreLibPlugin.general.audio.Soundboard;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import sexy.criss.gen.util.Util;

public class ShopMenu {
    static final int shop_size = 45;

    public static void open(Player p, Shop shop) {
            Inventory inv = Bukkit.createInventory(null, (int)(9 * QuickSell.cfg.getInt("options.sell-gui-rows")), Util.f("&0Давай сюда свои блоки"));
            if (QuickSell.cfg.getBoolean("options.enable-menu-line")) {
                inv.setItem(9 * QuickSell.cfg.getInt("options.sell-gui-rows") - 9, new CustomItem(Material.GRAY_STAINED_GLASS_PANE, " ", 0));
                inv.setItem(9 * QuickSell.cfg.getInt("options.sell-gui-rows") - 8, new CustomItem(Material.GREEN_STAINED_GLASS_PANE, QuickSell.local.getTranslation("menu.accept").get(0), 0));
                inv.setItem(9 * QuickSell.cfg.getInt("options.sell-gui-rows") - 7, new CustomItem(Material.GRAY_STAINED_GLASS_PANE, " ", 0));
                inv.setItem(9 * QuickSell.cfg.getInt("options.sell-gui-rows") - 6, new CustomItem(Material.GRAY_STAINED_GLASS_PANE, " ", 0));
                inv.setItem(9 * QuickSell.cfg.getInt("options.sell-gui-rows") - 5, new CustomItem(Material.YELLOW_STAINED_GLASS_PANE, QuickSell.local.getTranslation("menu.estimate").get(0), 0));
                inv.setItem(9 * QuickSell.cfg.getInt("options.sell-gui-rows") - 4, new CustomItem(Material.GRAY_STAINED_GLASS_PANE, " ", 0));
                inv.setItem(9 * QuickSell.cfg.getInt("options.sell-gui-rows") - 3, new CustomItem(Material.GRAY_STAINED_GLASS_PANE, " ", 0));
                inv.setItem(9 * QuickSell.cfg.getInt("options.sell-gui-rows") - 2, new CustomItem(Material.RED_STAINED_GLASS_PANE, QuickSell.local.getTranslation("menu.cancel").get(0), 0));
                inv.setItem(9 * QuickSell.cfg.getInt("options.sell-gui-rows") - 1, new CustomItem(Material.GRAY_STAINED_GLASS_PANE, " ", 0));
            }
            p.openInventory(inv);
            QuickSell.shop.put(p.getUniqueId(), shop);
    }

    public static void openMenu(final Player p) {
        if (QuickSell.cfg.getBoolean("shop.enable-hierarchy")) {
            if (Shop.getHighestShop(p) != null) {
                open(p, Shop.getHighestShop(p));
            }
            else {
                QuickSell.local.sendTranslation((CommandSender)p, "messages.no-access", false, new Variable[0]);
            }
        }
        else {
            final ChestMenu menu = new ChestMenu((String)QuickSell.local.getTranslation("menu.title").get(0));
            for (int i = 0; i < Shop.list().size(); ++i) {
                if (Shop.list().get(i) != null) {
                    final Shop shop = Shop.list().get(i);
                    menu.addItem(i, shop.getItem(shop.hasUnlocked(p) ? ShopStatus.UNLOCKED : ShopStatus.LOCKED));
                    menu.addMenuClickHandler(i, (p1, slot, item, action) -> {
                        ShopMenu.open(p1, shop);
                        return false;
                    });
                }
            }
            menu.open(p);
        }
    }

    public static void openPrices(Player p, final Shop shop, final int page) {
        int target;
        ChestMenu menu = new ChestMenu("Shop Prices");
        menu.addMenuOpeningHandler(new ChestMenu.MenuOpeningHandler(){

            public void onOpen(Player p) {
                p.playSound(p.getLocation(), Soundboard.getLegacySounds((String[])new String[]{"UI_BUTTON_CLICK", "CLICK"}), 1.0f, 1.0f);
            }
        });
        int index = 0;
        final int pages = shop.getPrices().getInfo().size() / 45 + 1;
        for (int i = 45; i < 54; ++i) {
            menu.addItem(i, (ItemStack)new CustomItem(new MaterialData(Material.GRAY_STAINED_GLASS_PANE, (byte) 0), " ", new String[0]));
            menu.addMenuClickHandler(i, new ChestMenu.MenuClickHandler(){

                public boolean onClick(Player arg0, int arg1, ItemStack arg2, ClickAction arg3) {
                    return false;
                }
            });
        }
        menu.addItem(46, (ItemStack)new CustomItem(new MaterialData(Material.LIME_STAINED_GLASS_PANE, (byte) 0), "&r\u21e6 Previous Page", new String[]{"", "&7(" + page + " / " + pages + ")"}));
        menu.addMenuClickHandler(46, new ChestMenu.MenuClickHandler(){

            public boolean onClick(Player p, int arg1, ItemStack arg2, ClickAction arg3) {
                int next = page - 1;
                if (next < 1) {
                    next = pages;
                }
                if (next != page) {
                    ShopMenu.openPrices(p, shop, next);
                }
                return false;
            }
        });
        menu.addItem(52, (ItemStack)new CustomItem(new MaterialData(Material.LIME_STAINED_GLASS_PANE, (byte) 0), "&rNext Page \u21e8", new String[]{"", "&7(" + page + " / " + pages + ")"}));
        menu.addMenuClickHandler(52, new ChestMenu.MenuClickHandler(){

            public boolean onClick(Player p, int arg1, ItemStack arg2, ClickAction arg3) {
                int next = page + 1;
                if (next > pages) {
                    next = 1;
                }
                if (next != page) {
                    ShopMenu.openPrices(p, shop, next);
                }
                return false;
            }
        });
        int shop_index = 45 * (page - 1);
        for (int i = 0; i < 45 && (target = shop_index + i) < shop.getPrices().getItems().size(); ++i) {
            String string = shop.getPrices().getItems().get(target);
            ItemStack item = shop.getPrices().getItem(string);
            menu.addItem(index, item);
            menu.addMenuClickHandler(index, new ChestMenu.MenuClickHandler(){

                public boolean onClick(Player p, int arg1, ItemStack arg2, ClickAction action) {
                    return false;
                }
            });
            ++index;
        }
        menu.build().open(new Player[]{p});
    }
}

