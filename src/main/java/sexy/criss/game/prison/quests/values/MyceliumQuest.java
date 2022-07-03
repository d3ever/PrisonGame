package sexy.criss.game.prison.quests.values;

import com.google.common.collect.Maps;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import sexy.criss.game.prison.quests.Quest;
import sexy.criss.game.prison.quests.QuestLevel;
import sexy.criss.gen.util.Util;

import java.util.HashMap;

public class MyceliumQuest extends Quest {

    public MyceliumQuest() {
        super("mycelium", Util.createItem(Material.MYCELIUM, ""), "Сборщик мицелия", 1);
        ItemStack mycelium = Util.createItem(Material.MYCELIUM, "&6Мицелий");

        {
            addLevel(new QuestLevel(mycelium, 1, 10, new HashMap<>(){{ put(Material.MYCELIUM, 32); }}, Maps.newHashMap()));
            addLevel(new QuestLevel(mycelium, 2, 15, new HashMap<>(){{ put(Material.MYCELIUM, 64); }}, Maps.newHashMap()));
            addLevel(new QuestLevel(mycelium, 3, 20, new HashMap<>(){{ put(Material.MYCELIUM, 128); }}, Maps.newHashMap()));
            addLevel(new QuestLevel(mycelium, 4, 25, new HashMap<>(){{ put(Material.MYCELIUM, 256); }}, Maps.newHashMap()));
            addLevel(new QuestLevel(mycelium, 5, 30, new HashMap<>(){{ put(Material.MYCELIUM, 512); }}, Maps.newHashMap()));
            addLevel(new QuestLevel(mycelium, 6, 35, new HashMap<>(){{ put(Material.MYCELIUM, 1024); }}, Maps.newHashMap()));
            addLevel(new QuestLevel(mycelium, 7, 50, new HashMap<>(){{ put(Material.MYCELIUM, 2048); }}, Maps.newHashMap()));
            addLevel(new QuestLevel(mycelium, 8, 80, new HashMap<>(){{ put(Material.MYCELIUM, 4096); }}, Maps.newHashMap()));
            addLevel(new QuestLevel(mycelium, 9, 140, new HashMap<>(){{ put(Material.MYCELIUM, 8192); }}, Maps.newHashMap())); }
    }
}
