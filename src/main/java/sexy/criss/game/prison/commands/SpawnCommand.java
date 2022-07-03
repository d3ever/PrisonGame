package sexy.criss.game.prison.commands;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import sexy.criss.game.prison.Main;
import sexy.criss.game.prison.commands.manager.AbstractCommand;
import sexy.criss.game.prison.language.Reference;
import sexy.criss.game.prison.timer.SexyTimer;
import sexy.criss.gen.util.Util;

public class SpawnCommand extends AbstractCommand {
	public static Location spawnLoc = Util.getLoc(Main.getDatFile().get("spawnLoc", "world 1 1 1 0 0"));

	public SpawnCommand() {
		super("spawn");
		this.unavailableFromConsole();
	}

	@Override
	public void handle(CommandSender s, String cmd, String[] arg) {
		Player p = (Player) s;
		boolean perms = Util.isAdmin(p);

		if (perms && arg.length == 1 && "set".equalsIgnoreCase(arg[0])) {
			Main.getDatFile().parse("spawnLoc", Util.getLoc(p.getLocation()));
			Main.getDatFile().update();
			spawnLoc = p.getLocation();
			p.sendMessage(Reference.LOCATION_SPAWN_SET.get());
		}

		if(spawnLoc == null) {
			p.sendMessage(Reference.LOCATION_SPAWN_FAIL.get());
			return;
		}

		new SexyTimer(p, 3) {
			@Override
			protected void handle() {
				p.teleport(spawnLoc);
			}
		}.run();

	}
}
