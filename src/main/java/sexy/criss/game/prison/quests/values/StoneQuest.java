package sexy.criss.game.prison.quests.values;

import com.google.common.collect.Maps;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import sexy.criss.game.prison.quests.Quest;
import sexy.criss.game.prison.quests.QuestLevel;
import sexy.criss.gen.util.Util;

import java.util.HashMap;

public class StoneQuest extends Quest {

    public StoneQuest() {
        super("stone", Util.createItem(Material.STONE, ""), "Настоящий шахтёр", 4);
        ItemStack stone = Util.createItem(Material.STONE, "&6Камень");
        ItemStack coal = Util.createItem(Material.COAL_ORE, "&6Уголь");
        ItemStack iron = Util.createItem(Material.IRON_ORE, "&6Железо");
        ItemStack gold = Util.createItem(Material.GOLD_ORE, "&6Золото");

        {
            addLevel(new QuestLevel(stone, 1, 10, new HashMap<>(){{ put(Material.COBBLESTONE, 32); }}, Maps.newHashMap()));
            addLevel(new QuestLevel(stone, 2, 15, new HashMap<>(){{ put(Material.COBBLESTONE, 64); }}, Maps.newHashMap()));
            addLevel(new QuestLevel(stone, 3, 20, new HashMap<>(){{ put(Material.COBBLESTONE, 128); }}, Maps.newHashMap()));
            addLevel(new QuestLevel(stone, 4, 25, new HashMap<>(){{ put(Material.COBBLESTONE, 256); }}, Maps.newHashMap()));
            addLevel(new QuestLevel(stone, 5, 30, new HashMap<>(){{ put(Material.COBBLESTONE, 512); }}, Maps.newHashMap()));
            addLevel(new QuestLevel(stone, 6, 35, new HashMap<>(){{ put(Material.COBBLESTONE, 1024); }}, Maps.newHashMap()));
            addLevel(new QuestLevel(stone, 7, 50, new HashMap<>(){{ put(Material.COBBLESTONE, 2048); }}, Maps.newHashMap()));
            addLevel(new QuestLevel(stone, 8, 65, new HashMap<>(){{ put(Material.COBBLESTONE, 4096); }}, Maps.newHashMap()));
            addLevel(new QuestLevel(stone, 9, 80, new HashMap<>(){{ put(Material.COBBLESTONE, 8192); }}, Maps.newHashMap())); }
        {
            addLevel(new QuestLevel(coal, 10, 15, new HashMap<>(){{ put(Material.COAL_ORE, 32); }}, Maps.newHashMap()));
            addLevel(new QuestLevel(coal, 11, 25, new HashMap<>(){{ put(Material.COAL_ORE, 64); }}, Maps.newHashMap()));
            addLevel(new QuestLevel(coal, 12, 35, new HashMap<>(){{ put(Material.COAL_ORE, 128); }}, Maps.newHashMap()));
            addLevel(new QuestLevel(coal, 13, 45, new HashMap<>(){{ put(Material.COAL_ORE, 256); }}, Maps.newHashMap()));
            addLevel(new QuestLevel(coal, 14, 55, new HashMap<>(){{ put(Material.COAL_ORE, 512); }}, Maps.newHashMap()));
            addLevel(new QuestLevel(coal, 15, 65, new HashMap<>(){{ put(Material.COAL_ORE, 1024); }}, Maps.newHashMap()));
            addLevel(new QuestLevel(coal, 16, 75, new HashMap<>(){{ put(Material.COAL_ORE, 2048); }}, Maps.newHashMap()));
            addLevel(new QuestLevel(coal, 17, 85, new HashMap<>(){{ put(Material.COAL_ORE, 4096); }}, Maps.newHashMap()));
            addLevel(new QuestLevel(coal, 18, 100, new HashMap<>(){{ put(Material.COAL_ORE, 8192); }}, Maps.newHashMap())); }
        {
            addLevel(new QuestLevel(iron, 19, 30, new HashMap<>(){{ put(Material.IRON_ORE, 32); }}, Maps.newHashMap()));
            addLevel(new QuestLevel(iron, 20, 50, new HashMap<>(){{ put(Material.IRON_ORE, 64); }}, Maps.newHashMap()));
            addLevel(new QuestLevel(iron, 21, 70, new HashMap<>(){{ put(Material.IRON_ORE, 128); }}, Maps.newHashMap()));
            addLevel(new QuestLevel(iron, 22, 90, new HashMap<>(){{ put(Material.IRON_ORE, 256); }}, Maps.newHashMap()));
            addLevel(new QuestLevel(iron, 23, 110, new HashMap<>(){{ put(Material.IRON_ORE, 512); }}, Maps.newHashMap()));
            addLevel(new QuestLevel(iron, 24, 130, new HashMap<>(){{ put(Material.IRON_ORE, 1024); }}, Maps.newHashMap()));
            addLevel(new QuestLevel(iron, 25, 150, new HashMap<>(){{ put(Material.IRON_ORE, 2048); }}, Maps.newHashMap()));
            addLevel(new QuestLevel(iron, 26, 170, new HashMap<>(){{ put(Material.IRON_ORE, 4096); }}, Maps.newHashMap()));
            addLevel(new QuestLevel(iron, 27, 200, new HashMap<>(){{ put(Material.IRON_ORE, 8192); }}, Maps.newHashMap())); }
        {
            addLevel(new QuestLevel(gold, 28, 40, new HashMap<>(){{ put(Material.GOLD_ORE, 32); }}, Maps.newHashMap()));
            addLevel(new QuestLevel(gold, 29, 60, new HashMap<>(){{ put(Material.GOLD_ORE, 64); }}, Maps.newHashMap()));
            addLevel(new QuestLevel(gold, 30, 80, new HashMap<>(){{ put(Material.GOLD_ORE, 128); }}, Maps.newHashMap()));
            addLevel(new QuestLevel(gold, 31, 100, new HashMap<>(){{ put(Material.GOLD_ORE, 256); }}, Maps.newHashMap()));
            addLevel(new QuestLevel(gold, 32, 120, new HashMap<>(){{ put(Material.GOLD_ORE, 512); }}, Maps.newHashMap()));
            addLevel(new QuestLevel(gold, 33, 140, new HashMap<>(){{ put(Material.GOLD_ORE, 1024); }}, Maps.newHashMap()));
            addLevel(new QuestLevel(gold, 34, 160, new HashMap<>(){{ put(Material.GOLD_ORE, 2048); }}, Maps.newHashMap()));
            addLevel(new QuestLevel(gold, 35, 180, new HashMap<>(){{ put(Material.GOLD_ORE, 4096); }}, Maps.newHashMap()));
            addLevel(new QuestLevel(gold, 36, 240, new HashMap<>(){{ put(Material.GOLD_ORE, 8192); }}, Maps.newHashMap())); }
    }
}
