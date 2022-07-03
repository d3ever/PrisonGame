package sexy.criss.game.prison.commands.blocks;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import me.mrCookieSlime.CSCoreLibPlugin.general.Math.DoubleHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import sexy.criss.game.prison.boosters.Shop;
import sexy.criss.game.prison.prison_data.PrisonPlayer;
import sexy.criss.game.prison.utilities.SkullCreator;
import sexy.criss.gen.api.GENAPI;
import sexy.criss.gen.api.inventory.DynamicInventory;
import sexy.criss.gen.api.inventory.DynamicItem;
import sexy.criss.gen.util.LoreCreator;
import sexy.criss.gen.util.Util;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public final record BlocksGui (Map<Material, Integer> blocks, Player p) {

    private Map<Integer, Map<Integer, ItemStack>> getPages() {
        PrisonPlayer pp = PrisonPlayer.getPlayer(p.getUniqueId());
        Map<Integer, Map<Integer, ItemStack>> items = Maps.newHashMap();
        int page = 1;
        int c = 0;
        for(Map.Entry<String, Double> m : Shop.getShop("a").getPrices().getPrices().entrySet()) {
            if(c >= 45) {
                page++;
                c=0;
            }

            if(!items.containsKey(page)) items.put(page, Maps.newHashMap());

            double v;
            try { v = DoubleHandler.fixDouble(m.getValue()); } catch (NullPointerException ex) { v = 0; }

            items.get(page).put(c, Util.createItem(Material.getMaterial(m.getKey()), ChatColor.GOLD + m.getKey(),
                    new LoreCreator()
                    .addEmpty()
                    .addLine("&fЦена продажи &6"+v)
                    .addLinesIf(Material.getMaterial(m.getKey()).isBlock(), Lists.newArrayList("", "&fВы добыли &6"+pp.getBlock_log().getOrDefault(Material.getMaterial(m.getKey()), 0))).build()));
            c++;
        }
        items.values().forEach(itemStacks -> System.out.println(itemStacks.size()));
        return items;
    }

    public void handle() {
        Map<Integer, Map<Integer, ItemStack>> content = getPages();
        int max = content.keySet().size();
        AtomicInteger current = new AtomicInteger(1);
        boolean hasNext = current.get() +1 <= max;
        boolean hasBack = current.get() -1 >= 1;
        boolean single = max == 1;

        DynamicInventory inv = GENAPI.getInventoryAPI().createNewInventory("&0ЦЕНОВАЯ ПОЛИТИКА", 6);
        DynamicItem em = new DynamicItem(Util.createItem(Material.GRAY_STAINED_GLASS_PANE, "", Lists.newArrayList("")), (p1, clickType, slot) -> {});
        for(int i = 0; i < inv.getSize(); i++) { switch (i) { case 45,46,47,48,49,50,51 -> inv.addItem(i, em); } }
        content.get(current.get()).forEach((s, stack) -> {
            inv.addItem(s, new DynamicItem(stack, (p1, clickType, slot1) -> {}));
        });
        DynamicItem next = new DynamicItem(SkullCreator.itemWithBase64(Util.createItem(SkullCreator.getSkullMaterial(), "&bСледующая страница", Lists.newArrayList(
                "",
                "&7Нажмите для перехода"
        ), 1, (short) 3), SkullCreator.ARROW_RIGHT_BASE64), (p1, clickType, slot) -> {
                clear(inv);
                content.get(2).forEach((c, v) -> {
                    inv.addItem(c, new DynamicItem(v, (p2, clickType1, slot1) -> {
                    }));
                });
        });
        DynamicItem back = new DynamicItem(SkullCreator.itemWithBase64(Util.createItem(SkullCreator.getSkullMaterial(), "&bПредыдущая страница", Lists.newArrayList(
                "",
                "&7Нажмите для перехода"
        ), 1, (short) 3), SkullCreator.ARROW_LEFT_BASE64), (p1, clickType, slot) -> {
                clear(inv);
                content.get(1).forEach((c, s) -> {
                    inv.addItem(c, new DynamicItem(s, (p2, clickType1, slot1) -> {}));
                });
        });

        inv.addItem(6, 8, back);
        inv.addItem(6, 9, next);
        inv.open(p);
    }

    private void clear(DynamicInventory inv) {
        for(int i = 0; i < inv.getSize(); i++) {
            switch (i) {
                case 45,46,47,48,49,50,51,52,53:continue;
                default: inv.addItem(i, new DynamicItem(new ItemStack(Material.AIR), (p1, clickType, slot) -> {}));
            }
        }
    }

}
