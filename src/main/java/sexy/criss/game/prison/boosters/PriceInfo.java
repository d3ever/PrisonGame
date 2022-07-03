package sexy.criss.game.prison.boosters;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem;
import me.mrCookieSlime.CSCoreLibPlugin.general.Math.DoubleHandler;
import me.mrCookieSlime.CSCoreLibPlugin.general.String.StringUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import sexy.criss.gen.util.Util;

public class PriceInfo {
    Shop shop;
    Map<String, Double> prices;
    Map<String, ItemStack> info;
    List<String> order;
    int amount;
    private static final Map<String, PriceInfo> map = new HashMap<String, PriceInfo>();

    public PriceInfo(Shop shop) {
        this.shop = shop;
        this.prices = new HashMap<String, Double>();
        this.order = new ArrayList<String>();
        this.amount = QuickSell.cfg.getInt("shops." + shop.getID() + ".amount");
        for (String key : QuickSell.cfg.getConfiguration().getConfigurationSection("shops." + shop.getID() + ".price").getKeys(false)) {
            if (this.prices.containsKey(key)) continue;
            StringBuilder stringBuilder = new StringBuilder();
            if (!(QuickSell.cfg.getDouble(stringBuilder.append("shops.").append(shop.getID()).append(".price.").append(key).toString()) > 0.0)) continue;
            this.prices.put(key, QuickSell.cfg.getDouble("shops." + shop.getID() + ".price." + key) / (double)this.amount);
        }
        for (String parent : QuickSell.cfg.getStringList("shops." + shop.getID() + ".inheritance")) {
            this.loadParent(parent);
        }
        this.info = new HashMap<String, ItemStack>();
        for (String item : this.prices.keySet()) {
            if (this.info.size() >= 54) break;
            if (Material.getMaterial(item) != null) {
                this.info.put(item, new CustomItem(Material.getMaterial(item), Util.f(Material.getMaterial(item).name()), 0, new String[]{"", "&7Worth (1): &6" + DoubleHandler.getFancyDouble(this.getPrices().get(item)), "&7Worth (64): &6" + DoubleHandler.getFancyDouble((this.getPrices().get(item) * 64.0))}));
                this.order.add(item);
                continue;
            }
            if (item.split("-").length > 1) {
                boolean data = true;
                try {
                    Integer.parseInt(item.split("-")[1]);
                }
                catch (NumberFormatException x) {
                    data = false;
                }
                if (Material.getMaterial(item.split("-")[0]) != null) {
                    if (data) {
                        this.info.put(item, new CustomItem(new CustomItem(new MaterialData(Material.getMaterial(item.split("-")[0]), (byte)Integer.parseInt(item.split("-")[1])), "\u00a7r" + StringUtils.formatItemName(new MaterialData(Material.getMaterial(item.split("-")[0]), (byte)Integer.parseInt(item.split("-")[1])).toItemStack(1), false), "", "&7Worth (1): &6" + DoubleHandler.getFancyDouble(this.getPrices().get(item)), "&7Worth (64): &6" + DoubleHandler.getFancyDouble((this.getPrices().get(item) * 64.0)))));
                        this.order.add(item);
                        continue;
                    }
                    if (!item.split("-")[1].equals("nodata")) {
                        this.info.put(item, new CustomItem(new CustomItem(Material.getMaterial(item.split("-")[0]), item.split("-")[1], 0, new String[]{"", "&7Worth (1): &6" + DoubleHandler.getFancyDouble(this.getPrices().get(item)), "&7Worth (64): &6" + DoubleHandler.getFancyDouble((this.getPrices().get(item) * 64.0))}), this.getAmount()));
                        this.order.add(item);
                        continue;
                    }
                    this.info.put(item, new CustomItem(Material.getMaterial(item.split("-")[0]), "\u00a7r" + StringUtils.formatItemName(new ItemStack(Material.getMaterial(item.split("-")[0])), false), 0, new String[]{"", "&7Worth (1): &6" + DoubleHandler.getFancyDouble(this.getPrices().get(item)), "&7Worth (64): &6" + DoubleHandler.getFancyDouble((this.getPrices().get(item) * 64.0))}));
                    this.order.add(item);
                    continue;
                }
                System.err.println("[QuickSell] Could not recognize Item String: \"" + item + "\"");
                continue;
            }
            System.err.println("[QuickSell] Could not recognize Item String: \"" + item + "\"");
        }
        map.put(shop.getID(), this);
    }

