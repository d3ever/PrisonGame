package sexy.criss.game.prison.quests.values;

import com.google.common.collect.Maps;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import sexy.criss.game.prison.quests.Quest;
import sexy.criss.game.prison.quests.QuestLevel;
import sexy.criss.gen.util.Util;

import java.util.HashMap;

public class IceQuest extends Quest {

    public IceQuest() {
        super("ice", Util.createItem(Material.DIRT, ""), "Ледниковый период", 3);
        ItemStack snow = Util.createItem(Material.SNOW_BLOCK, "&6Блок снега");
        ItemStack ice = Util.createItem(Material.PACKED_ICE, "&6Кубы льда");
        ItemStack quartz = Util.createItem(Material.QUARTZ_BLOCK, "&6Кварцовые блоки");

        /******************************************* DIRT SECTION **************************************************/
        {
            addLevel(new QuestLevel(snow, 1, 10, new HashMap<>(){{ put(Material.SNOW_BLOCK, 32); }}, Maps.newHashMap()));
            addLevel(new QuestLevel(snow, 2, 15, new HashMap<>(){{ put(Material.SNOW_BLOCK, 64); }}, Maps.newHashMap()));
            addLevel(new QuestLevel(snow, 3, 20, new HashMap<>(){{ put(Material.SNOW_BLOCK, 128); }}, Maps.newHashMap()));
            addLevel(new QuestLevel(snow, 4, 25, new HashMap<>(){{ put(Material.SNOW_BLOCK, 256); }}, Maps.newHashMap()));
            addLevel(new QuestLevel(snow, 5, 30, new HashMap<>(){{ put(Material.SNOW_BLOCK, 512); }}, Maps.newHashMap()));
            addLevel(new QuestLevel(snow, 6, 35, new HashMap<>(){{ put(Material.SNOW_BLOCK, 1024); }}, Maps.newHashMap()));
            addLevel(new QuestLevel(snow, 7, 50, new HashMap<>(){{ put(Material.SNOW_BLOCK, 2048); }}, Maps.newHashMap()));
            addLevel(new QuestLevel(snow, 8, 65, new HashMap<>(){{ put(Material.SNOW_BLOCK, 4096); }}, Maps.newHashMap()));
            addLevel(new QuestLevel(snow, 9, 80, new HashMap<>(){{ put(Material.SNOW_BLOCK, 8192); }}, Maps.newHashMap())); }
        /******************************************* SAND SECTION **************************************************/
        {
            addLevel(new QuestLevel(ice, 10, 10, new HashMap<>(){{ put(Material.PACKED_ICE, 32); }}, Maps.newHashMap()));
            addLevel(new QuestLevel(ice, 11, 15, new HashMap<>(){{ put(Material.PACKED_ICE, 64); }}, Maps.newHashMap()));
            addLevel(new QuestLevel(ice, 12, 20, new HashMap<>(){{ put(Material.PACKED_ICE, 128); }}, Maps.newHashMap()));
            addLevel(new QuestLevel(ice, 13, 25, new HashMap<>(){{ put(Material.PACKED_ICE, 256); }}, Maps.newHashMap()));
            addLevel(new QuestLevel(ice, 14, 30, new HashMap<>(){{ put(Material.PACKED_ICE, 512); }}, Maps.newHashMap()));
            addLevel(new QuestLevel(ice, 15, 35, new HashMap<>(){{ put(Material.PACKED_ICE, 1024); }}, Maps.newHashMap()));
            addLevel(new QuestLevel(ice, 16, 50, new HashMap<>(){{ put(Material.PACKED_ICE, 2048); }}, Maps.newHashMap()));
            addLevel(new QuestLevel(ice, 17, 65, new HashMap<>(){{ put(Material.PACKED_ICE, 4096); }}, Maps.newHashMap()));
            addLevel(new QuestLevel(ice, 18, 80, new HashMap<>(){{ put(Material.PACKED_ICE, 8192); }}, Maps.newHashMap())); }
        /******************************************* GRAVEL SECTION **************************************************/
        {
            addLevel(new QuestLevel(quartz, 19, 10, new HashMap<>(){{ put(Material.QUARTZ_BLOCK, 32); }}, Maps.newHashMap()));
            addLevel(new QuestLevel(quartz, 20, 15, new HashMap<>(){{ put(Material.QUARTZ_BLOCK, 64); }}, Maps.newHashMap()));
            addLevel(new QuestLevel(quartz, 21, 20, new HashMap<>(){{ put(Material.QUARTZ_BLOCK, 128); }}, Maps.newHashMap()));
            addLevel(new QuestLevel(quartz, 22, 25, new HashMap<>(){{ put(Material.QUARTZ_BLOCK, 256); }}, Maps.newHashMap()));
            addLevel(new QuestLevel(quartz, 23, 30, new HashMap<>(){{ put(Material.QUARTZ_BLOCK, 512); }}, Maps.newHashMap()));
            addLevel(new QuestLevel(quartz, 24, 35, new HashMap<>(){{ put(Material.QUARTZ_BLOCK, 1024); }}, Maps.newHashMap()));
            addLevel(new QuestLevel(quartz, 25, 50, new HashMap<>(){{ put(Material.QUARTZ_BLOCK, 2048); }}, Maps.newHashMap()));
            addLevel(new QuestLevel(quartz, 26, 65, new HashMap<>(){{ put(Material.QUARTZ_BLOCK, 4096); }}, Maps.newHashMap()));
            addLevel(new QuestLevel(quartz, 27, 80, new HashMap<>(){{ put(Material.QUARTZ_BLOCK, 8192); }}, Maps.newHashMap())); }
    }
}
