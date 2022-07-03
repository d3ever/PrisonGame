package sexy.criss.game.prison.boosters.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import sexy.criss.game.prison.boosters.Booster;
import sexy.criss.game.prison.prison_data.PrisonPlayer;
import sexy.criss.gen.commands.SpigotCommand;
import sexy.criss.gen.player.permission.PermissionGroup;
import sexy.criss.gen.util.Util;

import java.util.concurrent.atomic.AtomicInteger;

public class ThanksCommand extends SpigotCommand {

    public ThanksCommand() {
        super("thx", PermissionGroup.PLAYER, "/thx", "thanks");
        this.unavailableFromConsole();
    }

    @Override
    public void handle(CommandSender sender, String cmd, String[] args) {
        Player p = (Player) sender;
        AtomicInteger ai = new AtomicInteger(0);
        Booster.getBoosters(p.getName()).stream().filter(booster -> booster.getOwner().length() > 0 && !booster.getThansk().contains(p.getName()) && !booster.isPrivate() && !booster.isInfinite() && !booster.isSilent() && !booster.getOwner().equalsIgnoreCase(p.getName())).forEach(booster -> {
            PrisonPlayer.getPlayer(Bukkit.getOfflinePlayer(booster.getOwner()).getUniqueId()).addGold(10);
            ai.addAndGet(5);
            PrisonPlayer.getPlayer(p.getUniqueId()).addGold(5);
        });
        if(ai.get() != 0) {
            Util.ps("Prison", p, "&fВы поблагодарили всех владельцев бустеров.");
            Util.ps("Prison", p, "&fВы получили: &6" + ai.get() + "$");
        } else Util.ps("Prison", p, "&fНекого благодарить");
    }
}