    private void loadParent(String parent) {
        for (String key : QuickSell.cfg.getKeys("shops." + parent + ".price")) {
            if (this.prices.containsKey(key)) continue;
            StringBuilder stringBuilder = new StringBuilder();
            if (!(QuickSell.cfg.getDouble(stringBuilder.append("shops.").append(parent).append(".price.").append(key).toString()) > 0.0)) continue;
            this.prices.put(key, QuickSell.cfg.getDouble("shops." + parent + ".price." + key) / (double)this.amount);
        }
        for (String p : QuickSell.cfg.getStringList("shops." + parent + ".inheritance")) {
            this.loadParent(p);
        }
    }

    public PriceInfo(String shop) {
        this.prices = new HashMap<String, Double>();
        for (String key : QuickSell.cfg.getConfiguration().getConfigurationSection("shops." + shop + ".price").getKeys(false)) {
            if (this.prices.containsKey(key)) continue;
            StringBuilder stringBuilder = new StringBuilder();
            if (!(QuickSell.cfg.getDouble(stringBuilder.append("shops.").append(shop).append(".price.").append(key).toString()) > 0.0)) continue;
            this.prices.put(key, QuickSell.cfg.getDouble("shops." + shop + ".price." + key) / (double)this.amount);
        }
        for (String parent : QuickSell.cfg.getStringList("shops." + shop + ".inheritance")) {
            for (String key : PriceInfo.getInfo(parent).getPrices().keySet()) {
                if (this.prices.containsKey(key)) continue;
                StringBuilder stringBuilder = new StringBuilder();
                if (!(QuickSell.cfg.getDouble(stringBuilder.append("shops.").append(parent).append(".price.").append(key).toString()) > 0.0)) continue;
                this.prices.put(key, QuickSell.cfg.getDouble("shops." + parent + ".price." + key) / (double)this.amount);
            }
        }
    }

    public Map<String, Double> getPrices() {
        return this.prices;
    }

    public double getPrice(ItemStack item) {
        if (item == null) {
            return 0.0;
        }
        String string = this.toString(item);
        if (this.prices.containsKey(string)) {
            return DoubleHandler.fixDouble((double)(this.prices.get(string) * (double)item.getAmount()));
        }
        return 0.0;
    }

    public double getPrice(String string) {
        return this.prices.get(string);
    }

    public String toString(ItemStack item) {
        String name;
        if (item == null) {
            return "null";
        }
        name = item.hasItemMeta() ? (item.getItemMeta().hasDisplayName() ? item.getItemMeta().getDisplayName().replace("\u00a7", "&") : "") : "";
        if (!name.equalsIgnoreCase("") && this.prices.containsKey(item.getType().toString() + "-" + name)) {
            return item.getType().toString() + "-" + name;
        }
        if (item.isSimilar(item.getData().toItemStack(item.getAmount())) && this.prices.containsKey(item.getType().toString() + "-" + item.getData().getData() + "-nodata")) {
            return item.getType().toString() + "-" + item.getData().getData() + "-nodata";
        }
        if (item.isSimilar(new ItemStack(item.getType(), item.getAmount())) && this.prices.containsKey(item.getType().toString() + "-nodata")) {
            return item.getType().toString() + "-nodata";
        }
        if (this.prices.containsKey(item.getType().toString() + "-" + item.getData().getData())) {
            return item.getType().toString() + "-" + item.getData().getData();
        }
        if (this.prices.containsKey(item.getType().toString())) {
            return item.getType().toString();
        }
        return "null";
    }

    public static PriceInfo getInfo(String shop) {
        return map.containsKey(shop) ? map.get(shop) : new PriceInfo(shop);
    }

    public int getAmount() {
        return this.amount;
    }

    public Collection<ItemStack> getInfo() {
        return this.info.values();
    }

    public List<String> getItems() {
        return this.order;
    }

    public ItemStack getItem(String string) {
        return this.info.get(string);
    }
}

