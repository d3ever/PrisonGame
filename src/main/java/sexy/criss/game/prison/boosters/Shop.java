/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  me.mrCookieSlime.CSCoreLibPlugin.Configuration.Variable
 *  me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.InvUtils
 *  me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem
 *  me.mrCookieSlime.CSCoreLibPlugin.general.Math.DoubleHandler
 *  me.mrCookieSlime.CSCoreLibPlugin.general.Player.PlayerInventory
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.Sound
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.material.MaterialData
 */
package sexy.criss.game.prison.boosters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Variable;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.InvUtils;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem;
import me.mrCookieSlime.CSCoreLibPlugin.general.Math.DoubleHandler;
import me.mrCookieSlime.CSCoreLibPlugin.general.Player.PlayerInventory;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import sexy.criss.game.prison.prison_data.PrisonPlayer;
import sexy.criss.gen.util.Util;

public class Shop
{
    private static final List<Shop> shops;
    private static Map<String, Shop> map;
    String shop;
    String permission;
    PriceInfo prices;
    ItemStack unlocked;
    ItemStack locked;
    String name;

    public Shop(final String id) {
        this.shop = id;
        this.prices = new PriceInfo(this);
        this.name = QuickSell.cfg.getString("shops." + this.shop + ".name");
        this.permission = QuickSell.cfg.getString("shops." + this.shop + ".permission");
        List<String> lore = new ArrayList<String>();
        lore.add("");
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7<&a&l Click to open &7>"));
        for (final String line : QuickSell.cfg.getStringList("shops." + this.shop + ".lore")) {
            lore.add(ChatColor.translateAlternateColorCodes('&', line));
        }
        MaterialData md = null;
        if (QuickSell.cfg.getString("shops." + this.shop + ".itemtype").contains("-")) {
            md = new MaterialData(Material.getMaterial(QuickSell.cfg.getString("shops." + this.shop + ".itemtype").split("-")[0]), (byte)Integer.parseInt(QuickSell.cfg.getString("shops." + this.shop + ".itemtype").split("-")[1]));
        }
        else {
            md = new MaterialData(Material.getMaterial(QuickSell.cfg.getString("shops." + this.shop + ".itemtype")));
        }
        this.unlocked = (ItemStack)new CustomItem(md, this.name, (String[])lore.toArray(new String[lore.size()]));
        lore = new ArrayList<String>();
        lore.add(ChatColor.translateAlternateColorCodes('&', (String)QuickSell.local.getTranslation("messages.no-access").get(0)));
        for (final String line2 : QuickSell.cfg.getStringList("shops." + this.shop + ".lore")) {
            lore.add(ChatColor.translateAlternateColorCodes('&', line2));
        }
        MaterialData md2 = null;
        if (QuickSell.cfg.getString("options.locked-item").contains("-")) {
            md2 = new MaterialData(Material.getMaterial(QuickSell.cfg.getString("options.locked-item").split("-")[0]), (byte)Integer.parseInt(QuickSell.cfg.getString("options.locked-item").split("-")[1]));
        }
        else {
            md2 = new MaterialData(Material.getMaterial(QuickSell.cfg.getString("options.locked-item")));
        }
        try {
            this.locked = new CustomItem(Material.CHEST, this.name, 0, lore.toArray(new String[0]));
            Shop.shops.add(this);
            Shop.map.put(this.shop.toLowerCase(), this);
        }catch (Exception ex) {
            System.out.println(QuickSell.cfg.getString("shops." + this.shop + ".itemtype"));
            ex.printStackTrace();
        }
    }

    public Shop() {
        Shop.shops.add(null);
    }

    public boolean hasUnlocked(final Player p) {
        return this.permission.equalsIgnoreCase("") || p.hasPermission(this.permission);
    }

    public static void reset() {
        Shop.shops.clear();
        Shop.map.clear();
    }

    public static List<Shop> list() {
        return Shop.shops;
    }

    public static Shop getHighestShop(final Player p) {
        for (int i = Shop.shops.size() - 1; i >= 0; --i) {
            if (Shop.shops.get(i) != null && Shop.shops.get(i).hasUnlocked(p)) {
                return Shop.shops.get(i);
            }
        }
        return null;
    }

    public String getID() {
        return this.shop;
    }

    public String getPermission() {
        return this.permission;
    }

    public static Shop getShop(final String id) {
        return Shop.map.get(id.toLowerCase());
    }

    public PriceInfo getPrices() {
        return this.prices;
    }

    public void sellall(final Player p, final String item) {
        this.sellall(p, item, SellEvent.Type.UNKNOWN);
    }

