/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu
 *  me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem
 *  me.mrCookieSlime.CSCoreLibPlugin.general.String.StringUtils
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.material.MaterialData
 */
package sexy.criss.game.prison.boosters;

import java.util.ArrayList;
import java.util.Map;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem;
import me.mrCookieSlime.CSCoreLibPlugin.general.String.StringUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

public class BoosterMenu {
    static int moneySlot = 2;
    static int blockSlot = 4;
    static int keyBlock = 6;

    public static void showBoosterOverview(Player p) {
        ChestMenu menu = new ChestMenu("\u00a7c\u0410\u043a\u0442\u0438\u0432\u043d\u044b\u0435 \u0411\u0443\u0441\u0442\u0435\u0440\u044b");
        menu.addItem(moneySlot, (ItemStack)new CustomItem(new MaterialData(Material.GOLD_INGOT), "\u00a7b\u0414\u0435\u043d\u0435\u0436\u043d\u044b\u0435 \u0411\u0443\u0441\u0442\u0435\u0440\u044b", new String[]{"\u00a77\u0422\u0435\u043a\u0443\u0449\u0438\u0439 \u041c\u043d\u043e\u0436\u0438\u0442\u0435\u043b\u044c: \u00a7b" + Booster.getMultiplier(p.getName(), Booster.BoosterType.MONETARY), "", "\u00a77\u21e8 \u041d\u0430\u0436\u043c\u0438\u0442\u0435 \u0434\u043b\u044f \u043f\u043e\u043b\u0443\u0447\u0435\u043d\u0438\u044f \u043f\u043e\u0434\u0440\u043e\u0431\u043d\u043e\u0441\u0442\u0435\u0439"}));
        menu.addMenuClickHandler(moneySlot, (player, slot, item, action) -> {
            BoosterMenu.showBoosterDetails(p, Booster.BoosterType.MONETARY);
            return false;
        });
        menu.addItem(blockSlot, (ItemStack)new CustomItem(new MaterialData(Material.DIAMOND_SHOVEL), "\u00a7b\u0411\u0443\u0441\u0442\u0435\u0440\u044b \u0411\u043b\u043e\u043a\u043e\u0432", new String[]{"\u00a77\u0422\u0435\u043a\u0443\u0449\u0438\u0439 \u041c\u043d\u043e\u0436\u0438\u0442\u0435\u043b\u044c: \u00a7b" + Booster.getMultiplier(p.getName(), Booster.BoosterType.BLOCKS), "", "\u00a77\u21e8 \u041d\u0430\u0436\u043c\u0438\u0442\u0435 \u0434\u043b\u044f \u043f\u043e\u043b\u0443\u0447\u0435\u043d\u0438\u044f \u043f\u043e\u0434\u0440\u043e\u0431\u043d\u043e\u0441\u0442\u0435\u0439"}));
        menu.addMenuClickHandler(blockSlot, (player, slot, item, action) -> {
            BoosterMenu.showBoosterDetails(p, Booster.BoosterType.BLOCKS);
            return false;
        });
        menu.addItem(keyBlock, (ItemStack)new CustomItem(new MaterialData(Material.GHAST_TEAR), "\u00a7b\u0411\u0443\u0441\u0442\u0435\u0440\u044b \u041a\u043b\u044e\u0447\u0435\u0439", new String[]{"\u00a77\u0422\u0435\u043a\u0443\u0449\u0438\u0439 \u041c\u043d\u043e\u0436\u0438\u0442\u0435\u043b\u044c: \u00a7b" + Booster.getMultiplier(p.getName(), Booster.BoosterType.KEYS), "", "\u00a77\u21e8 \u041d\u0430\u0436\u043c\u0438\u0442\u0435 \u0434\u043b\u044f \u043f\u043e\u043b\u0443\u0447\u0435\u043d\u0438\u044f \u043f\u043e\u0434\u0440\u043e\u0431\u043d\u043e\u0441\u0442\u0435\u0439"}));
        menu.addMenuClickHandler(keyBlock, (player, slot, item, action) -> {
            BoosterMenu.showBoosterDetails(p, Booster.BoosterType.KEYS);
            return false;
        });
        menu.build().open(new Player[]{p});
    }

