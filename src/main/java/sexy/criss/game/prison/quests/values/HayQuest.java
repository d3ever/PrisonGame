package sexy.criss.game.prison.quests.values;

import com.google.common.collect.Maps;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import sexy.criss.game.prison.quests.Quest;
import sexy.criss.game.prison.quests.QuestLevel;
import sexy.criss.gen.util.Util;

import java.util.HashMap;

public class HayQuest extends Quest {

    public HayQuest() {
        super("hay", Util.createItem(Material.HAY_BLOCK, ""), "Кто тут фермер?", 1);
        ItemStack hay = Util.createItem(Material.HAY_BLOCK, "&6Сноп сена и тыквы");

        addLevel(new QuestLevel(hay, 1, 10, new HashMap<>() {{ put(Material.HAY_BLOCK, 32); put(Material.PUMPKIN, 32); }}, Maps.newHashMap()));
        addLevel(new QuestLevel(hay, 2, 25, new HashMap<>() {{ put(Material.HAY_BLOCK, 64); put(Material.PUMPKIN, 64); }}, Maps.newHashMap()));
        addLevel(new QuestLevel(hay, 3, 40, new HashMap<>() {{ put(Material.HAY_BLOCK, 128); put(Material.PUMPKIN, 128); }}, Maps.newHashMap()));
        addLevel(new QuestLevel(hay, 4, 55, new HashMap<>() {{ put(Material.HAY_BLOCK, 256); put(Material.PUMPKIN, 256); }}, Maps.newHashMap()));
        addLevel(new QuestLevel(hay, 5, 80, new HashMap<>() {{ put(Material.HAY_BLOCK, 512); put(Material.PUMPKIN, 512); }}, Maps.newHashMap()));
        addLevel(new QuestLevel(hay, 6, 110, new HashMap<>() {{ put(Material.HAY_BLOCK, 1024); put(Material.PUMPKIN, 1024); }}, Maps.newHashMap()));
        addLevel(new QuestLevel(hay, 7, 160, new HashMap<>() {{ put(Material.HAY_BLOCK, 2048); put(Material.PUMPKIN, 2048); }}, Maps.newHashMap()));
        addLevel(new QuestLevel(hay, 8, 240, new HashMap<>() {{ put(Material.HAY_BLOCK, 4096); put(Material.PUMPKIN, 4096); }}, Maps.newHashMap()));
        addLevel(new QuestLevel(hay, 9, 350, new HashMap<>() {{ put(Material.HAY_BLOCK, 8192); put(Material.PUMPKIN, 8192); }}, Maps.newHashMap()));
    }
}
