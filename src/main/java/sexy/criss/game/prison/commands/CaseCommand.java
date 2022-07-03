package sexy.criss.game.prison.commands;

import com.google.common.collect.Sets;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import sexy.criss.game.prison.Main;
import sexy.criss.game.prison.cases.Case;
import sexy.criss.game.prison.cases.CaseType;
import sexy.criss.game.prison.prison_data.PrisonItem;
import sexy.criss.gen.commands.SpigotCommand;
import sexy.criss.gen.player.permission.PermissionGroup;
import sexy.criss.gen.util.Util;

import java.util.Set;

public class CaseCommand extends SpigotCommand implements Listener {
    Set<Player> player = Sets.newHashSet();

    public CaseCommand() {
        super("case", PermissionGroup.ADMINISTRATOR, "/case set [left click to chest]");
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
        this.unavailableFromConsole();
    }

    @Override
    public void handle(CommandSender sender, String cmd, String[] args) {
        Player p = (Player) sender;
        if(args.length == 0) {
            this.notEnoughArguments(sender, usageMessage);
            return;
        }

        if(args[0].equalsIgnoreCase("set")){
            if(player.contains(p)) {
                Util.s(p, "&7Режим установки блоков &cвыключен");
                player.remove(p);
                return;
            }
            player.add(p);
            Util.s(p, "&7Режим установки блоков &aвключен");
            Util.s(p, "&7Нажмите ЛКМ по сундуку, чтобы уставить его локацию.");
            Util.s(p, "&7Типы сундуков: "+ "&6CHEST&7, " + "&6TRAPPED_CHEST&7");
        }
    }

    @EventHandler
    public void onClick(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        Set<Material> types = Sets.newHashSet(Material.CHEST, Material.TRAPPED_CHEST);
        if(e.getClickedBlock() != null) {
            Block b = e.getClickedBlock();
            if(types.contains(b.getType()) && player.contains(p)) {
                e.setCancelled(true);
                Case.caseMap.get(CaseType.DEFAULT_CASE).addLocation(b.getLocation());
                p.getInventory().addItem(PrisonItem.getPrisonItem("key").getUsableItem());
                Util.s(p, "&7Сундук был зарегистрирован");
            }
        }
    }

}