    public static void showBoosterDetails(Player p, Booster.BoosterType type) {
        ChestMenu menu = new ChestMenu("\u00a7c\u0410\u043a\u0442\u0438\u0432\u043d\u044b\u0435 " + StringUtils.format((String)type.toString()).replace("Monetary", "\u0414\u0435\u043d\u0435\u0436\u043d\u044b\u0435").replace("Blocks", "\u0411\u043b\u043e\u043a\u043e\u0432").replace("Keys", "\u041a\u043b\u044e\u0447\u0435\u0439") + " \u0411\u0443\u0441\u0442\u0435\u0440\u044b");
        menu.addItem(moneySlot, (ItemStack)new CustomItem(new MaterialData(Material.GOLD_INGOT), "\u00a7b\u0414\u0435\u043d\u0435\u0436\u043d\u044b\u0435 \u0411\u0443\u0441\u0442\u0435\u0440\u044b", new String[]{"\u00a77\u0422\u0435\u043a\u0443\u0449\u0438\u0439 \u041c\u043d\u043e\u0436\u0438\u0442\u0435\u043b\u044c: \u00a7b" + Booster.getMultiplier(p.getName(), Booster.BoosterType.MONETARY), "", "\u00a77\u21e8 \u041d\u0430\u0436\u043c\u0438\u0442\u0435 \u0434\u043b\u044f \u043f\u043e\u043b\u0443\u0447\u0435\u043d\u0438\u044f \u043f\u043e\u0434\u0440\u043e\u0431\u043d\u043e\u0441\u0442\u0435\u0439"}));
        menu.addMenuClickHandler(moneySlot, (player, slot, item, action) -> {
            BoosterMenu.showBoosterDetails(p, Booster.BoosterType.MONETARY);
            return false;
        });
        menu.addItem(blockSlot, (ItemStack)new CustomItem(new MaterialData(Material.DIAMOND_SHOVEL), "\u00a7b\u0411\u0443\u0441\u0442\u0435\u0440\u044b \u0411\u043b\u043e\u043a\u043e\u0432", new String[]{"\u00a77\u0422\u0435\u043a\u0443\u0449\u0438\u0439 \u041c\u043d\u043e\u0436\u0438\u0442\u0435\u043b\u044c: \u00a7b" + Booster.getMultiplier(p.getName(), Booster.BoosterType.BLOCKS), "", "\u00a77\u21e8 \u041d\u0430\u0436\u043c\u0438\u0442\u0435 \u0434\u043b\u044f \u043f\u043e\u043b\u0443\u0447\u0435\u043d\u0438\u044f \u043f\u043e\u0434\u0440\u043e\u0431\u043d\u043e\u0441\u0442\u0435\u0439"}));
        menu.addMenuClickHandler(blockSlot, (player, slot, item, action) -> {
            BoosterMenu.showBoosterDetails(p, Booster.BoosterType.BLOCKS);
            return false;
        });
        menu.addItem(keyBlock, (ItemStack)new CustomItem(new MaterialData(Material.GHAST_TEAR), "\u00a7b\u0411\u0443\u0441\u0442\u0435\u0440\u044b \u041a\u043b\u044e\u0447\u0435\u0439", new String[]{"\u00a77\u0422\u0435\u043a\u0443\u0449\u0438\u0439 \u041c\u043d\u043e\u0436\u0438\u0442\u0435\u043b\u044c: \u00a7b" + Booster.getMultiplier(p.getName(), Booster.BoosterType.KEYS), "", "\u00a77\u21e8 \u041d\u0430\u0436\u043c\u0438\u0442\u0435 \u0434\u043b\u044f \u043f\u043e\u043b\u0443\u0447\u0435\u043d\u0438\u044f \u043f\u043e\u0434\u0440\u043e\u0431\u043d\u043e\u0441\u0442\u0435\u0439"}));
        menu.addMenuClickHandler(keyBlock, (player, slot, item, action) -> {
            BoosterMenu.showBoosterDetails(player, Booster.BoosterType.KEYS);
            return false;
        });
        int index = 9;
        for (Booster booster : Booster.getBoosters(p.getName(), type)) {
            menu.addItem(index, BoosterMenu.getBoosterItem(booster));
            menu.addMenuClickHandler(index, (player, slot, item, action) -> false);
            ++index;
        }
        menu.build().open(new Player[]{p});
    }

