package sexy.criss.game.prison.listener.players;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import sexy.criss.game.prison.prison_data.PrisonItem;
import sexy.criss.game.prison.prison_data.PrisonPlayer;
import sexy.criss.gen.api.GENAPI;
import sexy.criss.gen.api.inventory.DynamicInventory;
import sexy.criss.gen.api.inventory.DynamicItem;
import sexy.criss.gen.util.Util;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

public record UpgradeGui(Player p, PrisonPlayer pp) {

    public void handle() {
        DynamicInventory inv = GENAPI.getInventoryAPI().createNewInventory("&0СНАРЯЖЕНИЕ", 2);
        String sword = "sword", pickaxe = "pickaxe", rarepickaxe = "newpickaxe", luckpickaxe = "pickaxetwo", spade = "spade", axe = "axe";
        String bow = "bow", fbow = "firebow", cap = "cap", chest = "chest", leggs = "legg", boots = "boots", fsword = "firesword";
        String ver_pickaxe = getIds().contains("pickaxetwo") ? luckpickaxe : getIds().contains("newpickaxe") ? rarepickaxe : pickaxe;
        String ver_bow = getIds().contains("firebow") ? fbow : bow;
        String ver_sword = getIds().contains("firesword") ? fsword : sword;

        inv.addItem(1, 2, getDynamicItem(cap, inv, 1, 2));
        inv.addItem(1, 3, getDynamicItem(chest, inv, 1, 3));
        inv.addItem(1, 4, getDynamicItem(leggs, inv, 1, 4));
        inv.addItem(1, 5, getDynamicItem(boots, inv, 1, 5));

        inv.addItem(2, 2, getDynamicItem(ver_sword, inv, 2, 2));
        inv.addItem(2, 3, getDynamicItem(ver_bow, inv, 2, 3));
        inv.addItem(2, 4, getDynamicItem(ver_pickaxe, inv, 2, 4));
        inv.addItem(2, 5, getDynamicItem(spade, inv, 2, 5));
        inv.addItem(2, 6, getDynamicItem(axe, inv, 2, 6));

        inv.open(p);
    }

    private DynamicItem getEmpty() {
        return new DynamicItem(Material.BARRIER, "&6Не открыт", Lists.newArrayList(), (p1, clickType, slot) -> {
        });
    }

    private DynamicItem getDynamicItem(String id, DynamicInventory inv, int r, int s) {
        if(getPrisonItem(id) == null) return getEmpty();
        if(getUpgrade(getPrisonItem(id).getValue()) == null) return getEmpty();
        Map.Entry<Integer, PrisonItem> entry = getPrisonItem(id);
        PrisonItem pi = entry.getValue();
        if(pi == null) return getEmpty();
        boolean max = !pi.hasNext();
        ItemStack ico = pi.getUpgradingItem(pp);
        return new DynamicItem(pi.getUpgradingItem(pp), (p1, clickType, slot) -> {
            if(!max) {
                PrisonItem next = pi.getNext();
                if(next == null) return;
                if(!pp.hasMoney(next.getPrice())) {
                    Util.ps("Prison", p, "&fУ вас недостаточно средств.");
                    return;
                }
                AtomicBoolean allowed = new AtomicBoolean(true);
                pi.getRequirements().forEach((material, integer) -> {
                    if(!pp.hasBlocks(material, integer)) allowed.set(false);
                });
                pi.getMob_requirements().forEach((entityType, integer) -> {
                    if(!pp.hasMob(entityType, integer)) allowed.set(false);
                });
                if(!allowed.get()) {
                    Util.ps("Prison", p, "&fВы не выполнили условия улучшения.");
                    return;
                }

                pp.takeMoney(next.getPrice());
                p.getInventory().setItem(entry.getKey(), next.getUsableItem());
                inv.addItem(r, s, getDynamicItem(id, inv, r, s));
            }
        });
    }

    private ItemStack getUpgrade(PrisonItem pi) {
        if(pi == null) return null;
        ItemStack is = pi.hasNext() ? pi.getUpgradingItem(pp) : pi.getUsableItem();
        ItemMeta m = is.getItemMeta();
        if(!pi.hasNext()) m.setDisplayName(Util.f("&6МАКСИМАЛЬНЫЙ УРОВЕНЬ"));
        is.setItemMeta(m);
        return is;
    }

    private Map.Entry<Integer, PrisonItem> getPrisonItem(String key) {
        Optional<Map.Entry<Integer, PrisonItem>> obj = getItems().entrySet().stream().filter(integerPrisonItemEntry -> integerPrisonItemEntry.getValue().getId().split("_")[0].equalsIgnoreCase(key)).findAny();
        return obj.orElse(null);
    }

    private List<String> getIds() {
        List<String> ids = Lists.newArrayList();
        getItems().values().forEach(prisonItem -> ids.add(prisonItem.getId().split("_")[0]));
        return ids;
    }

    private Map<Integer, PrisonItem> getItems() {
        Map<Integer, PrisonItem> items = Maps.newHashMap();
        Map<String, Map<Integer, PrisonItem>> itemsMap = Maps.newHashMap();
        itemsMap.put("cap", Maps.newHashMap());
        itemsMap.put("chest", Maps.newHashMap());
        itemsMap.put("legg", Maps.newHashMap());
        itemsMap.put("boots", Maps.newHashMap());
        itemsMap.put("axe", Maps.newHashMap());
        itemsMap.put("sword", Maps.newHashMap());
        itemsMap.put("firesword", Maps.newHashMap());
        itemsMap.put("pickaxe", Maps.newHashMap());
        itemsMap.put("spade", Maps.newHashMap());
        itemsMap.put("pickaxetwo", Maps.newHashMap());
        itemsMap.put("newpickaxe", Maps.newHashMap());
        itemsMap.put("bow", Maps.newHashMap());
        itemsMap.put("firebow", Maps.newHashMap());
        for(int i = 0; i < p.getInventory().getSize(); i++) {
            ItemStack is = p.getInventory().getItem(i);
            if(PrisonItem.isCustomItem(is)) {
                PrisonItem pi = PrisonItem.getPrisonItem(is);
                String id = Util.strip(pi.getId()).split("_")[0];
                itemsMap.get(id).put(i, pi);
            }
        }

        itemsMap.forEach((s, integerPrisonItemMap) -> {
            if(integerPrisonItemMap.size() > 1) {
                Map.Entry<Integer, PrisonItem> entry = integerPrisonItemMap.entrySet().stream().max(Comparator.comparingInt(o -> o.getValue().getLevel())).get();
                items.put(entry.getKey(), entry.getValue());
            } else if(integerPrisonItemMap.size() > 0) {
                Map.Entry<Integer, PrisonItem> entry = integerPrisonItemMap.entrySet().stream().findFirst().get();
                items.put(entry.getKey(), entry.getValue());
            }
        });
        return items;
    }

}
