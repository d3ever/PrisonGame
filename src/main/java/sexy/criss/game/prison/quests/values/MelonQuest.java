package sexy.criss.game.prison.quests.values;

import com.google.common.collect.Maps;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import sexy.criss.game.prison.quests.Quest;
import sexy.criss.game.prison.quests.QuestLevel;
import sexy.criss.gen.util.Util;

import java.util.HashMap;

public class MelonQuest extends Quest {

    public MelonQuest() {
        super("melon", Util.createItem(Material.MELON, ""), "Сборщик арбузов", 1);
        ItemStack melon = Util.createItem(Material.MELON, "&6Арбузы");

        {
            addLevel(new QuestLevel(melon, 1, 10, new HashMap<>(){{ put(Material.MELON, 32); }}, Maps.newHashMap()));
            addLevel(new QuestLevel(melon, 2, 15, new HashMap<>(){{ put(Material.MELON, 64); }}, Maps.newHashMap()));
            addLevel(new QuestLevel(melon, 3, 20, new HashMap<>(){{ put(Material.MELON, 128); }}, Maps.newHashMap()));
            addLevel(new QuestLevel(melon, 4, 25, new HashMap<>(){{ put(Material.MELON, 256); }}, Maps.newHashMap()));
            addLevel(new QuestLevel(melon, 5, 30, new HashMap<>(){{ put(Material.MELON, 512); }}, Maps.newHashMap()));
            addLevel(new QuestLevel(melon, 6, 35, new HashMap<>(){{ put(Material.MELON, 1024); }}, Maps.newHashMap()));
            addLevel(new QuestLevel(melon, 7, 50, new HashMap<>(){{ put(Material.MELON, 2048); }}, Maps.newHashMap()));
            addLevel(new QuestLevel(melon, 8, 80, new HashMap<>(){{ put(Material.MELON, 4096); }}, Maps.newHashMap()));
            addLevel(new QuestLevel(melon, 9, 140, new HashMap<>(){{ put(Material.MELON, 8192); }}, Maps.newHashMap())); }

    }
}
