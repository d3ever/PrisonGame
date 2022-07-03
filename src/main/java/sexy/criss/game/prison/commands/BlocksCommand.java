package sexy.criss.game.prison.commands;

import net.minecraft.server.v1_16_R3.EntityArmorStand;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import sexy.criss.game.prison.commands.blocks.BlocksGui;
import sexy.criss.game.prison.prison_data.PrisonPlayer;
import sexy.criss.gen.commands.SpigotCommand;
import sexy.criss.gen.player.permission.PermissionGroup;
import sexy.criss.gen.util.Util;

public class BlocksCommand extends SpigotCommand {

    public BlocksCommand() {
        super("blocks", PermissionGroup.PLAYER, "/blocks");
        this.unavailableFromConsole();
    }

    @Override
    public void handle(CommandSender sender, String cmd, String[] args) {
        Player p = (Player) sender;
        new BlocksGui(PrisonPlayer.getPlayer(p.getUniqueId()).getBlock_log(), p).handle();
    }
}
