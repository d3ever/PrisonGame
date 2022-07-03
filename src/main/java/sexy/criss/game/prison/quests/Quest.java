package sexy.criss.game.prison.quests;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import sexy.criss.game.prison.prison_data.PrisonPlayer;
import sexy.criss.game.prison.prison_data.data.quests.Quests;
import sexy.criss.game.prison.quests.interfaces.IQuest;
import sexy.criss.gen.api.GENAPI;
import sexy.criss.gen.api.inventory.DynamicInventory;
import sexy.criss.gen.api.inventory.DynamicItem;
import sexy.criss.gen.util.LoreCreator;
import sexy.criss.gen.util.Util;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public abstract class Quest implements IQuest {
    public static Map<String, Quest> questMap = Maps.newHashMap();

    private final String id;
    private final ItemStack icon;
    private final String name;
    private final List<QuestLevel> levels;
    private final int rows;

    public Quest(String id, ItemStack icon, String name, int rows) {
        this.id = id;
        this.icon = icon;
        this.name = name;
        this.levels = Lists.newArrayList();
        this.rows = rows;

        questMap.put(id, this);
    }

    public void addLevel(QuestLevel level) {
        this.levels.add(level);
    }

    public List<QuestLevel> getLevels() {
        return this.levels;
    }

    public String getName() {
        return this.name;
    }

    public String getId() {
        return this.id;
    }

    public ItemStack getIcon() {
        return this.icon;
    }

    public void handle(Player p) {
        DynamicInventory inv = GENAPI.getInventoryAPI().createNewInventory(ChatColor.BLACK+getName(), rows);
        PrisonPlayer pp = PrisonPlayer.getPlayer(p.getUniqueId());
        Quests qc = pp.getQuests();
        getLevels().forEach(questLevel -> inv.addItem(questLevel.getLevel()-1, new DynamicItem(Util.format(questLevel.getIcon(pp, this), questLevel.getIcon(pp, this).getItemMeta().getDisplayName(), getRequirements(p, questLevel)), (p1, clickType, slot) -> updateQuest(p, questLevel))));
        inv.open(p);
    }

    public void updateQuest(Player p, QuestLevel ql) {
        PrisonPlayer pp = PrisonPlayer.getPlayer(p.getUniqueId());
        Quests qc = pp.getQuests();
        if(qc.has(this, ql.getLevel())) {
            Util.ps("Prison", p, "&fВы уже выполнили данное задание.");
        } else {
            if(ql.check(pp)) {
                pp.getQuests().grant(this, ql.getLevel());
                Util.ps("Prison", p, "&fПоздравляем! Вы выполнили задание. Получите награду");
                Util.ps("Prison", p, "&fНа ваш счёт было зачислено &6%d$", ql.getGold());
                pp.addGold(ql.getGold());
            } else {
                Util.ps("Prison", p, "&fВы не выполнили условия задания.");
                Util.ps("Prison", p, "&fВыполните условия и попробуйте снова.");
            }
        }
        p.closeInventory();
    }

    public List<String> getRequirements(Player p, QuestLevel ql) {
        PrisonPlayer pp = PrisonPlayer.getPlayer(p.getUniqueId());
        List<String> l = Lists.newArrayList();
        List<String> blocks = Lists.newArrayList();
        boolean hb = ql.getBlock_req().size() > 0;
        boolean hm = ql.getMobs_req().size() > 0;
        boolean have = pp.getQuests().has(this, ql.getLevel());
        if(have) return Lists.newArrayList("", "&fВы получили &6"+ql.getGold()+"$", "", "&6Выполнено");
        if(hb) {
            l.add("  &fБлоки:");
            ql.getBlock_req().forEach((material, integer) -> {
                boolean c = pp.hasBlocks(material, integer);
                l.add("  " + (c ? "&2" : "&4") + material.name() + " " + pp.getBlock_log().getOrDefault(material, 0) + "/" + integer);
            });
        }
        if(hm) {
            if(l.size() > 0) l.add("");
            l.add("  &fМонстры:");
            ql.getMobs_req().forEach((entityType, integer) -> {
                boolean c = pp.hasMob(entityType, integer);
                l.add("  " + (c ? "&2" : "&4") + entityType.name() + " " + pp.getMob_log().getOrDefault(entityType, 0) + "/" + integer);
            });
        }
        boolean h = ql.check(pp);
        return new LoreCreator().addEmpty()
                .addLineIf(l.size() > 0, "&fТребования:")
                .addLinesIf(hb || hm, l)
                .addEmpty().addLineIf(!h, "&c&mВознаграждение " + ql.getGold() + "$")
                .addLineIf(h, "&fВознаграждение &6" + ql.getGold() + "$")
                .addLineIf(h, "")
                .addLineIf(h, "&6Нажмите, чтобы получить награду")
                .build();
    }

    public static Quest getByClass(Class<? extends Quest> cl) {
        try {
            Quest ql = cl.getDeclaredConstructor().newInstance();
            return questMap.getOrDefault(ql.getId(), ql);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Quest getByItemStack(ItemStack icon) {
        return questMap.values().stream().filter(quest -> quest.icon.equals(icon)).findAny().orElse(null);
    }

}
