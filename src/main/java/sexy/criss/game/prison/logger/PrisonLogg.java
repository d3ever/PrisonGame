package sexy.criss.game.prison.logger;

import org.bukkit.Bukkit;
import sexy.criss.gen.util.Util;

public class PrisonLogg {

	public void log(PrisonLoggType type, String message) {
		String pref = type.getPrefix();

		Bukkit.getConsoleSender().sendMessage(Util.f(pref + message));
	}

	public void log(String message) {
		log(PrisonLoggType.INFO, message);
	}

	public void log(boolean debug, String message, String... args) {
		if (!debug)
			return;
		log(message, args);
	}

	public void log(String message, Object... args) {
		log(PrisonLoggType.INFO, String.format(message, args));
	}

	public void log(PrisonLoggType type, String message, Object... args) {
		log(type, String.format(message, args));
	}

}