    public void sellall(final Player p, final String item, final SellEvent.Type type) {
        final List<ItemStack> items = new ArrayList<ItemStack>();
        for (int slot = 0; slot < p.getInventory().getSize(); ++slot) {
            final ItemStack is = p.getInventory().getItem(slot);
            if (this.getPrices().getPrice(is) > 0.0) {
                items.add(is);
                p.getInventory().setItem(slot, null);
            }
        }
        PlayerInventory.update(p);
        this.sell(p, false, type, items.toArray(new ItemStack[0]));
    }

    public void sell(final Player p, final boolean silent, final ItemStack... soldItems) {
        this.sell(p, silent, SellEvent.Type.UNKNOWN, soldItems);
    }

    public void sell(final Player p, final boolean silent, final SellEvent.Type type, final ItemStack... soldItems) {
        if (soldItems.length == 0) {
            if (!silent) {
                Util.ps("Prison", p, "&fУ вас нет предметов, которые можно продать.");
            }
        }
        else {
            double money = 0.0;
            int sold = 0;
            int total = 0;
            for (final ItemStack item : soldItems) {
                if (item != null) {
                    total += item.getAmount();
                    if (this.getPrices().getPrice(item) > 0.0) {
                        sold += item.getAmount();
                        money += this.getPrices().getPrice(item);
                    }
                    else if (InvUtils.fits(p.getInventory(), item)) {
                        p.getInventory().addItem(item);
                    }
                    else {
                        p.getWorld().dropItemNaturally(p.getLocation(), item);
                    }
                }
            }
            money = DoubleHandler.fixDouble(money, 2);
            if (money > 0.0) {
                final double totalmoney = this.handoutReward(p, money, sold, silent);
                if (!silent) {
                    if (QuickSell.cfg.getBoolean("sound.enabled")) {
                        p.playSound(p.getLocation(), Sound.valueOf(QuickSell.cfg.getString("sound.sound")), 1.0f, 1.0f);
                    }
                    for (String cmd : QuickSell.cfg.getStringList("commands-on-sell")) {
                        final String command = cmd;
                        if (cmd.contains("{PLAYER}")) {
                            cmd = cmd.replace("{PLAYER}", p.getName());
                        }
                        if (cmd.contains("{MONEY}")) {
                            cmd = cmd.replace("{MONEY}", String.valueOf(DoubleHandler.fixDouble(totalmoney, 2)));
                        }
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
                    }
                }
                for (final SellEvent event : QuickSell.getSellEvents()) {
                    event.onSell(p, type, total, totalmoney);
                }
            }
            else if (!silent && total > 0) {
                QuickSell.local.sendTranslation(p, "messages.get-nothing", false, new Variable[0]);
            }
            if (!silent && sold < total && total > 0) {
                QuickSell.local.sendTranslation(p, "messages.dropped", false, new Variable[0]);
            }
        }
        PlayerInventory.update(p);
    }

    public double handoutReward(final Player p, final double totalmoney, final int items, final boolean silent) {
        double money = totalmoney;
//        if (!silent) QuickSell.local.sendTranslation(p, "messages.sell", false, new Variable("{MONEY}", DoubleHandler.getFancyDouble(money)), new Variable("{ITEMS}", String.valueOf(items)));
        boolean has = !Booster.getBoosters(p.getName()).isEmpty() && Booster.getBoosters(p.getName(), Booster.BoosterType.MONETARY).size() > 0;
        if(!silent) {
            if(!has) Util.ps("Prison", p, "Вы продали предметов на &6"+DoubleHandler.getFancyDouble(money)+"$&f.");
            else Util.ps("Prison", p, "Без множителя сумма проданных предметов &6"+DoubleHandler.getFancyDouble(money)+"$&f.");
        }
        for (Booster booster : Booster.getBoosters(p.getName())) {
            if (booster.getType().equals(Booster.BoosterType.MONETARY)) {
                if (!silent) {
//                    booster.sendMessage(p, new Variable("{MONEY}", DoubleHandler.getFancyDouble(money * (booster.getMultiplier() - 1.0))));
                }
                money += money * (booster.getMultiplier() - 1.0);
            }
        }
        money = DoubleHandler.fixDouble(money);

        if(!silent) {
            if(has) {
                Util.ps("Prison", p, "&fС множителем вам было зачисленно &6"+money+"$&f.");
            }
        }
        PrisonPlayer.getPlayer(p.getUniqueId()).addGold(money);
        return money;
    }

    public ItemStack getItem(final ShopStatus status) {
        switch (status) {
            case LOCKED: {
                return this.locked;
            }
            case UNLOCKED: {
                return this.unlocked;
            }
            default: {
                return null;
            }
        }
    }

    public String getName() {
        return ChatColor.translateAlternateColorCodes('&', this.name);
    }

    public void showPrices(final Player p) {
        ShopMenu.openPrices(p, this, 1);
    }

    static {
        shops = new ArrayList<Shop>();
        Shop.map = new HashMap<String, Shop>();
    }
}


