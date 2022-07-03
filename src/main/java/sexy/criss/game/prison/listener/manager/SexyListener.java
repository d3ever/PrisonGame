package sexy.criss.game.prison.listener.manager;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import com.google.common.collect.Lists;

import sexy.criss.game.prison.Main;
import sexy.criss.game.prison.listener.*;
import sexy.criss.game.prison.listener.players.ChatHandler;
import sexy.criss.game.prison.listener.players.EntityHandler;
import sexy.criss.game.prison.listener.players.PlayerHandler;
import sexy.criss.game.prison.listener.players.StatisticBookHandler;
import sexy.criss.game.prison.listener.players.damage.PlayerDamageHandler;
import sexy.criss.game.prison.logger.PrisonLogg;
import sexy.criss.game.prison.logger.PrisonLoggType;
import sexy.criss.game.prison.notify.Notify;
import sexy.criss.game.prison.small_system.events.GeneralHandler;

public abstract class SexyListener implements Listener {

	public PrisonLogg getLog() {
		return Main.getInstance().getLog();
	}

	public abstract String getName();

	public abstract String getType();

	public void register() {
		Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
		getLog().log(PrisonLoggType.SUCCESSFULLY, "Registered listener [%s]", this.getName());
	}

	public static void REGISTER_ALL() {
		Lists.newArrayList(
				new PlayerHandler(),
				new BlockListener(),
				new FractionHandler(),
				new EntityHandler(),
				new FractionListHandler(),
				new WorldHandler(),
				new ChatHandler(),
				new MineHandler(),
				new StatisticBookHandler(),
				new NPCHandler(),
				new PlayerDamageHandler(),
				new GeneralHandler(),
				new Notify()
		)
				.forEach(SexyListener::register);
	}

}
