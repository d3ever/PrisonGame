package sexy.criss.game.prison.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import sexy.criss.game.prison.donate_part.DonateGui;
import sexy.criss.gen.commands.SpigotCommand;
import sexy.criss.gen.player.permission.PermissionGroup;

public class DonateCommand extends SpigotCommand {

    public DonateCommand() {
        super("donate", PermissionGroup.PLAYER, "/donate");
        this.unavailableFromConsole();
    }

    @Override
    public void handle(CommandSender sender, String cmd, String[] args) {
        Player p = (Player) sender;
        new DonateGui(p).handle();
    }
}
