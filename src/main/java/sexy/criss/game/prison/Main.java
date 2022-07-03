package sexy.criss.game.prison;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import sexy.criss.game.prison.additional.autopickup.AutoPickup;
import sexy.criss.game.prison.additional.autopickup.commands.AutoCommand;
import sexy.criss.game.prison.additional.mines.MineResetLitePlugin;
import sexy.criss.game.prison.boosters.BoosterBlocks;
import sexy.criss.game.prison.boosters.BoosterMonetary;
import sexy.criss.game.prison.boosters.QuickSell;
import sexy.criss.game.prison.bossbar.SexyBossBar;
import sexy.criss.game.prison.bossbar.manager.SexyBossBarManager;
import sexy.criss.game.prison.bosses.updater.BossManager;
import sexy.criss.game.prison.bosses.updater.Spawner;
import sexy.criss.game.prison.bots.ShopBot;
import sexy.criss.game.prison.cases.invoke.DefaultCase;
import sexy.criss.game.prison.commands.*;
import sexy.criss.game.prison.configuration.Configs;
import sexy.criss.game.prison.configuration.Configuration;
import sexy.criss.game.prison.connection.DataSource;
import sexy.criss.game.prison.listener.manager.SexyListener;
import sexy.criss.game.prison.logger.PrisonLogg;
import sexy.criss.game.prison.logger.PrisonLoggType;
import sexy.criss.game.prison.notify.Notify;
import sexy.criss.game.prison.prison_data.PrisonPlayer;
import sexy.criss.game.prison.prison_data.data.levels.Levels;
import sexy.criss.game.prison.prison_data.data.passives.Passives;
import sexy.criss.game.prison.random.RandomEffects;
import sexy.criss.gen.util.configurations.DatFile;

public class Main extends JavaPlugin {
	public static boolean DEBUG = true;
	public static String SERVER_NAME = "Prison-1";
	private static DatFile datFile;
	public static ShopBot SHOP_BOT;
	public static QuickSell getQuickSell;
	public static AutoPickup getAutoPickup;
	private static WorldGuardPlugin worldGuardPlugin;
	private static MineResetLitePlugin minereset;

	private static Main instance;

	private PrisonLogg logger;
	private Configuration conf;
	private DataSource dataSource;

	private static ProtocolManager protocol_manager;

	public static MineResetLitePlugin getMinereset() {
		return minereset;
	}

	public static WorldGuardPlugin getWorldGuard() {
		return worldGuardPlugin;
	}

	public static ProtocolManager getProtocol() {
		return protocol_manager;
	}

	public static DatFile getDatFile() {
		return datFile;
	}

	public static Main getInstance() {
		return instance;
	}

	public DataSource getSource() {
		return this.dataSource;
	}

	public PrisonLogg getLog() {
		return this.logger;
	}

	public void onEnable() {
		instance = this;
		datFile = new DatFile("locs_prison");

		if(Bukkit.getPluginManager().getPlugin("GenCore") != null) {

			if (!getDataFolder().exists()) getDataFolder().mkdir();
			logger = new PrisonLogg();

			conf = new Configuration(this);

			conf.registerConfig();
			conf.registerMines();
			conf.registerItems();
			conf.registerBoosters();
			conf.registerPromo();
			conf.registerMobs();
			conf.registerPlayers();

			new FactionCommand().register();
			new SpawnCommand().register();
			new MineCommand().register();
			new PromocodeCommand().register();
			new BalanceCommand();
			new PayCommand();
			new ShopCommand();
			new CaseCommand();
			new KitCommand();
			new LocationsCommand();
			new BlocksCommand();
			new addCommand();
			new AutoCommand();
			new GiftCommand();
			new DonateCommand();
			SpeedCommands.init();
			SpeedStaff.register();

			new BossManager(this).loadSpawners();
//			(new SpawnerUpdater()).runTaskTimer(this, 20L, 600L);
			MineCommand.register_pages();
			Levels.registerLeveling();
			dataSource = new DataSource();
			dataSource.createTable();
			logger.log("Starting registering listeners...");
			SexyListener.REGISTER_ALL();
			logger.log(PrisonLoggType.SUCCESSFULLY, "All listeners registered");

			Notify.load();

			logger.log(PrisonLoggType.INFO, "Register protocol manager");
			protocol_manager = ProtocolLibrary.getProtocolManager();

			logger.log(PrisonLoggType.SUCCESSFULLY, "Prison-PLUGIN has been enabled");

			SexyBossBarManager.CREATE_BOSS_BAR();
			new SexyBossBar(SexyBossBarManager.BOSS_BAR).runTaskTimer(this, 0, 1);
			new RandomEffects().runTaskTimer(this, 10000, 10000);
			getDatFile().update();

			new BukkitRunnable(){
				@Override
				public void run() {
					Bukkit.getOnlinePlayers().forEach(player -> PrisonPlayer.getPlayer(player.getUniqueId()).save());
				}
			}.runTaskTimer(this, 0, 20 * 60 * 5);

			Bukkit.getWorlds().forEach(world -> world.getEntities().forEach(Entity::remove));
			KitCommand.load();
			new DefaultCase();

			{
				getAutoPickup = new AutoPickup();
				getAutoPickup.register();
			}

			getQuickSell = new QuickSell();
			getQuickSell.register();

			SHOP_BOT = new ShopBot();
			for (int i = 11; i <= 100; ++i) {
				new BoosterMonetary(i / 10.0);
				new BoosterBlocks(i / 10.0);
			}
			QuickSell.local.setDefault("PermissionBoosters.booster-use", "&a&l+ ${MONEY} &7[ &e\u0412\u0430\u0448 %multiplier%x \u0411\u0443\u0441\u0442\u0435\u0440 &7]");
			QuickSell.local.save();
			new BukkitRunnable() {
				@Override
				public void run() {
					Passives.register();
				}
			}.runTaskLater(this, 200);

			minereset = new MineResetLitePlugin();
			minereset.load();
		}
	}

	public void onDisable() {
		minereset.unload();
		Spawner.spawners.values().stream().filter(s -> s.getCurrent().getBukkitEntity() != null).forEach(s -> s.getCurrent().getBukkitEntity().remove());
		getDatFile().update();
		KitCommand.unload();
		logger.log("Saving all plugin configuration");
		conf.unregisterPromo();
		Configs.all_save();
		logger.log(PrisonLoggType.SUCCESSFULLY, "All plugin configurations has been saved");

		logger.log("Saving all players configuration");
		PrisonPlayer.players_map.values().forEach(PrisonPlayer::save);
		logger.log(PrisonLoggType.SUCCESSFULLY, "All players configuration has been saved");

		logger.log("Saving all fractions configuration");
//		Fraction.fractions_map.values().forEach(Fraction::save);
		logger.log(PrisonLoggType.SUCCESSFULLY, "All fractions configuration has been saved");

		this.getServer().getScheduler().cancelTasks(this);
		logger.log(PrisonLoggType.SUCCESSFULLY, "Stop all plugin tasks");
		logger.log(PrisonLoggType.SUCCESSFULLY, "Prison-PLUGIN was disabled");

		getQuickSell.unregister();
		Notify.unload();
//		Bukkit.getOnlinePlayers().forEach(p -> Bukkit.getPluginManager().callEvent(new PlayerQuitEvent(p, SERVER_NAME)));
	}

}
