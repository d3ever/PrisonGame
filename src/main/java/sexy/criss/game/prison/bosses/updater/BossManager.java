package sexy.criss.game.prison.bosses.updater;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import sexy.criss.game.prison.Main;
import sexy.criss.game.prison.bosses.EntityTypes;
import sexy.criss.game.prison.configuration.Configs;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.logging.Level;

public class BossManager {
    Main instance;
    FileConfiguration spawners_storage = Configs.MOBS.getConfig();

    public BossManager(Main instance) {
        this.instance = instance;
    }

    public void loadSpawners() {
        spawners_storage.getKeys(false).forEach(s -> {
            ConfigurationSection sc = spawners_storage.getConfigurationSection(s);
            EntityTypes type = EntityTypes.valueOf(sc.getString("type"));
            World world = Bukkit.getWorld(sc.getString("world"));
            double x = sc.getDouble("x");
            double y = sc.getDouble("y");
            double z = sc.getDouble("z");
            int interval = sc.getInt("interval");
            (new Spawner(new Location(world, x, y, z), type, interval)).update();
        });

        instance.getLogger().log(Level.INFO, "Loaded " + Spawner.spawners.size() + " mob spawners");
    }

}
