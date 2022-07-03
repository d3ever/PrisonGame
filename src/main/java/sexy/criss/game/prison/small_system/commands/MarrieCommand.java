package sexy.criss.game.prison.small_system.commands;

import com.google.common.collect.Maps;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import sexy.criss.game.prison.small_system.SPlayer;
import sexy.criss.gen.commands.SpigotCommand;
import sexy.criss.gen.player.permission.PermissionGroup;
import sexy.criss.gen.util.Util;

import java.util.Map;

public class MarrieCommand extends SpigotCommand {
    Map<String, String> marrie_accept = Maps.newHashMap();

    public MarrieCommand() {
        super("marrie", PermissionGroup.PLAYER, "marrie");
        this.unavailableFromConsole();
    }

    @Override
    public void handle(CommandSender sender, String cmd, String[] args) {
        Player p = (Player) sender;
        if(args.length == 0) Util.ps("Prison", p, "&a/marrie [игрок]");
        else {
            String s = args[0];
            Player t = Bukkit.getPlayerExact(args[0]);
            if(t == null) Util.ps("Prison", p, "&fИгрока &c%s&f нет на сервере", t);
            else {
                SPlayer tsp = SPlayer.get(t);
                SPlayer sp = SPlayer.get(p);
                if(tsp.getPartner().length() > 0) Util.ps("Prison", p, "&fУ игрока &a%s&f есть партнёр.", t);
                else {
                    if(sp.getPartner().length() > 0) Util.ps("Prison", p, "&fУ вас уже есть партнёр.");
                    else {

                    }
                }
            }
        }
    }
}
