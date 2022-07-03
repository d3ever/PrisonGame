package sexy.criss.game.prison.quests.values;

import com.google.common.collect.Maps;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import sexy.criss.game.prison.quests.Quest;
import sexy.criss.game.prison.quests.QuestLevel;
import sexy.criss.gen.util.Util;

import java.util.HashMap;

public class PrismarineQuest extends Quest {

    public PrismarineQuest() {
        super("prismarine", Util.createItem(Material.PRISMARINE, ""), "Изучаем подводный мир", 1);
        ItemStack prismarine = Util.createItem(Material.PRISMARINE, "&6Призмарин");

        {
            addLevel(new QuestLevel(prismarine, 1, 40, new HashMap<>(){{ put(Material.PRISMARINE, 32); }}, Maps.newHashMap()));
            addLevel(new QuestLevel(prismarine, 2, 60, new HashMap<>(){{ put(Material.PRISMARINE, 64); }}, Maps.newHashMap()));
            addLevel(new QuestLevel(prismarine, 3, 80, new HashMap<>(){{ put(Material.PRISMARINE, 128); }}, Maps.newHashMap()));
            addLevel(new QuestLevel(prismarine, 4, 120, new HashMap<>(){{ put(Material.PRISMARINE, 256); }}, Maps.newHashMap()));
            addLevel(new QuestLevel(prismarine, 5, 140, new HashMap<>(){{ put(Material.PRISMARINE, 512); }}, Maps.newHashMap()));
            addLevel(new QuestLevel(prismarine, 6, 170, new HashMap<>(){{ put(Material.PRISMARINE, 1024); }}, Maps.newHashMap()));
            addLevel(new QuestLevel(prismarine, 7, 200, new HashMap<>(){{ put(Material.PRISMARINE, 2048); }}, Maps.newHashMap()));
            addLevel(new QuestLevel(prismarine, 8, 240, new HashMap<>(){{ put(Material.PRISMARINE, 4096); }}, Maps.newHashMap()));
            addLevel(new QuestLevel(prismarine, 9, 300, new HashMap<>(){{put(Material.PRISMARINE, 8192); }}, Maps.newHashMap())); }
    }
}
