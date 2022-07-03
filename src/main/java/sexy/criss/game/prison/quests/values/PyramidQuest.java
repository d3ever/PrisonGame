package sexy.criss.game.prison.quests.values;

import com.google.common.collect.Maps;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import sexy.criss.game.prison.quests.Quest;
import sexy.criss.game.prison.quests.QuestLevel;
import sexy.criss.gen.util.Util;

import java.util.HashMap;

public class PyramidQuest extends Quest {

    public PyramidQuest() {
        super("pyramid", Util.createItem(Material.SANDSTONE, ""), "Забытый пустыней", 2);
        ItemStack sandstone = Util.createItem(Material.SANDSTONE, "&6Песчанный камень");

        {
            addLevel(new QuestLevel(sandstone, 1, 10, new HashMap<>(){{ put(Material.SANDSTONE, 32); }}, Maps.newHashMap()));
            addLevel(new QuestLevel(sandstone, 2, 15, new HashMap<>(){{ put(Material.SANDSTONE, 64); }}, Maps.newHashMap()));
            addLevel(new QuestLevel(sandstone, 3, 20, new HashMap<>(){{ put(Material.SANDSTONE, 128); }}, Maps.newHashMap()));
            addLevel(new QuestLevel(sandstone, 4, 25, new HashMap<>(){{ put(Material.SANDSTONE, 256); }}, Maps.newHashMap()));
            addLevel(new QuestLevel(sandstone, 5, 30, new HashMap<>(){{ put(Material.SANDSTONE, 512); }}, Maps.newHashMap()));
            addLevel(new QuestLevel(sandstone, 6, 35, new HashMap<>(){{ put(Material.SANDSTONE, 1024); }}, Maps.newHashMap()));
            addLevel(new QuestLevel(sandstone, 7, 50, new HashMap<>(){{ put(Material.SANDSTONE, 2048); }}, Maps.newHashMap()));
            addLevel(new QuestLevel(sandstone, 8, 80, new HashMap<>(){{ put(Material.SANDSTONE, 4096); }}, Maps.newHashMap()));
            addLevel(new QuestLevel(sandstone, 9, 140, new HashMap<>(){{put(Material.SANDSTONE, 8192); }}, Maps.newHashMap())); }
        {
            addLevel(new QuestLevel(sandstone, 10, 400, new HashMap<>(){{ put(Material.SANDSTONE, 16384); }}, Maps.newHashMap()));
            addLevel(new QuestLevel(sandstone, 11, 800, new HashMap<>(){{ put(Material.SANDSTONE, 32768); }}, Maps.newHashMap()));
            addLevel(new QuestLevel(sandstone, 12, 1200, new HashMap<>(){{ put(Material.SANDSTONE, 65536); }}, Maps.newHashMap()));
            addLevel(new QuestLevel(sandstone, 13, 1500, new HashMap<>(){{ put(Material.SANDSTONE, 131072); }}, Maps.newHashMap()));
            addLevel(new QuestLevel(sandstone, 14, 2000, new HashMap<>(){{ put(Material.SANDSTONE, 262144); }}, Maps.newHashMap()));
            addLevel(new QuestLevel(sandstone, 15, 2400, new HashMap<>(){{ put(Material.SANDSTONE, 524288); }}, Maps.newHashMap()));
            addLevel(new QuestLevel(sandstone, 16, 2600, new HashMap<>(){{ put(Material.SANDSTONE, 748596); }}, Maps.newHashMap()));
            addLevel(new QuestLevel(sandstone, 17, 3000, new HashMap<>(){{ put(Material.SANDSTONE, 926102); }}, Maps.newHashMap()));
            addLevel(new QuestLevel(sandstone, 18, 15000, new HashMap<>(){{put(Material.SANDSTONE, 1100000); }}, Maps.newHashMap())); }
    }
}
