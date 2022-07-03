package sexy.criss.game.prison.additional.autopickup;

import com.google.common.collect.Sets;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import sexy.criss.game.prison.Main;
import sexy.criss.game.prison.additional.autopickup.listeners.*;
import sexy.criss.game.prison.additional.autopickup.utils.Messages;
import sexy.criss.game.prison.additional.autopickup.utils.Metrics;
import sexy.criss.game.prison.additional.autopickup.utils.PickupObjective;
import sexy.criss.game.prison.additional.autopickup.utils.TallCrops;

import java.io.File;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

public class AutoPickup {
    public HashSet<Player> autopickup_list = new HashSet<>(); // Blocks
    public HashSet<Player> auto_smelt_blocks = new HashSet<>(); // AutoSmelt - Blocks
    public Set<Player> autosell_list = Sets.newHashSet();
    public Messages messages = null;
    public boolean UP2Date = true;
    public TallCrops crops;

    public static ArrayList<String> worldsBlacklist = null;

    // Custom Items Patch
    public static HashMap<String, PickupObjective> customItemPatch = new HashMap<>();
    public static HashSet<UUID> droppedItems = new HashSet<>();

    public void register() {
        createPlayerDataDir();

        messages = new Messages();

        // Listeners
        Main.getInstance().getServer().getPluginManager().registerEvents(new BlockDropItemEventListener(), Main.getInstance());
        Main.getInstance().getServer().getPluginManager().registerEvents(new PlayerJoinEventListener(), Main.getInstance());
        Main.getInstance().getServer().getPluginManager().registerEvents(new BlockBreakEventListener(), Main.getInstance());
        Main.getInstance().getServer().getPluginManager().registerEvents(new PlayerInteractEventListener(), Main.getInstance());
        Main.getInstance().getServer().getPluginManager().registerEvents(new PlayerDropItemEventListener(), Main.getInstance());

        crops = new TallCrops();

        //bStats
        Metrics metrics = new Metrics(Main.getInstance(), 5914);

        // Pickup Objective Cleaner
        new BukkitRunnable() {
            @Override
            public void run() {
                customItemPatch.keySet().removeIf(key -> (Duration.between(Instant.now(), customItemPatch.get(key).getCreatedAt()).getSeconds() < -15));
            }
        }.runTaskTimerAsynchronously(Main.getInstance(), 300L, 300L); // 15 sec

        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    droppedItems.removeIf(uuid -> (Bukkit.getEntity(uuid))==null);
                    droppedItems.removeIf(uuid -> (Bukkit.getEntity(uuid)).isDead()); ///////
                } catch (NullPointerException ignored) {}
            }
        }.runTaskTimer(Main.getInstance(), 6000L, 6000L); // 5 min
    }

    public void createPlayerDataDir() {
        File dir = new File(Main.getInstance().getDataFolder(), "PlayerData");
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    public TallCrops getCrops() {
        return crops;
    }

    public Messages getMsg() {
        return messages;
    }

}
