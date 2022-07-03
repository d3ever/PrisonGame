package sexy.criss.game.prison.boosters.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import sexy.criss.game.prison.boosters.SellEvent;
import sexy.criss.game.prison.boosters.Shop;
import sexy.criss.game.prison.prison_data.PrisonPlayer;
import sexy.criss.gen.commands.SpigotCommand;
import sexy.criss.gen.player.RPlayer;
import sexy.criss.gen.player.permission.PermissionGroup;
import sexy.criss.gen.util.Util;

public class SellCommand extends SpigotCommand {

    public SellCommand() {
        super("sell", PermissionGroup.PLAYER, "/sell", "sellall");
        this.unavailableFromConsole();
    }

    @Override
    public void handle(CommandSender sender, String cmd, String[] args) {
        Player p = (Player) sender;
        PrisonPlayer pp = PrisonPlayer.getPlayer(p.getUniqueId());
        if(!pp.hasAutoSell() || !RPlayer.get(p).hasGroup(PermissionGroup.VIP)) Util.permissions("Prison", p, PermissionGroup.VIP);
        else Shop.getShop("a").sellall(p, "", SellEvent.Type.SELLALL);
    }
}
