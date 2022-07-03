package sexy.criss.game.prison.quests.values;

import com.google.common.collect.Maps;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import sexy.criss.game.prison.quests.Quest;
import sexy.criss.game.prison.quests.QuestLevel;
import sexy.criss.gen.util.Util;

import java.util.HashMap;

public class LogQuest extends Quest {

    public LogQuest() {
        super("log", Util.createItem(Material.DARK_OAK_LOG, ""), "", 1);
        ItemStack log = Util.createItem(Material.DARK_OAK_LOG, "");

        {
            addLevel(new QuestLevel(log, 1, 10, new HashMap<>(){{ put(Material.DARK_OAK_LOG, 32); }}, Maps.newHashMap()));
            addLevel(new QuestLevel(log, 2, 15, new HashMap<>(){{ put(Material.DARK_OAK_LOG, 64); }}, Maps.newHashMap()));
            addLevel(new QuestLevel(log, 3, 20, new HashMap<>(){{ put(Material.DARK_OAK_LOG, 128); }}, Maps.newHashMap()));
            addLevel(new QuestLevel(log, 4, 25, new HashMap<>(){{ put(Material.DARK_OAK_LOG, 256); }}, Maps.newHashMap()));
            addLevel(new QuestLevel(log, 5, 30, new HashMap<>(){{ put(Material.DARK_OAK_LOG, 512); }}, Maps.newHashMap()));
            addLevel(new QuestLevel(log, 6, 35, new HashMap<>(){{ put(Material.DARK_OAK_LOG, 1024); }}, Maps.newHashMap()));
            addLevel(new QuestLevel(log, 7, 70, new HashMap<>(){{ put(Material.DARK_OAK_LOG, 2048); }}, Maps.newHashMap()));
            addLevel(new QuestLevel(log, 8, 220, new HashMap<>(){{ put(Material.DARK_OAK_LOG, 4096); }}, Maps.newHashMap()));
            addLevel(new QuestLevel(log, 9, 300, new HashMap<>(){{ put(Material.DARK_OAK_LOG, 8192); }}, Maps.newHashMap())); }

    }
}
