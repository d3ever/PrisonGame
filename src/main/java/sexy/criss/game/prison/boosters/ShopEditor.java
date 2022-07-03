/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  me.mrCookieSlime.CSCoreLibPlugin.Configuration.Variable
 *  me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu
 *  me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu$MenuClickHandler
 *  me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu$MenuOpeningHandler
 *  me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ClickAction
 *  me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem
 *  me.mrCookieSlime.CSCoreLibPlugin.general.Math.DoubleHandler
 *  me.mrCookieSlime.CSCoreLibPlugin.general.audio.Soundboard
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.AsyncPlayerChatEvent
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.material.MaterialData
 *  org.bukkit.plugin.Plugin
 */
package sexy.criss.game.prison.boosters;

import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Variable;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ClickAction;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem;
import me.mrCookieSlime.CSCoreLibPlugin.general.Math.DoubleHandler;
import me.mrCookieSlime.CSCoreLibPlugin.general.audio.Soundboard;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import sexy.criss.game.prison.Main;

import java.util.*;

public class ShopEditor implements Listener {
    Map<UUID, Input> input;
    final int shop_size = 36;
    QuickSell quicksell;

    public ShopEditor(QuickSell quickSell) {
        this.quicksell = quickSell;
        this.input = new HashMap<>();
        Main.getInstance().getServer().getPluginManager().registerEvents(this, Main.getInstance());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onChat(final AsyncPlayerChatEvent e) {
        if (this.input.containsKey(e.getPlayer().getUniqueId())) {
            e.setCancelled(true);
            final Input input = this.input.get(e.getPlayer().getUniqueId());
            switch (input.getType()) {
                case NEW_SHOP: {
                    final List<String> list = new ArrayList<String>();
                    for (final Shop shop : Shop.list()) {
                        list.add(shop.getID());
                    }
                    for (int i = list.size(); i <= (int)input.getValue(); ++i) {
                        list.add("");
                    }
                    list.set((int)input.getValue(), ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', e.getMessage())));
                    QuickSell.cfg.setValue("list", (Object)list);
                    QuickSell.cfg.setValue("shops." + ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', e.getMessage())) + ".name", (Object)e.getMessage());
                    QuickSell.cfg.save();
                    QuickSell.local.sendTranslation((CommandSender)e.getPlayer(), "commands.shop-created", false, new Variable[] { new Variable("%shop%", e.getMessage()) });
                    this.openEditor(e.getPlayer());
                    this.input.remove(e.getPlayer().getUniqueId());
                    break;
                }
                case RENAME: {
                    final Shop shop2 = (Shop)input.getValue();
                    QuickSell.cfg.setValue("shops." + shop2.getID() + ".name", (Object)e.getMessage());
                    QuickSell.cfg.save();
                    this.quicksell.reload();
                    this.openShopEditor(e.getPlayer(), Shop.getShop(shop2.getID()));
                    QuickSell.local.sendTranslation((CommandSender)e.getPlayer(), "editor.renamed-shop", false, new Variable[0]);
                    this.input.remove(e.getPlayer().getUniqueId());
                    break;
                }
                case SET_PERMISSION: {
                    final Shop shop2 = (Shop)input.getValue();
                    QuickSell.cfg.setValue("shops." + shop2.getID() + ".permission", (Object)(e.getMessage().equals("none") ? "" : e.getMessage()));
                    QuickSell.cfg.save();
                    this.quicksell.reload();
                    this.openShopEditor(e.getPlayer(), Shop.getShop(shop2.getID()));
                    QuickSell.local.sendTranslation((CommandSender)e.getPlayer(), "editor.permission-set-shop", false, new Variable[0]);
                    this.input.remove(e.getPlayer().getUniqueId());
                    break;
                }
            }
        }
    }

    public void openEditor(final Player p) {
        this.quicksell.reload();
        final ChestMenu menu = new ChestMenu("&6QuickSell - Shop Editor");
        menu.addMenuOpeningHandler((ChestMenu.MenuOpeningHandler)new ChestMenu.MenuOpeningHandler() {
            public void onOpen(final Player p) {
                p.playSound(p.getLocation(), Soundboard.getLegacySounds(new String[] { "BLOCK_NOTE_PLING", "NOTE_PLING" }), 1.0f, 1.0f);
            }
        });
        for (int i = 0; i < 54; ++i) {
            final Shop shop = (Shop.list().size() > i) ? Shop.list().get(i) : null;
            if (shop == null) {
                menu.addItem(i, (ItemStack)new CustomItem(new MaterialData(Material.GOLD_NUGGET), "§cNew Shop", new String[] { "", "§rLeft Click: §7Create a new Shop" }));
                menu.addMenuClickHandler(i, (ChestMenu.MenuClickHandler)new ChestMenu.MenuClickHandler() {
                    public boolean onClick(final Player p, final int slot, final ItemStack item, final ClickAction action) {
                        ShopEditor.this.input.put(p.getUniqueId(), new Input(InputType.NEW_SHOP, slot));
                        QuickSell.local.sendTranslation((CommandSender)p, "editor.create-shop", false, new Variable[0]);
                        p.closeInventory();
                        return false;
                    }
                });
            }
            else {
                menu.addItem(i, (ItemStack)new CustomItem(shop.getItem(ShopStatus.UNLOCKED), shop.getName(), new String[] { "", "§rLeft Click: §7Edit Shop", "§rRight Click: §7Edit Shop Contents", "§rShift + Right Click: §4Delete Shop" }));
                menu.addMenuClickHandler(i, (ChestMenu.MenuClickHandler)new ChestMenu.MenuClickHandler() {
                    public boolean onClick(final Player p, final int slot, final ItemStack item, final ClickAction action) {
                        if (action.isRightClicked()) {
                            if (action.isShiftClicked()) {
                                final List<String> list = new ArrayList<String>();
                                for (final Shop shop : Shop.list()) {
                                    list.add(shop.getID());
                                }
                                list.remove(shop.getID());
                                QuickSell.cfg.setValue("list", (Object)list);
                                QuickSell.cfg.save();
                                ShopEditor.this.quicksell.reload();
                                ShopEditor.this.openEditor(p);
                            }
                            else {
                                ShopEditor.this.openShopContentEditor(p, shop, 1);
                            }
                        }
                        else {
                            ShopEditor.this.openShopEditor(p, shop);
                        }
                        return false;
                    }
                });
            }
        }
        menu.open(new Player[] { p });
    }

    public void openShopEditor(final Player p, final Shop shop) {
        this.quicksell.reload();
        final ChestMenu menu = new ChestMenu("&6QuickSell - Shop Editor");
        menu.addMenuOpeningHandler((ChestMenu.MenuOpeningHandler)new ChestMenu.MenuOpeningHandler() {
            public void onOpen(final Player p) {
                p.playSound(p.getLocation(), Soundboard.getLegacySounds(new String[] { "BLOCK_NOTE_PLING", "NOTE_PLING" }), 1.0f, 1.0f);
            }
        });
        menu.addItem(0, (ItemStack)new CustomItem(new MaterialData(Material.NAME_TAG), shop.getName(), new String[] { "", "§rClick: §7Change Name" }));
        menu.addMenuClickHandler(0, (ChestMenu.MenuClickHandler)new ChestMenu.MenuClickHandler() {
            public boolean onClick(final Player p, final int slot, final ItemStack item, final ClickAction action) {
                ShopEditor.this.input.put(p.getUniqueId(), new Input(InputType.RENAME, shop));
                QuickSell.local.sendTranslation((CommandSender)p, "editor.rename-shop", false, new Variable[0]);
                p.closeInventory();
                return false;
            }
        });
        menu.addItem(1, (ItemStack)new CustomItem(shop.getItem(ShopStatus.UNLOCKED), "§rDisplay Item", new String[] { "", "§rClick: §7Change Item to the Item held in your Hand" }));
        menu.addMenuClickHandler(1, (ChestMenu.MenuClickHandler)new ChestMenu.MenuClickHandler() {
            public boolean onClick(final Player p, final int slot, final ItemStack item, final ClickAction action) {
                if (p.getItemInHand() != null && p.getItemInHand().getType() != null && p.getItemInHand().getType() != Material.AIR) {
                    QuickSell.cfg.setValue("shops." + shop.getID() + ".itemtype", (Object)(p.getItemInHand().getType().toString() + "-" + p.getItemInHand().getData().getData()));
                    QuickSell.cfg.save();
                    ShopEditor.this.quicksell.reload();
                }
                ShopEditor.this.openShopEditor(p, Shop.getShop(shop.getID()));
                return false;
            }
        });
        menu.addItem(2, (ItemStack)new CustomItem(new MaterialData(Material.DIAMOND), "§7Shop Permission: §r" + (shop.getPermission().equals("") ? "None" : shop.getPermission()), new String[] { "", "§rClick: §7Change Permission Node" }));
        menu.addMenuClickHandler(2, (ChestMenu.MenuClickHandler)new ChestMenu.MenuClickHandler() {
            public boolean onClick(final Player p, final int slot, final ItemStack item, final ClickAction action) {
                ShopEditor.this.input.put(p.getUniqueId(), new Input(InputType.SET_PERMISSION, shop));
                QuickSell.local.sendTranslation((CommandSender)p, "editor.set-permission-shop", false, new Variable[0]);
                p.closeInventory();
                return false;
            }
        });
        menu.open(p);
    }

    public void openShopContentEditor(final Player p, final Shop shop, final int page) {
        this.quicksell.reload();
        final ChestMenu menu = new ChestMenu("&6QuickSell - Shop Editor");
        menu.addMenuOpeningHandler((ChestMenu.MenuOpeningHandler)new ChestMenu.MenuOpeningHandler() {
            public void onOpen(final Player p) {
                p.playSound(p.getLocation(), Soundboard.getLegacySounds(new String[] { "BLOCK_NOTE_PLING", "NOTE_PLING" }), 1.0f, 1.0f);
            }
        });
        int index = 9;
        final int pages = shop.getPrices().getInfo().size() / 36 + 1;
        for (int i = 0; i < 4; ++i) {
            menu.addItem(i, (ItemStack)new CustomItem(new MaterialData(Material.GRAY_STAINED_GLASS_PANE, (byte)0), " ", new String[0]));
            menu.addMenuClickHandler(i, (ChestMenu.MenuClickHandler)new ChestMenu.MenuClickHandler() {
                public boolean onClick(final Player arg0, final int arg1, final ItemStack arg2, final ClickAction arg3) {
                    return false;
                }
            });
        }
        menu.addItem(4, (ItemStack)new CustomItem(new MaterialData(Material.GOLD_INGOT), "&7\u21e6 Back", new String[0]));
        menu.addMenuClickHandler(4, (ChestMenu.MenuClickHandler)new ChestMenu.MenuClickHandler() {
            public boolean onClick(final Player p, final int arg1, final ItemStack arg2, final ClickAction arg3) {
                ShopEditor.this.openEditor(p);
                return false;
            }
        });
        for (int i = 5; i < 9; ++i) {
            menu.addItem(i, (ItemStack)new CustomItem(new MaterialData(Material.GRAY_STAINED_GLASS_PANE, (byte)0), " ", new String[0]));
            menu.addMenuClickHandler(i, (ChestMenu.MenuClickHandler)new ChestMenu.MenuClickHandler() {
                public boolean onClick(final Player arg0, final int arg1, final ItemStack arg2, final ClickAction arg3) {
                    return false;
                }
            });
        }
        for (int i = 45; i < 54; ++i) {
            menu.addItem(i, (ItemStack)new CustomItem(new MaterialData(Material.GRAY_STAINED_GLASS_PANE, (byte)0), " ", new String[0]));
            menu.addMenuClickHandler(i, (ChestMenu.MenuClickHandler)new ChestMenu.MenuClickHandler() {
                public boolean onClick(final Player arg0, final int arg1, final ItemStack arg2, final ClickAction arg3) {
                    return false;
                }
            });
        }
        menu.addItem(46, (ItemStack)new CustomItem(new MaterialData(Material.LIME_STAINED_GLASS_PANE, (byte)0), "&r\u21e6 Previous Page", new String[] { "", "&7(" + page + " / " + pages + ")" }));
        menu.addMenuClickHandler(46, (ChestMenu.MenuClickHandler)new ChestMenu.MenuClickHandler() {
            public boolean onClick(final Player p, final int arg1, final ItemStack arg2, final ClickAction arg3) {
                int next = page - 1;
                if (next < 1) {
                    next = pages;
                }
                if (next != page) {
                    ShopEditor.this.openShopContentEditor(p, shop, next);
                }
                return false;
            }
        });
        menu.addItem(52, (ItemStack)new CustomItem(new MaterialData(Material.LIME_STAINED_GLASS_PANE, (byte)0), "&rNext Page \u21e8", new String[] { "", "&7(" + page + " / " + pages + ")" }));
        menu.addMenuClickHandler(52, (ChestMenu.MenuClickHandler)new ChestMenu.MenuClickHandler() {
            public boolean onClick(final Player p, final int arg1, final ItemStack arg2, final ClickAction arg3) {
                int next = page + 1;
                if (next > pages) {
                    next = 1;
                }
                if (next != page) {
                    ShopEditor.this.openShopContentEditor(p, shop, next);
                }
                return false;
            }
        });
        final int shop_index = 36 * (page - 1);
        for (int j = 0; j < 36; ++j) {
            final int target = shop_index + j;
            if (target >= shop.getPrices().getItems().size()) {
                menu.addItem(index, (ItemStack)new CustomItem(new MaterialData(Material.COMMAND_BLOCK), "§cAdd Item", new String[] { "", "§rLeft Click: §7Add an Item to this Shop" }));
                menu.addMenuClickHandler(index, (ChestMenu.MenuClickHandler)new ChestMenu.MenuClickHandler() {
                    public boolean onClick(final Player p, final int arg1, final ItemStack arg2, final ClickAction arg3) {
                        ShopEditor.this.openItemEditor(p, shop);
                        return false;
                    }
                });
                break;
            }
            final String string = shop.getPrices().getItems().get(target);
            final ItemStack item = shop.getPrices().getItem(string);
            menu.addItem(index, (ItemStack)new CustomItem(item.getData(), item.getItemMeta().getDisplayName(), new String[] { "§7Price (1): §6§$" + DoubleHandler.getFancyDouble(shop.getPrices().getPrice(string)), "", "§rLeft Click: §7Edit Price", "§rShift + Right Click: §7Remove this Item from this Shop" }));
            menu.addMenuClickHandler(index, (ChestMenu.MenuClickHandler)new ChestMenu.MenuClickHandler() {
                public boolean onClick(final Player p, final int arg1, final ItemStack arg2, final ClickAction action) {
                    if (action.isShiftClicked() && action.isRightClicked()) {
                        QuickSell.cfg.setValue("shops." + shop.getID() + ".price." + string, (Object)0.0);
                        QuickSell.cfg.save();
                        ShopEditor.this.quicksell.reload();
                        ShopEditor.this.openShopContentEditor(p, Shop.getShop(shop.getID()), 1);
                    }
                    else if (!action.isRightClicked()) {
                        ShopEditor.this.openPriceEditor(p, Shop.getShop(shop.getID()), item, string, shop.getPrices().getPrice(string));
                    }
                    return false;
                }
            });
            ++index;
        }
        menu.open(new Player[] { p });
    }

    public void openItemEditor(final Player p, final Shop shop) {
        final ItemStack item = p.getItemInHand();
        if (item == null || item.getType() == null || item.getType() == Material.AIR) {
            p.sendMessage("§4§lYou need to be holding the Item you want to add in your Hand!");
            return;
        }
        final ChestMenu menu = new ChestMenu("&6QuickSell - Shop Editor");
        menu.addMenuOpeningHandler((ChestMenu.MenuOpeningHandler)new ChestMenu.MenuOpeningHandler() {
            public void onOpen(final Player p) {
                p.playSound(p.getLocation(), Soundboard.getLegacySounds(new String[] { "BLOCK_NOTE_PLING", "NOTE_PLING" }), 1.0f, 1.0f);
            }
        });
        menu.addItem(4, item);
        menu.addMenuClickHandler(4, (ChestMenu.MenuClickHandler)new ChestMenu.MenuClickHandler() {
            public boolean onClick(final Player arg0, final int arg1, final ItemStack arg2, final ClickAction arg3) {
                return false;
            }
        });
        menu.addItem(10, (ItemStack)new CustomItem(new MaterialData(Material.GREEN_WOOL, (byte)0), "§2Material Only §7(e.g. STONE)", new String[] { "§rAdds the Item above to the Shop", "§rThis Option is going to ignore", "§rany Metadata and Sub-IDs" }));
        menu.addMenuClickHandler(10, (ChestMenu.MenuClickHandler)new ChestMenu.MenuClickHandler() {
            public boolean onClick(final Player p, final int arg1, final ItemStack arg2, final ClickAction arg3) {
                QuickSell.cfg.setValue("shops." + shop.getID() + ".price." + item.getType().toString(), (Object)1.0);
                QuickSell.cfg.save();
                ShopEditor.this.quicksell.reload();
                ShopEditor.this.openShopContentEditor(p, Shop.getShop(shop.getID()), 1);
                QuickSell.local.sendTranslation((CommandSender)p, "commands.price-set", false, new Variable[] { new Variable("%item%", item.getType().toString()), new Variable("%shop%", shop.getName()), new Variable("%price%", "1.0") });
                p.sendMessage("§7§oYou can edit the Price afterwards.");
                return false;
            }
        });
        menu.addItem(11, (ItemStack)new CustomItem(new MaterialData(Material.GREEN_WOOL, (byte)0), "§2Material Only and exclude Metadata §7(e.g. STONE-nodata)", new String[] { "§rAdds the Item above to the Shop", "§rThis Option is going to only take Items", "§rwhich are NOT renamed and do NOT have Lore", "§rbut still ignores Sub-IDs" }));
        menu.addMenuClickHandler(11, (ChestMenu.MenuClickHandler)new ChestMenu.MenuClickHandler() {
            public boolean onClick(final Player p, final int arg1, final ItemStack arg2, final ClickAction arg3) {
                QuickSell.cfg.setValue("shops." + shop.getID() + ".price." + item.getType().toString() + "-nodata", (Object)1.0);
                QuickSell.cfg.save();
                ShopEditor.this.quicksell.reload();
                ShopEditor.this.openShopContentEditor(p, Shop.getShop(shop.getID()), 1);
                QuickSell.local.sendTranslation((CommandSender)p, "commands.price-set", false, new Variable[] { new Variable("%item%", item.getType().toString() + ":" + item.getData().getData()), new Variable("%shop%", shop.getName()), new Variable("%price%", "1.0") });
                p.sendMessage("§7§oYou can edit the Price afterwards.");
                return false;
            }
        });
        menu.addItem(12, (ItemStack)new CustomItem(new MaterialData(Material.RED_WOOL, (byte)0), "§2Material + Sub-ID §7(e.g. STONE:1)", new String[] { "§rAdds the Item above to the Shop", "§rThis Option is going to ignore", "§rany Metadata but respect Sub-IDs" }));
        menu.addMenuClickHandler(12, (ChestMenu.MenuClickHandler)new ChestMenu.MenuClickHandler() {
            public boolean onClick(final Player p, final int arg1, final ItemStack arg2, final ClickAction arg3) {
                QuickSell.cfg.setValue("shops." + shop.getID() + ".price." + item.getType().toString() + "-" + item.getData().getData(), (Object)1.0);
                QuickSell.cfg.save();
                ShopEditor.this.quicksell.reload();
                ShopEditor.this.openShopContentEditor(p, Shop.getShop(shop.getID()), 1);
                QuickSell.local.sendTranslation((CommandSender)p, "commands.price-set", false, new Variable[] { new Variable("%item%", item.getType().toString() + ":" + item.getData().getData()), new Variable("%shop%", shop.getName()), new Variable("%price%", "1.0") });
                p.sendMessage("§7§oYou can edit the Price afterwards.");
                return false;
            }
        });
        menu.addItem(13, (ItemStack)new CustomItem(new MaterialData(Material.RED_WOOL, (byte)0), "§2Material + Sub-ID and exclude Metadata §7(e.g. STONE:1-nodata)", new String[] { "§rAdds the Item above to the Shop", "§rThis Option is going to respect Sub-IDs", "§rbut is not going to take Items which", "§rare named or have Lore" }));
        menu.addMenuClickHandler(13, (ChestMenu.MenuClickHandler)new ChestMenu.MenuClickHandler() {
            public boolean onClick(final Player p, final int arg1, final ItemStack arg2, final ClickAction arg3) {
                QuickSell.cfg.setValue("shops." + shop.getID() + ".price." + item.getType().toString() + "-" + item.getData().getData(), (Object)1.0);
                QuickSell.cfg.save();
                ShopEditor.this.quicksell.reload();
                ShopEditor.this.openShopContentEditor(p, Shop.getShop(shop.getID()), 1);
                QuickSell.local.sendTranslation((CommandSender)p, "commands.price-set", false, new Variable[] { new Variable("%item%", item.getType().toString() + ":" + item.getData().getData()), new Variable("%shop%", shop.getName()), new Variable("%price%", "1.0") });
                p.sendMessage("§7§oYou can edit the Price afterwards.");
                return false;
            }
        });
        menu.addItem(14, (ItemStack)new CustomItem(new MaterialData(Material.RED_WOOL, (byte)0), "§2Material + Display Name §7(e.g. STONE named &5Cool Stone §7)", new String[] { "§rAdds the Item above to the Shop", "§rThis Option is going to ignore", "§rany Sub-IDs but respect Display names" }));
        menu.addMenuClickHandler(14, (ChestMenu.MenuClickHandler)new ChestMenu.MenuClickHandler() {
            public boolean onClick(final Player p, final int arg1, final ItemStack arg2, final ClickAction arg3) {
                if (!item.getItemMeta().hasDisplayName()) {
                    p.sendMessage("§cYou can only choose this Option if the selected Item had a Display Name!");
                    return false;
                }
                QuickSell.cfg.setValue("shops." + shop.getID() + ".price." + item.getType().toString() + "-" + item.getItemMeta().getDisplayName().replaceAll("§", "&"), (Object)1.0);
                QuickSell.cfg.save();
                ShopEditor.this.quicksell.reload();
                ShopEditor.this.openShopContentEditor(p, Shop.getShop(shop.getID()), 1);
                QuickSell.local.sendTranslation((CommandSender)p, "commands.price-set", false, new Variable[] { new Variable("%item%", item.getType().toString() + " named " + item.getItemMeta().getDisplayName()), new Variable("%shop%", shop.getName()), new Variable("%price%", "1.0") });
                p.sendMessage("§7§oYou can edit the Price afterwards.");
                return false;
            }
        });
        menu.addItem(16, (ItemStack)new CustomItem(new MaterialData(Material.RED_WOOL, (byte)0), "§cCancel", new String[0]));
        menu.addMenuClickHandler(16, (ChestMenu.MenuClickHandler)new ChestMenu.MenuClickHandler() {
            public boolean onClick(final Player p, final int arg1, final ItemStack arg2, final ClickAction arg3) {
                ShopEditor.this.openShopContentEditor(p, Shop.getShop(shop.getID()), 1);
                return false;
            }
        });
        menu.build().open(new Player[] { p });
    }

    public void openPriceEditor(final Player p, final Shop shop, final ItemStack item, final String string, final double worth) {
        final ChestMenu menu = new ChestMenu("&6QuickSell - Shop Editor");
        menu.addMenuOpeningHandler((ChestMenu.MenuOpeningHandler)new ChestMenu.MenuOpeningHandler() {
            public void onOpen(final Player p) {
                p.playSound(p.getLocation(), Soundboard.getLegacySounds(new String[] { "BLOCK_NOTE_PLING", "NOTE_PLING" }), 1.0f, 1.0f);
            }
        });
        menu.addItem(4, (ItemStack)new CustomItem(item, item.getItemMeta().getDisplayName(), new String[] { "", "§8Price: §6$" + DoubleHandler.getFancyDouble(worth) }));
        menu.addMenuClickHandler(4, (ChestMenu.MenuClickHandler)new ChestMenu.MenuClickHandler() {
            public boolean onClick(final Player arg0, final int arg1, final ItemStack arg2, final ClickAction arg3) {
                return false;
            }
        });
        menu.addItem(9, (ItemStack)new CustomItem(new MaterialData(Material.GOLD_INGOT), "§7Price: §6$" + DoubleHandler.getFancyDouble(worth), new String[] { "", "§7Left Click: §r+0.1", "§7Shift + Left Click: §r+1", "§7Right Click: §r-0.1", "§7Shift + Right Click: §r-1" }));
        menu.addMenuClickHandler(9, (ChestMenu.MenuClickHandler)new ChestMenu.MenuClickHandler() {
            public boolean onClick(final Player p, final int slot, final ItemStack stack, final ClickAction action) {
                double price = worth;
                if (action.isRightClicked()) {
                    price -= (action.isShiftClicked() ? 1.0 : 0.1);
                }
                else {
                    price += (action.isShiftClicked() ? 1.0 : 0.1);
                }
                if (price <= 0.0) {
                    price = 0.1;
                }
                ShopEditor.this.openPriceEditor(p, shop, item, string, price);
                return false;
            }
        });
        menu.addItem(10, (ItemStack)new CustomItem(new MaterialData(Material.GOLD_INGOT), "§7Price: §6$" + DoubleHandler.getFancyDouble(worth), new String[] { "", "§7Left Click: §r+10", "§7Shift + Left Click: §r+100", "§7Right Click: §r-10", "§7Shift + Right Click: §r-100" }));
        menu.addMenuClickHandler(10, (ChestMenu.MenuClickHandler)new ChestMenu.MenuClickHandler() {
            public boolean onClick(final Player p, final int slot, final ItemStack stack, final ClickAction action) {
                double price = worth;
                if (action.isRightClicked()) {
                    price -= (action.isShiftClicked() ? 100 : 10);
                }
                else {
                    price += (action.isShiftClicked() ? 100 : 10);
                }
                if (price <= 0.0) {
                    price = 0.1;
                }
                ShopEditor.this.openPriceEditor(p, shop, item, string, price);
                return false;
            }
        });
        menu.addItem(11, (ItemStack)new CustomItem(new MaterialData(Material.GOLD_INGOT), "§7Price: §6$" + DoubleHandler.getFancyDouble(worth), new String[] { "", "§7Left Click: §r+1K", "§7Shift + Left Click: §r+10K", "§7Right Click: §r-1K", "§7Shift + Right Click: §r-10K" }));
        menu.addMenuClickHandler(11, (ChestMenu.MenuClickHandler)new ChestMenu.MenuClickHandler() {
            public boolean onClick(final Player p, final int slot, final ItemStack stack, final ClickAction action) {
                double price = worth;
                if (action.isRightClicked()) {
                    price -= (action.isShiftClicked() ? 10000 : 1000);
                }
                else {
                    price += (action.isShiftClicked() ? 10000 : 1000);
                }
                if (price <= 0.0) {
                    price = 0.1;
                }
                ShopEditor.this.openPriceEditor(p, shop, item, string, price);
                return false;
            }
        });
        menu.addItem(12, (ItemStack)new CustomItem(new MaterialData(Material.GOLD_INGOT), "§7Price: §6$" + DoubleHandler.getFancyDouble(worth), new String[] { "", "§7Left Click: §r+100K", "§7Shift + Left Click: §r+1M", "§7Right Click: §r-100K", "§7Shift + Right Click: §r-1M" }));
        menu.addMenuClickHandler(12, (ChestMenu.MenuClickHandler)new ChestMenu.MenuClickHandler() {
            public boolean onClick(final Player p, final int slot, final ItemStack stack, final ClickAction action) {
                double price = worth;
                if (action.isRightClicked()) {
                    price -= (action.isShiftClicked() ? 1000000 : 100000);
                }
                else {
                    price += (action.isShiftClicked() ? 1000000 : 100000);
                }
                if (price <= 0.0) {
                    price = 0.1;
                }
                ShopEditor.this.openPriceEditor(p, shop, item, string, price);
                return false;
            }
        });
        menu.addItem(13, (ItemStack)new CustomItem(new MaterialData(Material.GOLD_INGOT), "§7Price: §6$" + DoubleHandler.getFancyDouble(worth), new String[] { "", "§7Left Click: §r+10M", "§7Shift + Left Click: §r+100M", "§7Right Click: §r-10M", "§7Shift + Right Click: §r-100M" }));
        menu.addMenuClickHandler(13, (ChestMenu.MenuClickHandler)new ChestMenu.MenuClickHandler() {
            public boolean onClick(final Player p, final int slot, final ItemStack stack, final ClickAction action) {
                double price = worth;
                if (action.isRightClicked()) {
                    price -= (action.isShiftClicked() ? 100000000 : 10000000);
                }
                else {
                    price += (action.isShiftClicked() ? 100000000 : 10000000);
                }
                if (price <= 0.0) {
                    price = 0.1;
                }
                ShopEditor.this.openPriceEditor(p, shop, item, string, price);
                return false;
            }
        });
        menu.addItem(14, (ItemStack)new CustomItem(new MaterialData(Material.GOLD_INGOT), "§7Price: §6$" + DoubleHandler.getFancyDouble(worth), new String[] { "", "§7Left Click: §r+1B", "§7Shift + Left Click: §r+10B", "§7Right Click: §r-1B", "§7Shift + Right Click: §r-10B" }));
        menu.addMenuClickHandler(14, (ChestMenu.MenuClickHandler)new ChestMenu.MenuClickHandler() {
            public boolean onClick(final Player p, final int slot, final ItemStack stack, final ClickAction action) {
                double price = worth;
                if (action.isRightClicked()) {
                    price -= (action.isShiftClicked() ? 1.0E10 : 1.0E9);
                }
                else {
                    price += (action.isShiftClicked() ? 1.0E10 : 1.0E9);
                }
                if (price <= 0.0) {
                    price = 0.1;
                }
                ShopEditor.this.openPriceEditor(p, shop, item, string, price);
                return false;
            }
        });
        menu.addItem(15, (ItemStack)new CustomItem(new MaterialData(Material.GOLD_INGOT), "§7Price: §6$" + DoubleHandler.getFancyDouble(worth), new String[] { "", "§7Left Click: §r+100B", "§7Shift + Left Click: §r+1T", "§7Right Click: §r-100B", "§7Shift + Right Click: §r-1T" }));
        menu.addMenuClickHandler(15, (ChestMenu.MenuClickHandler)new ChestMenu.MenuClickHandler() {
            public boolean onClick(final Player p, final int slot, final ItemStack stack, final ClickAction action) {
                double price = worth;
                if (action.isRightClicked()) {
                    price -= (action.isShiftClicked() ? 1.0E12 : 1.0E11);
                }
                else {
                    price += (action.isShiftClicked() ? 1.0E12 : 1.0E11);
                }
                if (price <= 0.0) {
                    price = 0.1;
                }
                ShopEditor.this.openPriceEditor(p, shop, item, string, price);
                return false;
            }
        });
        menu.addItem(16, (ItemStack)new CustomItem(new MaterialData(Material.GOLD_INGOT), "§7Price: §6$" + DoubleHandler.getFancyDouble(worth), new String[] { "", "§7Left Click: §r+10T", "§7Shift + Left Click: §r+100T", "§7Right Click: §r-10T", "§7Shift + Right Click: §r-100T" }));
        menu.addMenuClickHandler(16, (ChestMenu.MenuClickHandler)new ChestMenu.MenuClickHandler() {
            public boolean onClick(final Player p, final int slot, final ItemStack stack, final ClickAction action) {
                double price = worth;
                if (action.isRightClicked()) {
                    price -= (action.isShiftClicked() ? 1.0E14 : 1.0E13);
                }
                else {
                    price += (action.isShiftClicked() ? 1.0E14 : 1.0E13);
                }
                if (price <= 0.0) {
                    price = 0.1;
                }
                ShopEditor.this.openPriceEditor(p, shop, item, string, price);
                return false;
            }
        });
        menu.addItem(17, (ItemStack)new CustomItem(new MaterialData(Material.GOLD_INGOT), "§7Price: §6$" + DoubleHandler.getFancyDouble(worth), new String[] { "", "§7Left Click: §r+1Q", "§7Shift + Left Click: §r+10Q", "§7Right Click: §r-1Q", "§7Shift + Right Click: §r-10Q" }));
        menu.addMenuClickHandler(17, (ChestMenu.MenuClickHandler)new ChestMenu.MenuClickHandler() {
            public boolean onClick(final Player p, final int slot, final ItemStack stack, final ClickAction action) {
                double price = worth;
                if (action.isRightClicked()) {
                    price -= (action.isShiftClicked() ? 1.0E16 : 1.0E15);
                }
                else {
                    price += (action.isShiftClicked() ? 1.0E16 : 1.0E15);
                }
                if (price <= 0.0) {
                    price = 0.1;
                }
                ShopEditor.this.openPriceEditor(p, shop, item, string, price);
                return false;
            }
        });
        menu.addItem(20, (ItemStack)new CustomItem(new MaterialData(Material.GREEN_WOOL, (byte)0), "§2Save", new String[0]));
        menu.addMenuClickHandler(20, (ChestMenu.MenuClickHandler)new ChestMenu.MenuClickHandler() {
            public boolean onClick(final Player p, final int arg1, final ItemStack arg2, final ClickAction arg3) {
                QuickSell.cfg.setValue("shops." + shop.getID() + ".price." + string, (Object)worth);
                QuickSell.cfg.save();
                ShopEditor.this.quicksell.reload();
                QuickSell.local.sendTranslation((CommandSender)p, "commands.price-set", false, new Variable[] { new Variable("%item%", string), new Variable("%shop%", shop.getName()), new Variable("%price%", DoubleHandler.getFancyDouble(worth)) });
                ShopEditor.this.openShopContentEditor(p, Shop.getShop(shop.getID()), 1);
                return false;
            }
        });
        menu.addItem(24, (ItemStack)new CustomItem(new MaterialData(Material.RED_WOOL, (byte)0), "§4Cancel", new String[0]));
        menu.addMenuClickHandler(24, (ChestMenu.MenuClickHandler)new ChestMenu.MenuClickHandler() {
            public boolean onClick(final Player p, final int arg1, final ItemStack arg2, final ClickAction arg3) {
                ShopEditor.this.openShopContentEditor(p, Shop.getShop(shop.getID()), 1);
                return false;
            }
        });
        menu.build().open(new Player[] { p });
    }

    public void openShopInheritanceEditor(final Player p, final Shop s) {
        this.quicksell.reload();
        final ChestMenu menu = new ChestMenu("&6QuickSell - Shop Editor");
        menu.addMenuOpeningHandler((ChestMenu.MenuOpeningHandler)new ChestMenu.MenuOpeningHandler() {
            public void onOpen(final Player p) {
                p.playSound(p.getLocation(), Soundboard.getLegacySounds(new String[] { "BLOCK_NOTE_PLING", "NOTE_PLING" }), 1.0f, 1.0f);
            }
        });
        for (int i = 0; i < 54 && Shop.list().size() > i; ++i) {
            final Shop shop = Shop.list().get(i);
            if (!shop.getID().equalsIgnoreCase(s.getID())) {
                final boolean inherit = QuickSell.cfg.getStringList("shops." + s.getID() + ".inheritance").contains(shop.getID());
                menu.addItem(i, (ItemStack)new CustomItem(shop.getItem(ShopStatus.UNLOCKED), shop.getName(), new String[] { "", "§7Inherit: " + (inherit ? "§2§l\u2714" : "§4§l\u2718"), "", "§7§oClick to toggle" }));
                menu.addMenuClickHandler(i, (ChestMenu.MenuClickHandler)new ChestMenu.MenuClickHandler() {
                    public boolean onClick(final Player p, final int slot, final ItemStack item, final ClickAction action) {
                        final List<String> shops = (List<String>)QuickSell.cfg.getStringList("shops." + s.getID() + ".inheritance");
                        if (inherit) {
                            shops.remove(shop.getID());
                        }
                        else {
                            shops.add(shop.getID());
                        }
                        QuickSell.cfg.setValue("shops." + s.getID() + ".inheritance", (Object)shops);
                        QuickSell.cfg.save();
                        ShopEditor.this.openShopInheritanceEditor(p, Shop.getShop(s.getID()));
                        return false;
                    }
                });
            }
        }
        menu.build().open(new Player[] { p });
    }
}


