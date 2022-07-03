package sexy.criss.game.prison.quests.values;

import com.google.common.collect.Maps;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import sexy.criss.game.prison.quests.Quest;
import sexy.criss.game.prison.quests.QuestLevel;
import sexy.criss.gen.util.Util;

import java.util.HashMap;

public class DirtQuest extends Quest {

    public DirtQuest() {
        super("dirt", Util.createItem(Material.DIRT, ""), "Землекоп", 3);
        ItemStack dirt = Util.createItem(Material.DIRT, "&6Земля");
        ItemStack sand = Util.createItem(Material.SAND, "&6Песок");
        ItemStack gravel = Util.createItem(Material.GRAVEL, "&6Гравий");

        /******************************************* DIRT SECTION **************************************************/
        {
            addLevel(new QuestLevel(dirt, 1, 10, new HashMap<>(){{ put(Material.DIRT, 32); }}, Maps.newHashMap()));
            addLevel(new QuestLevel(dirt, 2, 15, new HashMap<>(){{ put(Material.DIRT, 64); }}, Maps.newHashMap()));
            addLevel(new QuestLevel(dirt, 3, 20, new HashMap<>(){{ put(Material.DIRT, 128); }}, Maps.newHashMap()));
            addLevel(new QuestLevel(dirt, 4, 25, new HashMap<>(){{ put(Material.DIRT, 256); }}, Maps.newHashMap()));
            addLevel(new QuestLevel(dirt, 5, 30, new HashMap<>(){{ put(Material.DIRT, 512); }}, Maps.newHashMap()));
            addLevel(new QuestLevel(dirt, 6, 35, new HashMap<>(){{ put(Material.DIRT, 1024); }}, Maps.newHashMap()));
            addLevel(new QuestLevel(dirt, 7, 50, new HashMap<>(){{ put(Material.DIRT, 2048); }}, Maps.newHashMap()));
            addLevel(new QuestLevel(dirt, 8, 65, new HashMap<>(){{ put(Material.DIRT, 4096); }}, Maps.newHashMap()));
            addLevel(new QuestLevel(dirt, 9, 80, new HashMap<>(){{ put(Material.DIRT, 8192); }}, Maps.newHashMap())); }
        /******************************************* SAND SECTION **************************************************/
        {
            addLevel(new QuestLevel(sand, 10, 10, new HashMap<>(){{ put(Material.SAND, 32); }}, Maps.newHashMap()));
            addLevel(new QuestLevel(sand, 11, 15, new HashMap<>(){{ put(Material.SAND, 64); }}, Maps.newHashMap()));
            addLevel(new QuestLevel(sand, 12, 20, new HashMap<>(){{ put(Material.SAND, 128); }}, Maps.newHashMap()));
            addLevel(new QuestLevel(sand, 13, 25, new HashMap<>(){{ put(Material.SAND, 256); }}, Maps.newHashMap()));
            addLevel(new QuestLevel(sand, 14, 30, new HashMap<>(){{ put(Material.SAND, 512); }}, Maps.newHashMap()));
            addLevel(new QuestLevel(sand, 15, 35, new HashMap<>(){{ put(Material.SAND, 1024); }}, Maps.newHashMap()));
            addLevel(new QuestLevel(sand, 16, 50, new HashMap<>(){{ put(Material.SAND, 2048); }}, Maps.newHashMap()));
            addLevel(new QuestLevel(sand, 17, 65, new HashMap<>(){{ put(Material.SAND, 4096); }}, Maps.newHashMap()));
            addLevel(new QuestLevel(sand, 18, 80, new HashMap<>(){{ put(Material.SAND, 8192); }}, Maps.newHashMap())); }
            /******************************************* GRAVEL SECTION **************************************************/
        {
            addLevel(new QuestLevel(gravel, 19, 10, new HashMap<>(){{ put(Material.GRAVEL, 32); }}, Maps.newHashMap()));
            addLevel(new QuestLevel(gravel, 20, 15, new HashMap<>(){{ put(Material.GRAVEL, 64); }}, Maps.newHashMap()));
            addLevel(new QuestLevel(gravel, 21, 20, new HashMap<>(){{ put(Material.GRAVEL, 128); }}, Maps.newHashMap()));
            addLevel(new QuestLevel(gravel, 22, 25, new HashMap<>(){{ put(Material.GRAVEL, 256); }}, Maps.newHashMap()));
            addLevel(new QuestLevel(gravel, 23, 30, new HashMap<>(){{ put(Material.GRAVEL, 512); }}, Maps.newHashMap()));
            addLevel(new QuestLevel(gravel, 24, 35, new HashMap<>(){{ put(Material.GRAVEL, 1024); }}, Maps.newHashMap()));
            addLevel(new QuestLevel(gravel, 25, 50, new HashMap<>(){{ put(Material.GRAVEL, 2048); }}, Maps.newHashMap()));
            addLevel(new QuestLevel(gravel, 26, 65, new HashMap<>(){{ put(Material.GRAVEL, 4096); }}, Maps.newHashMap()));
            addLevel(new QuestLevel(gravel, 27, 80, new HashMap<>(){{ put(Material.GRAVEL, 8192); }}, Maps.newHashMap())); }
    }
}
