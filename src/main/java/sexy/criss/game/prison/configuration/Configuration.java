package sexy.criss.game.prison.configuration;

import java.io.File;
import java.io.IOException;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import sexy.criss.game.prison.Main;
import sexy.criss.game.prison.logger.PrisonLoggType;
import sexy.criss.game.prison.prison_data.PrisonItem;
import sexy.criss.game.prison.prison_data.data.promocodes.Promo;

public class Configuration {
	Main instance;

	public static FileConfiguration CONFIG;
	public static FileConfiguration MINES;
	public static FileConfiguration ITEMS;
	public static FileConfiguration BOOSTERS;
	public static FileConfiguration PROMO;
	public static FileConfiguration MOBS;
	public static FileConfiguration PLAYERS;

	public Configuration(Main instance) {
		this.instance = instance;
		instance.getLog().log(PrisonLoggType.INFO, "Configuration loading start-up");
	}

	public void registerConfig() {
		instance.saveDefaultConfig();
		instance.saveConfig();

		CONFIG = instance.getConfig();
		instance.getLog().log(PrisonLoggType.SUCCESSFULLY, "Default configuration loaded");
	}

	public void registerMines() {
		MINES = createFile("mines");
		instance.getLog().log(PrisonLoggType.SUCCESSFULLY, "Mines data file loaded");
	}

	public void registerPlayers() {
		PLAYERS = createFile("players");
	}

	public void registerItems() {
		ITEMS = createFile("items_container");
		if(ITEMS.getKeys(false).size() > 0) {
			ITEMS.getKeys(false).forEach(s -> new PrisonItem(s, ITEMS.getConfigurationSection(s)));
		}
		instance.getLog().log(PrisonLoggType.SUCCESSFULLY, "Items data file loaded");
	}

	public void registerBoosters() {
		BOOSTERS = createFile("boosters");
		instance.getLog().log(PrisonLoggType.SUCCESSFULLY, "Boosters data file loaded");
	}

	public void registerPromo() {
		PROMO = createFile("promo");
		PROMO.getKeys(false).forEach(id -> {
			ConfigurationSection c = PROMO.getConfigurationSection(id);
			String own = c.getString("own");
			int reward = c.getInt("reward");
			int own_reward = c.getInt("own_reward");
			new Promo(id, own, reward, own_reward);
		});
		instance.getLog().log(PrisonLoggType.SUCCESSFULLY, "Promo data file loaded");
	}

	public void unregisterPromo() {
		Promo.promos.forEach(x -> {
			String id = x.getName();
			if(!PROMO.getKeys(false).contains(id)) {
				String own = x.getOwn();
				int reward = x.getReward();
				int own_reword = x.getRewardOwn();
				ConfigurationSection s = PROMO.createSection(id);
				s.set("own", own);
				s.set("reward", reward);
				s.set("own_reward", own_reword);
			}
		});
	}

	public void registerMobs() {
		MOBS = createFile("mobs");
	}

	private FileConfiguration createFile(String fileName) {
		File f = new File(instance.getDataFolder(), fileName.concat(".yml"));
		if (!(f.exists())) {
			try {
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return YamlConfiguration.loadConfiguration(f);
	}

}
