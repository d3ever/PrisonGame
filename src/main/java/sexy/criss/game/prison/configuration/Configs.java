package sexy.criss.game.prison.configuration;

import java.io.File;
import java.io.IOException;
import java.util.stream.Stream;

import org.bukkit.configuration.file.FileConfiguration;

import sexy.criss.game.prison.Main;
import sexy.criss.game.prison.logger.PrisonLogg;
import sexy.criss.game.prison.logger.PrisonLoggType;

public enum Configs {

	CONFIG(Configuration.CONFIG, "config"),
	MINES(Configuration.MINES, "mines"),
	ITEMS(Configuration.ITEMS, "items_container"),
	BOOSTERS(Configuration.BOOSTERS, "boosters"),
	PROMO(Configuration.PROMO, "promo"),
	MOBS(Configuration.MOBS, "mobs"),
	PLAYERS(Configuration.PLAYERS, "players");

	private PrisonLogg log = Main.getInstance().getLog();

	FileConfiguration conf;
	String file;

	Configs(FileConfiguration conf, String file) {
		this.conf = conf;
		this.file = file;
	}

	public FileConfiguration getConfig() {
		return this.conf;
	}

	public void save() {
		File f = new File(Main.getInstance().getDataFolder(), file.concat(".yml"));
		try {
			conf.save(f);
			log.log(PrisonLoggType.SUCCESSFULLY, "Saved file configuration ".concat(file));
		} catch (IOException e) {
			log.log(PrisonLoggType.ERROR, "Error saving file configuration ".concat(file));
			e.printStackTrace();
		}
	}

	public static void all_save() {
		Stream.of(values()).forEach(Configs::save);
	}

}
