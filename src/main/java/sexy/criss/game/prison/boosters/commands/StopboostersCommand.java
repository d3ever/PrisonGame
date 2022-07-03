package sexy.criss.game.prison.boosters.commands;

import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Variable;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import sexy.criss.game.prison.boosters.Booster;
import sexy.criss.gen.commands.SpigotCommand;
import sexy.criss.gen.player.permission.PermissionGroup;
import sexy.criss.gen.util.Util;

import java.util.Iterator;

public class StopboostersCommand extends SpigotCommand {

    public StopboostersCommand() {
        super("stopboosters", PermissionGroup.ADMINISTRATOR, "/stopboosters [игрок]");
    }

    @Override
    public void handle(CommandSender sender, String cmd, String[] args) {
        if (args.length == 0) {
            Util.ps("Prison", sender, "&fИспользование: &6/stopboosters [игрок]");
        } else {
            Player t = Bukkit.getPlayerExact(args[0]);
            if (t == null) {
                Util.ps("Prison", sender, "&fИгрок &6%s&f не в сети.");
                return;
            }

            Iterator<Booster> boosters = Booster.iterate();
            while (boosters.hasNext()) {
                Booster booster = boosters.next();
                if (!booster.getAppliedPlayers().contains(args[0])) continue;
                boosters.remove();
                booster.deactivate();
            }
            Util.ps("Prison", sender, "&fВсе множители игрока &6%s&f были сброшены.", t.getName());
        }
    }
}