    public static ItemStack getBoosterItem(Booster booster) {
        ArrayList<String> lore = new ArrayList<String>();
        lore.add("");
        lore.add("\u00a77\u0423\u043c\u043d\u043e\u0436\u0438\u0442\u0435\u043b\u044c: \u00a7e" + booster.getMultiplier() + "x");
        lore.add("\u00a77\u0412\u0440\u0435\u043c\u0435\u043d\u0438 \u041e\u0441\u0442\u0430\u043b\u043e\u0441\u044c: \u00a7e" + (booster.isInfinite() ? "\u0411\u0435\u0441\u043a\u043e\u043d\u0435\u0447\u043d\u044b\u0439" : booster.formatTime() + "m"));
        lore.add("\u00a77\u0413\u043b\u043e\u0431\u0430\u043b\u044c\u043d\u044b\u0439: " + (booster.isPrivate() ? "\u00a7c\u00a7l\u2718" : "\u00a72\u00a7l\u2714"));
        lore.add("");
        lore.add("\u00a77\u0421\u043f\u043e\u043d\u0441\u043e\u0440\u044b:");
        for (Map.Entry<String, Integer> entry : booster.getContributors().entrySet()) {
            lore.add(" \u00a78\u21e8 \u00a77" + entry.getKey() + ": \u00a78+" + entry.getValue() + " \u043c\u0438\u043d\u0443\u0442.");
        }
        return new CustomItem(new MaterialData(Material.EXPERIENCE_BOTTLE), "\u00a7b" + booster.getUniqueName() + " \u00a73" + booster.getMultiplier() + "x", lore.toArray(new String[lore.size()]));
    }

    public static String getTellRawMessage(Booster booster) {
        StringBuilder builder = new StringBuilder("\u00a7b" + booster.getUniqueName() + " \u00a73" + booster.getMultiplier() + "x\n \n");
        builder.append("\u00a77\u0423\u043c\u043d\u043e\u0436\u0438\u0442\u0435\u043b\u044c: \u00a7e" + booster.getMultiplier() + "x\n");
        builder.append("\u00a77\u0412\u0440\u0435\u043c\u0435\u043d\u0438 \u041e\u0441\u0442\u0430\u043b\u043e\u0441\u044c: \u00a7e" + (booster.isInfinite() ? "\u0411\u0435\u0441\u043a\u043e\u043d\u0435\u0447\u043d\u044b\u0439" : booster.formatTime() + " \u043c\u0438\u043d\u0443\u0442.") + "\n");
        builder.append("\u00a77\u0413\u043b\u043e\u0431\u0430\u043b\u044c\u043d\u044b\u0439: " + (booster.isPrivate() ? "\u00a7c\u00a7l\u2718" : "\u00a72\u00a7l\u2714") + "\n\n\u00a77\u0421\u043f\u043e\u043d\u0441\u043e\u0440\u044b:\n");
        for (Map.Entry<String, Integer> entry : booster.getContributors().entrySet()) {
            builder.append(" \u00a78\u21e8 \u00a77" + entry.getKey() + ": \u00a78+" + entry.getValue() + "m\n");
        }
        return builder.toString();
    }
}

