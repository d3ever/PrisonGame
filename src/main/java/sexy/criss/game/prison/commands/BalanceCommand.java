package sexy.criss.game.prison.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import sexy.criss.game.prison.prison_data.PrisonPlayer;
import sexy.criss.gen.commands.SpigotCommand;
import sexy.criss.gen.player.permission.PermissionGroup;
import sexy.criss.gen.util.Util;

public class BalanceCommand extends SpigotCommand {

    public BalanceCommand() {
        super("balance", PermissionGroup.PLAYER, "/balance", "bal", "money");
        this.unavailableFromConsole();
    }

    @Override
    public void handle(CommandSender sender, String cmd, String[] args) {
        Player p = (Player) sender;
        PrisonPlayer pp = PrisonPlayer.getPlayer(p.getUniqueId());

        Util.ps("Prison", p, "&fВаш баланс: &6%.2f", pp.getBalance());
    }
}
