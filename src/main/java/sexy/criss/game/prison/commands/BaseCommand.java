package sexy.criss.game.prison.commands;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import sexy.criss.game.prison.Main;
import sexy.criss.game.prison.prison_data.PrisonPlayer;
import sexy.criss.game.prison.timer.SexyTimer;
import sexy.criss.gen.commands.SpigotCommand;
import sexy.criss.gen.player.permission.PermissionGroup;
import sexy.criss.gen.util.Util;

public class BaseCommand extends SpigotCommand {

    public BaseCommand() {
        super("base", PermissionGroup.PLAYER, "home", "home");
        this.unavailableFromConsole();
    }

    @Override
    public void handle(CommandSender sender, String cmd, String[] args) {
        Player p = (Player) sender;
        PrisonPlayer pp = PrisonPlayer.getPlayer(p.getUniqueId());
        if(!pp.hasFraction()) {
            if(pp.getLevel() < 5) Util.ps("Prison", p, "&fВам нужен &65&f уровень, чтобы телепортироваться на базу.");
            else Util.ps("Prison", p, "&fВам нужно выбрать фракцию. &6/faction&f.");
        }else {
            Location l = Util.getLoc(Main.getDatFile().get(pp.getFraction().getId()));
            if(l == null) Util.ps("Prison", p, "&fБаза фракции &6%s&f не установлена. Обратитесь к администратору.");
            else new SexyTimer(p, 3) {
                @Override
                protected void handle() {
                    p.teleport(l);
                }
            };
        }
    }
}
